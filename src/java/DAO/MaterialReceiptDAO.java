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
                System.out.println("Connection is null or closed!");
                return false;
            }

            // BẮT ĐẦU TRANSACTION
            conn.setAutoCommit(false); 

            // --- Bước 1: Lấy đơn vị tính của vật tư ---
            String unit = "kg";
            String sqlUnit = "SELECT unit FROM materials WHERE id = ?";
            try (PreparedStatement psU = conn.prepareStatement(sqlUnit)) {
                psU.setInt(1, materialId);
                try (ResultSet rsU = psU.executeQuery()) {
                    if (rsU.next()) unit = rsU.getString("unit");
                }
            }

            // --- Bước 2: Tạo Phiếu Nhập (Header) ---
            String rCode = "PN-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            double debt = total - paid;
            String sqlR = "INSERT INTO material_receipts (receipt_code, material_id, partner_id, contract_id, " +
                          "total_amount, amount_paid, debt_amount, note, receipt_date) VALUES (?,?,?,?,?,?,?,?,?)";
            
            int receiptId = 0;
            try (PreparedStatement psR = conn.prepareStatement(sqlR, Statement.RETURN_GENERATED_KEYS)) {
                psR.setString(1, rCode);
                psR.setInt(2, materialId);
                psR.setInt(3, partnerId);
                
                if (contractId != null && contractId > 0) {
                    psR.setInt(4, contractId);
                } else {
                    psR.setNull(4, Types.INTEGER);
                }
                
                psR.setDouble(5, total);
                psR.setDouble(6, paid);
                psR.setDouble(7, debt);
                psR.setString(8, note);
                psR.setString(9, rDate);
                psR.executeUpdate();
                
                try (ResultSet rs = psR.getGeneratedKeys()) {
                    if (rs.next()) receiptId = rs.getInt(1);
                }
            }

            // --- Bước 3 & 4: Tạo Chi Tiết & Cập Nhật Tồn Kho ---
            String sqlD = "INSERT INTO material_receipt_details (receipt_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?,?,?,?,?)";
            String sqlI = "INSERT INTO material_inventory (material_id, warehouse_id, quantity, unit) VALUES (?,?,?,?) " +
                          "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity), updated_at = NOW()";

            try (PreparedStatement psD = conn.prepareStatement(sqlD);
                 PreparedStatement psI = conn.prepareStatement(sqlI)) {
                
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

                    psI.setInt(1, materialId);
                    psI.setInt(2, w);
                    psI.setDouble(3, q);
                    psI.setString(4, unit);
                    psI.addBatch();
                }
                psD.executeBatch();
                psI.executeBatch();
            }

            // --- Bước 5: Tạo Ghi Nợ (Ledger) với Số dư lũy kế ---
            if (debt > 0) {
                // 5.1 Tìm số dư nợ cũ gần nhất của HTX này
                double lastBalance = 0;
                String sqlGetLastBal = "SELECT balance_after FROM member_transaction_ledger " +
                                       "WHERE member_id = ? ORDER BY id DESC LIMIT 1";
                try (PreparedStatement psB = conn.prepareStatement(sqlGetLastBal)) {
                    psB.setInt(1, debtorId);
                    try (ResultSet rsB = psB.executeQuery()) {
                        if (rsB.next()) {
                            lastBalance = rsB.getDouble("balance_after");
                        }
                    }
                }

                // 5.2 Tính số dư mới (Nợ tăng thêm)
                double newBalance = lastBalance + debt;

                // 5.3 Chèn vào sổ cái
                String sqlL = "INSERT INTO member_transaction_ledger (member_id, partner_id, transaction_date, " +
                              "reference_type, reference_id, amount, entry_type, balance_after, note) " +
                              "VALUES (?,?,?,?,?,?,?,?,?)";
                try (PreparedStatement psL = conn.prepareStatement(sqlL)) {
                    psL.setInt(1, debtorId);
                    psL.setInt(2, partnerId);
                    psL.setString(3, rDate);
                    psL.setString(4, "MATERIAL_RECEIPT");
                    psL.setInt(5, receiptId);
                    psL.setDouble(6, debt);
                    psL.setString(7, "CREDIT"); // CREDIT: Tăng nghĩa vụ trả nợ
                    psL.setDouble(8, newBalance); // <--- Đã có số dư lũy kế
                    psL.setString(9, "Nợ nhập vật tư phiếu: " + rCode);
                    psL.executeUpdate();
                }
            }

            // CHỐT GIAO DỊCH
            conn.commit(); 
            return true;

        } catch (Exception e) {
            if (conn != null) {
                try {
                    System.err.println("Transaction failed! Rolling back...");
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