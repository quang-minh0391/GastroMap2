package controller.login;

import DAO.DAOMember1;
import model.member;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

@WebServlet(name = "loginController", urlPatterns = {"/loginURL"})
public class loginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 60);
        DAOMember1 dao = new DAOMember1();
        String service = request.getParameter("service");

        if (service == null) {
            service = "listUser";
        }

        try {
            if (service.equals("logoutUser")) {
                session.invalidate();
                response.sendRedirect("login/login.jsp");
                return;

            } else if (service.equals("loginUser")) {
                String loginInput = request.getParameter("user");
                String pass = request.getParameter("pass");

                // 1. Thực hiện Login
                member user = dao.login(loginInput, pass);

                if (user != null) {
                    // 2. Kiểm tra trạng thái Status (Active/Inactive/Deleted)
                    if (user.getStatus() != null && (user.getStatus().equalsIgnoreCase("Deleted") || user.getStatus().equalsIgnoreCase("Inactive"))) {
                        request.setAttribute("message", "Tài khoản của bạn đã bị khóa hoặc không còn tồn tại.");
                        dao.dispatch(request, response, "login/login.jsp");
                        return;
                    }

                    LocalDate today = LocalDate.now();

                    // 3. KIỂM TRA HẾT HẠN CHO LOẠI 1 (Nông dân) & 3 (Nhân viên)
                    if (user.getMember_type() == 1 || user.getMember_type() == 3) {
                        member coopOwner = dao.getMemberById(user.getCoop_id());
                        if (coopOwner != null && coopOwner.getExpiry_date() != null) {
                            LocalDate coopExpiry = LocalDate.parse(coopOwner.getExpiry_date());
                            if (coopExpiry.isBefore(today)) {
                                // Gửi cờ coopExpired để JSP hiện thông báo Error (không có nút gia hạn)
                                request.setAttribute("coopExpired", true);
                                request.setAttribute("message", "Hợp tác xã chủ quản đã hết hạn dịch vụ.");
                                dao.dispatch(request, response, "login/login.jsp");
                                return;
                            }
                        }
                    }

                    // 4. KIỂM TRA HẾT HẠN CHO LOẠI 2 (Chủ Hợp tác xã)
                    if (user.getMember_type() == 2) {
                        if (user.getExpiry_date() != null) {
                            LocalDate personalExpiry = LocalDate.parse(user.getExpiry_date());
                            if (personalExpiry.isBefore(today)) {
                                // Gửi cờ accountExpired để JSP hiện thông báo Warning (có nút gia hạn)
                                request.setAttribute("accountExpired", true);
                                request.setAttribute("message", "Gói dịch vụ của bạn đã hết hạn.");
                                dao.dispatch(request, response, "login/login.jsp");
                                return;
                            }
                        }
                    }

                    // 5. ĐĂNG NHẬP THÀNH CÔNG - Thiết lập Session
                    session.setAttribute("user", user);
                    session.setAttribute("id", user.getId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("email", user.getEmail());
                    session.setAttribute("full_name", user.getFull_name());
                    session.setAttribute("phone", user.getPhone());
                    session.setAttribute("address", user.getAddress());
                    session.setAttribute("member_type", user.getMember_type());
                    session.setAttribute("coop_id", user.getCoop_id());
                    session.setAttribute("status", user.getStatus());
                    session.setAttribute("plan_type", user.getPlan_type());
                    session.setAttribute("expiry_date", user.getExpiry_date());

                    // Điều hướng vào trang chủ
                    response.sendRedirect("index.jsp");

                } else {
                    // Sai User hoặc Pass
                    request.setAttribute("message", "Email hoặc mật khẩu không đúng.");
                    dao.dispatch(request, response, "login/login.jsp");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra trong quá trình xử lý.");
            dao.dispatch(request, response, "login/login.jsp");
        }
    }

    @Override
    public String getServletInfo() {
        return "Login Controller with Expiry Check Logic";
    }
}