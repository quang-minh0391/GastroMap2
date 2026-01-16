package DAO;


import model.Material;
import DAL.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO extends DBContext {

    public static final MaterialDAO INSTANCE = new MaterialDAO();

    private MaterialDAO() {
        super(); // gọi DBContext() để mở connection
    }

   public List<Material> listMaterials(String keyword, String sort) {

    List<Material> list = new ArrayList<>();

    String sql =
        "SELECT m.id, m.name, m.unit, m.description, m.image, " +
        "       IFNULL(SUM(mi.quantity), 0) AS total_stock " +
        "FROM materials m " +
        "LEFT JOIN material_inventory mi ON m.id = mi.material_id " +
        "WHERE m.name LIKE ? " +
        "GROUP BY m.id, m.name, m.unit,  m.description, m.image ";

    // lọc theo tổng tồn kho
    if ("low".equals(sort)) {
        sql += "HAVING total_stock < 10 AND total_stock > 0 ";
    } else if ("out".equals(sort)) {
        sql += "HAVING total_stock = 0 ";
    } else if ("in".equals(sort)) {
        sql += "HAVING total_stock > 0 ";
    }

    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        Connection conn = this.getConnection();

        ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");

        rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Material(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("unit"),
               
                rs.getDouble("total_stock"),   // 👈 tổng tồn
                rs.getString("description"),
                rs.getString("image")
                   
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (Exception ignored) {}
        try { if (ps != null) ps.close(); } catch (Exception ignored) {}
    }

    return list;
}
   public Material getMaterialById(int id) {

    String sql =
        "SELECT m.id, m.name, m.unit, m.description, m.image, " +
        "       IFNULL(SUM(mi.quantity), 0) AS total_stock " +
        "FROM materials m " +
        "LEFT JOIN material_inventory mi ON m.id = mi.material_id " +
        "WHERE m.id = ? " +
        "GROUP BY m.id, m.name, m.unit,  m.description, m.image";

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
   public List<Material> searchMaterials(String keyword) {
    List<Material> list = new ArrayList<>();
    String sql = "SELECT id, name, unit FROM materials WHERE name LIKE ? LIMIT 10";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Material m = new Material();
            m.setId(rs.getInt("id"));
            m.setName(rs.getString("name"));
            m.setUnit(rs.getString("unit")); // Lấy đơn vị từ DB
            list.add(m);
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}


}
