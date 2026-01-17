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
import java.util.List;

/**
 *
 * @author Admin
 */
public class MaterialWarehouseDAO extends DBContext {

    public static final MaterialWarehouseDAO INSTANCE = new MaterialWarehouseDAO();

    public List<WarehouseStockDTO> searchWarehousesWithStock(int materialId, String keyword) {
        List<WarehouseStockDTO> list = new ArrayList<>();
        // SQL: Đã loại bỏ cột w.status vì bảng của bạn không có cột này
        String sql = "SELECT w.id, w.name, IFNULL(mi.quantity, 0) as current_stock "
                + "FROM material_warehouses w "
                + "LEFT JOIN material_inventory mi ON w.id = mi.warehouse_id AND mi.material_id = ? "
                + "WHERE w.name LIKE ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, materialId);
            ps.setString(2, "%" + keyword + "%");

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
}
