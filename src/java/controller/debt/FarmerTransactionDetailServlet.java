package controller.debt;

import DAO.DebtDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;

public class FarmerTransactionDetailServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int refId = Integer.parseInt(request.getParameter("refId"));
        String type = request.getParameter("type");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new Gson().toJson(new DebtDAO().getFarmerTransactionPopupDetail(refId, type)));
    }
}