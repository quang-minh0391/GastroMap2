package controller.production;

import DAO.DAOBatchStockIn;
import DAO.DAOWarehouse;
import DAO.DAOProductionBatch;
import DAO.DAOFarmProduct;
import model.BatchStockIn;
import model.StorageWarehouse;
import model.ProductionBatch;
import model.FarmProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Controller for Batch Stock-In management
 * URL Pattern: /stock-ins
 */
@WebServlet(name = "StockInController", urlPatterns = {"/stock-ins"})
public class StockInController extends HttpServlet {

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
        // Lấy coop_id từ session
        HttpSession session = request.getSession();
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id");
        }
        
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

        DAOBatchStockIn dao = new DAOBatchStockIn();
        DAOWarehouse daoWarehouse = new DAOWarehouse();
        DAOProductionBatch daoBatch = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();

        List<BatchStockIn> list;
        int totalRecords;
        List<StorageWarehouse> warehouses;
        List<ProductionBatch> batches;
        List<FarmProduct> products;
        
        if (coopId != null) {
            // Filter by coop_id
            list = dao.getPaginatedByCoopId(page, pageSize, coopId);
            totalRecords = dao.countByCoopId(coopId);
            warehouses = daoWarehouse.getAllByCoopId(coopId);
            batches = daoBatch.getAllByCoopId(coopId);
            products = daoProduct.getActiveByCoopId(coopId);
        } else {
            list = dao.getPaginated(page, pageSize);
            totalRecords = dao.countAll();
            warehouses = daoWarehouse.getAll();
            batches = daoBatch.getAll();
            products = daoProduct.getAll();
        }
        
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("stockInList", list);
        request.setAttribute("warehouseList", warehouses);
        request.setAttribute("batchList", batches);
        request.setAttribute("productList", products);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/stock-ins/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy coop_id từ session
        HttpSession session = request.getSession();
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id");
        }
        
        DAOWarehouse daoWarehouse = new DAOWarehouse();
        DAOProductionBatch daoBatch = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();

        List<StorageWarehouse> warehouses;
        List<ProductionBatch> batches;
        List<FarmProduct> products;
        
        if (coopId != null) {
            warehouses = daoWarehouse.getAllByCoopId(coopId);
            batches = daoBatch.getAvailableByCoopId(coopId);
            products = daoProduct.getActiveByCoopId(coopId);
        } else {
            warehouses = daoWarehouse.getAll();
            batches = daoBatch.getAvailable();
            products = daoProduct.getAll();
        }

        request.setAttribute("warehouseList", warehouses);
        request.setAttribute("batchList", batches);
        request.setAttribute("productList", products);

        request.getRequestDispatcher("/production/stock-ins/create.jsp").forward(request, response);
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String batchIdStr = request.getParameter("batchId");
        String warehouseIdStr = request.getParameter("warehouseId");
        String quantityStr = request.getParameter("quantity");
        String unit = request.getParameter("unit");
        String receivedDateStr = request.getParameter("receivedDate");
        String receivedBy = request.getParameter("receivedBy");
        String note = request.getParameter("note");

        // Validation
        if (batchIdStr == null || warehouseIdStr == null || 
            quantityStr == null || receivedDateStr == null) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
            showCreateForm(request, response);
            return;
        }

        try {
            BatchStockIn stockIn = new BatchStockIn();
            stockIn.setBatchId(Integer.parseInt(batchIdStr));
            stockIn.setWarehouseId(Integer.parseInt(warehouseIdStr));
            stockIn.setQuantity(Double.parseDouble(quantityStr));
            stockIn.setUnit(unit != null && !unit.isEmpty() ? unit.trim() : "kg");
            stockIn.setReceivedDate(Date.valueOf(receivedDateStr));
            stockIn.setReceivedBy(receivedBy != null ? receivedBy.trim() : null);
            stockIn.setNote(note != null ? note.trim() : null);

            DAOBatchStockIn dao = new DAOBatchStockIn();
            boolean success = dao.insert(stockIn);

            if (success) {
                request.setAttribute("success", "Nhập kho thành công");
            } else {
                request.setAttribute("error", "Nhập kho thất bại");
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

