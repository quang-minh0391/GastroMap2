package controller.supply;

import DAO.ContractDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Contract;


public class ContractServlet extends HttpServlet {
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    HttpSession session = request.getSession(false);
    
    // 1. Kiểm tra đăng nhập để tránh lỗi 500
    if (session == null || session.getAttribute("id") == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return;
    }

    // 2. Lấy coop_id từ session (xử lý cho cả Type 1, 2 và 3)
    Integer coopId = (Integer) session.getAttribute("coop_id");
    if (coopId == null || coopId == 0) {
        coopId = (Integer) session.getAttribute("id"); // Nếu là Type 2, id chính là coopId
    }

    String term = request.getParameter("term");
    String keyword = (term != null) ? term : "";

    // 3. Gọi DAO với tham số coopId
    List<Contract> list = ContractDAO.INSTANCE.searchContracts(keyword, coopId);

    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(gson.toJson(list));
}
}