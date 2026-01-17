package DAO;

import DAL.DBContext;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaterialSupplyDAO extends DBContext {

    public static final MaterialSupplyDAO INSTANCE = new MaterialSupplyDAO();

    public boolean saveFullSupply(int memberId, int staffId, int materialId, Integer contractId,
            String note, double total, double paid,
            String[] whIds, String[] qtys, String[] prices) {
        Connection conn = null;
        try {
            conn = this.getConnection();
            conn.setAutoCommit(false); // BẮT ĐẦU TRANSACTION

            // 1. Tạo Mã Phiếu Xuất (PX)
            String sCode = "PX-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            double debt = total - paid;

            // 2. Lưu Header (material_supplies)
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
                    psS.setNull(4, Types.INTEGER);
                }
                psS.setDouble(5, total);
                psS.setDouble(6, paid);
                psS.setDouble(7, debt);
                psS.setString(8, note);
                psS.executeUpdate();
                ResultSet rs = psS.getGeneratedKeys();
                if (rs.next()) {
                    supplyId = rs.getInt(1);
                }
            }

            // 3. Lưu Chi Tiết & Trừ Kho (Kiểm tra tồn kho thực tế)
            String sqlD = "INSERT INTO material_supply_details (supply_id, material_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?,?,?,?,?,?)";
            String sqlUpdateInv = "UPDATE material_inventory SET quantity = quantity - ? WHERE material_id = ? AND warehouse_id = ? AND quantity >= ?";

            try (PreparedStatement psD = conn.prepareStatement(sqlD); PreparedStatement psI = conn.prepareStatement(sqlUpdateInv)) {

                for (int i = 0; i < whIds.length; i++) {
                    double q = Double.parseDouble(qtys[i]);
                    double p = Double.parseDouble(prices[i]);
                    int w = Integer.parseInt(whIds[i]);

                    // Lưu chi tiết
                    psD.setInt(1, supplyId);
                    psD.setInt(2, materialId);
                    psD.setInt(3, w);
                    psD.setDouble(4, q);
                    psD.setDouble(5, p);
                    psD.setDouble(6, q * p);
                    psD.addBatch();

                    // Cập nhật kho (Trừ đi)
                    psI.setDouble(1, q);
                    psI.setInt(2, materialId);
                    psI.setInt(3, w);
                    psI.setDouble(4, q); // Điều kiện: số tồn hiện tại phải >= số xuất
                    int affected = psI.executeUpdate();

                    if (affected == 0) {
                        throw new SQLException("Lỗi: Kho ID " + w + " không đủ số lượng tồn để xuất!");
                    }
                }
                psD.executeBatch();
            }

            // 4. Ghi Nợ vào Ledger (Thành viên nợ HTX)
            if (debt > 0) {
                // 4.1 Lấy số dư nợ cũ của Thành viên (partner_id IS NULL)
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

                // 4.2 Tính số dư mới
                double newBalance = lastBalance + debt;

                // 4.3 Chèn vào sổ cái (Cẩn thận số lượng dấu ? và chỉ số index)
                // Tổng cộng có 7 dấu '?' tương ứng với các chỉ số từ 1 đến 7
                String sqlL = "INSERT INTO member_transaction_ledger "
                        + "(member_id, partner_id, reference_type, reference_id, amount, entry_type, balance_after, note, transaction_date) "
                        + "VALUES (?, NULL, 'MATERIAL_EXPORT', ?, ?, 'DEBIT', ?, ?, NOW())";

                try (PreparedStatement psL = conn.prepareStatement(sqlL)) {
                    psL.setInt(1, memberId);      // Dấu ? thứ 1: member_id
                    psL.setInt(2, supplyId);      // Dấu ? thứ 2: reference_id
                    psL.setDouble(3, debt);       // Dấu ? thứ 3: amount
                    psL.setDouble(4, newBalance); // Dấu ? thứ 4: balance_after
                    psL.setString(5, "Nông dân nợ vật tư phiếu: " + sCode); // Dấu ? thứ 5: note

                    // Lưu ý: Nếu SQL của bạn có nhiều dấu ? hơn, hãy đếm lại. 
                    // Với chuỗi SQL ở trên, chỉ có 5 dấu '?' cần truyền.
                    psL.executeUpdate();
                }
            }

            conn.commit(); // CHỐT GIAO DỊCH
            return true;
        } catch (Exception e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}
