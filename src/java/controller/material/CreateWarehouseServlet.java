
package controller.material;

import DAO.MaterialWarehouseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
public class CreateWarehouseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Điều hướng người dùng đến trang nhập liệu
        request.getRequestDispatcher("/supplyQ/create_warehouse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Cấu hình tiếng Việt và lấy Session
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // 2. Kiểm tra đăng nhập và điều hướng tuyệt đối nếu Session hết hạn
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect(request.getContextPath() + "/login/login.jsp");
            return;
        }

        // 3. Xác định coop_id dựa trên loại tài khoản
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Tài khoản HTX (Type 2) dùng chính ID của mình
        }

        // 4. Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String location = request.getParameter("location");
        String description = request.getParameter("description");

        // 5. Gọi DAO để lưu vào Database kèm theo coopId
        boolean success = MaterialWarehouseDAO.INSTANCE.insertWarehouse(name, location, description, coopId);

        if (success) {
            // Chuyển hướng về danh sách sử dụng đường dẫn tuyệt đối
            response.sendRedirect(request.getContextPath() + "/ListWarehouseServlet?status=success");
        } else {
            // Nếu lỗi, quay lại form (RequestDispatcher mặc định hiểu đường dẫn từ gốc ứng dụng nếu có dấu /)
            request.setAttribute("error", "Không thể tạo kho mới. Vui lòng thử lại!");
            request.getRequestDispatcher("/supplyQ/create_warehouse.jsp").forward(request, response);
        }
    }
}
