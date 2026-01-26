package DAO;

import DAL.DBContext;
import model.FundCategory;
import model.FundTransaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.member;

public class FundDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // --- 1. Lấy danh sách thành viên (Đã cập nhật thêm username để tìm kiếm) ---
    public List<member> getAllMembers() {
        List<member> list = new ArrayList<>();
        // Lấy thêm username để phục vụ tính năng tìm kiếm trong JSP
        String sql = "SELECT id, username, full_name, phone FROM members WHERE status = 'Active' ORDER BY full_name ASC"; 
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                member m = new member();
                m.setId(rs.getInt("id"));
                m.setUsername(rs.getString("username")); // Cần đảm bảo model.member có hàm setUsername
                m.setFull_name(rs.getString("full_name"));
                m.setPhone(rs.getString("phone")); 
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // --- 2. Lấy danh sách Quỹ (Đã cập nhật Sắp xếp giảm dần theo tiền) ---
    public List<FundCategory> getAllFunds() {
        List<FundCategory> list = new ArrayList<>();
        // Thêm ORDER BY current_balance DESC để đưa các quỹ nhiều tiền lên đầu (Top 4)
        String sql = "SELECT * FROM fund_categories ORDER BY current_balance DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new FundCategory(
                        rs.getInt("id"),
                        rs.getString("fund_name"),
                        rs.getString("description"),
                        rs.getBigDecimal("current_balance")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // --- 3. Thêm Quỹ Mới (METHOD MỚI) ---
    public boolean insertFundCategory(String name, String desc, BigDecimal initialBalance) {
        String sql = "INSERT INTO fund_categories (fund_name, description, current_balance) VALUES (?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, desc);
            ps.setBigDecimal(3, initialBalance);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    // --- 4. Lấy lịch sử giao dịch gần đây ---
    public List<FundTransaction> getRecentTransactions(int limit) {
        List<FundTransaction> list = new ArrayList<>();
        String sql = "SELECT ft.*, fc.fund_name, m.full_name "
                + "FROM fund_transactions ft "
                + "JOIN fund_categories fc ON ft.fund_id = fc.id "
                + "LEFT JOIN members m ON ft.member_id = m.id "
                + "ORDER BY ft.transaction_date DESC LIMIT ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                FundTransaction ft = new FundTransaction(
                        rs.getInt("id"),
                        rs.getInt("fund_id"),
                        rs.getInt("member_id"),
                        rs.getString("transaction_type"),
                        rs.getBigDecimal("amount"),
                        rs.getTimestamp("transaction_date"),
                        rs.getString("note")
                );
                ft.setFundName(rs.getString("fund_name"));
                ft.setMemberName(rs.getString("full_name"));
                list.add(ft);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // --- 5. Xử lý Giao dịch (Giữ nguyên logic Transaction) ---
    public boolean insertFundTransaction(FundTransaction trans) {
        boolean success = false;

        String insertFundLog = "INSERT INTO fund_transactions (fund_id, member_id, transaction_type, amount, note, transaction_date) VALUES (?, ?, ?, ?, ?, ?)";
        String updateBalance = "UPDATE fund_categories SET current_balance = current_balance + ? WHERE id = ?";
        String insertLedger = "INSERT INTO financial_ledger (transaction_date, category_id, amount, transaction_type, description, source_table, source_id) VALUES (?, ?, ?, ?, ?, 'fund_transactions', ?)";

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // B1: Insert Fund Transaction
            ps = conn.prepareStatement(insertFundLog, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, trans.getFundId());
            if (trans.getMemberId() > 0) {
                ps.setInt(2, trans.getMemberId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }

            ps.setString(3, trans.getTransactionType());
            ps.setBigDecimal(4, trans.getAmount());
            ps.setString(5, trans.getNote());
            ps.setTimestamp(6, trans.getTransactionDate());
            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();
            int fundTransId = 0;
            if (generatedKeys.next()) {
                fundTransId = generatedKeys.getInt(1);
            }
            ps.close();

            // B2: Update Balance
            ps = conn.prepareStatement(updateBalance);
            BigDecimal balanceChange = trans.getAmount();
            if ("WITHDRAW".equals(trans.getTransactionType())) {
                balanceChange = balanceChange.negate();
            }
            ps.setBigDecimal(1, balanceChange);
            ps.setInt(2, trans.getFundId());
            ps.executeUpdate();
            ps.close();

            // B3: Insert Ledger
            ps = conn.prepareStatement(insertLedger);
            ps.setTimestamp(1, trans.getTransactionDate());
            if ("DEPOSIT".equals(trans.getTransactionType())) {
                ps.setInt(2, 1); // ID 1: Thu khác
                ps.setString(4, "IN");
            } else {
                ps.setInt(2, 3); // ID 3: Chi khác
                ps.setString(4, "OUT");
            }
            ps.setBigDecimal(3, trans.getAmount());
            ps.setString(5, "Tự động từ Quỹ: " + trans.getNote());
            ps.setInt(6, fundTransId);
            ps.executeUpdate();

            conn.commit(); // Lưu tất cả
            success = true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
            }
            closeResources();
        }
        return success;
    }

    // Trong FundDAO.java

// 1. Cập nhật hàm searchFundTransactions
public List<FundTransaction> searchFundTransactions(String fundName, String memberName, String type, 
                                                    Date dateFrom, Date dateTo, 
                                                    int pageIndex, int pageSize) {
    List<FundTransaction> list = new ArrayList<>();
    
    // JOIN bảng fund_categories (fc) để lấy tên quỹ, members (m) lấy tên thành viên
    StringBuilder sql = new StringBuilder("SELECT ft.*, fc.fund_name, m.full_name " +
                                          "FROM fund_transactions ft " +
                                          "JOIN fund_categories fc ON ft.fund_id = fc.id " +
                                          "LEFT JOIN members m ON ft.member_id = m.id " +
                                          "WHERE 1=1 ");
    
    List<Object> params = new ArrayList<>();

    // --- SỬA Ở ĐÂY: Tìm theo tên quỹ (LIKE) ---
    if (fundName != null && !fundName.trim().isEmpty()) {
        sql.append(" AND fc.fund_name LIKE ?");
        params.add("%" + fundName.trim() + "%");
    }
    
    // Tìm theo tên thành viên
    if (memberName != null && !memberName.trim().isEmpty()) {
        sql.append(" AND m.full_name LIKE ?");
        params.add("%" + memberName.trim() + "%");
    }

    // Tìm theo loại giao dịch
    if (type != null && !type.isEmpty()) {
        sql.append(" AND ft.transaction_type = ?");
        params.add(type);
    }

    // Lọc theo ngày
    if (dateFrom != null) {
        sql.append(" AND DATE(ft.transaction_date) >= ?");
        params.add(dateFrom);
    }
    if (dateTo != null) {
        sql.append(" AND DATE(ft.transaction_date) <= ?");
        params.add(dateTo);
    }

    // Sắp xếp & Phân trang
    sql.append(" ORDER BY ft.transaction_date DESC LIMIT ? OFFSET ?");
    params.add(pageSize);
    params.add((pageIndex - 1) * pageSize);

    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
        rs = ps.executeQuery();
        while (rs.next()) {
            FundTransaction ft = new FundTransaction(
                    rs.getInt("id"),
                    rs.getInt("fund_id"),
                    rs.getInt("member_id"),
                    rs.getString("transaction_type"),
                    rs.getBigDecimal("amount"),
                    rs.getTimestamp("transaction_date"),
                    rs.getString("note")
            );
            ft.setFundName(rs.getString("fund_name"));
            ft.setMemberName(rs.getString("full_name"));
            list.add(ft);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
    return list;
}

// 2. Cập nhật hàm countFundTransactions (Logic tương tự)
public int countFundTransactions(String fundName, String memberName, String type, Date dateFrom, Date dateTo) {
    int count = 0;
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM fund_transactions ft " +
                                          "JOIN fund_categories fc ON ft.fund_id = fc.id " + // Cần JOIN để lọc tên
                                          "LEFT JOIN members m ON ft.member_id = m.id " +
                                          "WHERE 1=1 ");
    List<Object> params = new ArrayList<>();

    // --- SỬA Ở ĐÂY ---
    if (fundName != null && !fundName.trim().isEmpty()) {
        sql.append(" AND fc.fund_name LIKE ?");
        params.add("%" + fundName.trim() + "%");
    }
    
    if (memberName != null && !memberName.trim().isEmpty()) {
        sql.append(" AND m.full_name LIKE ?");
        params.add("%" + memberName.trim() + "%");
    }
    if (type != null && !type.isEmpty()) {
        sql.append(" AND ft.transaction_type = ?");
        params.add(type);
    }
    if (dateFrom != null) {
        sql.append(" AND DATE(ft.transaction_date) >= ?");
        params.add(dateFrom);
    }
    if (dateTo != null) {
        sql.append(" AND DATE(ft.transaction_date) <= ?");
        params.add(dateTo);
    }

    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
        rs = ps.executeQuery();
        if (rs.next()) {
            count = rs.getInt(1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        closeResources();
    }
    return count;
}

    // Đóng kết nối
    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        }
    }
}