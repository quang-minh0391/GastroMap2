package memberController;

import DAO.DAOMember1;
import model.member;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet(name = "memberController", urlPatterns = {"/memberManager"})
public class memberController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        DAOMember1 dao = new DAOMember1();
        HttpSession session = request.getSession();

        // 1. Lấy thông tin từ Session (đã lưu từ loginController)
        Integer loginId = (Integer) session.getAttribute("id");
        Integer loginMemberType = (Integer) session.getAttribute("member_type");
        Integer loginCoopId = (Integer) session.getAttribute("coop_id");

        // Kiểm tra đăng nhập
        if (loginId == null || loginMemberType == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // 2. Logic xác định targetCoopId (HTX mục tiêu để lọc dữ liệu)
        int targetCoopId = -1;
        if (loginMemberType == 2) {
            // Type 2 (Chủ HTX): Quản lý thành viên có coop_id = ID của chính mình
            targetCoopId = loginId;
        } else if (loginMemberType == 3) {
            // Type 3 (Nhân viên HTX): Quản lý thành viên có coop_id = coop_id của mình
            targetCoopId = (loginCoopId != null) ? loginCoopId : 0;
        } else {
            // Các quyền khác không được phép truy cập trang quản lý này
            response.sendRedirect("index.jsp"); 
            return;
        }

        // 3. Xử lý các dịch vụ (Services)
        if ("delete".equals(service)) {
            // --- CHỨC NĂNG ẨN THÀNH VIÊN ---
            int id = Integer.parseInt(request.getParameter("id"));
            dao.softDeleteMember(id);
            response.sendRedirect("memberManager?service=list");

        } else if ("add".equals(service)) {
            // --- CHUYỂN SANG TRANG THÊM MỚI ---
            request.getRequestDispatcher("addMember.jsp").forward(request, response);

        } else {
            // --- CHỨC NĂNG HIỂN THỊ & TÌM KIẾM (Mặc định) ---
            String searchName = request.getParameter("searchName");
            if (searchName == null) searchName = "";

            String indexPage = request.getParameter("index");
            if (indexPage == null) indexPage = "1";
            int index = Integer.parseInt(indexPage);
            int pageSize = 5; // Số dòng trên mỗi trang

            // Lấy danh sách theo targetCoopId đã xác định ở trên
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

    if ("insert".equals(service)) {
        // 1. Kiểm tra quyền từ Session (Người đang thực hiện thêm)
        Integer loginId = (Integer) session.getAttribute("id");
        Integer loginType = (Integer) session.getAttribute("member_type");
        Integer loginCoopId = (Integer) session.getAttribute("coop_id");

        if (loginId == null || loginType == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // 2. Lấy member_type động từ Form (Giá trị 1: Thành viên, 3: Quản lý)
        int selectedType = 1; // Mặc định là 1 nếu có lỗi
        try {
            String typeRaw = request.getParameter("member_type");
            if (typeRaw != null) {
                selectedType = Integer.parseInt(typeRaw);
            }
        } catch (NumberFormatException e) {
            selectedType = 1; 
        }

        // 3. Xác định coop_id để gắn thành viên mới vào HTX tương ứng
        int assignedCoopId = 0;
        if (loginType == 2) {
            // Nếu Chủ HTX tạo: Gán coop_id = ID của chính Chủ HTX đó
            assignedCoopId = loginId; 
        } else if (loginType == 3) {
            // Nếu Nhân viên tạo: Gán coop_id = coop_id của Nhân viên đó
            assignedCoopId = (loginCoopId != null) ? loginCoopId : 0;
        }

        // 4. Lấy các thông tin cá nhân khác từ Form
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String email = request.getParameter("email");
        String fname = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // 5. Chuẩn bị dữ liệu thời gian theo yêu cầu
        String now = java.time.LocalDate.now().toString();
        String nowTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        
        // Cố định ngày hết hạn vĩnh viễn
        String expiry = "9999-01-01"; 

        // 6. Tạo đối tượng member mới với selectedType và expiry đã xử lý
        // Thứ tự tham số: id, user, pass, email, fname, phone, address, type, coop_id, status, expiry, plan, joined, created
        model.member newMem = new model.member(
            0, 
            user, 
            pass, 
            email, 
            fname, 
            phone, 
            address, 
            selectedType,  // Truyền loại đã chọn (1 hoặc 3)
            assignedCoopId, 
            "Active", 
            expiry,        // Truyền "9999-01-01"
            "Free", 
            now, 
            nowTime
        );

        // 7. Gọi DAO để thực hiện câu lệnh INSERT
        int result = dao.addFarmer(newMem);

        if (result > 0) {
            // Thành công: Quay về danh sách
            response.sendRedirect("memberManager?service=list");
        } else {
            // Thất bại: Ở lại trang thêm và báo lỗi
            request.setAttribute("error", "Lỗi: Không thể thêm thành viên. Vui lòng kiểm tra lại Tên đăng nhập hoặc Email!");
            request.getRequestDispatcher("addMember.jsp").forward(request, response);
        }
    }
}
}