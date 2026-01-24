package DAO;

import DAL.DBContext;
import model.FarmProduct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for farm_products table
 */
public class DAOFarmProduct extends DBContext {

    public DAOFarmProduct() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public FarmProduct getFromResultSet(ResultSet rs) throws SQLException {
        FarmProduct product = new FarmProduct();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setUnit(rs.getString("unit"));
        product.setDescription(rs.getString("description"));
        product.setStatus(rs.getString("status"));
        product.setCreatedBy((Integer) rs.getObject("created_by"));
        product.setCreatedAt(rs.getString("created_at"));
        return product;
    }

    public List<FarmProduct> getAll() {
        List<FarmProduct> list = new ArrayList<>();
        String sql = "SELECT * FROM farm_products ORDER BY id DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return list;
    }

    public List<FarmProduct> getActive() {
        List<FarmProduct> list = new ArrayList<>();
        String sql = "SELECT * FROM farm_products WHERE status = 'ACTIVE' ORDER BY name";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return list;
    }

    public FarmProduct getById(Integer id) {
        String sql = "SELECT * FROM farm_products WHERE id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return getFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return null;
    }

    public boolean insert(FarmProduct product) {
        String sql = "INSERT INTO farm_products (name, unit, description, status, created_by) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getUnit());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getStatus() != null ? product.getStatus() : "ACTIVE");
            if (product.getCreatedBy() != null) {
                ps.setInt(5, product.getCreatedBy());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean update(FarmProduct product) {
        String sql = "UPDATE farm_products SET name = ?, unit = ?, description = ?, status = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getUnit());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getStatus());
            ps.setInt(5, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM farm_products WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public List<FarmProduct> getPaginated(int page, int pageSize) {
        List<FarmProduct> list = new ArrayList<>();
        String sql = "SELECT * FROM farm_products ORDER BY id DESC LIMIT ? OFFSET ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return list;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM farm_products";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return 0;
    }
    
    public List<FarmProduct> searchProducts(String keyword, int coopId) {
        List<FarmProduct> list = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* " +
                     "FROM farm_products p " +
                     "LEFT JOIN members m ON p.created_by = m.id " +
                     "WHERE p.name LIKE ? AND p.status = 'ACTIVE' " +
                     "AND (p.created_by = ? OR m.coop_id = ?) " +
                     "LIMIT 20";
        
        try {
            if (conn != null) {
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, "%" + keyword + "%");
                    ps.setInt(2, coopId);
                    ps.setInt(3, coopId);
                    
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            list.add(getFromResultSet(rs));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Get paginated farm products by cooperative ID
     * Returns products created by the coop or its members
     */
    public List<FarmProduct> getPaginatedByCoopId(int page, int pageSize, Integer coopId) {
        List<FarmProduct> list = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM farm_products p " +
                     "LEFT JOIN members m ON p.created_by = m.id " +
                     "WHERE p.created_by = ? OR m.coop_id = ? " +
                     "ORDER BY p.id DESC LIMIT ? OFFSET ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, coopId);
            ps.setInt(2, coopId);
            ps.setInt(3, pageSize);
            ps.setInt(4, (page - 1) * pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return list;
    }
    
    /**
     * Count farm products by cooperative ID
     */
    public int countByCoopId(Integer coopId) {
        String sql = "SELECT COUNT(DISTINCT p.id) FROM farm_products p " +
                     "LEFT JOIN members m ON p.created_by = m.id " +
                     "WHERE p.created_by = ? OR m.coop_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, coopId);
            ps.setInt(2, coopId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return 0;
    }
    
    /**
     * Get all active products by cooperative ID
     */
    public List<FarmProduct> getActiveByCoopId(Integer coopId) {
        List<FarmProduct> list = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM farm_products p " +
                     "LEFT JOIN members m ON p.created_by = m.id " +
                     "WHERE p.status = 'ACTIVE' AND (p.created_by = ? OR m.coop_id = ?) " +
                     "ORDER BY p.name";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, coopId);
            ps.setInt(2, coopId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(getFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return list;
    }
}

