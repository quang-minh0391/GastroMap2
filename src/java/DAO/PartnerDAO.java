package DAO;

import DAL.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Partner;

public class PartnerDAO extends DBContext {

    public static final PartnerDAO INSTANCE = new PartnerDAO();

    private PartnerDAO() {
        super();
    }

    public List<Partner> searchPartners(String keyword) {
        List<Partner> list = new ArrayList<>();
        // Thêm phone và address vào câu lệnh SELECT
        String sql = "SELECT id, name, phone, address FROM partners WHERE name LIKE ? LIMIT 10";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Partner p = new Partner();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPhone(rs.getString("phone"));   // Lấy số điện thoại
                p.setAddress(rs.getString("address")); // Lấy địa chỉ
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
