package DAO;

import DAL.DBContext;
import model.BatchStockIn;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for batch_stock_ins table
 * Note: insert() uses transaction to also update batch_inventory
 */
public class DAOBatchStockIn extends DBContext {

    public DAOBatchStockIn() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public BatchStockIn getFromResultSet(ResultSet rs) throws SQLException {
        BatchStockIn stockIn = new BatchStockIn();
        stockIn.setId(rs.getInt("id"));
        stockIn.setBatchId(rs.getInt("batch_id"));
        stockIn.setWarehouseId(rs.getInt("warehouse_id"));
        stockIn.setQuantity(rs.getDouble("quantity"));
        stockIn.setUnit(rs.getString("unit"));
        stockIn.setReceivedDate(rs.getDate("received_date"));
        stockIn.setReceivedBy(rs.getString("received_by"));
        stockIn.setNote(rs.getString("note"));
        stockIn.setCreatedAt(rs.getTimestamp("created_at"));
        return stockIn;
    }

    public List<BatchStockIn> getAll() {
        List<BatchStockIn> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_stock_ins ORDER BY created_at DESC";
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

    public BatchStockIn getById(Integer id) {
        String sql = "SELECT * FROM batch_stock_ins WHERE id = ?";
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

    public List<BatchStockIn> getByBatch(Integer batchId) {
        List<BatchStockIn> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_stock_ins WHERE batch_id = ? ORDER BY created_at DESC";
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

    public List<BatchStockIn> getByWarehouse(Integer warehouseId) {
        List<BatchStockIn> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_stock_ins WHERE warehouse_id = ? ORDER BY created_at DESC";
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

    /**
     * Insert stock-in record with transaction
     * Also creates or updates batch_inventory
     */
    public boolean insert(BatchStockIn stockIn) {
        String insertStockInSql = "INSERT INTO batch_stock_ins (batch_id, warehouse_id, quantity, unit, received_date, received_by, note) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String checkInventorySql = "SELECT id, remaining_quantity FROM batch_inventory WHERE batch_id = ? AND warehouse_id = ?";
        String insertInventorySql = "INSERT INTO batch_inventory (warehouse_id, batch_id, remaining_quantity, unit) VALUES (?, ?, ?, ?)";
        String updateInventorySql = "UPDATE batch_inventory SET remaining_quantity = remaining_quantity + ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        PreparedStatement psStockIn = null;
        PreparedStatement psCheck = null;
        PreparedStatement psInventory = null;
        ResultSet rs = null;
        
        try {
            // Start transaction
            conn.setAutoCommit(false);
            
            // 1. Insert stock-in record
            psStockIn = conn.prepareStatement(insertStockInSql);
            psStockIn.setInt(1, stockIn.getBatchId());
            psStockIn.setInt(2, stockIn.getWarehouseId());
            psStockIn.setDouble(3, stockIn.getQuantity());
            psStockIn.setString(4, stockIn.getUnit() != null ? stockIn.getUnit() : "kg");
            psStockIn.setDate(5, stockIn.getReceivedDate());
            psStockIn.setString(6, stockIn.getReceivedBy());
            psStockIn.setString(7, stockIn.getNote());
            psStockIn.executeUpdate();
            
            // 2. Check if inventory record exists
            psCheck = conn.prepareStatement(checkInventorySql);
            psCheck.setInt(1, stockIn.getBatchId());
            psCheck.setInt(2, stockIn.getWarehouseId());
            rs = psCheck.executeQuery();
            
            if (rs.next()) {
                // 3a. Update existing inventory
                int inventoryId = rs.getInt("id");
                psInventory = conn.prepareStatement(updateInventorySql);
                psInventory.setDouble(1, stockIn.getQuantity());
                psInventory.setInt(2, inventoryId);
                psInventory.executeUpdate();
            } else {
                // 3b. Create new inventory record
                psInventory = conn.prepareStatement(insertInventorySql);
                psInventory.setInt(1, stockIn.getWarehouseId());
                psInventory.setInt(2, stockIn.getBatchId());
                psInventory.setDouble(3, stockIn.getQuantity());
                psInventory.setString(4, stockIn.getUnit() != null ? stockIn.getUnit() : "kg");
                psInventory.executeUpdate();
            }
            
            // Commit transaction
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources(null, psStockIn, null);
            closeResources(null, psCheck, rs);
            closeResources(null, psInventory, null);
        }
        return false;
    }

    public List<BatchStockIn> getPaginated(int page, int pageSize) {
        List<BatchStockIn> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_stock_ins ORDER BY created_at DESC LIMIT ? OFFSET ?";
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
        String sql = "SELECT COUNT(*) FROM batch_stock_ins";
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
    
    /**
     * Get paginated stock-ins by cooperative ID (through warehouse)
     */
    public List<BatchStockIn> getPaginatedByCoopId(int page, int pageSize, Integer coopId) {
        List<BatchStockIn> list = new ArrayList<>();
        String sql = "SELECT bsi.* FROM batch_stock_ins bsi " +
                     "INNER JOIN storage_warehouses sw ON bsi.warehouse_id = sw.id " +
                     "WHERE sw.coop_id = ? " +
                     "ORDER BY bsi.created_at DESC LIMIT ? OFFSET ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, coopId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);
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
     * Count stock-ins by cooperative ID
     */
    public int countByCoopId(Integer coopId) {
        String sql = "SELECT COUNT(*) FROM batch_stock_ins bsi " +
                     "INNER JOIN storage_warehouses sw ON bsi.warehouse_id = sw.id " +
                     "WHERE sw.coop_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, coopId);
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

