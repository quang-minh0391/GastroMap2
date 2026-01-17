package controller.debt;

import DAO.DebtDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

public class PartnerDebtDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer coopId = (Integer) session.getAttribute("id");
        String pIdStr = request.getParameter("partnerId");

        if (coopId == null || pIdStr == null) {
            response.sendRedirect("DebtManagementServlet");
            return;
        }

        int partnerId = Integer.parseInt(pIdStr);
        DebtDAO dao = new DebtDAO();
        
        // Lấy tên đối tác để hiển thị tiêu đề
        request.setAttribute("history", dao.getPartnerTransactionHistory(coopId, partnerId));
        request.setAttribute("partnerId", partnerId);
        
        request.getRequestDispatcher("/debt/partner_debt_detail.jsp").forward(request, response);
    }
}