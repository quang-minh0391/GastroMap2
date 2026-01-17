package controller.debt;

import DAO.DebtDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

public class MemberDebtDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String mIdStr = request.getParameter("memberId");
        if (mIdStr == null) {
            response.sendRedirect("DebtManagementServlet");
            return;
        }

        int memberId = Integer.parseInt(mIdStr);
        DebtDAO dao = new DebtDAO();
        
        request.setAttribute("history", dao.getMemberTransactionHistory(memberId));
        request.setAttribute("mId", memberId);
        
        request.getRequestDispatcher("/debt/member_debt_detail.jsp").forward(request, response);
    }
}