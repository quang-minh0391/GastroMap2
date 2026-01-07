package DAO;

import DAL.DBContext;
import model.QRScanHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for qr_scan_history table
 */
public class DAOQRScanHistory extends DBContext {

    public DAOQRScanHistory() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public QRScanHistory getFromResultSet(ResultSet rs) throws SQLException {
        QRScanHistory history = new QRScanHistory();
        history.setId(rs.getInt("id"));
        history.setQrId(rs.getInt("qr_id"));
        history.setScanTime(rs.getTimestamp("scan_time"));
        history.setScanLocation(rs.getString("scan_location"));
        history.setScanActor(rs.getString("scan_actor"));
        history.setNote(rs.getString("note"));
        return history;
    }

    public List<QRScanHistory> getAll() {
        List<QRScanHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM qr_scan_history ORDER BY scan_time DESC";
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

    public QRScanHistory getById(Integer id) {
        String sql = "SELECT * FROM qr_scan_history WHERE id = ?";
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

    public List<QRScanHistory> getByQrId(Integer qrId) {
        List<QRScanHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM qr_scan_history WHERE qr_id = ? ORDER BY scan_time DESC";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, qrId);
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

    public boolean insert(QRScanHistory history) {
        String sql = "INSERT INTO qr_scan_history (qr_id, scan_location, scan_actor, note) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, history.getQrId());
            ps.setString(2, history.getScanLocation());
            ps.setString(3, history.getScanActor());
            ps.setString(4, history.getNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, ps, null);
        }
        return false;
    }

    public List<QRScanHistory> getPaginated(int page, int pageSize) {
        List<QRScanHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM qr_scan_history ORDER BY scan_time DESC LIMIT ? OFFSET ?";
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
        String sql = "SELECT COUNT(*) FROM qr_scan_history";
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

    public int countByQrId(Integer qrId) {
        String sql = "SELECT COUNT(*) FROM qr_scan_history WHERE qr_id = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, qrId);
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

