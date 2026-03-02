package DAO;

import DAL.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Admin
 */
public class MaterialReceiptDAO extends DBContext {

    // Sử dụng Singleton Pattern
    public static final MaterialReceiptDAO INSTANCE = new MaterialReceiptDAO();

    public MaterialReceiptDAO() {
        super();
    }

   public boolean saveFullReceipt(int materialId, int partnerId, Integer contractId, String note,
            double total, double paid, String rDate,
            String[] whIds, String[] qtys, String[] prices, int debtorId) {
    Connection conn = null;
    try {
        conn = this.getConnection();
        if (conn == null || conn.isClosed()) {
            return false;
        }

        conn.setAutoCommit(false);

        // --- Bước 1: Lấy đơn vị tính --- (Giữ nguyên)
        String unit = "kg";
        String sqlUnit = "SELECT unit FROM materials WHERE id = ?";
        try (PreparedStatement psU = conn.prepareStatement(sqlUnit)) {
            psU.setInt(1, materialId);
            try (ResultSet rsU = psU.executeQuery()) {
                if (rsU.next()) {
                    unit = rsU.getString("unit");
                }
            }
        }

        // --- Bước 2: Tạo Phiếu Nhập (Header) ---
        // Sử dụng giờ hệ thống để tạo mã phiếu cho đồng bộ
        String rCode = "PN-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date());
        double debt = total - paid;

        // FIX: Bỏ receipt_date khỏi danh sách cột để Database tự sinh theo DEFAULT CURRENT_TIMESTAMP
        String sqlR = "INSERT INTO material_receipts (receipt_code, material_id, partner_id, contract_id, "
                + "total_amount, amount_paid, debt_amount, note) VALUES (?,?,?,?,?,?,?,?)";

        int receiptId = 0;
        try (PreparedStatement psR = conn.prepareStatement(sqlR, Statement.RETURN_GENERATED_KEYS)) {
            psR.setString(1, rCode);
            psR.setInt(2, materialId);
            psR.setInt(3, partnerId);
            if (contractId != null && contractId > 0) {
                psR.setInt(4, contractId);
            } else {
                psR.setNull(4, java.sql.Types.INTEGER);
            }
            psR.setDouble(5, total);
            psR.setDouble(6, paid);
            psR.setDouble(7, debt);
            psR.setString(8, note);
            // Database sẽ tự động điền receipt_date và created_at giống nhau

            psR.executeUpdate();
            try (ResultSet rs = psR.getGeneratedKeys()) {
                if (rs.next()) {
                    receiptId = rs.getInt(1);
                }
            }
        }

        // --- Bước 3 & 4: Chi Tiết & Tồn Kho --- (Giữ nguyên)
        String sqlD = "INSERT INTO material_receipt_details (receipt_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?,?,?,?,?)";
        String sqlI = "INSERT INTO material_inventory (coop_id, material_id, warehouse_id, quantity, unit) VALUES (?,?,?,?,?) "
                + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity), updated_at = NOW()";

        try (PreparedStatement psD = conn.prepareStatement(sqlD); PreparedStatement psI = conn.prepareStatement(sqlI)) {
            for (int i = 0; i < whIds.length; i++) {
                double q = Double.parseDouble(qtys[i]);
                double p = Double.parseDouble(prices[i]);
                int w = Integer.parseInt(whIds[i]);
                psD.setInt(1, receiptId);
                psD.setInt(2, w);
                psD.setDouble(3, q);
                psD.setDouble(4, p);
                psD.setDouble(5, q * p);
                psD.addBatch();
                psI.setInt(1, debtorId);
                psI.setInt(2, materialId);
                psI.setInt(3, w);
                psI.setDouble(4, q);
                psI.setString(5, unit);
                psI.addBatch();
            }
            psD.executeBatch();
            psI.executeBatch();
        }

        // --- Bước 5: Tạo Ghi Nợ (Ledger) ---
        if (debt > 0) {
            double lastBalance = 0;
            String sqlGetLastBal = "SELECT balance_after FROM member_transaction_ledger "
                    + "WHERE member_id = ? AND partner_id = ? "
                    + "ORDER BY id DESC LIMIT 1";
            try (PreparedStatement psB = conn.prepareStatement(sqlGetLastBal)) {
                psB.setInt(1, debtorId);
                psB.setInt(2, partnerId);
                try (ResultSet rsB = psB.executeQuery()) {
                    if (rsB.next()) {
                        lastBalance = rsB.getDouble("balance_after");
                    }
                }
            }

            double newBalance = lastBalance + debt;
            // FIX: Bỏ transaction_date khỏi danh sách cột để Database tự sinh (giống bảng QR)
            String sqlL = "INSERT INTO member_transaction_ledger (member_id, partner_id, "
                    + "reference_type, reference_id, amount, entry_type, balance_after, note) "
                    + "VALUES (?,?,?,?,?,?,?,?)";
            try (PreparedStatement psL = conn.prepareStatement(sqlL)) {
                psL.setInt(1, debtorId);
                psL.setInt(2, partnerId);
                psL.setString(3, "MATERIAL_RECEIPT");
                psL.setInt(4, receiptId);
                psL.setDouble(5, debt);
                psL.setString(6, "CREDIT");
                psL.setDouble(7, newBalance);
                psL.setString(8, "Nợ nhập vật tư phiếu: " + rCode);
                psL.executeUpdate();
            }
        }

        conn.commit();
        return true;
    } catch (Exception e) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        e.printStackTrace();
        return false;
    } finally {
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
}
