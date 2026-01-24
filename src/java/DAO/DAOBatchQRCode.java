package DAO;

import DAL.DBContext;
import model.BatchQRCode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DAO class for batch_qr_codes table
 */
public class DAOBatchQRCode extends DBContext {

    public DAOBatchQRCode() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public BatchQRCode getFromResultSet(ResultSet rs) throws SQLException {
        BatchQRCode qrCode = new BatchQRCode();
        qrCode.setId(rs.getInt("id"));
        qrCode.setBatchId(rs.getInt("batch_id"));
        qrCode.setQrValue(rs.getString("qr_value"));
        qrCode.setStatus(rs.getString("status"));
        qrCode.setCreatedAt(rs.getTimestamp("created_at"));
        return qrCode;
    }

    public List<BatchQRCode> getAll() {
        List<BatchQRCode> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_qr_codes ORDER BY created_at DESC";
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

    public BatchQRCode getById(Integer id) {
        String sql = "SELECT * FROM batch_qr_codes WHERE id = ?";
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

    public BatchQRCode getByQrValue(String qrValue) {
        String sql = "SELECT * FROM batch_qr_codes WHERE qr_value = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, qrValue);
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

    public List<BatchQRCode> getByBatch(Integer batchId) {
        List<BatchQRCode> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_qr_codes WHERE batch_id = ? ORDER BY created_at DESC";
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

    public boolean insert(BatchQRCode qrCode) {
        String sql = "INSERT INTO batch_qr_codes (batch_id, qr_value, status) VALUES (?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, qrCode.getBatchId());
            ps.setString(2, qrCode.getQrValue());
            ps.setString(3, qrCode.getStatus() != null ? qrCode.getStatus() : "CREATED");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean updateStatus(Integer id, String status) {
        String sql = "UPDATE batch_qr_codes SET status = ? WHERE id = ?";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public boolean delete(Integer id) {
        String sql = "DELETE FROM batch_qr_codes WHERE id = ?";
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

    public List<BatchQRCode> getPaginated(int page, int pageSize) {
        List<BatchQRCode> list = new ArrayList<>();
        String sql = "SELECT * FROM batch_qr_codes ORDER BY created_at DESC LIMIT ? OFFSET ?";
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
        String sql = "SELECT COUNT(*) FROM batch_qr_codes";
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

    public int countByBatch(Integer batchId) {
        String sql = "SELECT COUNT(*) FROM batch_qr_codes WHERE batch_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, batchId);
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
     * Generate unique QR value for a batch
     */
    public String generateQRValue(Integer batchId) {
        return "QR-" + batchId + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Get paginated QR codes by cooperative ID (through batch -> member -> coop)
     */
    public List<BatchQRCode> getPaginatedByCoopId(int page, int pageSize, Integer coopId) {
        List<BatchQRCode> list = new ArrayList<>();
        String sql = "SELECT qr.* FROM batch_qr_codes qr " +
                     "INNER JOIN production_batches pb ON qr.batch_id = pb.id " +
                     "INNER JOIN members m ON pb.member_id = m.id " +
                     "WHERE m.coop_id = ? OR m.id = ? " +
                     "ORDER BY qr.created_at DESC LIMIT ? OFFSET ?";
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
     * Count QR codes by cooperative ID
     */
    public int countByCoopId(Integer coopId) {
        String sql = "SELECT COUNT(*) FROM batch_qr_codes qr " +
                     "INNER JOIN production_batches pb ON qr.batch_id = pb.id " +
                     "INNER JOIN members m ON pb.member_id = m.id " +
                     "WHERE m.coop_id = ? OR m.id = ?";
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
     * Get all QR codes by batch IDs (for coop-filtered views)
     */
    public List<BatchQRCode> getAllByCoopId(Integer coopId) {
        List<BatchQRCode> list = new ArrayList<>();
        String sql = "SELECT qr.* FROM batch_qr_codes qr " +
                     "INNER JOIN production_batches pb ON qr.batch_id = pb.id " +
                     "INNER JOIN members m ON pb.member_id = m.id " +
                     "WHERE m.coop_id = ? OR m.id = ? " +
                     "ORDER BY qr.created_at DESC";
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

