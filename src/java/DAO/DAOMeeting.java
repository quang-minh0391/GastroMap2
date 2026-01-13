package DAO;

import model.Meeting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOMeeting extends DAL.DBContext {

    // 1. Lấy tất cả cuộc họp hiển thị lên trang danh sách
    public List<Meeting> getAllMeetings() {
        List<Meeting> list = new ArrayList<>();
        String sql = "SELECT * FROM meetings ORDER BY id DESC";
        // Sử dụng try-with-resources để tự động đóng Statement và ResultSet
        try (PreparedStatement pre = conn.prepareStatement(sql);
             ResultSet rs = pre.executeQuery()) {
            while (rs.next()) {
                list.add(new Meeting(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("meeting_date"),
                    rs.getString("location"),
                    rs.getInt("member_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới cuộc họp
    public int insertMeeting(Meeting m) {
        String sql = "INSERT INTO meetings (title, description, meeting_date, location, member_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pre.setString(1, m.getTitle());
            pre.setString(2, m.getDescription());
            pre.setString(3, m.getMeeting_date());
            pre.setString(4, m.getLocation());
            pre.setInt(5, m.getMember_id());
            pre.executeUpdate();

            try (ResultSet rs = pre.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 3. Lấy thông tin chi tiết 1 cuộc họp (HÀM NÀY GIÚP HẾT TRANG TRẮNG)
    public Meeting getMeetingById(int id) {
        String sql = "SELECT * FROM meetings WHERE id = ?";
        // Đã sửa lại để dùng biến 'conn' từ DBContext cho đồng bộ
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Meeting(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("meeting_date"),
                        rs.getString("location"),
                        rs.getInt("member_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. Lấy danh sách thành viên cùng HTX để gửi thông báo
    public List<Integer> getMemberIdsToNotify(int creatorMemberId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT id FROM members WHERE member_id = (SELECT member_id FROM members WHERE id = ?) AND id <> ?";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, creatorMemberId);
            pre.setInt(2, creatorMemberId);
            try (ResultSet rs = pre.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}