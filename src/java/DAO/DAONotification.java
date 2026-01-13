package DAO;

import DAL.DBContext;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAONotification extends DBContext {

    // Lưu thông báo mới vào DB
    public void insertNotification(Notification n) {
        String sql = "INSERT INTO notifications (member_id, type, reference_id, message, status) VALUES (?, ?, ?, ?, 'Unread')";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, n.getMember_id());
            pre.setString(2, n.getType());
            pre.setInt(3, n.getReference_id());
            pre.setString(4, n.getMessage());
            pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách thông báo chưa đọc của 1 thành viên
    public List<Notification> getUnreadByMember(int memberId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE member_id = ? AND status = 'Unread' ORDER BY created_at DESC";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, memberId);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                list.add(new Notification(
                    rs.getInt("id"), rs.getInt("member_id"), rs.getString("type"),
                    rs.getInt("reference_id"), rs.getString("message"),
                    rs.getString("status"), rs.getString("created_at")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}