/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.material;

import DAO.PartnerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
public class CreatePartnerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Chỉ đơn giản là hiển thị trang JSP chứa form thêm mới
        request.getRequestDispatcher("/supplyQ/create_partner.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
    // 1. Cấu hình tiếng Việt cho dữ liệu
    request.setCharacterEncoding("UTF-8");
    HttpSession session = request.getSession(false);
    
    // 2. Kiểm tra Session và điều hướng tuyệt đối nếu hết hạn
    if (session == null || session.getAttribute("id") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // 3. Xác định coop_id dựa trên loại tài khoản
    Integer coopId = (Integer) session.getAttribute("coop_id");
    if (coopId == null || coopId == 0) {
        coopId = (Integer) session.getAttribute("id"); // Type 2 dùng chính ID tài khoản làm coopId
    }

    // 4. Lấy dữ liệu từ form
    String name = request.getParameter("name");
    String phone = request.getParameter("phone");
    String address = request.getParameter("address");
    String taxCode = request.getParameter("tax_code");
    String note = request.getParameter("note");

    // 5. Gọi DAO với tham số coopId
    boolean success = PartnerDAO.INSTANCE.insertPartner(name, phone, address, taxCode, note, coopId);
    
    if (success) {
        // Sử dụng đường dẫn tuyệt đối để quay về danh sách
        response.sendRedirect(request.getContextPath() + "/ListPartnerServlet?status=add_success");
    } else {
        request.setAttribute("error", "Lỗi: Không thể thêm nhà cung cấp.");
        request.getRequestDispatcher("/supplyQ/create_partner.jsp").forward(request, response);
    }
}
}