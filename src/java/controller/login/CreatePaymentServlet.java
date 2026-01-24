package controller.login;

import DAO.DAOPayment;
import config.PayOSConfig;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

// SỬA LẠI IMPORT THEO ĐÚNG FILE JAR BẠN CHỤP
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

@WebServlet(name = "create-payment", urlPatterns = {"/create-payment"})
public class CreatePaymentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int planId = Integer.parseInt(request.getParameter("plan_id"));
            long amount = (planId == 1) ? 2000L : 2500L; 
            long orderCode = System.currentTimeMillis() / 2000L;

            new DAOPayment().createPendingPayment(orderCode, amount, planId);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

            PaymentLinkItem item = PaymentLinkItem.builder()
                    .name("Goi " + planId)
                    .quantity(1)
                    .price(amount)
                    .build();

            List<PaymentLinkItem> itemList = new ArrayList<>();
            itemList.add(item);

            // Sử dụng đúng class CreatePaymentLinkRequest
            CreatePaymentLinkRequest paymentData = CreatePaymentLinkRequest.builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .description("Thanh toan")
                    .items(itemList) 
                    .cancelUrl(baseUrl + "/select_plan.jsp")
                    .returnUrl(baseUrl + "/payment-success")
                    .build();

            // Gọi hàm từ PayOSConfig
          CreatePaymentLinkResponse checkoutData =
    PayOSConfig.getPayOS().paymentRequests().create(paymentData);



            
            response.sendRedirect(checkoutData.getCheckoutUrl());

        } catch (Exception e) {
            e.printStackTrace(); response.setContentType("text/html;charset=UTF-8"); response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); response.getWriter().println("<h2>Lỗi xảy ra khi tạo link thanh toán</h2>"); response.getWriter().println("<pre>"); e.printStackTrace(response.getWriter()); response.getWriter().println("</pre>");
        }
    }
}