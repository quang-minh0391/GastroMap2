package memberController;

import DAO.DAOMember1;
import model.member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "memberController", urlPatterns = {"/memberManager"})
public class memberController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        DAOMember1 dao = new DAOMember1();
        HttpSession session = request.getSession();

        // 1. Lấy thông tin từ Session
        Integer loginId = (Integer) session.getAttribute("id");
        Integer loginMemberType = (Integer) session.getAttribute("member_type");
        Integer loginCoopId = (Integer) session.getAttribute("coop_id");

        // Kiểm tra đăng nhập
        if (loginId == null || loginMemberType == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // 2. Logic xác định targetCoopId để lọc dữ liệu
        int targetCoopId = -1;
        if (loginMemberType == 2) {
            targetCoopId = loginId;
        } else if (loginMemberType == 3) {
            targetCoopId = (loginCoopId != null) ? loginCoopId : 0;
        } else {
            response.sendRedirect("index.jsp");
            return;
        }

        // 3. Xử lý các dịch vụ GET
        if ("delete".equals(service)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.softDeleteMember(id);
            response.sendRedirect("memberManager?service=list");

        } else if ("add".equals(service)) {
            request.getRequestDispatcher("addMember.jsp").forward(request, response);

        } else {
            // Mặc định: Hiển thị danh sách và tìm kiếm
            String searchName = request.getParameter("searchName");
            if (searchName == null) searchName = "";

            String indexPage = request.getParameter("index");
            if (indexPage == null) indexPage = "1";
            int index = Integer.parseInt(indexPage);
            int pageSize = 5;

            List<member> list = dao.getMembersWithPaging(searchName, targetCoopId, index, pageSize);
            int totalCount = dao.getTotalMembers(searchName, targetCoopId);
            int endPage = (int) Math.ceil((double) totalCount / pageSize);

            request.setAttribute("listM", list);
            request.setAttribute("endP", endPage);
            request.setAttribute("tag", index);
            request.setAttribute("searchName", searchName);
            request.getRequestDispatcher("member.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        DAOMember1 dao = new DAOMember1();
        HttpSession session = request.getSession();

        // Lấy ID người dùng đang đăng nhập từ Session
        Integer loginId = (Integer) session.getAttribute("id");
        if (loginId == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // --- CHỨC NĂNG 1: THÊM MỚI THÀNH VIÊN ---
        if ("insert".equals(service)) {
            Integer loginType = (Integer) session.getAttribute("member_type");
            Integer loginCoopId = (Integer) session.getAttribute("coop_id");

            int selectedType = 1;
            try {
                String typeRaw = request.getParameter("member_type");
                if (typeRaw != null) selectedType = Integer.parseInt(typeRaw);
            } catch (NumberFormatException e) {
                selectedType = 1;
            }

            int assignedCoopId = (loginType == 2) ? loginId : ((loginCoopId != null) ? loginCoopId : 0);

            String user = request.getParameter("username");
            String pass = request.getParameter("password"); // Mật khẩu thuần từ form
            String email = request.getParameter("email");
            String fname = request.getParameter("full_name");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            String now = java.time.LocalDate.now().toString();
            String nowTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String expiry = "9999-01-01";

            // QUAN TRỌNG: Mã hóa mật khẩu trước khi đưa vào đối tượng member
            String hashedPass = dao.hashPassword(pass);

            member newMem = new member(0, user, hashedPass, email, fname, phone, address, 
                                       selectedType, assignedCoopId, "Active", expiry, "Free", now, nowTime);

            int result = dao.addFarmer(newMem);

            if (result > 0) {
                response.sendRedirect("memberManager?service=list");
            } else {
                request.setAttribute("error", "Lỗi: Tên đăng nhập hoặc Email đã tồn tại!");
                request.getRequestDispatcher("addMember.jsp").forward(request, response);
            }
        } 
        
        // --- CHỨC NĂNG 2: ĐỔI MẬT KHẨU (TỪ MODAL) ---
        else if ("updatePassword".equals(service)) {
            String oldPass = request.getParameter("oldPassword");
            String newPass = request.getParameter("newPassword");
            String confirmPass = request.getParameter("confirmPassword");

            // 1. Kiểm tra mật khẩu mới và xác nhận
            if (newPass == null || !newPass.equals(confirmPass)) {
                request.setAttribute("error", "Xác nhận mật khẩu mới không khớp!");
            } 
            // 2. Kiểm tra mật khẩu cũ (DAO tự băm để so sánh)
            else if (!dao.checkOldPassword(loginId, oldPass)) {
                request.setAttribute("error", "Mật khẩu hiện tại không chính xác!");
            } 
            // 3. Thực hiện cập nhật (DAO tự băm mật khẩu mới)
            else {
                if (dao.changePassword(loginId, newPass)) {
                    request.setAttribute("message", "Đổi mật khẩu thành công!");
                } else {
                    request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại sau!");
                }
            }
            // Sau khi xử lý xong, quay lại trang danh sách để hiển thị thông báo
            doGet(request, response);
        }
    }
}