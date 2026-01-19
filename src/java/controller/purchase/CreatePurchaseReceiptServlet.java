package controller.purchase;

import DAO.DAOPurchase;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreatePurchaseReceiptServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // Lấy dữ liệu từ Form
            Date purchaseDate = Date.valueOf(request.getParameter("purchaseDate"));
            Date expiryDate = Date.valueOf(request.getParameter("expiryDate"));
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            String unit = request.getParameter("unit");
            String note = request.getParameter("note");
            boolean createQR = request.getParameter("createQR") != null;

            // Lấy các mảng dữ liệu chi tiết
            String[] warehouseIds = request.getParameterValues("warehouseId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] buyPrices = request.getParameterValues("buyPrice[]"); // Giá trị thực (đã bỏ dấu chấm)

            double amountPaid = Double.parseDouble(request.getParameter("amountPaid"));

            // Gọi DAO xử lý
            DAOPurchase dao = new DAOPurchase();
            boolean success = dao.createPurchaseTransaction(
                memberId, productId, purchaseDate, expiryDate, unit, note, createQR,
                warehouseIds, quantities, buyPrices, amountPaid
            );

            if (success) {
                response.sendRedirect("purchase/purchase_receipt.jsp?status=success");
            } else {
                response.sendRedirect("purchase/purchase_receipt.jsp?status=error");
            }

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug nếu có
            response.sendRedirect("purchase/purchase_receipt.jsp?status=error");
        }
    }
}