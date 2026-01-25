package DAO;

import DAL.DBContext;
import model.FinancialTransaction;
import model.TransactionCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.math.BigDecimal;

public class FinanceDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. Lấy tất cả danh mục (Dropdown)
    public List<TransactionCategory> getAllCategories() {
        List<TransactionCategory> list = new ArrayList<>();
        String query = "SELECT * FROM transaction_categories";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new TransactionCategory(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getString("code")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // 2. Tìm kiếm & Phân trang (Sổ cái)
    public List<FinancialTransaction> searchTransactions(java.sql.Date fromDate, java.sql.Date toDate, String type, Integer categoryId, String sortBy, String sortOrder, int pageIndex, int pageSize) {
        List<FinancialTransaction> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT f.*, c.name as cat_name FROM financial_ledger f LEFT JOIN transaction_categories c ON f.category_id = c.id WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (fromDate != null) {
            sql.append(" AND f.transaction_date >= ?");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND f.transaction_date <= ?");
            params.add(toDate);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND f.transaction_type = ?");
            params.add(type);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND f.category_id = ?");
            params.add(categoryId);
        }

        String col = "f.transaction_date";
        if ("amount".equals(sortBy)) {
            col = "f.amount";
        } else if ("cat_name".equals(sortBy)) {
            col = "c.name";
        } else if ("type".equals(sortBy)) {
            col = "f.transaction_type";
        }
        String dir = "DESC";
        if ("ASC".equalsIgnoreCase(sortOrder)) {
            dir = "ASC";
        }

        sql.append(" ORDER BY ").append(col).append(" ").append(dir);
        sql.append(" LIMIT ? OFFSET ?");
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
                FinancialTransaction t = new FinancialTransaction(rs.getInt("id"), rs.getTimestamp("transaction_date"), rs.getInt("category_id"), rs.getBigDecimal("amount"), rs.getString("transaction_type"), rs.getString("source_table"), rs.getInt("source_id"), rs.getString("description"));
//                t.setCategoryName(rs.getString("cat_name"));
//                list.add(t);
                String catName = rs.getString("cat_name");
                t.setCategoryName(catName != null ? catName : "Khác (hoặc ID " + rs.getInt("category_id") + ")");
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    // 3. Đếm tổng số giao dịch
    public int countTransactions(java.sql.Date fromDate, java.sql.Date toDate, String type, Integer categoryId) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM financial_ledger f WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (fromDate != null) {
            sql.append(" AND f.transaction_date >= ?");
            params.add(fromDate);
        }
        if (toDate != null) {
            sql.append(" AND f.transaction_date <= ?");
            params.add(toDate);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND f.transaction_type = ?");
            params.add(type);
        }
        if (categoryId != null && categoryId > 0) {
            sql.append(" AND f.category_id = ?");
            params.add(categoryId);
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
        } finally {
            closeResources();
        }
        return count;
    }

    // 4. Thống kê Theo Năm (5 năm gần nhất)
    public List<double[]> getYearlyStatistics() {
        List<double[]> stats = new ArrayList<>();
        String sql = "SELECT YEAR(transaction_date) as y, transaction_type, SUM(amount) FROM financial_ledger WHERE transaction_date >= DATE_SUB(NOW(), INTERVAL 5 YEAR) GROUP BY YEAR(transaction_date), transaction_type ORDER BY y ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            Map<Integer, double[]> map = new TreeMap<>();
            while (rs.next()) {
                int year = rs.getInt("y");
                String type = rs.getString("transaction_type");
                double amount = rs.getDouble(3);
                if (!map.containsKey(year)) {
                    map.put(year, new double[]{0, 0});
                }
                if ("IN".equals(type)) {
                    map.get(year)[0] = amount;
                } else {
                    map.get(year)[1] = amount;
                }
            }
            if (map.isEmpty()) {
                map.put(java.time.Year.now().getValue(), new double[]{0, 0});
            }
            for (Map.Entry<Integer, double[]> entry : map.entrySet()) {
                stats.add(new double[]{entry.getKey(), entry.getValue()[0], entry.getValue()[1]});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return stats;
    }

    // 5. Thống kê Theo Tháng (Năm hiện tại)
    public List<double[]> getMonthlyStatistics(int year) {
        List<double[]> stats = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            stats.add(new double[]{i, 0, 0});
        }
        String sql = "SELECT MONTH(transaction_date) as m, transaction_type, SUM(amount) FROM financial_ledger WHERE YEAR(transaction_date) = ? GROUP BY MONTH(transaction_date), transaction_type";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, year);
            rs = ps.executeQuery();
            while (rs.next()) {
                int month = rs.getInt("m");
                String type = rs.getString("transaction_type");
                double amount = rs.getDouble(3);
                if ("IN".equals(type)) {
                    stats.get(month - 1)[1] = amount;
                } else {
                    stats.get(month - 1)[2] = amount;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return stats;
    }

    // Các hàm phụ trợ (Giữ nguyên)
    public void insertTransaction(FinancialTransaction trans) {
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement("INSERT INTO financial_ledger (transaction_date, category_id, amount, transaction_type, description) VALUES (?, ?, ?, ?, ?)");
            // Nếu có ngày thì dùng, nếu null thì lấy giờ hiện tại (phòng hờ)
            if (trans.getTransactionDate() != null) {
                ps.setTimestamp(1, trans.getTransactionDate());
            } else {
                ps.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            }

            ps.setInt(2, trans.getCategoryId());
            ps.setBigDecimal(3, trans.getAmount());
            ps.setString(4, trans.getTransactionType());
            ps.setString(5, trans.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("DEBUG: LỖI INSERT GIAO DỊCH !!!!!");
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public double getTotalAmount(String type) {
        double total = 0;
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement("SELECT SUM(amount) FROM financial_ledger WHERE transaction_type = ?");
            ps.setString(1, type);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception e) {
        } finally {
            closeResources();
        }
        return total;
    }

    public String getCategoryType(int categoryId) {
        String type = "";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement("SELECT type FROM transaction_categories WHERE id = ?");
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();
            if (rs.next()) {
                type = rs.getString("type");
            }
        } catch (Exception e) {
        } finally {
            closeResources();
        }
        return type;
    }

    // Đóng kết nối an toàn (Tránh leak)
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

    // Giữ hàm này để tương thích nếu code cũ còn gọi
    public List<FinancialTransaction> getAllTransactions() {
        return searchTransactions(null, null, null, null, null, null, 1, 1000);
    }
}
