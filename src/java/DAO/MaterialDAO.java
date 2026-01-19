package DAO;

import model.Material;
import DAL.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaterialDAO extends DBContext {

    public static final MaterialDAO INSTANCE = new MaterialDAO();

    private MaterialDAO() {
        super(); // gọi DBContext() để mở connection
    }

    public boolean insertMaterial(String name, String unit, String description, String image, int coopId) {
        // Thêm cột coop_id vào câu lệnh INSERT
        String sql = "INSERT INTO materials (name, unit, description, image, coop_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setString(3, description);
            ps.setString(4, image);
            ps.setInt(5, coopId); // Gán ID của HTX sở hữu vật tư này

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa lại hàm listMaterials để đồng bộ và an toàn hơn
    public List<Material> listMaterials(String keyword, String sort, int coopId) {
        List<Material> list = new ArrayList<>();
        // Thêm m.coop_id vào điều kiện WHERE để lọc đúng vật tư của HTX
        String sql = "SELECT m.id, m.name, m.unit, m.description, m.image, "
                + "IFNULL(SUM(mi.quantity), 0) AS total_stock "
                + "FROM materials m "
                + "LEFT JOIN material_inventory mi ON m.id = mi.material_id "
                + "WHERE m.name LIKE ? AND m.coop_id = ? "
                + "GROUP BY m.id, m.name, m.unit, m.description, m.image ";

        // Giữ nguyên logic lọc theo tình trạng kho
        if ("low".equals(sort)) {
            sql += "HAVING total_stock < 10 AND total_stock > 0 ";
        } else if ("out".equals(sort)) {
            sql += "HAVING total_stock = 0 ";
        } else if ("in".equals(sort)) {
            sql += "HAVING total_stock > 0 ";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, coopId); // Gán ID của HTX

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Material(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("unit"),
                            rs.getDouble("total_stock"),
                            rs.getString("description"),
                            rs.getString("image")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateMaterial(int id, String name, String unit, String description, String image) {
        // Nếu image truyền vào là null (người dùng không chọn ảnh mới), 
        // ta cần logic giữ nguyên ảnh cũ hoặc viết 2 câu SQL riêng.
        String sql = "UPDATE materials SET name = ?, unit = ?, description = ?, image = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, unit);
            ps.setString(3, description);
            ps.setString(4, image);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Material getMaterialById(int id) {

        String sql
                = "SELECT m.id, m.name, m.unit, m.description, m.image, "
                + "       IFNULL(SUM(mi.quantity), 0) AS total_stock "
                + "FROM materials m "
                + "LEFT JOIN material_inventory mi ON m.id = mi.material_id "
                + "WHERE m.id = ? "
                + "GROUP BY m.id, m.name, m.unit,  m.description, m.image";

        try {
            Connection conn = this.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Material(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getDouble("total_stock"),
                        rs.getString("description"),
                        rs.getString("image")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Material> searchMaterials(String keyword, int coopId) {
        List<Material> list = new ArrayList<>();
        // Thêm điều kiện lọc theo coop_id
        String sql = "SELECT id, name, unit FROM materials WHERE name LIKE ? AND coop_id = ? LIMIT 10";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, coopId); // Gán ID của HTX

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("id"));
                m.setName(rs.getString("name"));
                m.setUnit(rs.getString("unit"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

// Lấy lịch sử nhập hàng từ Nhà cung cấp
    public List<Map<String, Object>> getInboundHistory(int materialId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT mr.receipt_date, mr.receipt_code, p.name AS partner_name, "
                + "c.contract_code, c.id AS contract_id, mrd.quantity, mrd.unit_price, "
                + "mt.unit " // Lấy unit từ bảng materials (mt)
                + "FROM material_receipt_details mrd "
                + "JOIN material_receipts mr ON mrd.receipt_id = mr.id "
                + "JOIN partners p ON mr.partner_id = p.id "
                + "JOIN materials mt ON mr.material_id = mt.id " // JOIN thêm bảng này
                + "LEFT JOIN contracts c ON mr.contract_id = c.id "
                + "WHERE mr.material_id = ? "
                + "ORDER BY mr.receipt_date DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("receipt_date", rs.getTimestamp("receipt_date"));
                map.put("receipt_code", rs.getString("receipt_code"));
                map.put("partner_name", rs.getString("partner_name"));
                map.put("contract_code", rs.getString("contract_code"));
                map.put("contract_id", rs.getInt("contract_id"));
                map.put("quantity", rs.getDouble("quantity"));
                map.put("unit_price", rs.getDouble("unit_price"));
                map.put("unit", rs.getString("unit")); // Đã có dữ liệu unit
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

// Lấy lịch sử cung ứng cho Thành viên (Nông dân)
    public List<Map<String, Object>> getOutboundHistory(int materialId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT ms.supply_date, ms.supply_code, m.full_name AS member_name, "
                + "c.contract_code, c.id AS contract_id, msd.quantity, msd.unit_price, "
                + "mt.unit " // Lấy từ bảng vật tư gốc
                + "FROM material_supply_details msd "
                + "JOIN material_supplies ms ON msd.supply_id = ms.id "
                + "JOIN members m ON ms.member_id = m.id "
                + "JOIN materials mt ON msd.material_id = mt.id " // Phải có dòng này
                + "LEFT JOIN contracts c ON ms.contract_id = c.id "
                + "WHERE msd.material_id = ? ORDER BY ms.supply_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("supply_date", rs.getTimestamp("supply_date"));
                map.put("supply_code", rs.getString("supply_code"));
                map.put("member_name", rs.getString("member_name"));
                map.put("contract_code", rs.getString("contract_code"));
                map.put("contract_id", rs.getInt("contract_id"));
                map.put("quantity", rs.getDouble("quantity"));
                map.put("unit_price", rs.getDouble("unit_price"));
                map.put("unit", rs.getString("unit"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
