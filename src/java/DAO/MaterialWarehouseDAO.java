/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAL.DBContext;
import java.sql.Connection;
import model.WarehouseStockDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class MaterialWarehouseDAO extends DBContext {

    public static final MaterialWarehouseDAO INSTANCE = new MaterialWarehouseDAO();

    public List<WarehouseStockDTO> searchWarehousesWithStock(int materialId, String keyword, int coopId) {
        List<WarehouseStockDTO> list = new ArrayList<>();
        // SQL: Lọc kho theo tên và coop_id của HTX/Cửa hàng
        String sql = "SELECT w.id, w.name, IFNULL(mi.quantity, 0) as current_stock "
                + "FROM material_warehouses w "
                + "LEFT JOIN material_inventory mi ON w.id = mi.warehouse_id AND mi.material_id = ? "
                + "WHERE w.name LIKE ? AND w.coop_id = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, coopId); // Chỉ lấy kho thuộc HTX này

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                WarehouseStockDTO ws = new WarehouseStockDTO();
                ws.setId(rs.getInt("id"));
                ws.setName(rs.getString("name"));
                ws.setCurrent_stock(rs.getDouble("current_stock"));
                list.add(ws);
            }
        } catch (Exception e) {
            System.err.println("Error in searchWarehousesWithStock: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, Object>> getAllWarehouses() {
        List<Map<String, Object>> list = new ArrayList<>();
        // Truy vấn thông tin kho và đếm số loại vật tư đang có trong mỗi kho
        String sql = "SELECT mw.*, "
                + "(SELECT COUNT(*) FROM material_inventory mi WHERE mi.warehouse_id = mw.id) as total_items "
                + "FROM material_warehouses mw";

        try (Connection conn = getConnection();PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("location", rs.getString("location"));
                map.put("description", rs.getString("description"));
                map.put("total_items", rs.getInt("total_items"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertWarehouse(String name, String location, String description, int coopId) {
        // Thêm trường coop_id vào câu lệnh INSERT để xác định kho thuộc đơn vị nào
        String sql = "INSERT INTO material_warehouses (name, location, description, created_date, coop_id) "
                + "VALUES (?, ?, ?, CURDATE(), ?)";

        try (Connection conn = getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, description);
            ps.setInt(4, coopId); // Gán ID của HTX chủ quản

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy thông tin một kho theo ID
    public Map<String, Object> getWarehouseById(int id) {
        String sql = "SELECT * FROM material_warehouses WHERE id = ?";
        try (Connection conn = getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("location", rs.getString("location"));
                map.put("description", rs.getString("description"));
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

// Lấy danh sách vật tư và số lượng tồn trong kho này
    public List<Map<String, Object>> getInventoryItems(int warehouseId) {
        List<Map<String, Object>> list = new ArrayList<>();
        // JOIN giữa bảng tồn kho (material_inventory) và bảng vật tư (materials)
        String sql = "SELECT m.id, m.name, m.image, mi.quantity, mi.unit "
                + "FROM material_inventory mi "
                + "JOIN materials m ON mi.material_id = m.id "
                + "WHERE mi.warehouse_id = ?";
        try (Connection conn = getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, warehouseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                map.put("image", rs.getString("image"));
                map.put("quantity", rs.getDouble("quantity"));
                map.put("unit", rs.getString("unit"));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateWarehouse(int id, String name, String location, String description) {
        // Câu lệnh SQL cập nhật các thông tin cơ bản của kho
        String sql = "UPDATE material_warehouses SET name = ?, location = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, location);
            ps.setString(3, description);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
