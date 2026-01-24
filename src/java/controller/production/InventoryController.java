package controller.production;

import DAO.DAOBatchInventory;
import DAO.DAOWarehouse;
import DAO.DAOProductionBatch;
import DAO.DAOFarmProduct;
import model.BatchInventory;
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
import java.util.List;

/**
 * Controller for Batch Inventory management
 * URL Pattern: /batch-inventory
 */
@WebServlet(name = "InventoryController", urlPatterns = {"/batch-inventory"})
public class InventoryController extends HttpServlet {

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
            default:
                handleList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
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

        // Filter parameters
        Integer warehouseId = null;
        Integer batchId = null;
        String warehouseIdStr = request.getParameter("warehouseId");
        String batchIdStr = request.getParameter("batchId");

        if (warehouseIdStr != null && !warehouseIdStr.isEmpty()) {
            try {
                warehouseId = Integer.parseInt(warehouseIdStr);
            } catch (NumberFormatException e) {
                // ignore
            }
        }
        if (batchIdStr != null && !batchIdStr.isEmpty()) {
            try {
                batchId = Integer.parseInt(batchIdStr);
            } catch (NumberFormatException e) {
                // ignore
            }
        }

        DAOBatchInventory dao = new DAOBatchInventory();
        DAOWarehouse daoWarehouse = new DAOWarehouse();
        DAOProductionBatch daoBatch = new DAOProductionBatch();
        DAOFarmProduct daoProduct = new DAOFarmProduct();

        List<BatchInventory> list;
        int totalRecords;
        List<StorageWarehouse> warehouses;
        List<ProductionBatch> batches;
        List<FarmProduct> products;
        
        if (coopId != null) {
            // Filter by coop_id
            list = dao.getPaginatedByCoopId(page, pageSize, coopId, warehouseId, batchId);
            totalRecords = dao.countByCoopId(coopId, warehouseId, batchId);
            warehouses = daoWarehouse.getAllByCoopId(coopId);
            batches = daoBatch.getAllByCoopId(coopId);
            products = daoProduct.getActiveByCoopId(coopId);
        } else {
            list = dao.getPaginatedWithFilters(page, pageSize, warehouseId, batchId);
            totalRecords = dao.countWithFilters(warehouseId, batchId);
            warehouses = daoWarehouse.getAll();
            batches = daoBatch.getAll();
            products = daoProduct.getAll();
        }
        
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("inventoryList", list);
        request.setAttribute("warehouseList", warehouses);
        request.setAttribute("batchList", batches);
        request.setAttribute("productList", products);
        request.setAttribute("selectedWarehouseId", warehouseId);
        request.setAttribute("selectedBatchId", batchId);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/inventory/list.jsp").forward(request, response);
    }
}

