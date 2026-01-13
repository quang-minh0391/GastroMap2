package controller.login;

import DAO.DAOMember1;
import DAO.DAOPayment; // Thêm import này
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.member;

@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String service = request.getParameter("service");
        DAOMember1 dao = new DAOMember1();
        DAOPayment payDao = new DAOPayment(); // Khởi tạo DAO Payment

        if (service == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        if (service.equals("registerUser")) {
            // 1. Lấy dữ liệu từ JSP
            String user = request.getParameter("username");
            String email = request.getParameter("email");
            String pass = request.getParameter("pass");
            String repass = request.getParameter("repass");
            String fullName = request.getParameter("full_name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            
            // LẤY MÃ ĐƠN HÀNG TỪ FORM ẨN
            String orderCodeRaw = request.getParameter("orderCode");

            // 2. Kiểm tra logic
            if (!pass.equals(repass)) {
                request.setAttribute("message", "Mật khẩu xác nhận không chính xác!");
                request.getRequestDispatcher("login/Register.jsp").forward(request, response);
                return;
            }

            if (dao.isEmailExists(email)) {
                request.setAttribute("message", "Email này đã được sử dụng!");
                request.getRequestDispatcher("login/Register.jsp").forward(request, response);
                return;
            }

            // 3. CHUẨN BỊ DỮ LIỆU NGÀY THÁNG DỰA TRÊN THANH TOÁN
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateStr = now.format(formatter);
            
            String expiryDate = "";
            String planName = "Trial Plan"; 

            // Kiểm tra xem khách có vừa thanh toán thành công không
            if (orderCodeRaw != null && !orderCodeRaw.trim().isEmpty()) {
                try {
                    long orderCode = Long.parseLong(orderCodeRaw);
                    int planId = payDao.getPlanIdByOrderCode(orderCode);

                    if (planId == 1) { // Gói Tháng (ID 1)
                        expiryDate = now.plusMonths(1).format(formatter);
                        planName = "Monthly Plan";
                    } else if (planId == 2) { // Gói Năm (ID 2)
                        expiryDate = now.plusYears(1).format(formatter);
                        planName = "Yearly Plan";
                    } else {
                        // Nếu không tìm thấy gói cụ thể, mặc định cho 7 ngày dùng thử
                        expiryDate = now.plusDays(7).format(formatter);
                    }
                } catch (Exception e) {
                    expiryDate = now.plusDays(7).format(formatter);
                }
            } else {
                // Đăng ký thường, không qua link thanh toán
                expiryDate = now.plusDays(7).format(formatter);
                planName = "Free Trial";
            }

            // 4. Tạo đối tượng Member
            member newMem = new member(
                0, user, pass, email, fullName, phone, address, 
                2, // member_type
                0, // coop_id
                "Active", 
                expiryDate, // Ngày đã được tính toán ở bước 3
                planName,   // Tên gói tương ứng
                dateStr,    // joined_date
                dateStr     // created_at
            );

            // 5. Lưu vào Database
            int resultId = dao.insertmember(newMem);

            if (resultId > 0) {
                // LIÊN KẾT MEMBER VỚI PAYMENT (Để biết đơn hàng này là của ai)
                if (orderCodeRaw != null && !orderCodeRaw.isEmpty()) {
                    dao.linkMemberToPayment(resultId, orderCodeRaw);
                }
                
                request.getSession().setAttribute("message", "Đăng ký thành công gói " + planName + "! Hạn dùng đến: " + expiryDate);
                response.sendRedirect(request.getContextPath() + "/login/login.jsp");
            } else {
                request.setAttribute("message", "Lỗi khi lưu dữ liệu vào hệ thống!");
                request.getRequestDispatcher("login/Register.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}