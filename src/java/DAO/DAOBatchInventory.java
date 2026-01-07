package DAO;

import DAL.DBContext;
import model.BatchInventory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for batch_inventory table
 */
public class DAOBatchInventory extends DBContext {

    public DAOBatchInventory() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public BatchInventory getFromResultSet(ResultSet rs) throws SQLException {
        BatchInventory inventory = new BatchInventory();
        inventory.setId(rs.getInt("id"));
        inventory.setWarehouseId(rs.getInt("warehouse_id"));
        inventory.setBatchId(rs.getInt("batch_id"));
        inventory.setRemainingQuantity(rs.getDouble("remaining_quantity"));
        inventory.setUnit(rs.getString("unit"));
        inventory.setUpdatedAt(rs.getTimestamp("updated_at"));
        return inventory;
    }

    public List<BatchInventory> getAll() {
        List<BatchInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_inventory ORDER BY updated_at DESC";
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

    public BatchInventory getById(Integer id) {
        String sql = "SELECT * FROM batch_inventory WHERE id = ?";
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

    public BatchInventory getByBatchAndWarehouse(Integer batchId, Integer warehouseId) {
        String sql = "SELECT * FROM batch_inventory WHERE batch_id = ? AND warehouse_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, batchId);
            ps.setInt(2, warehouseId);
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

    public List<BatchInventory> getByWarehouse(Integer warehouseId) {
        List<BatchInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_inventory WHERE warehouse_id = ? ORDER BY updated_at DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, warehouseId);
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

    public List<BatchInventory> getByBatch(Integer batchId) {
        List<BatchInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_inventory WHERE batch_id = ? ORDER BY updated_at DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, batchId);
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

    public boolean insert(BatchInventory inventory) {
        String sql = "INSERT INTO batch_inventory (warehouse_id, batch_id, remaining_quantity, unit) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, inventory.getWarehouseId());
            ps.setInt(2, inventory.getBatchId());
            ps.setDouble(3, inventory.getRemainingQuantity());
            ps.setString(4, inventory.getUnit() != null ? inventory.getUnit() : "kg");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean update(BatchInventory inventory) {
        String sql = "UPDATE batch_inventory SET remaining_quantity = ?, unit = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, inventory.getRemainingQuantity());
            ps.setString(2, inventory.getUnit());
            ps.setInt(3, inventory.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean updateQuantity(Integer id, Double quantity) {
        String sql = "UPDATE batch_inventory SET remaining_quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, quantity);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean addQuantity(Integer batchId, Integer warehouseId, Double quantity, String unit) {
        String sql = "UPDATE batch_inventory SET remaining_quantity = remaining_quantity + ?, updated_at = CURRENT_TIMESTAMP WHERE batch_id = ? AND warehouse_id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, quantity);
            ps.setInt(2, batchId);
            ps.setInt(3, warehouseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public List<BatchInventory> getPaginated(int page, int pageSize) {
        List<BatchInventory> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_inventory ORDER BY updated_at DESC LIMIT ? OFFSET ?";
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

    public List<BatchInventory> getPaginatedWithFilters(int page, int pageSize, Integer warehouseId, Integer batchId) {
        List<BatchInventory> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM batch_inventory WHERE 1=1");
        
        if (warehouseId != null) {
            sql.append(" AND warehouse_id = ?");
        }
        if (batchId != null) {
            sql.append(" AND batch_id = ?");
        }
        sql.append(" ORDER BY updated_at DESC LIMIT ? OFFSET ?");
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (warehouseId != null) {
                ps.setInt(paramIndex++, warehouseId);
            }
            if (batchId != null) {
                ps.setInt(paramIndex++, batchId);
            }
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);
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
        String sql = "SELECT COUNT(*) FROM batch_inventory";
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

    public int countWithFilters(Integer warehouseId, Integer batchId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM batch_inventory WHERE 1=1");
        
        if (warehouseId != null) {
            sql.append(" AND warehouse_id = ?");
        }
        if (batchId != null) {
            sql.append(" AND batch_id = ?");
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (warehouseId != null) {
                ps.setInt(paramIndex++, warehouseId);
            }
            if (batchId != null) {
                ps.setInt(paramIndex, batchId);
            }
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

