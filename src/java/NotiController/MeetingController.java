package NotiController;

import DAO.DAOMeeting;
import DAO.DAONotification;
import DAO.DAOVoting; 
import model.Meeting;
import model.Notification;
import model.VotingQuestion; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MeetingController", urlPatterns = {"/meetingManager"})
public class MeetingController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        DAOMeeting daoM = new DAOMeeting();
        DAOVoting daoV = new DAOVoting();
        String service = request.getParameter("service");

        if (service == null || service.equals("list")) {
            List<Meeting> list = daoM.getAllMeetings();
            request.setAttribute("listMeetings", list);
            request.getRequestDispatcher("meeting_list.jsp").forward(request, response);
        } 
        else if (service.equals("viewDetail")) {
    try {
        String idRaw = request.getParameter("id");
        System.out.println("DEBUG: Dang vao hop voi ID = " + idRaw); // Kiểm tra xem ID có lấy được không

        if (idRaw == null) {
            System.out.println("LOI: ID bi null!");
            response.sendRedirect("meetingManager?service=list");
            return;
        }

        int id = Integer.parseInt(idRaw);
        Meeting meeting = daoM.getMeetingById(id);
        
        if (meeting != null) {
            // Lấy danh sách câu hỏi biểu quyết
            List<VotingQuestion> listQuestions = daoV.getQuestionsByMeetingId(id);
            
            request.setAttribute("meeting", meeting);
            request.setAttribute("listQuestions", listQuestions);
            request.getRequestDispatcher("meeting_detail.jsp").forward(request, response);
        } else {
            System.out.println("LOI: Khong tim thay cuoc hop voi ID: " + id);
            response.sendRedirect("meetingManager?service=list");
        }
    } catch (Exception e) {
        // QUAN TRỌNG: Thay đổi dòng này để hiện lỗi ra màn hình Console
        System.out.println("====== LOI CHI TIET TAI DAY ======");
        e.printStackTrace(); 
        System.out.println("==================================");
        
        // Tạm thời bỏ redirect để trình duyệt đứng im tại chỗ cho bạn đọc log
        // response.sendRedirect("meetingManager?service=list");
    }
}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String service = request.getParameter("service");
        DAOMeeting daoM = new DAOMeeting();

        if ("addMeeting".equals(service)) {
            HttpSession session = request.getSession();
            
            // --- SỬA TẠI ĐÂY: Thay "userId" thành "id" để khớp với loginController ---
            Integer currentUserId = (Integer) session.getAttribute("id");
            
            // Nếu không có session (chưa đăng nhập), đẩy về trang login
            if (currentUserId == null) {
                response.sendRedirect("login/login.jsp");
                return;
            }

            String title = request.getParameter("title");
            String desc = request.getParameter("description");
            String date = request.getParameter("meeting_date");
            String loc = request.getParameter("location");

            // Tạo đối tượng Meeting với ID người tạo lấy từ session
            Meeting m = new Meeting(0, title, desc, date, loc, currentUserId);
            int meetingId = daoM.insertMeeting(m);

            if (meetingId > 0) {
                try {
                    DAONotification daoN = new DAONotification();
                    // Thông báo cho các thành viên liên quan
                    List<Integer> members = daoM.getMemberIdsToNotify(currentUserId);
                    for (Integer mId : members) {
                        daoN.insertNotification(new model.Notification(0, mId, "MEETING", meetingId, "Họp mới: " + title, "Unread", ""));
                    }
                    // Gửi thông báo qua WebSocket
                    websocket.NotificationWebSocket.broadcast("MEETING_NEW:" + title);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            response.sendRedirect("meetingManager?service=list");
        }
    }
}