package controller.production;

import DAO.DAOFarmProduct;
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
 * Controller for Farm Products management
 * URL Pattern: /farm-products
 */
@WebServlet(name = "FarmProductController", urlPatterns = {"/farm-products"})
public class FarmProductController extends HttpServlet {

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
        // Lấy coop_id từ session
        HttpSession session = request.getSession();
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Tài khoản HTX (Type 2) dùng chính ID của mình
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

        DAOFarmProduct dao = new DAOFarmProduct();
        List<FarmProduct> list;
        int totalRecords;
        
        if (coopId != null) {
            // Filter by coop_id
            list = dao.getPaginatedByCoopId(page, pageSize, coopId);
            totalRecords = dao.countByCoopId(coopId);
        } else {
            // Fallback for admin or no coop
            list = dao.getPaginated(page, pageSize);
            totalRecords = dao.countAll();
        }
        
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);

        request.setAttribute("productList", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);

        request.getRequestDispatcher("/production/farm-products/list.jsp").forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/production/farm-products/create.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/farm-products?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOFarmProduct dao = new DAOFarmProduct();
            FarmProduct product = dao.getById(id);

            if (product == null) {
                request.setAttribute("error", "Không tìm thấy sản phẩm");
                handleList(request, response);
                return;
            }

            request.setAttribute("product", product);
            request.getRequestDispatcher("/production/farm-products/edit.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/farm-products?action=list");
        }
    }

    private void handleSave(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy user ID từ session để lưu createdBy
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("id");
        
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String description = request.getParameter("description");
        String status = request.getParameter("status");

        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Tên sản phẩm không được để trống");
            request.getRequestDispatcher("/production/farm-products/create.jsp").forward(request, response);
            return;
        }

        FarmProduct product = new FarmProduct();
        product.setName(name.trim());
        product.setUnit(unit != null && !unit.isEmpty() ? unit.trim() : "kg");
        product.setDescription(description != null ? description.trim() : null);
        product.setStatus(status != null && !status.isEmpty() ? status : "ACTIVE");
        product.setCreatedBy(userId); // Lưu người tạo

        DAOFarmProduct dao = new DAOFarmProduct();
        boolean success = dao.insert(product);

        if (success) {
            request.setAttribute("success", "Thêm sản phẩm thành công");
        } else {
            request.setAttribute("error", "Thêm sản phẩm thất bại");
        }
        handleList(request, response);
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String description = request.getParameter("description");
        String status = request.getParameter("status");

        if (idStr == null || name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Dữ liệu không hợp lệ");
            handleList(request, response);
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            FarmProduct product = new FarmProduct();
            product.setId(id);
            product.setName(name.trim());
            product.setUnit(unit != null && !unit.isEmpty() ? unit.trim() : "kg");
            product.setDescription(description != null ? description.trim() : null);
            product.setStatus(status != null && !status.isEmpty() ? status : "ACTIVE");

            DAOFarmProduct dao = new DAOFarmProduct();
            boolean success = dao.update(product);

            if (success) {
                request.setAttribute("success", "Cập nhật sản phẩm thành công");
            } else {
                request.setAttribute("error", "Cập nhật sản phẩm thất bại");
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
            response.sendRedirect(request.getContextPath() + "/farm-products?action=list");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            DAOFarmProduct dao = new DAOFarmProduct();
            boolean success = dao.delete(id);

            if (success) {
                request.setAttribute("success", "Xóa sản phẩm thành công");
            } else {
                request.setAttribute("error", "Xóa sản phẩm thất bại. Có thể sản phẩm đang được sử dụng.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ");
        }
        handleList(request, response);
    }
}

