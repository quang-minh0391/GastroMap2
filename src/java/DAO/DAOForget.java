package DAO;

import DAL.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.member; // Giả sử model của bạn tên là member

public class DAOForget extends DBContext {

    // Kiểm tra xem email có tồn tại trong bảng members hay không
    public member checkEmail(String email) {
        try {
            // Đổi bảng thành 'members'
            String sql = "SELECT * FROM members WHERE email = ?";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                member user = new member();
                // Cập nhật mapping theo các thuộc tính mới của bảng members
                user.setId(rs.getInt("id")); 
                user.setUsername(rs.getString("username")); 
                user.setPassword(rs.getString("password")); 
                user.setEmail(rs.getString("email"));
                user.setFull_name(rs.getString("full_name"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setMember_type(rs.getInt("member_type"));
                
                // Xử lý coop_id (có thể null)
                int coopId = rs.getInt("coop_id");
                user.setCoop_id(rs.wasNull() ? null : coopId);
                
                user.setStatus(rs.getString("status"));
                user.setExpiry_date(rs.getString("expiry_date"));
                user.setPlan_type(rs.getString("plan_type"));
                user.setJoined_date(rs.getString("joined_date"));
                user.setCreated_at(rs.getString("created_at"));
            
                return user;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOForget.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; 
    }

    // Cập nhật mật khẩu mới cho thành viên dựa trên email
    public boolean updatePassword(String email, String password) {
    String sql = "UPDATE members SET password = ? WHERE email = ?";
    try (PreparedStatement pre = conn.prepareStatement(sql)) {
        pre.setString(1, password);
        pre.setString(2, email);
        
        int rowsUpdated = pre.executeUpdate();
        
        // In ra để debug
        System.out.println("DEBUG: Dang update cho email: " + email);
        System.out.println("DEBUG: So dong bi anh huong: " + rowsUpdated);
        
        return rowsUpdated > 0; 
    } catch (SQLException ex) {
        // In loi chi tiet ra Console
        System.out.println("LỖI SQL: " + ex.getMessage());
        ex.printStackTrace(); 
        return false; 
    }
}

    public static void main(String[] args) {
    DAOForget dao = new DAOForget();

    // 1. THAY ĐỔI: Sử dụng một email thực sự có trong bảng members của bạn
    String emailTest = "quangminhh0301@gmail.com"; 
    
    // 2. THAY ĐỔI: Mật khẩu test (giả sử đã hash hoặc chuỗi bất kỳ để test)
    String newPassTest = "123456"; 

    System.out.println("=== BẮT ĐẦU KIỂM TRA UPDATE PASSWORD ===");
    
    // Bước 1: Kiểm tra xem email có tồn tại không
    member m = dao.checkEmail(emailTest);
    
    if (m != null) {
        System.out.println("-> Bước 1: Tìm thấy người dùng: " + m.getFull_name());
        System.out.println("-> Bước 2: Đang tiến hành cập nhật mật khẩu...");
        
        // Bước 2: Gọi hàm update
        boolean isSuccess = dao.updatePassword(emailTest, newPassTest);
        
        if (isSuccess) {
            System.out.println("=> KẾT QUẢ: Cập nhật THÀNH CÔNG!");
        } else {
            System.out.println("=> KẾT QUẢ: Cập nhật THẤT BẠI.");
            System.out.println("Lưu ý: Nếu 'So dong bi anh huong' là 0, hãy kiểm tra lại email có khoảng trắng thừa không.");
        }
    } else {
        System.out.println("=> KẾT QUẢ: Thất bại ngay từ đầu - Email '" + emailTest + "' không tồn tại trong DB.");
    }
    System.out.println("=== KẾT THÚC KIỂM TRA ===");
}
}