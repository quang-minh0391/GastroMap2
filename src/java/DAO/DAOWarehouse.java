package DAO;

import DAL.DBContext;
import model.StorageWarehouse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for storage_warehouses table
 */
public class DAOWarehouse extends DBContext {

    public DAOWarehouse() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public StorageWarehouse getFromResultSet(ResultSet rs) throws SQLException {
        StorageWarehouse warehouse = new StorageWarehouse();
        warehouse.setId(rs.getInt("id"));
        warehouse.setName(rs.getString("name"));
        warehouse.setLocation(rs.getString("location"));
        warehouse.setDescription(rs.getString("description"));
        return warehouse;
    }

    public List<StorageWarehouse> getAll() {
        List<StorageWarehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM storage_warehouses ORDER BY id DESC";
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

    public StorageWarehouse getById(Integer id) {
        String sql = "SELECT * FROM storage_warehouses WHERE id = ?";
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

    public boolean insert(StorageWarehouse warehouse) {
        String sql = "INSERT INTO storage_warehouses (name, location, description) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            ps.setString(3, warehouse.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean update(StorageWarehouse warehouse) {
        String sql = "UPDATE storage_warehouses SET name = ?, location = ?, description = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, warehouse.getName());
            ps.setString(2, warehouse.getLocation());
            ps.setString(3, warehouse.getDescription());
            ps.setInt(4, warehouse.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM storage_warehouses WHERE id = ?";
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

    public List<StorageWarehouse> getPaginated(int page, int pageSize) {
        List<StorageWarehouse> list = new ArrayList<>();
        String sql = "SELECT * FROM storage_warehouses ORDER BY id DESC LIMIT ? OFFSET ?";
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
        String sql = "SELECT COUNT(*) FROM storage_warehouses";
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
}

