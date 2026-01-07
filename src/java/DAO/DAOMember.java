package DAO;

import DAL.DBContext;
import model.Member;
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

    public Member getFromResultSet(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setUsername(rs.getString("username"));
        member.setPassword(rs.getString("password"));
        member.setFullName(rs.getString("full_name"));
        member.setPhone(rs.getString("phone"));
        member.setAddress(rs.getString("address"));
        member.setMemberType(rs.getInt("member_type"));
        member.setCoopId(rs.getObject("coop_id") != null ? rs.getInt("coop_id") : null);
        member.setStatus(rs.getString("status"));
        member.setExpiryDate(rs.getDate("expiry_date"));
        member.setPlanType(rs.getString("plan_type"));
        member.setJoinedDate(rs.getDate("joined_date"));
        member.setCreatedAt(rs.getTimestamp("created_at"));
        return member;
    }

    public List<Member> getAll() {
        List<Member> list = new ArrayList<>();
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

    public List<Member> getActive() {
        List<Member> list = new ArrayList<>();
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

    public Member getById(Integer id) {
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

    public List<Member> getPaginated(int page, int pageSize) {
        List<Member> list = new ArrayList<>();
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
