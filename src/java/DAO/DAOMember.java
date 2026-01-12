package DAO;

import DAL.DBContext;
import model.member;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for members table (READ ONLY for Person 1)
 * Only SELECT operations are allowed
 */
public class DAOMember extends DBContext {

    public DAOMember() {
        super();
    }

    protected void closeResources(Connection conn, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }

    public member getFromResultSet(ResultSet rs) throws SQLException {
        member m = new member();
        m.setId(rs.getInt("id"));
        m.setUsername(rs.getString("username"));
        m.setPassword(rs.getString("password"));
        m.setEmail(rs.getString("email"));
        m.setFull_name(rs.getString("full_name"));
        m.setPhone(rs.getString("phone"));
        m.setAddress(rs.getString("address"));
        m.setMember_type(rs.getInt("member_type"));
        
        // Handle nullable coop_id
        int coopId = rs.getInt("coop_id");
        m.setCoop_id(rs.wasNull() ? null : coopId);
        
        m.setStatus(rs.getString("status"));
        
        // Handle date fields as String
        Date expiryDate = rs.getDate("expiry_date");
        m.setExpiry_date(expiryDate != null ? expiryDate.toString() : null);
        
        m.setPlan_type(rs.getString("plan_type"));
        
        Date joinedDate = rs.getDate("joined_date");
        m.setJoined_date(joinedDate != null ? joinedDate.toString() : null);
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        m.setCreated_at(createdAt != null ? createdAt.toString() : null);
        
        return m;
    }

    public List<member> getAll() {
        List<member> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY full_name";
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

    public List<member> getActive() {
        List<member> list = new ArrayList<>();
        String sql = "SELECT * FROM members WHERE status = 'Active' ORDER BY full_name";
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

    public member getById(Integer id) {
        String sql = "SELECT * FROM members WHERE id = ?";
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

    public List<member> getPaginated(int page, int pageSize) {
        List<member> list = new ArrayList<>();
        String sql = "SELECT * FROM members ORDER BY full_name LIMIT ? OFFSET ?";
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
        String sql = "SELECT COUNT(*) FROM members";
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
