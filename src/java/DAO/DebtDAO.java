package DAO;

import DAL.DBContext;
import java.sql.*;
import java.util.*;
import model.DebtDTO;

public class DebtDAO extends DBContext {

    public List<DebtDTO> getMemberDebtList(int coopId) {
        List<DebtDTO> list = new ArrayList<>();
        String sql = "SELECT m.id, m.full_name, m.phone, "
                + "(SELECT l.balance_after FROM member_transaction_ledger l "
                + " WHERE l.member_id = m.id AND l.partner_id IS NULL "
                + " ORDER BY l.id DESC LIMIT 1) as current_debt "
                + "FROM members m WHERE m.coop_id = ? AND m.member_type = 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coopId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DebtDTO d = new DebtDTO();
                    d.setId(rs.getInt("id"));
                    d.setName(rs.getString("full_name"));
                    d.setPhone(rs.getString("phone"));
                    double debt = rs.getDouble("current_debt");
                    d.setAmount(rs.wasNull() ? 0 : debt);
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<DebtDTO> getPartnerDebtList(int coopId) {
        List<DebtDTO> list = new ArrayList<>();
        // Đã loại bỏ điều kiện WHERE p.type để lấy toàn bộ đối tác trong bảng
        String sql = "SELECT p.id, p.name, p.phone, "
                + "(SELECT l.balance_after FROM member_transaction_ledger l "
                + " WHERE l.member_id = ? AND l.partner_id = p.id "
                + " ORDER BY l.id DESC LIMIT 1) as current_debt "
                + "FROM partners p";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coopId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DebtDTO d = new DebtDTO();
                    d.setId(rs.getInt("id"));
                    d.setName(rs.getString("name"));
                    d.setPhone(rs.getString("phone"));
                    double debt = rs.getDouble("current_debt");
                    d.setAmount(rs.wasNull() ? 0 : debt);
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean saveVoucher(int mId, Integer pId, String vType, double amount, String method, String note, String img, String entryType) {
        String sqlV = "INSERT INTO payment_vouchers (voucher_code, voucher_type, member_id, partner_id, amount, payment_method, description, image_path) VALUES (?,?,?,?,?,?,?,?)";
        String sqlB = "SELECT balance_after FROM member_transaction_ledger WHERE member_id = ? AND " + (pId == null ? "partner_id IS NULL " : "partner_id = ? ") + "ORDER BY id DESC LIMIT 1";
        // CẬP NHẬT: Thêm cột reference_id vào cuối câu lệnh (8 tham số ?)
        String sqlL = "INSERT INTO member_transaction_ledger (member_id, partner_id, reference_type, amount, entry_type, balance_after, note, reference_id) VALUES (?,?,?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);

            // 1. Lưu Voucher và lấy ID tự tăng (Generated Keys)
            int generatedVoucherId = 0;
            String vCode = (vType.equals("RECEIPT") ? "PT-" : "PC-") + System.currentTimeMillis();
            try (PreparedStatement psV = conn.prepareStatement(sqlV, Statement.RETURN_GENERATED_KEYS)) {
                psV.setString(1, vCode);
                psV.setString(2, vType);
                psV.setInt(3, mId);
                if (pId != null) {
                    psV.setInt(4, pId);
                } else {
                    psV.setNull(4, Types.INTEGER);
                }
                psV.setDouble(5, amount);
                psV.setString(6, method);
                psV.setString(7, note);
                psV.setString(8, img);
                psV.executeUpdate();

                // Lấy ID tự tăng từ Database
                ResultSet rsKeys = psV.getGeneratedKeys();
                if (rsKeys.next()) {
                    generatedVoucherId = rsKeys.getInt(1);
                }
            }

            // 2. Lấy số dư cũ (Giữ nguyên)
            double lastBal = 0;
            try (PreparedStatement psB = conn.prepareStatement(sqlB)) {
                psB.setInt(1, mId);
                if (pId != null) {
                    psB.setInt(2, pId);
                }
                try (ResultSet rs = psB.executeQuery()) {
                    if (rs.next()) {
                        lastBal = rs.getDouble("balance_after");
                    }
                }
            }

            // SỬA LỖI LOGIC TẠI ĐÂY:
            // DEBIT = Tăng số dư (+) | CREDIT = Giảm số dư (-)
            double newBal = ("DEBIT".equals(entryType)) ? lastBal + amount : lastBal - amount;

            try (PreparedStatement psL = conn.prepareStatement(sqlL)) {
                psL.setInt(1, mId);
                if (pId != null) {
                    psL.setInt(2, pId);
                } else {
                    psL.setNull(2, Types.INTEGER);
                }
                psL.setString(3, vType.equals("RECEIPT") ? "CASH_RECEIPT" : "CASH_PAYMENT");
                psL.setDouble(4, amount);
                psL.setString(5, entryType);
                psL.setDouble(6, newBal);
                psL.setString(7, note + " (" + method + ")");
                psL.setInt(8, generatedVoucherId);
                psL.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }
// Hàm dành riêng cho Nhà cung cấp - LUÔN LUÔN TRỪ NỢ

    public boolean saveSupplierVoucher(int mId, int pId, double amount, String method, String note, String img) {
        String sqlV = "INSERT INTO payment_vouchers (voucher_code, voucher_type, member_id, partner_id, amount, payment_method, description, image_path) VALUES (?,?,?,?,?,?,?,?)";
        String sqlB = "SELECT balance_after FROM member_transaction_ledger WHERE member_id = ? AND partner_id = ? ORDER BY id DESC LIMIT 1";
        String sqlL = "INSERT INTO member_transaction_ledger (member_id, partner_id, reference_type, amount, entry_type, balance_after, note, reference_id) VALUES (?,?,?,?,?,?,?,?)";

        try {
            conn.setAutoCommit(false);
            int voucherId = 0;
            String vCode = "PC-" + System.currentTimeMillis();

            // 1. Lưu phiếu chi
            try (PreparedStatement psV = conn.prepareStatement(sqlV, Statement.RETURN_GENERATED_KEYS)) {
                psV.setString(1, vCode);
                psV.setString(2, "PAYMENT");
                psV.setInt(3, mId);
                psV.setInt(4, pId);
                psV.setDouble(5, amount);
                psV.setString(6, method);
                psV.setString(7, note);
                psV.setString(8, img);
                psV.executeUpdate();
                ResultSet rs = psV.getGeneratedKeys();
                if (rs.next()) {
                    voucherId = rs.getInt(1);
                }
            }

            // 2. Lấy nợ hiện tại
            double lastBal = 0;
            try (PreparedStatement psB = conn.prepareStatement(sqlB)) {
                psB.setInt(1, mId);
                psB.setInt(2, pId);
                try (ResultSet rs = psB.executeQuery()) {
                    if (rs.next()) {
                        lastBal = rs.getDouble("balance_after");
                    }
                }
            }

            // 3. Logic: Trả nợ NCC thì nợ phải GIẢM (Phép Trừ)
            double newBal = lastBal - amount;

            // 4. Ghi vào sổ cái (Dùng CREDIT để ký hiệu giảm nợ)
            try (PreparedStatement psL = conn.prepareStatement(sqlL)) {
                psL.setInt(1, mId);
                psL.setInt(2, pId);
                psL.setString(3, "CASH_PAYMENT");
                psL.setDouble(4, amount);
                psL.setString(5, "CREDIT");
                psL.setDouble(6, newBal);
                psL.setString(7, note + " (Trả nợ NCC)");
                psL.setInt(8, voucherId);
                psL.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
    }

    public List<Map<String, Object>> getPartnerTransactionHistory(int coopId, int partnerId) {
        List<Map<String, Object>> history = new ArrayList<>();
        String sql = "SELECT l.transaction_date, l.reference_type, l.amount, l.entry_type, l.balance_after, l.note, "
                + "COALESCE(r.receipt_code, v.voucher_code) as doc_code, "
                + "v.image_path, v.payment_method "
                + "FROM member_transaction_ledger l "
                + "LEFT JOIN material_receipts r ON l.reference_id = r.id AND l.reference_type = 'MATERIAL_RECEIPT' "
                + "LEFT JOIN payment_vouchers v ON l.reference_id = v.id AND (l.reference_type = 'CASH_PAYMENT' OR l.reference_type = 'CASH_RECEIPT') "
                + "WHERE l.member_id = ? AND l.partner_id = ? "
                + "ORDER BY l.id ASC"; // Sắp xếp từ cũ đến mới để xem luồng tiền

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, coopId);
            ps.setInt(2, partnerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("date", rs.getTimestamp("transaction_date"));
                    row.put("type", rs.getString("reference_type"));
                    row.put("amount", rs.getDouble("amount"));
                    row.put("entry", rs.getString("entry_type"));
                    row.put("balance", rs.getDouble("balance_after"));
                    row.put("note", rs.getString("note"));
                    row.put("code", rs.getString("doc_code"));
                    row.put("img", rs.getString("image_path"));
                    row.put("method", rs.getString("payment_method"));
                    history.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
    // Lấy lịch sử giao dịch riêng cho Nông dân (partner_id IS NULL)

    public List<Map<String, Object>> getMemberTransactionHistory(int memberId) {
        List<Map<String, Object>> history = new ArrayList<>();
        // JOIN với payment_vouchers để lấy image_path và voucher_code
        String sql = "SELECT l.transaction_date, l.reference_type, l.amount, l.entry_type, l.balance_after, l.note, "
                + "COALESCE(v.voucher_code, l.id) as doc_code, "
                + "v.image_path "
                + "FROM member_transaction_ledger l "
                + "LEFT JOIN payment_vouchers v ON l.reference_id = v.id AND l.reference_type IN ('CASH_PAYMENT', 'CASH_RECEIPT') "
                + "WHERE l.member_id = ? AND l.partner_id IS NULL "
                + "ORDER BY l.id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("date", rs.getTimestamp("transaction_date"));
                    row.put("type", rs.getString("reference_type"));
                    row.put("amount", rs.getDouble("amount"));
                    row.put("entry", rs.getString("entry_type"));
                    row.put("balance", rs.getDouble("balance_after"));
                    row.put("note", rs.getString("note"));
                    row.put("code", rs.getString("doc_code"));
                    row.put("img", rs.getString("image_path"));
                    history.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
}
