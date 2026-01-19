package controller.purchase; // Đã đổi package

import DAO.DAOWarehouse;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.StorageWarehouse; // Vẫn dùng model StorageWarehouse bạn đã có

/**
 * Servlet tìm kiếm kho nông sản/thành phẩm (StorageWarehouse)
 */
public class SearchProductWarehouseServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Cấu hình hỗ trợ tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 2. Lấy Session và xác định coop_id
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.getWriter().write("[]");
            return;
        }

        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Loại 2 (HTX) lấy id chính mình
        }

        // 3. Lấy từ khóa tìm kiếm
        String term = request.getParameter("term");
        if (term == null) {
            term = "";
        }

        // 4. Gọi DAO tìm kiếm theo HTX chủ quản
        DAOWarehouse dao = new DAOWarehouse();
        List<StorageWarehouse> list = dao.searchStorageWarehouses(term, coopId);

        // 5. Trả về kết quả JSON
        String json = new Gson().toJson(list);
        response.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
