package DAO;

import DAL.DBContext;
import java.sql.*;

public class DAOPayment extends DBContext {
    // Truyền trực tiếp long và int vào đây
    public void createPendingPayment(long orderCode, long amount, int planId) {
    String sql = "INSERT INTO payments (plan_id, amount, order_code, status) VALUES (?, ?, ?, 'PENDING')";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, planId);
        ps.setLong(2, amount); 
        ps.setLong(3, orderCode); 
        ps.executeUpdate();
    } catch (Exception e) { e.printStackTrace(); }
} 

    public void updateStatus(long orderCode, String status) {
        String sql = "UPDATE payments SET status = ?, payment_date = CURRENT_TIMESTAMP WHERE order_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, orderCode);
            ps.executeUpdate();
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    } 
    public void createPendingExtension(long orderCode, long amount, int planId, int memberId) {
    String sql = "INSERT INTO payments (plan_id, amount, order_code, status, member_id) VALUES (?, ?, ?, 'PENDING', ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, planId);
        ps.setLong(2, amount); 
        ps.setLong(3, orderCode); 
        ps.setInt(4, memberId); // Lưu ID người đang gia hạn
        ps.executeUpdate();
    } catch (Exception e) { 
        e.printStackTrace(); 
    }
}
    public int getMemberIdByOrderCode(long orderCode) {
    int memberId = 0; // Mặc định là 0 nếu không tìm thấy (hoặc là khách vãng lai đăng ký mới)
    String sql = "SELECT member_id FROM payments WHERE order_code = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setLong(1, orderCode);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Sử dụng getObject để kiểm tra NULL vì member_id trong DB có thể NULL
                Object val = rs.getObject("member_id");
                if (val != null) {
                    memberId = (Integer) val;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return memberId;
}
    public int getPlanIdByOrderCode(long orderCode) {
        int planId = 0;
        String sql = "SELECT plan_id FROM payments WHERE order_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                planId = rs.getInt("plan_id");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return planId;
    }
    // Hàm dành riêng cho GIA HẠN (đã có member_id)
     // Thêm hàm này vào DAOPayment.java
    public long getAmountByOrderCode(long orderCode) {
        long amount = 0;
        String sql = "SELECT amount FROM payments WHERE order_code = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    amount = rs.getLong("amount");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }

}