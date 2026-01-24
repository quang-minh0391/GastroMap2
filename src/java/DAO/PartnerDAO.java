package DAO;

import DAL.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Partner;

public class PartnerDAO extends DBContext {

    public static final PartnerDAO INSTANCE = new PartnerDAO();

    private PartnerDAO() {
        super();
    }

    public List<Map<String, Object>> getAllPartners(int coopId) {
        List<Map<String, Object>> list = new ArrayList<>();
        // Thêm điều kiện lọc theo coop_id của đối tác
        String sql = "SELECT p.*, "
                + "(SELECT l.balance_after FROM member_transaction_ledger l "
                + " WHERE l.partner_id = p.id "
                + " ORDER BY l.id DESC LIMIT 1) as current_debt "
                + "FROM partners p "
                + "WHERE p.coop_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coopId); // Thiết lập ID của HTX

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", rs.getInt("id"));
                    map.put("name", rs.getString("name"));
                    map.put("phone", rs.getString("phone"));
                    map.put("address", rs.getString("address"));
                    map.put("tax_code", rs.getString("tax_code"));
                    map.put("note", rs.getString("note"));

                    double debt = rs.getDouble("current_debt");
                    // Nếu chưa từng có giao dịch, debt sẽ là 0
                    map.put("debt", rs.wasNull() ? 0 : debt);
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thông tin chi tiết một nhà cung cấp theo ID
    public Map<String, Object> getPartnerById(int id) {
        String sql = "SELECT * FROM partners WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("phone", rs.getString("phone"));
                map.put("address", rs.getString("address"));
                map.put("tax_code", rs.getString("tax_code"));
                map.put("note", rs.getString("note"));
                return map;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

// Cập nhật thông tin nhà cung cấp
    public boolean updatePartner(int id, String name, String phone, String address, String taxCode, String note) {
        String sql = "UPDATE partners SET name = ?, phone = ?, address = ?, tax_code = ?, note = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.setString(4, taxCode);
            ps.setString(5, note);
            ps.setInt(6, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertPartner(String name, String phone, String address, String taxCode, String note, int coopId) {
    // Thêm trường coop_id để xác định đối tác thuộc HTX nào
    String sql = "INSERT INTO partners (coop_id, name, type, phone, address, tax_code, note, created_date) "
               + "VALUES (?, ?, 'Supplier', ?, ?, ?, ?, CURDATE())";
               
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, coopId); // Gán ID của HTX
        ps.setString(2, name);
        ps.setString(3, phone);
        ps.setString(4, address);
        ps.setString(5, taxCode);
        ps.setString(6, note);
        
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public List<Partner> searchPartners(String keyword, int coopId) {
        List<Partner> list = new ArrayList<>();
        // Thêm điều kiện lọc theo coop_id để đảm bảo dữ liệu không bị lẫn giữa các HTX
        String sql = "SELECT id, name, phone, address FROM partners WHERE name LIKE ? AND coop_id = ? LIMIT 10";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, coopId); // Gán ID của HTX vào câu truy vấn

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partner p = new Partner();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPhone(rs.getString("phone"));
                p.setAddress(rs.getString("address"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm nhanh và trả về ID vừa tạo
    public int insertPartnerShort(String name) {
        String sql = "INSERT INTO partners (name, created_date) VALUES (?, CURDATE())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
