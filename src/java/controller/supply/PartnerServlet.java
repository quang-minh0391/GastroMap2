package controller.supply;

import DAO.PartnerDAO;
import model.Partner;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class PartnerServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String term = request.getParameter("term");
        List<Partner> list = PartnerDAO.INSTANCE.searchPartners(term != null ? term : "");
        
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(list));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        int newId = PartnerDAO.INSTANCE.insertPartnerShort(name);
        
        Map<String, Object> res = new HashMap<>();
        res.put("id", newId);
        res.put("name", name);
        
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(res));
    }
}