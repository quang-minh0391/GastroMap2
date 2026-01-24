package DAO;

import DAL.DBContext;
import model.VotingQuestion;
import model.VotingOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOVoting extends DBContext {

    // 1. Lấy danh sách câu hỏi CÙNG VỚI các phương án
    public List<VotingQuestion> getQuestionsByMeetingId(int meetingId) {
        List<VotingQuestion> list = new ArrayList<>();
        String sql = "SELECT * FROM voting_questions WHERE meeting_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, meetingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VotingQuestion vq = new VotingQuestion(
                        rs.getInt("id"),
                        rs.getInt("meeting_id"),
                        rs.getInt("member_id"),
                        rs.getString("question_text")
                    );
                    // Lấy options kèm theo số phiếu bầu
                    vq.setOptions(getOptionsByQuestionId(vq.getId())); 
                    list.add(vq);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy danh sách phương án cho 1 câu hỏi (ĐÃ SỬA ĐỂ LẤY VOTE_COUNT)
    public List<VotingOption> getOptionsByQuestionId(int questionId) {
        List<VotingOption> options = new ArrayList<>();
        // Sử dụng LEFT JOIN để đếm số lượng bản ghi trong bảng voting_results
        String sql = "SELECT o.*, COUNT(r.id) AS total_votes " +
                     "FROM voting_options o " +
                     "LEFT JOIN voting_results r ON o.id = r.option_id " +
                     "WHERE o.question_id = ? " +
                     "GROUP BY o.id, o.question_id, o.member_id, o.option_text";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Truyền thêm tham số rs.getInt("total_votes") vào cuối
                    options.add(new VotingOption(
                        rs.getInt("id"),
                        rs.getInt("question_id"),
                        rs.getInt("member_id"),
                        rs.getString("option_text"),
                        rs.getInt("total_votes") 
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return options;
    }

    // 3. Tạo câu hỏi biểu quyết mới
    public int insertQuestion(VotingQuestion q) {
        String sql = "INSERT INTO voting_questions (meeting_id, question_text, member_id) VALUES (?, ?, ?)";
        try (PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setInt(1, q.getMeeting_id());
            pre.setString(2, q.getQuestion_text());
            pre.setInt(3, q.getMember_id());
            pre.executeUpdate();
            
            try (ResultSet rs = pre.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. Thêm các phương án
    public void insertOptions(int questionId, int creatorId, String[] options) {
        String sql = "INSERT INTO voting_options (question_id, option_text, member_id) VALUES (?, ?, ?)";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            for (String opt : options) {
                pre.setInt(1, questionId);
                pre.setString(2, opt);
                pre.setInt(3, creatorId);
                pre.addBatch();
            }
            pre.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Lưu kết quả bầu chọn
    public boolean castVote(int memberId, int questionId, int optionId) {
    // Thêm question_id vào câu lệnh INSERT
    String sql = "INSERT INTO voting_results (member_id, question_id, option_id, voted_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
    try (PreparedStatement pre = conn.prepareStatement(sql)) {
        pre.setInt(1, memberId);
        pre.setInt(2, questionId);
        pre.setInt(3, optionId);
        
        return pre.executeUpdate() > 0;
    } catch (SQLException e) { 
        // Nếu vi phạm UNIQUE constraint, chương trình sẽ nhảy vào đây
        System.out.println("Lỗi: Tài khoản này đã biểu quyết cho câu hỏi này rồi!");
    }
    return false;
}
}