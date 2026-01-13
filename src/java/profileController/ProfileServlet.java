/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package profileController;

import DAO.DAOMember1;
import model.member;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "profile", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // 1. Kiểm tra nếu chưa đăng nhập thì bắt quay về trang login
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // 2. Lấy ID từ session
        int memberId = (int) session.getAttribute("id");
        
        // 3. Lấy thông tin mới nhất từ Database
        DAOMember1 dao = new DAOMember1();
        member user = dao.getmemberbyID(memberId);

        if (user != null) {
            // Đưa đối tượng user vào request để JSP hiển thị
            request.setAttribute("userProfile", user);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("login/login.jsp");
        }
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Có thể dùng để xử lý cập nhật profile sau này
        doGet(request, response);
    }
}
