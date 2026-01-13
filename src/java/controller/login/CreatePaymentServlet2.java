package controller.login;

import DAO.DAOPayment;
import DAO.DAOMember1;
import config.PayOSConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Đảm bảo có import này
import java.util.ArrayList;
import java.util.List;
import model.member; // Import model của bạn

import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

@WebServlet(name = "create-payment2", urlPatterns = {"/create-payment2"})
public class CreatePaymentServlet2 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Lấy thông tin gói từ form
            int planId = Integer.parseInt(request.getParameter("plan_id"));
            long amount = (planId == 1) ? 2000L : 2500L; // Sửa lại giá tiền thực tế nếu cần
            long orderCode = System.currentTimeMillis() / 2000L;

            // --- BẮT ĐẦU PHẦN SỬA ĐỔI ---
            // 2. Kiểm tra Session để lấy ID người dùng
            HttpSession session = request.getSession();
            member user = (member) session.getAttribute("user");

            DAOPayment daoP = new DAOPayment();
            if (user != null) {
                // Luồng: GIA HẠN (Đã đăng nhập)
                // Gọi hàm mới có chứa member_id mà bạn vừa tạo
                daoP.createPendingExtension(orderCode, amount, planId, user.getId());
            } else {
                // Luồng: ĐĂNG KÝ MỚI (Chưa có tài khoản)
                // Gọi hàm cũ của bạn
                daoP.createPendingPayment(orderCode, amount, planId);
            }
            // --- KẾT THÚC PHẦN SỬA ĐỔI ---

            // 3. Chuẩn bị dữ liệu cho PayOS
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

            PaymentLinkItem item = PaymentLinkItem.builder()
                    .name("Goi " + (planId == 1 ? "Thang" : "Nam"))
                    .quantity(1)
                    .price(amount)
                    .build();

            List<PaymentLinkItem> itemList = new ArrayList<>();
            itemList.add(item);

            CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .description("Thanh toan dich vu")
                    .items(itemList) 
                    .cancelUrl(baseUrl + "/extension.jsp")
                    .returnUrl(baseUrl + "/payment-success2") // Sẽ xử lý cộng ngày tại đây
                    .build();

            // 4. Tạo link và redirect
            CreatePaymentLinkResponse checkoutData = PayOSConfig.getPayOS().paymentRequests().create(paymentData);
            response.sendRedirect(checkoutData.getCheckoutUrl());

        } catch (Exception e) {
            e.printStackTrace(); 
            response.setContentType("text/html;charset=UTF-8"); 
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
            response.getWriter().println("<h2>Lỗi xảy ra khi tạo link thanh toán</h2>");
            e.printStackTrace(response.getWriter());
        }
    }
}