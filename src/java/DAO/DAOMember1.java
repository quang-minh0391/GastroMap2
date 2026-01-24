/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAL.DBContext;
import model.member;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Types;
import java.time.LocalDate;

public class DAOMember1 extends DBContext {

    public member getCurrentmember(String username) {
        String sql = "SELECT * FROM members WHERE username = ?";
        member user = null;

        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, username);
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                user = new member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("member_type"),
                        rs.getInt("coop_id"),
                        rs.getString("status"),
                        rs.getString("expiry_date"),
                        rs.getString("plan_type"),
                        rs.getString("joined_date"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public int addFarmer(member user) {
        int generatedId = -1;
        // Câu SQL giống hệt nhưng logic xử lý tham số sẽ chuẩn xác cho thành viên
        String sql = "INSERT INTO members (username, password, email, full_name, phone, address, member_type, coop_id, status, expiry_date, plan_type, joined_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement pre = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            pre.setString(1, user.getUsername());
            pre.setString(2, hashPassword(user.getPassword()));
            pre.setString(3, user.getEmail());
            pre.setString(4, user.getFull_name());
            pre.setString(5, user.getPhone());
            pre.setString(6, user.getAddress());
            pre.setInt(7, user.getMember_type()); // Cố định luôn là 1 (Farmer) cho chắc chắn
            pre.setInt(8, user.getCoop_id()); // Lấy đúng ID của HTX từ Controller truyền sang
            pre.setString(9, "Active");      // Mặc định là hoạt động
            pre.setString(10, user.getExpiry_date());
            pre.setString(11, "Free");       // Hoặc gói mặc định của HTX
            pre.setString(12, user.getJoined_date());
            pre.setString(13, user.getCreated_at());

            pre.executeUpdate();
            java.sql.ResultSet rs = pre.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (java.sql.SQLException ex) {
            System.err.println("Lỗi tại addFarmer: " + ex.getMessage());
            ex.printStackTrace();
        }
        return generatedId;
    }
    // Lấy danh sách member có phân trang và tìm kiếm theo tên, lọc theo coop_id của người đang đăng nhập

    public List<member> getMembersWithPaging(String name, int coopId, int index, int pageSize) {
        List<member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE full_name LIKE ? AND coop_id = ? AND member_type = 1 and status = 'Active' "
                + "ORDER BY id DESC LIMIT ?, ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, "%" + name + "%");
            pre.setInt(2, coopId);
            pre.setInt(3, (index - 1) * pageSize);
            pre.setInt(4, pageSize);
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                list.add(new member(
                        rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("full_name"), rs.getString("phone"),
                        rs.getString("address"), rs.getInt("member_type"), rs.getInt("coop_id"),
                        rs.getString("status"), rs.getString("expiry_date"), rs.getString("plan_type"),
                        rs.getString("joined_date"), rs.getString("created_at")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

// Đếm tổng số member để tính số trang
    public int getTotalMembers(String name, int coopId) {
        String sql = "SELECT COUNT(*) FROM members WHERE full_name LIKE ? AND coop_id = ? AND member_type = 1";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, "%" + name + "%");
            pre.setInt(2, coopId);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int softDeleteMember(int id) {
        int n = 0;
        String sql = "UPDATE members SET status = 'Deleted' WHERE id = ?";
        try {
            java.sql.PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, id);
            n = pre.executeUpdate();
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        return n;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }

    public member login(String email, String password) {
        String sql = "SELECT * FROM members WHERE email=? AND password=?";
        String hashedPassword = hashPassword(password);

        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, email);
            pre.setString(2, hashedPassword);
            ResultSet rs = pre.executeQuery();

            if (rs.next()) {
                return new member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("member_type"),
                        rs.getInt("coop_id"),
                        rs.getString("status"),
                        rs.getString("expiry_date"),
                        rs.getString("plan_type"),
                        rs.getString("joined_date"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAOMember1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean updatePassword(int id, String newPassword) {
        String sql = "UPDATE members SET password = ? WHERE id = ?";
        String hashedPassword = hashPassword(newPassword);

        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, hashedPassword);
            pre.setInt(2, id);

            int rowsUpdated = pre.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException ex) {
            Logger.getLogger(DAOMember1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

//    public int insertmember(member user) {
//        int n = 0;
//        String sql = "INSERT INTO members (username, password, email, full_name, phone, address, member_type, coop_id, status, expiry_date, plan_type, joined_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try {
//            PreparedStatement pre = conn.prepareStatement(sql);
//            pre.setString(1, user.getUsername());
//            pre.setString(2, hashPassword(user.getPassword()));
//            pre.setString(3, user.getEmail());
//            pre.setString(4, user.getFull_name());
//            pre.setString(5, user.getPhone());
//            pre.setString(6, user.getAddress());
//            pre.setInt(7, user.getMember_type());
//            if (user.getCoop_id() == 0) {
//                pre.setNull(8, Types.INTEGER);
//            } else {
//                pre.setInt(8, user.getCoop_id());
//            }
//            pre.setString(9, user.getStatus());
//            pre.setString(10, user.getExpiry_date());
//            pre.setString(11, user.getPlan_type());
//            pre.setString(12, user.getJoined_date());
//            pre.setString(13, user.getCreated_at());
//            n = pre.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(DAOMember1.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return n;
//    }
    public int insertmember(member user) {
        int generatedId = -1;
        String sql = "INSERT INTO members (username, password, email, full_name, phone, address, member_type, coop_id, status, expiry_date, plan_type, joined_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            java.sql.PreparedStatement pre = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            pre.setString(1, user.getUsername());
            pre.setString(2, hashPassword(user.getPassword())); // Bạn nên hash password ở đây nếu cần
            pre.setString(3, user.getEmail());
            pre.setString(4, user.getFull_name());
            pre.setString(5, user.getPhone());
            pre.setString(6, user.getAddress());
            pre.setInt(7, user.getMember_type());
            pre.setNull(8, java.sql.Types.INTEGER);
            pre.setString(9, user.getStatus());
            pre.setString(10, user.getExpiry_date());
            pre.setString(11, user.getPlan_type());
            pre.setString(12, user.getJoined_date());
            pre.setString(13, user.getCreated_at());

            pre.executeUpdate();
            java.sql.ResultSet rs = pre.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        return generatedId;
    }

    public int updatemember(member user) {
        int n = 0;
        String sql = "UPDATE members SET username=?, password=?, email=?, full_name=?, phone=?, address=?, member_type=?, coop_id=?, status=?, expiry_date=?, plan_type=?, joined_date=? WHERE id=?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, user.getUsername());
            pre.setString(2, hashPassword(user.getPassword())); // Lưu ý: Cần hash trước khi truyền vào nếu muốn đổi pass
            pre.setString(3, user.getEmail());
            pre.setString(4, user.getFull_name());
            pre.setString(5, user.getPhone());
            pre.setString(6, user.getAddress());
            pre.setInt(7, user.getMember_type());
            pre.setInt(8, user.getCoop_id());
            pre.setString(9, user.getStatus());
            pre.setString(10, user.getExpiry_date());
            pre.setString(11, user.getPlan_type());
            pre.setString(12, user.getJoined_date());
            pre.setInt(13, user.getId());
            n = pre.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DAOMember1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }

    public List<member> getmembers(String sql) {
        List<member> list = new ArrayList<>();
        try {
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                list.add(new member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("member_type"),
                        rs.getInt("coop_id"),
                        rs.getString("status"),
                        rs.getString("expiry_date"),
                        rs.getString("plan_type"),
                        rs.getString("joined_date"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public member getmemberbyID(int id) {
        member user = null;
        String sql = "SELECT * FROM members WHERE id = " + id;
        try {
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            if (rs.next()) {
                user = new member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("member_type"),
                        rs.getInt("coop_id"),
                        rs.getString("status"),
                        rs.getString("expiry_date"),
                        rs.getString("plan_type"),
                        rs.getString("joined_date"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return user;
    }

    public List<member> listmembers() {
        String sql = "SELECT * FROM members ORDER BY id DESC";
        List<member> usersList = new ArrayList<>();
        try {
            Statement state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            while (rs.next()) {
                usersList.add(new member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("member_type"),
                        rs.getInt("coop_id"),
                        rs.getString("status"),
                        rs.getString("expiry_date"),
                        rs.getString("plan_type"),
                        rs.getString("joined_date"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return usersList;
    }

    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM members WHERE email = ? AND status <> 'Deleted'";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // 1. Kiểm tra Username đã tồn tại chưa (Rất quan trọng để tránh lỗi khi nhấn Register cuối cùng)

    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM members WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

// 2. Lấy Plan Name (Gói Tháng/Năm) dựa trên mã đơn hàng PayOS
// Hàm này giúp bạn biết khách đã trả tiền cho gói nào để set expiry_date cho đúng
// Thêm hàm này vào DAOMember1.java
    public member getMemberById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, id);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                // Lưu ý: Phải truyền đúng 14 tham số theo thứ tự constructor trong Model
                return new member(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getInt("member_type"),
                        rs.getInt("coop_id"),
                        rs.getString("status"),
                        rs.getString("expiry_date"), // expiry_date trước
                        rs.getString("plan_type"), // plan_type sau
                        rs.getString("joined_date"), // Thêm cái này
                        rs.getString("created_at") // Thêm cái này
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlanTypeByOrderCode(String orderCode) {
        String planType = "NONE";
        String sql = "SELECT sp.plan_name FROM payments p "
                + "JOIN subscription_plans sp ON p.plan_id = sp.id "
                + "WHERE p.order_code = ?";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setString(1, orderCode);
            ResultSet rs = pre.executeQuery();
            if (rs.next()) {
                planType = rs.getString("plan_name");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return planType;
    }

    public boolean updateMember(int id, String name, String email, String phone, String address) {
        String sql = "UPDATE members SET full_name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void extendMembership(int memberId, int planId) {
        // 1. Lấy thông tin ngày hết hạn hiện tại từ Database
        member user = getMemberById(memberId); // Sử dụng hàm getMemberById bạn đã có
        if (user == null) {
            return;
        }

        String sql = "UPDATE members SET expiry_date = ?, status = 'Active' WHERE id = ?";
        try {
            LocalDate today = LocalDate.now();
            LocalDate currentExpiry = null;

            // Kiểm tra xem đã có ngày hết hạn chưa và định dạng có đúng không
            if (user.getExpiry_date() != null && !user.getExpiry_date().isEmpty()) {
                try {
                    currentExpiry = LocalDate.parse(user.getExpiry_date());
                } catch (Exception e) {
                    currentExpiry = null; // Nếu lỗi định dạng thì coi như chưa có
                }
            }

            // 2. LOGIC QUAN TRỌNG: Xác định mốc thời gian bắt đầu cộng thêm
            LocalDate startDate;
            if (currentExpiry != null && currentExpiry.isAfter(today)) {
                // Nếu vẫn còn hạn: Cộng tiếp nối từ ngày hết hạn cũ
                startDate = currentExpiry;
            } else {
                // Nếu đã hết hạn hoặc chưa có hạn: Cộng từ ngày hôm nay
                startDate = today;
            }

            // 3. Tính toán ngày hết hạn mới dựa trên gói (Plan)
            LocalDate newExpiry;
            if (planId == 1) {
                newExpiry = startDate.plusMonths(1); // Gói tháng: +1 tháng
            } else {
                newExpiry = startDate.plusYears(1);  // Gói năm: +1 năm
            }

            // 4. Thực thi cập nhật vào Database
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newExpiry.toString()); // Lưu dạng YYYY-MM-DD
                ps.setInt(2, memberId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// 3. Liên kết Member ID vào bảng Payments
// Sau khi insert member thành công, ta lấy ID của họ cập nhật ngược lại vào bảng payments
    public boolean linkMemberToPayment(int memberId, String orderCode) {
        String sql = "UPDATE payments SET member_id = ? WHERE order_code = ?";
        try (PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, memberId);
            pre.setString(2, orderCode);
            int rows = pre.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    // 1. Kiểm tra mật khẩu cũ (Phải băm mật khẩu nhập vào rồi mới so sánh với DB)
public boolean checkOldPassword(int id, String oldPassword) {
    String sql = "SELECT * FROM members WHERE id = ? AND password = ?";
    try {
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setInt(1, id);
        // Dùng hàm hashPassword có sẵn trong DAO của bạn
        pre.setString(2, hashPassword(oldPassword)); 
        ResultSet rs = pre.executeQuery();
        return rs.next(); // Nếu trả về true tức là mật khẩu cũ khớp
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}

// 2. Cập nhật mật khẩu mới (Mật khẩu mới cũng được băm trước khi lưu)
public boolean changePassword(int id, String newPassword) {
    String sql = "UPDATE members SET password = ? WHERE id = ?";
    try (PreparedStatement pre = conn.prepareStatement(sql)) {
        pre.setString(1, hashPassword(newPassword)); // Mã hóa ở đây
        pre.setInt(2, id);
        return pre.executeUpdate() > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}
    public List<member> searchMembersByCoop(int coopId, String keyword) {
        List<member> list = new ArrayList<>();

        // Câu lệnh SQL lấy thông tin Member + Số dư nợ cuối cùng (nơi partner_id IS NULL)
        String sql = "SELECT m.id, m.full_name, m.phone, m.address, "
                + "(SELECT balance_after FROM member_transaction_ledger l "
                + " WHERE l.member_id = m.id AND l.partner_id IS NULL "
                + " ORDER BY l.id DESC LIMIT 1) AS current_debt "
                + "FROM members m "
                + "WHERE m.member_type = 1 AND m.coop_id = ? "
                + "AND (m.full_name LIKE ? OR m.phone LIKE ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, coopId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                member m = new member();
                m.setId(rs.getInt("id"));
                m.setFull_name(rs.getString("full_name"));
                m.setPhone(rs.getString("phone"));
                m.setAddress(rs.getString("address"));

                // Lấy giá trị nợ, nếu chưa có nợ thì mặc định là 0
                double debt = rs.getDouble("current_debt");
                if (rs.wasNull()) {
                    debt = 0.0;
                }
                // Bạn cần đảm bảo class member có trường này để lưu giá trị trả về cho JSP
                m.setCurrent_debt(debt);

                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        // 1. Khởi tạo đối tượng DAO
        DAOMember1 dao = new DAOMember1();

        // 2. Tạo một đối tượng member mới để test
        // Thứ tự tham số giả định theo Constructor:
        // id, username, password, email, full_name, phone, address, 
        // member_type, coop_id, status, expiry_date, plan_type, joined_date, created_at
        member testMember = new member(
                0, // id (thường để 0 nếu DB tự tăng)
                "test_user_02", // username
                "123456", // password (sẽ được hàm insert hash SHA-256)
                "quangminhh0301@gmail.com", // email
                "Nguyen Van B", // full_name
                "0987654321", // phone
                "123 Street, Hanoi", // address
                2, // member_type (int)
                0, // coop_id (int)
                "Active", // status
                "2026-12-31", // expiry_date
                "Premium", // plan_type
                "2025-01-06", // joined_date
                "2025-01-06 14:00:00" // created_at
        );

        // 3. Gọi hàm insert
        System.out.println("--- Đang thực hiện Insert Member ---");
        int result = dao.insertmember(testMember);

        // 4. Thông báo kết quả
        if (result > 0) {
            System.out.println("Kết quả: Insert THÀNH CÔNG!");

            // Thử lấy lại user vừa tạo bằng username để kiểm tra
            member check = dao.getCurrentmember("test_user_01");
            if (check != null) {
                System.out.println("Dữ liệu trong DB: " + check.getFull_name() + " - Email: " + check.getEmail());
            }
        } else {
            System.out.println("Kết quả: Insert THẤT BẠI!");
        }
    }
}
