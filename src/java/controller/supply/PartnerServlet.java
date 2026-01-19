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
    HttpSession session = request.getSession();
    
    // Lấy coop_id từ session
    Integer coopId = (Integer) session.getAttribute("coop_id");
    
    // Logic: Nếu là tài khoản HTX (Type 2), coop_id trong DB có thể null, 
    // khi đó ta lấy chính ID của tài khoản đó để lọc
    if (coopId == null || coopId == 0) {
        coopId = (Integer) session.getAttribute("id");
    }

    String term = request.getParameter("term");
    // Gọi DAO với cả từ khóa tìm kiếm và coopId
    List<Partner> list = PartnerDAO.INSTANCE.searchPartners(term != null ? term : "", coopId);
    
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