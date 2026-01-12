package controller;

import DAO.DAOBatchQRCode;
import DAO.DAOProductionBatch;
import DAO.DAOFarmProduct;
import DAO.DAOMember;
import model.BatchQRCode;
import model.ProductionBatch;
import model.FarmProduct;
import model.member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for QR Code management
 * URL Pattern: /qr-codes
 */
@WebServlet(name = "QRCodeController", urlPatterns = {"/qr-codes"})
public class QRCodeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                handleList(request, response);
                break;
            case "generate":
                showGenerateForm(request, response);
                break;
            case "view":
                handleView(request, response);
                break;
            case "delete":
                handleDelete(request, response);
                break;
            default:
                handleList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "save":
                handleSave(request, response);
                break;
            default:
                handleList(request, response);
        }
    }

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        String pageStr = request.getParameter("page");
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        DAOBatchQRCode dao = new DAOBatchQRCode();
        DAOProductionBatch daoBatch = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();

        List<BatchQRCode> list = dao.getPaginated(page, pageSize);
        int totalRecords = dao.countAll();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        List<ProductionBatch> batches = daoBatch.getAll();
        List<FarmProduct> products = daoProduct.getAll();

        request.setAttribute("qrCodeList", list);
        request.setAttribute("batchList", batches);
        request.setAttribute("productList", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/qr-codes/list.jsp").forward(request, response);
    }

    private void showGenerateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOProductionBatch daoBatch = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();

        List<ProductionBatch> batches = daoBatch.getAvailable();
        List<FarmProduct> products = daoProduct.getAll();

        request.setAttribute("batchList", batches);
        request.setAttribute("productList", products);

        request.getRequestDispatcher("/production/qr-codes/generate.jsp").forward(request, response);
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/qr-codes?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOBatchQRCode dao = new DAOBatchQRCode();
            DAOProductionBatch daoBatch = new DAOProductionBatch();
            DAOFarmProduct daoProduct = new DAOFarmProduct();
            DAOMember daoMember = new DAOMember();

            BatchQRCode qrCode = dao.getById(id);

            if (qrCode == null) {
                request.setAttribute("error", "Không tìm thấy mã QR");
                handleList(request, response);
                return;
            }

            ProductionBatch batch = daoBatch.getById(qrCode.getBatchId());
            FarmProduct product = null;
            member memberObj = null;

            if (batch != null) {
                product = daoProduct.getById(batch.getProductId());
                memberObj = daoMember.getById(batch.getMemberId());
            }

            request.setAttribute("qrCode", qrCode);
            request.setAttribute("batch", batch);
            request.setAttribute("product", product);
            request.setAttribute("member", memberObj);

            request.getRequestDispatcher("/production/qr-codes/view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/qr-codes?action=list");
        }
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String batchIdStr = request.getParameter("batchId");
        String quantityStr = request.getParameter("quantity");

        if (batchIdStr == null || batchIdStr.isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn lô sản xuất");
            showGenerateForm(request, response);
            return;
        }

        try {
            Integer batchId = Integer.parseInt(batchIdStr);
            int quantity = 1;
            if (quantityStr != null && !quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
                if (quantity < 1) quantity = 1;
                if (quantity > 100) quantity = 100; // Limit max QR codes per request
            }

            DAOBatchQRCode dao = new DAOBatchQRCode();
            int successCount = 0;

            for (int i = 0; i < quantity; i++) {
                BatchQRCode qrCode = new BatchQRCode();
                qrCode.setBatchId(batchId);
                qrCode.setQrValue(dao.generateQRValue(batchId));
                qrCode.setStatus("CREATED");

                if (dao.insert(qrCode)) {
                    successCount++;
                }
            }

            if (successCount > 0) {
                request.setAttribute("success", "Đã tạo " + successCount + " mã QR thành công");
            } else {
                request.setAttribute("error", "Tạo mã QR thất bại");
            }
            handleList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            showGenerateForm(request, response);
        }
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/qr-codes?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOBatchQRCode dao = new DAOBatchQRCode();
            boolean success = dao.delete(id);

            if (success) {
                request.setAttribute("success", "Xóa mã QR thành công");
            } else {
                request.setAttribute("error", "Xóa mã QR thất bại");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
        }
        handleList(request, response);
    }
}

