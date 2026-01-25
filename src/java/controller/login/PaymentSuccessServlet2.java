package controller.login;

import DAO.DAOPayment;
import DAO.DAOMember1;
import DAO.PaymentVoucherDAO;
import model.member; // Import model của bạn
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import model.PaymentVoucher;

@WebServlet(name = "payment-success2", urlPatterns = {"/payment-success2"})
public class PaymentSuccessServlet2 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String status = request.getParameter("status");
        String orderCodeRaw = request.getParameter("orderCode");

        if ("PAID".equals(status) && orderCodeRaw != null) {
            long orderCode = Long.parseLong(orderCodeRaw);
            DAOPayment daoP = new DAOPayment();
            DAOMember1 daoM = new DAOMember1();
            long amountLong = daoP.getAmountByOrderCode(orderCode);
            // 1. Cập nhật trạng thái thanh toán trong bảng payments
            daoP.updateStatus(orderCode, "SUCCESS");

            // 2. Lấy ID người dùng và mã gói từ hóa đơn
            int memberId = daoP.getMemberIdByOrderCode(orderCode);
            int planId = daoP.getPlanIdByOrderCode(orderCode);

            if (memberId > 0) {
                // --- TRƯỜNG HỢP GIA HẠN ---
                // Gọi hàm xử lý cộng dồn ngày đã viết ở Bước 1
                daoM.extendMembership(memberId, planId);

                // Cập nhật lại Session để hiển thị thông tin mới nhất
                member updatedUser = daoM.getMemberById(memberId);
                HttpSession session = request.getSession();
                session.setAttribute("user", updatedUser);
// --- B. KẾT NỐI TÀI CHÍNH: TẠO PHIẾU THU ---
                try {
                    PaymentVoucherDAO voucherDao = new PaymentVoucherDAO();
                    PaymentVoucher v = new PaymentVoucher();

                    v.setVoucherCode("PM-" + orderCode); // Mã phiếu dựa trên mã đơn hàng
                    v.setVoucherType("RECEIPT");         // Loại phiếu THU
                    v.setMemberId(memberId);             // Người nộp
                    v.setPartnerId(null);
                    v.setAmount(new BigDecimal(amountLong)); // Số tiền
                    v.setPaymentMethod("Chuyển khoản (PayOS)");
                    v.setDescription("Thu phí gia hạn gói dịch vụ qua cổng thanh toán");
                    v.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                    // Gọi hàm này sẽ tự động INSERT vào payment_vouchers VÀ financial_ledger
                    voucherDao.insertVoucher(v);

                } catch (Exception e) {
                    e.printStackTrace(); // Log lỗi nhưng không chặn luồng chính
                }
                // Chuyển hướng kèm thông báo thành công
                request.setAttribute("message", "Gia hạn thành công! Ngày hết hạn mới: " + updatedUser.getExpiry_date());
                request.getRequestDispatcher("login/login.jsp").forward(request, response);
            } else {
                // --- TRƯỜNG HỢP ĐĂNG KÝ MỚI ---
                request.setAttribute("orderCode", orderCode);
                request.getRequestDispatcher("profile.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("extension.jsp?msg=failed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
