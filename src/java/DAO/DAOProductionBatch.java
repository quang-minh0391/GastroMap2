package DAO;

import DAL.DBContext;
import model.ProductionBatch;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for production_batches table
 */
public class DAOProductionBatch extends DBContext {

    public DAOProductionBatch() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public ProductionBatch getFromResultSet(ResultSet rs) throws SQLException {
        ProductionBatch batch = new ProductionBatch();
        batch.setId(rs.getInt("id"));
        batch.setBatchCode(rs.getString("batch_code"));
        batch.setProductId(rs.getInt("product_id"));
        batch.setMemberId(rs.getInt("member_id"));
        batch.setHarvestDate(rs.getDate("harvest_date"));
        batch.setExpiryDate(rs.getDate("expiry_date"));
        batch.setTotalQuantity(rs.getDouble("total_quantity"));
        batch.setUnit(rs.getString("unit"));
        batch.setStatus(rs.getString("status"));
        batch.setCreatedAt(rs.getTimestamp("created_at"));
        return batch;
    }

    public List<ProductionBatch> getAll() {
        List<ProductionBatch> list = new ArrayList<>();
        String sql = "SELECT * FROM production_batches ORDER BY id DESC";
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

    public List<ProductionBatch> getAvailable() {
        List<ProductionBatch> list = new ArrayList<>();
        String sql = "SELECT * FROM production_batches WHERE status = 'AVAILABLE' ORDER BY batch_code";
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

    public ProductionBatch getById(Integer id) {
        String sql = "SELECT * FROM production_batches WHERE id = ?";
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

    public ProductionBatch getByBatchCode(String batchCode) {
        String sql = "SELECT * FROM production_batches WHERE batch_code = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, batchCode);
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

    public boolean insert(ProductionBatch batch) {
        String sql = "INSERT INTO production_batches (batch_code, product_id, member_id, harvest_date, expiry_date, total_quantity, unit, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, batch.getBatchCode());
            ps.setInt(2, batch.getProductId());
            ps.setInt(3, batch.getMemberId());
            ps.setDate(4, batch.getHarvestDate());
            ps.setDate(5, batch.getExpiryDate());
            ps.setDouble(6, batch.getTotalQuantity());
            ps.setString(7, batch.getUnit() != null ? batch.getUnit() : "kg");
            ps.setString(8, batch.getStatus() != null ? batch.getStatus() : "AVAILABLE");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean update(ProductionBatch batch) {
        String sql = "UPDATE production_batches SET batch_code = ?, product_id = ?, member_id = ?, harvest_date = ?, expiry_date = ?, total_quantity = ?, unit = ?, status = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, batch.getBatchCode());
            ps.setInt(2, batch.getProductId());
            ps.setInt(3, batch.getMemberId());
            ps.setDate(4, batch.getHarvestDate());
            ps.setDate(5, batch.getExpiryDate());
            ps.setDouble(6, batch.getTotalQuantity());
            ps.setString(7, batch.getUnit());
            ps.setString(8, batch.getStatus());
            ps.setInt(9, batch.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public List<ProductionBatch> getPaginated(int page, int pageSize) {
        List<ProductionBatch> list = new ArrayList<>();
        String sql = "SELECT * FROM production_batches ORDER BY id DESC LIMIT ? OFFSET ?";
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
        String sql = "SELECT COUNT(*) FROM production_batches";
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

    public String generateBatchCode() {
        String prefix = "BATCH-";
        String sql = "SELECT MAX(id) FROM production_batches";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                int maxId = rs.getInt(1);
                return prefix + String.format("%06d", maxId + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, rs);
        }
        return prefix + "000001";
    }
}

