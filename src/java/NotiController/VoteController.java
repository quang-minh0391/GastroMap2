package NotiController;

import DAO.DAOMeeting;
import DAO.DAOVoting;
import model.VotingQuestion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.Meeting;

@WebServlet(name = "VoteController", urlPatterns = {"/voteControl"})
public class VoteController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if (service == null || service.equals("list")) {
            response.sendRedirect("meetingManager?service=list");
        } else if (service.equals("viewDetail")) {
            String id = request.getParameter("id");
            response.sendRedirect("meetingManager?service=viewDetail&id=" + id);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        DAOVoting daoV = new DAOVoting();
        HttpSession session = request.getSession();

        // Lấy ID người dùng hiện tại
        Integer currentMemberId = (Integer) session.getAttribute("id");
        if (currentMemberId == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        String meetingIdRaw = request.getParameter("meeting_id");
        if (meetingIdRaw == null) {
            response.sendRedirect("meetingManager?service=list");
            return;
        }
        int meetingId = Integer.parseInt(meetingIdRaw);

        if ("createVote".equals(action)) {
            String questionText = request.getParameter("question");
            String[] options = request.getParameterValues("options");

            if (options != null && options.length > 0) {
                model.VotingQuestion vq = new model.VotingQuestion(0, meetingId, currentMemberId, questionText);
                int qId = daoV.insertQuestion(vq);
                if (qId > 0) {
                    daoV.insertOptions(qId, currentMemberId, options);
                }
            }
        } 
        else if ("submitVote".equals(action)) {
    String optionIdRaw = request.getParameter("option_id");
    String questionIdRaw = request.getParameter("question_id"); // Lấy ID câu hỏi từ JSP

    if (optionIdRaw != null && questionIdRaw != null) {
        try {
            int optionId = Integer.parseInt(optionIdRaw);
            int questionId = Integer.parseInt(questionIdRaw);
            
            // Gọi hàm castVote với ĐỦ 3 THAM SỐ
            boolean success = daoV.castVote(currentMemberId, questionId, optionId);
            
            if (success) {
                session.setAttribute("success_msg", "Biểu quyết thành công!");
            } else {
                session.setAttribute("error_msg", "Bạn đã thực hiện biểu quyết này rồi!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

        // Quay lại trang chi tiết cuộc họp
        response.sendRedirect("meetingManager?service=viewDetail&id=" + meetingId);
    }
}