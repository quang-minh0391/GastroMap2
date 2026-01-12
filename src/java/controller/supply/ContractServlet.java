package controller.supply;

import DAO.ContractDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ContractServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String term = request.getParameter("term");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(ContractDAO.INSTANCE.searchContracts(term != null ? term : "")));
    }
}