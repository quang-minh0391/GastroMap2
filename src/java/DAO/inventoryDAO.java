/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DAL.DBContext;
import java.util.List;
import model.MaterialInventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.MaterialWarehouse;

/**
 *
 * @author Admin
 */
public class inventoryDAO extends DBContext {

    public static final inventoryDAO INSTANCE = new inventoryDAO();

    private inventoryDAO() {
        super(); // gọi DBContext() để mở connection
    }

    public List<MaterialInventory> getInventoryByMaterialId(int materialId) {
        List<MaterialInventory> inventories = new ArrayList<>();

        String sql = "SELECT mi.id, mi.quantity, mi.unit, mi.updated_at, "
                + "w.id as warehouse_id, w.name as warehouse_name, w.location, w.description, w.created_date "
                + "FROM material_inventory mi "
                + "JOIN material_warehouses w ON mi.warehouse_id = w.id "
                + "WHERE mi.material_id = ?";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, materialId);

            rs = ps.executeQuery();
            while (rs.next()) {
                MaterialWarehouse warehouse = new MaterialWarehouse();
                warehouse.setId(rs.getInt("warehouse_id"));
                warehouse.setName(rs.getString("warehouse_name"));
                warehouse.setLocation(rs.getString("location"));
                warehouse.setDescription(rs.getString("description"));
                warehouse.setCreatedDate(rs.getDate("created_date"));

                MaterialInventory inv = new MaterialInventory();
                inv.setId(rs.getInt("id"));
                inv.setQuantity(rs.getDouble("quantity"));
                inv.setUnit(rs.getString("unit"));
                inv.setUpdatedAt(rs.getTimestamp("updated_at"));
                inv.setWarehouse(warehouse);

                inventories.add(inv);
            }

        } catch (Exception e) {
            e.printStackTrace(); // bắt lỗi luôn, không throw ra
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ignored) {
            }
        }

        return inventories;
    }

   public List<MaterialWarehouse> searchWarehouses(String keyword, int coopId) {
    List<MaterialWarehouse> list = new ArrayList<>();
    // Thêm điều kiện lọc theo coop_id vào câu lệnh SELECT
    String sql = "SELECT id, name, location FROM material_warehouses WHERE name LIKE ? AND coop_id = ? LIMIT 10";
    
    try {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, coopId); // Gán ID của HTX/Cửa hàng
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MaterialWarehouse w = new MaterialWarehouse();
                    w.setId(rs.getInt("id"));
                    w.setName(rs.getString("name"));
                    w.setLocation(rs.getString("location"));
                    list.add(w);
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
