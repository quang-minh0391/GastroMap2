package controller.purchase;

import DAO.DAOFarmProduct;
import com.google.gson.Gson;
import model.FarmProduct;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "SearchProductServlet", urlPatterns = {"/SearchProductServlet"})
public class SearchProductServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    HttpSession session = request.getSession(false);
    // 1. Kiểm tra quyền truy cập
    if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect(request.getContextPath() + "/login/login.jsp");
        return;
    }

    // 2. Lấy coop_id (Nếu là tài khoản HTX - Type 2 thì lấy chính id của họ)
    Integer coopId = (Integer) session.getAttribute("coop_id");
    if (coopId == null || coopId == 0) {
        coopId = (Integer) session.getAttribute("id");
    }

    String term = request.getParameter("term");
    if (term == null) term = "";

    // 3. Gọi DAO tìm kiếm theo HTX
    DAOFarmProduct dao = new DAOFarmProduct();
    List<FarmProduct> products = dao.searchProducts(term, coopId);

    // 4. Trả về kết quả JSON (Sử dụng Gson để tối ưu mã nguồn)
    response.getWriter().write(new Gson().toJson(products));
}
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}