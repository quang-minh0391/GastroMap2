package controller.login;

import DAO.DAOPayment;
import DAO.PaymentVoucherDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Timestamp;
import model.PaymentVoucher;

@WebServlet(name = "payment-success", urlPatterns = {"/payment-success"})
public class PaymentSuccessServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String status = request.getParameter("status");
        String orderCodeRaw = request.getParameter("orderCode");

        if ("PAID".equals(status) && orderCodeRaw != null) {
            long orderCode = Long.parseLong(orderCodeRaw);
            // Cập nhật trạng thái thanh toán thành SUCCESS
//            new DAOPayment().updateStatus(orderCode, "SUCCESS");
DAOPayment daoP = new DAOPayment();
            
            // 1. Cập nhật trạng thái
            daoP.updateStatus(orderCode, "SUCCESS");
            
            // 2. Lấy số tiền
            long amountLong = daoP.getAmountByOrderCode(orderCode);
            // --- KẾT NỐI TÀI CHÍNH ---
            try {
                PaymentVoucherDAO voucherDao = new PaymentVoucherDAO();
                PaymentVoucher v = new PaymentVoucher();
                
                v.setVoucherCode("REG-" + orderCode);
                v.setVoucherType("RECEIPT"); // Thu tiền
                v.setMemberId(null);         // Chưa có ID thành viên
                v.setPartnerId(null);
                v.setAmount(new BigDecimal(amountLong));
                v.setPaymentMethod("Chuyển khoản (PayOS)");
                v.setDescription("Thu phí đăng ký thành viên mới (Chờ kích hoạt)");
                v.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                // Ghi vào sổ cái tài chính
                voucherDao.insertVoucher(v);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            // -------------------------
            // Chuyển sang Register.jsp và cầm theo orderCode
            request.setAttribute("orderCode", orderCode);
            request.getRequestDispatcher("login/Register.jsp").forward(request, response);
        } else {
            response.sendRedirect("select_plan.jsp?msg=failed");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thông thường PayOS trả về qua GET, nhưng ta vẫn giữ doPost để tránh lỗi
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Xử lý kết quả thanh toán từ PayOS";
    }
}