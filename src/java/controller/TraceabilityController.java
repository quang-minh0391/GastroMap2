package controller;

import DAO.DAOBatchQRCode;
import DAO.DAOProductionBatch;
import DAO.DAOFarmProduct;
import DAO.DAOMember;
import DAO.DAOQRScanHistory;
import model.BatchQRCode;
import model.ProductionBatch;
import model.FarmProduct;
import model.member;
import model.QRScanHistory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Traceability - QR scanning and history
 * URL Pattern: /traceability
 */
@WebServlet(name = "TraceabilityController", urlPatterns = {"/traceability"})
public class TraceabilityController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "scan";
        }

        switch (action) {
            case "scan":
                showScanPage(request, response);
                break;
            case "result":
                handleResult(request, response);
                break;
            case "history":
                handleHistory(request, response);
                break;
            default:
                showScanPage(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "scan";
        }

        switch (action) {
            case "lookup":
                handleLookup(request, response);
                break;
            case "recordScan":
                handleRecordScan(request, response);
                break;
            default:
                showScanPage(request, response);
        }
    }

    private void showScanPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/production/traceability/scan.jsp").forward(request, response);
    }

    private void handleLookup(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String qrValue = request.getParameter("qrValue");

        if (qrValue == null || qrValue.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập mã QR");
            showScanPage(request, response);
            return;
        }

        DAOBatchQRCode daoQR = new DAOBatchQRCode();
        BatchQRCode qrCode = daoQR.getByQrValue(qrValue.trim());

        if (qrCode == null) {
            request.setAttribute("error", "Không tìm thấy mã QR: " + qrValue);
            showScanPage(request, response);
            return;
        }

        // Redirect to result page with QR ID
        response.sendRedirect(request.getContextPath() + "/traceability?action=result&qrId=" + qrCode.getId());
    }

    private void handleResult(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String qrIdStr = request.getParameter("qrId");
        String qrValue = request.getParameter("qrValue");

        DAOBatchQRCode daoQR = new DAOBatchQRCode();
        BatchQRCode qrCode = null;

        if (qrIdStr != null && !qrIdStr.isEmpty()) {
            try {
                Integer qrId = Integer.parseInt(qrIdStr);
                qrCode = daoQR.getById(qrId);
            } catch (NumberFormatException e) {
                // ignore
            }
        } else if (qrValue != null && !qrValue.isEmpty()) {
            qrCode = daoQR.getByQrValue(qrValue.trim());
        }

        if (qrCode == null) {
            request.setAttribute("error", "Không tìm thấy thông tin QR");
            showScanPage(request, response);
            return;
        }

        // Get full traceability info
        DAOProductionBatch daoBatch = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();
        DAOMember daoMember = new DAOMember();
        DAOQRScanHistory daoHistory = new DAOQRScanHistory();

        ProductionBatch batch = daoBatch.getById(qrCode.getBatchId());
        FarmProduct product = null;
        member memberObj = null;
        List<QRScanHistory> scanHistory = null;

        if (batch != null) {
            product = daoProduct.getById(batch.getProductId());
            memberObj = daoMember.getById(batch.getMemberId());
        }

        scanHistory = daoHistory.getByQrId(qrCode.getId());

        request.setAttribute("qrCode", qrCode);
        request.setAttribute("batch", batch);
        request.setAttribute("product", product);
        request.setAttribute("member", memberObj);
        request.setAttribute("scanHistory", scanHistory);

        request.getRequestDispatcher("/production/traceability/result.jsp").forward(request, response);
    }

    private void handleRecordScan(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String qrIdStr = request.getParameter("qrId");
        String scanLocation = request.getParameter("scanLocation");
        String scanActor = request.getParameter("scanActor");
        String note = request.getParameter("note");

        if (qrIdStr == null || qrIdStr.isEmpty()) {
            request.setAttribute("error", "Thiếu thông tin mã QR");
            showScanPage(request, response);
            return;
        }

        try {
            Integer qrId = Integer.parseInt(qrIdStr);

            QRScanHistory history = new QRScanHistory();
            history.setQrId(qrId);
            history.setScanLocation(scanLocation != null ? scanLocation.trim() : null);
            history.setScanActor(scanActor != null ? scanActor.trim() : "Anonymous");
            history.setNote(note != null ? note.trim() : null);

            DAOQRScanHistory dao = new DAOQRScanHistory();
            boolean success = dao.insert(history);

            if (success) {
                request.setAttribute("success", "Đã ghi nhận lịch sử quét");
            } else {
                request.setAttribute("error", "Ghi nhận lịch sử thất bại");
            }

            // Show result page again
            response.sendRedirect(request.getContextPath() + "/traceability?action=result&qrId=" + qrId);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
            showScanPage(request, response);
        }
    }

    private void handleHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 20;

        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        DAOQRScanHistory dao = new DAOQRScanHistory();
        DAOBatchQRCode daoQR = new DAOBatchQRCode();
        DAOProductionBatch daoBatch = new DAOProductionBatch();

        List<QRScanHistory> list = dao.getPaginated(page, pageSize);
        int totalRecords = dao.countAll();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        List<BatchQRCode> qrCodes = daoQR.getAll();
        List<ProductionBatch> batches = daoBatch.getAll();

        request.setAttribute("historyList", list);
        request.setAttribute("qrCodeList", qrCodes);
        request.setAttribute("batchList", batches);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/traceability/history.jsp").forward(request, response);
    }
}

