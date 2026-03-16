package DAO;

import DAL.DBContext;
import model.QRScanHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for qr_scan_history table.
 * Each method opens and closes its own connection to avoid connection leaks.
 */
public class DAOQRScanHistory extends DBContext {

    public DAOQRScanHistory() {
        super();
    }

    /**
     * Opens a fresh connection for each operation to avoid stale/leaked connections.
     */
    private Connection getConn() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found", e);
        }
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/gastromap2?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Ho_Chi_Minh",
            "root", "123456"
        );
    }

    private void closeResources(Connection c, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (c  != null) c.close();  } catch (SQLException e) { e.printStackTrace(); }
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
        Connection c = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) list.add(getFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(c, ps, rs);
        }
        return list;
    }

    public QRScanHistory getById(Integer id) {
        String sql = "SELECT * FROM qr_scan_history WHERE id = ?";
        Connection c = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return getFromResultSet(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(c, ps, rs);
        }
        return null;
    }

    public List<QRScanHistory> getByQrId(Integer qrId) {
        List<QRScanHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM qr_scan_history WHERE qr_id = ? ORDER BY scan_time DESC";
        Connection c = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            ps.setInt(1, qrId);
            rs = ps.executeQuery();
            while (rs.next()) list.add(getFromResultSet(rs));
            System.out.println("[DAOQRScanHistory.getByQrId] Found " + list.size() + " record(s) for qrId=" + qrId);
        } catch (SQLException e) {
            System.err.println("[DAOQRScanHistory.getByQrId] SQLException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(c, ps, rs);
        }
        return list;
    }

    public boolean insert(QRScanHistory history) {
        String sql = "INSERT INTO qr_scan_history (qr_id, scan_location, scan_actor, note) VALUES (?, ?, ?, ?)";
        Connection c = null; PreparedStatement ps = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            ps.setInt(1, history.getQrId());
            ps.setString(2, history.getScanLocation());
            ps.setString(3, history.getScanActor());
            ps.setString(4, history.getNote());
            int rows = ps.executeUpdate();
            System.out.println("[DAOQRScanHistory.insert] Inserted " + rows + " row(s) for qrId=" + history.getQrId());
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[DAOQRScanHistory.insert] SQLException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(c, ps, null);
        }
        return false;
    }

    public List<QRScanHistory> getPaginated(int page, int pageSize) {
        List<QRScanHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM qr_scan_history ORDER BY scan_time DESC LIMIT ? OFFSET ?";
        Connection c = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            rs = ps.executeQuery();
            while (rs.next()) list.add(getFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(c, ps, rs);
        }
        return list;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM qr_scan_history";
        Connection c = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(c, ps, rs);
        }
        return 0;
    }

    public int countByQrId(Integer qrId) {
        String sql = "SELECT COUNT(*) FROM qr_scan_history WHERE qr_id = ?";
        Connection c = null; PreparedStatement ps = null; ResultSet rs = null;
        try {
            c = getConn();
            ps = c.prepareStatement(sql);
            ps.setInt(1, qrId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(c, ps, rs);
        }
        return 0;
    }
}
