package DAO;

import DAL.DBContext;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaterialSupplyDAO extends DBContext {

    public static final MaterialSupplyDAO INSTANCE = new MaterialSupplyDAO();

    public boolean saveFullSupply(int memberId, int staffId, int materialId, Integer contractId,
            String note, double total, double paid,
            String[] whIds, String[] qtys, String[] prices, int coopId) {
    Connection conn = null;
    try {
        conn = this.getConnection();
        conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

        // Mốc thời gian này chỉ dùng để tạo mã phiếu (vCode) cho đồng bộ
        java.util.Date now = new java.util.Date();

        // 1. Tạo Mã Phiếu Xuất (PX)
        String sCode = "PX-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(now);
        double debt = total - paid;

        // 2. Lưu Header (material_supplies) 
        // FIX: Bỏ cột supply_date để Database tự xử lý mặc định
        String sqlS = "INSERT INTO material_supplies (supply_code, member_id, staff_id, contract_id, "
                + "total_amount, amount_paid, debt_amount, note) VALUES (?,?,?,?,?,?,?,?)";
        
        int supplyId = 0;
        try (PreparedStatement psS = conn.prepareStatement(sqlS, Statement.RETURN_GENERATED_KEYS)) {
            psS.setString(1, sCode);
            psS.setInt(2, memberId);
            psS.setInt(3, staffId);
            if (contractId != null && contractId > 0) {
                psS.setInt(4, contractId);
            } else {
                psS.setNull(4, java.sql.Types.INTEGER);
            }
            psS.setDouble(5, total);
            psS.setDouble(6, paid);
            psS.setDouble(7, debt);
            psS.setString(8, note);
            // Database sẽ tự điền supply_date và created_at dựa trên giờ máy chủ
            
            psS.executeUpdate();
            ResultSet rs = psS.getGeneratedKeys();
            if (rs.next()) {
                supplyId = rs.getInt(1);
            }
        }

        // 3. Lưu Chi Tiết & Trừ Kho (Giữ nguyên 100% logic của bạn)
        String sqlD = "INSERT INTO material_supply_details (supply_id, material_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?,?,?,?,?,?)";
        String sqlUpdateInv = "UPDATE material_inventory SET quantity = quantity - ?, updated_at = NOW() "
                + "WHERE material_id = ? AND warehouse_id = ? AND coop_id = ? AND quantity >= ?";
        
        try (PreparedStatement psD = conn.prepareStatement(sqlD); 
             PreparedStatement psI = conn.prepareStatement(sqlUpdateInv)) {

            for (int i = 0; i < whIds.length; i++) {
                double q = Double.parseDouble(qtys[i]);
                double p = Double.parseDouble(prices[i]);
                int w = Integer.parseInt(whIds[i]);

                psD.setInt(1, supplyId);
                psD.setInt(2, materialId);
                psD.setInt(3, w);
                psD.setDouble(4, q);
                psD.setDouble(5, p);
                psD.setDouble(6, q * p);
                psD.addBatch();

                psI.setDouble(1, q);
                psI.setInt(2, materialId);
                psI.setInt(3, w);
                psI.setInt(4, coopId);
                psI.setDouble(5, q);
                int affected = psI.executeUpdate();

                if (affected == 0) {
                    throw new SQLException("Lỗi: Kho ID " + w + " không đủ số lượng tồn để xuất!");
                }
            }
            psD.executeBatch();
        }

        // 4. Ghi Nợ vào Ledger (Thành viên nợ HTX)
        if (debt > 0) {
            double lastBalance = 0;
            String sqlGetBal = "SELECT balance_after FROM member_transaction_ledger "
                    + "WHERE member_id = ? AND partner_id IS NULL ORDER BY id DESC LIMIT 1";
            try (PreparedStatement psB = conn.prepareStatement(sqlGetBal)) {
                psB.setInt(1, memberId);
                try (ResultSet rsB = psB.executeQuery()) {
                    if (rsB.next()) {
                        lastBalance = rsB.getDouble("balance_after");
                    }
                }
            }

            double newBalance = lastBalance + debt;

            // FIX: Bỏ transaction_date để Database tự xử lý đồng bộ với phiếu xuất
            String sqlL = "INSERT INTO member_transaction_ledger "
                    + "(member_id, partner_id, reference_type, reference_id, amount, entry_type, balance_after, note) "
                    + "VALUES (?, NULL, 'MATERIAL_EXPORT', ?, ?, 'DEBIT', ?, ?)";

            try (PreparedStatement psL = conn.prepareStatement(sqlL)) {
                psL.setInt(1, memberId);
                psL.setInt(2, supplyId);
                psL.setDouble(3, debt);
                psL.setDouble(4, newBalance);
                psL.setString(5, "Nông dân nợ vật tư phiếu: " + sCode);
                // Database tự điền transaction_date

                psL.executeUpdate();
            }
        }

        conn.commit(); 
        return true;
    } catch (Exception e) {
        if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace();
        return false;
    } finally {
        if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
}
