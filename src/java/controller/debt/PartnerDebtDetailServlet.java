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

        // Lấy các thông tin cần thiết từ Session
        Integer userId = (Integer) session.getAttribute("id");
        Integer coopIdFromSession = (Integer) session.getAttribute("coop_id");
        Integer memberType = (Integer) session.getAttribute("member_type");
        String pIdStr = request.getParameter("partnerId");

        // 1. Kiểm tra đăng nhập
        if (userId == null || memberType == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // 2. Xác định ID HTX thực tế để truy vấn dữ liệu
        int effectiveCoopId;
        if (memberType == 2) {
            // Nếu là Chủ HTX: Dữ liệu thuộc về chính ID của họ
            effectiveCoopId = userId;
        } else {
            // Nếu là Nhân viên (Type 3): Dữ liệu thuộc về Hợp tác xã chủ quản
            effectiveCoopId = (coopIdFromSession != null) ? coopIdFromSession : 0;
        }

        if (effectiveCoopId == 0 || pIdStr == null) {
            response.sendRedirect("ListPartnerServlet");
            return;
        }

        try {
            int partnerId = Integer.parseInt(pIdStr);
            DebtDAO dao = new DebtDAO();

            // Luôn dùng effectiveCoopId để truy vấn lịch sử công nợ
            request.setAttribute("history", dao.getPartnerTransactionHistory(effectiveCoopId, partnerId));
            request.setAttribute("partnerId", partnerId);

            request.getRequestDispatcher("/debt/partner_debt_detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("ListPartnerServlet");
        }
    }
}
