package controller.debt;

import DAO.DebtDAO;
import model.member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FarmerDebtHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        // Lấy đối tượng member từ session (giả sử bạn lưu với key "user")
        member currentUser = (member) session.getAttribute("user");

        // Kiểm tra đăng nhập và phân quyền Nông dân (member_type = 1)
        if (currentUser == null || currentUser.getMember_type() != 1) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        DebtDAO dao = new DebtDAO();
        request.setAttribute("farmer", dao.getFarmerDebtDetail(currentUser.getId()));
        request.setAttribute("history", dao.getFarmerTransactionHistoryList(currentUser.getId()));
        request.getRequestDispatcher("/debt/view_history.jsp").forward(request, response);
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}