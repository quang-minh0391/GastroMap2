/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.material;

import DAO.MaterialWarehouseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class EditWarehouseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        // Sử dụng hàm getWarehouseById đã viết ở phần trước
        Map<String, Object> warehouse = MaterialWarehouseDAO.INSTANCE.getWarehouseById(id);
        
        if (warehouse != null) {
            request.setAttribute("w", warehouse);
            request.getRequestDispatcher("/supplyQ/edit_warehouse.jsp").forward(request, response);
        } else {
            response.sendRedirect("ListWarehouseServlet?status=not_found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Lấy dữ liệu từ form chỉnh sửa
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        // Gọi DAO thực hiện cập nhật
        boolean success = MaterialWarehouseDAO.INSTANCE.updateWarehouse(id, name, location, description);

        if (success) {
            response.sendRedirect("ListWarehouseServlet?status=update_success");
        } else {
            request.setAttribute("error", "Lỗi: Không thể cập nhật thông tin kho.");
            // Quay lại trang sửa với dữ liệu vừa nhập
            doGet(request, response);
        }
    }
}