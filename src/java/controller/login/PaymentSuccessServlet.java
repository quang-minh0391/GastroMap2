package controller.login;

import DAO.DAOPayment;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
            new DAOPayment().updateStatus(orderCode, "SUCCESS");

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