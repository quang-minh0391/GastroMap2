package controller;

import DAO.DAOProductionBatch;
import DAO.DAOFarmProduct;
import DAO.DAOMember;
import model.ProductionBatch;
import model.FarmProduct;
import model.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Controller for Production Batches management
 * URL Pattern: /batches
 */
@WebServlet(name = "ProductionBatchController", urlPatterns = {"/batches"})
public class ProductionBatchController extends HttpServlet {

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
            case "create":
                showCreateForm(request, response);
                break;
            case "view":
                handleView(request, response);
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

        DAOProductionBatch dao = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();
        DAOMember daoMember = new DAOMember();

        List<ProductionBatch> list = dao.getPaginated(page, pageSize);
        int totalRecords = dao.countAll();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        // Get product and member names for display
        List<FarmProduct> products = daoProduct.getAll();
        List<Member> members = daoMember.getAll();

        request.setAttribute("batchList", list);
        request.setAttribute("productList", products);
        request.setAttribute("memberList", members);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/batches/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOFarmProduct daoProduct = new DAOFarmProduct();
        DAOMember daoMember = new DAOMember();
        DAOProductionBatch daoBatch = new DAOProductionBatch();

        List<FarmProduct> products = daoProduct.getActive();
        List<Member> members = daoMember.getAll();
        String suggestedBatchCode = daoBatch.generateBatchCode();

        request.setAttribute("productList", products);
        request.setAttribute("memberList", members);
        request.setAttribute("suggestedBatchCode", suggestedBatchCode);

        request.getRequestDispatcher("/production/batches/create.jsp").forward(request, response);
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/batches?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOProductionBatch dao = new DAOProductionBatch();
            DAOFarmProduct daoProduct = new DAOFarmProduct();
            DAOMember daoMember = new DAOMember();

            ProductionBatch batch = dao.getById(id);

            if (batch == null) {
                request.setAttribute("error", "Không tìm thấy lô sản xuất");
                handleList(request, response);
                return;
            }

            FarmProduct product = daoProduct.getById(batch.getProductId());
            Member member = daoMember.getById(batch.getMemberId());

            request.setAttribute("batch", batch);
            request.setAttribute("product", product);
            request.setAttribute("member", member);
            request.getRequestDispatcher("/production/batches/view.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/batches?action=list");
        }
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String batchCode = request.getParameter("batchCode");
        String productIdStr = request.getParameter("productId");
        String memberIdStr = request.getParameter("memberId");
        String harvestDateStr = request.getParameter("harvestDate");
        String expiryDateStr = request.getParameter("expiryDate");
        String totalQuantityStr = request.getParameter("totalQuantity");
        String unit = request.getParameter("unit");
        String status = request.getParameter("status");

        // Validation
        if (batchCode == null || batchCode.trim().isEmpty() ||
            productIdStr == null || memberIdStr == null ||
            harvestDateStr == null || totalQuantityStr == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
            showCreateForm(request, response);
            return;
        }

        try {
            ProductionBatch batch = new ProductionBatch();
            batch.setBatchCode(batchCode.trim());
            batch.setProductId(Integer.parseInt(productIdStr));
            batch.setMemberId(Integer.parseInt(memberIdStr));
            batch.setHarvestDate(Date.valueOf(harvestDateStr));
            if (expiryDateStr != null && !expiryDateStr.isEmpty()) {
                batch.setExpiryDate(Date.valueOf(expiryDateStr));
            }
            batch.setTotalQuantity(Double.parseDouble(totalQuantityStr));
            batch.setUnit(unit != null && !unit.isEmpty() ? unit.trim() : "kg");
            batch.setStatus(status != null && !status.isEmpty() ? status : "AVAILABLE");

            DAOProductionBatch dao = new DAOProductionBatch();
            
            // Check if batch code already exists
            if (dao.getByBatchCode(batchCode.trim()) != null) {
                request.setAttribute("error", "Mã lô đã tồn tại");
                showCreateForm(request, response);
                return;
            }

            boolean success = dao.insert(batch);

            if (success) {
                request.setAttribute("success", "Tạo lô sản xuất thành công");
            } else {
                request.setAttribute("error", "Tạo lô sản xuất thất bại");
            }
            handleList(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu số không hợp lệ");
            showCreateForm(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Định dạng ngày không hợp lệ (YYYY-MM-DD)");
            showCreateForm(request, response);
        }
    }
}

