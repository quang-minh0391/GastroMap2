package controller.production;

import DAO.DAOWarehouse;
import model.StorageWarehouse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller for Storage Warehouses management
 * URL Pattern: /warehouses
 */
@WebServlet(name = "WarehouseController", urlPatterns = {"/warehouses"})
public class WarehouseController extends HttpServlet {

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
            case "edit":
                showEditForm(request, response);
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
            case "update":
                handleUpdate(request, response);
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

        DAOWarehouse dao = new DAOWarehouse();
        List<StorageWarehouse> list = dao.getPaginated(page, pageSize);
        int totalRecords = dao.countAll();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("warehouseList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/warehouses/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/production/warehouses/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/warehouses?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOWarehouse dao = new DAOWarehouse();
            StorageWarehouse warehouse = dao.getById(id);

            if (warehouse == null) {
                request.setAttribute("error", "Không tìm thấy kho");
                handleList(request, response);
                return;
            }

            request.setAttribute("warehouse", warehouse);
            request.getRequestDispatcher("/production/warehouses/edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/warehouses?action=list");
        }
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên kho không được để trống");
            request.getRequestDispatcher("/production/warehouses/create.jsp").forward(request, response);
            return;
        }

        StorageWarehouse warehouse = new StorageWarehouse();
        warehouse.setName(name.trim());
        warehouse.setLocation(location != null ? location.trim() : null);
        warehouse.setDescription(description != null ? description.trim() : null);

        DAOWarehouse dao = new DAOWarehouse();
        boolean success = dao.insert(warehouse);

        if (success) {
            request.setAttribute("success", "Thêm kho thành công");
        } else {
            request.setAttribute("error", "Thêm kho thất bại");
        }
        handleList(request, response);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        if (idStr == null || name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            handleList(request, response);
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            StorageWarehouse warehouse = new StorageWarehouse();
            warehouse.setId(id);
            warehouse.setName(name.trim());
            warehouse.setLocation(location != null ? location.trim() : null);
            warehouse.setDescription(description != null ? description.trim() : null);

            DAOWarehouse dao = new DAOWarehouse();
            boolean success = dao.update(warehouse);

            if (success) {
                request.setAttribute("success", "Cập nhật kho thành công");
            } else {
                request.setAttribute("error", "Cập nhật kho thất bại");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
        }
        handleList(request, response);
    }

    private void handleDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/warehouses?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOWarehouse dao = new DAOWarehouse();
            boolean success = dao.delete(id);

            if (success) {
                request.setAttribute("success", "Xóa kho thành công");
            } else {
                request.setAttribute("error", "Xóa kho thất bại. Có thể kho đang chứa hàng.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
        }
        handleList(request, response);
    }
}

