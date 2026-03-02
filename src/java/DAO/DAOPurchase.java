package DAO;

import DAL.DBContext;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DAOPurchase extends DBContext {

    public boolean createPurchaseTransaction(
            int memberId, int productId, Date harvestDate, Date expiryDate, String unit, String note, boolean createQR,
            String[] warehouseIds, String[] quantities, String[] buyPrices, double amountPaid) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = this.conn;
            if (conn == null) return false;
            conn.setAutoCommit(false);

            // 1. TẠO PHIẾU THU MUA
            String receiptCode = "PN_" + System.currentTimeMillis();
            double totalAmount = 0;
            for (int i = 0; i < quantities.length; i++) {
                totalAmount += Double.parseDouble(quantities[i]) * Double.parseDouble(buyPrices[i]);
            }

            String sqlReceipt = "INSERT INTO produce_receipts (receipt_code, member_id, purchase_date, total_amount, amount_paid, note) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlReceipt, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, receiptCode);
            ps.setInt(2, memberId);
            ps.setDate(3, harvestDate);
            ps.setDouble(4, totalAmount);
            ps.setDouble(5, amountPaid);
            ps.setString(6, note);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int receiptId = 0;
            if (rs.next()) receiptId = rs.getInt(1);
            ps.close(); // Đóng ngay sau khi xong bước 1

            // 2. SINH MÃ LÔ & TẠO LÔ
            String productName = getProductName(conn, productId);
            String batchCode = generateBatchCode(conn, productId, productName);
            double totalQuantity = 0;
            for (String q : quantities) totalQuantity += Double.parseDouble(q);

            String sqlBatch = "INSERT INTO production_batches (batch_code, product_id, member_id, harvest_date, expiry_date, total_quantity, unit, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'AVAILABLE')";
            ps = conn.prepareStatement(sqlBatch, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, batchCode);
            ps.setInt(2, productId);
            ps.setInt(3, memberId);
            ps.setDate(4, harvestDate);
            ps.setDate(5, expiryDate);
            ps.setDouble(6, totalQuantity);
            ps.setString(7, unit);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int batchId = 0;
            if (rs.next()) batchId = rs.getInt(1);
            ps.close(); // Đóng ngay sau khi xong bước 2

            // 3. CHI TIẾT PHIẾU & NHẬP KHO
            for (int i = 0; i < warehouseIds.length; i++) {
                int whId = Integer.parseInt(warehouseIds[i]);
                double qty = Double.parseDouble(quantities[i]);
                double price = Double.parseDouble(buyPrices[i]);

                ps = conn.prepareStatement("INSERT INTO produce_receipt_details (receipt_id, product_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, receiptId); ps.setInt(2, productId); ps.setInt(3, whId);
                ps.setDouble(4, qty); ps.setDouble(5, price); ps.setDouble(6, qty * price);
                ps.executeUpdate();
                ps.close();

                ps = conn.prepareStatement("INSERT INTO batch_stock_ins (batch_id, warehouse_id, quantity, unit, received_date, note) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setInt(1, batchId); ps.setInt(2, whId); ps.setDouble(3, qty); ps.setString(4, unit);
                ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                ps.setString(6, "Nhập từ phiếu: " + receiptCode);
                ps.executeUpdate();
                ps.close();

                ps = conn.prepareStatement("INSERT INTO batch_inventory (warehouse_id, batch_id, remaining_quantity, unit) VALUES (?, ?, ?, ?)");
                ps.setInt(1, whId); ps.setInt(2, batchId); ps.setDouble(3, qty); ps.setString(4, unit);
                ps.executeUpdate();
                ps.close();
            }

            // 4. QR CODE
            if (createQR) {
                ps = conn.prepareStatement("INSERT INTO batch_qr_codes (batch_id, qr_value, status) VALUES (?, ?, 'CREATED')");
                ps.setInt(1, batchId);
                ps.setString(2, "QR_" + batchCode);
                ps.executeUpdate();
                ps.close();
            }

            // 5. CÔNG NỢ: ĐÚNG LOGIC > 0 CỦA QUỲNH
            // Dùng Math.round để triệt tiêu sai số double, đảm bảo if(>0) chạy chuẩn
            double debt = Math.round(totalAmount - amountPaid); 
            if (debt > 0) {
                updateLedger(conn, memberId, receiptId, batchCode, totalAmount, amountPaid, receiptCode);
            }

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ex) {}
        }
    }

    private void updateLedger(Connection conn, int memberId, int receiptId, String batchCode, double totalAmount, double amountPaid, String receiptCode) throws SQLException {
        double currentBalance = 0;
        double debt = Math.round(totalAmount - amountPaid);

        String sqlGetBal = "SELECT balance_after FROM member_transaction_ledger WHERE member_id = ? AND partner_id IS NULL ORDER BY id DESC LIMIT 1";
        try (PreparedStatement psBal = conn.prepareStatement(sqlGetBal)) {
            psBal.setInt(1, memberId);
            try (ResultSet rsB = psBal.executeQuery()) {
                if (rsB.next()) currentBalance = rsB.getDouble("balance_after");
            }
        }

        String sqlLedger = "INSERT INTO member_transaction_ledger (member_id, transaction_date, reference_type, reference_id, amount, entry_type, balance_after, note) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement psL = conn.prepareStatement(sqlLedger)) {
            psL.setInt(1, memberId);
            psL.setString(2, "FARM_PURCHASE");
            psL.setInt(3, receiptId);
            psL.setDouble(4, debt); // Giữ nguyên biến của Quỳnh
            psL.setString(5, "CREDIT");
            psL.setDouble(6, currentBalance - debt);
            psL.setString(7, "Thu mua lô: " + batchCode);
            psL.executeUpdate();
        }
    }

    private String generateBatchCode(Connection conn, int productId, String productName) throws SQLException {
        String productCode = (productName != null && productName.length() >= 3) ? productName.substring(0, 3).toUpperCase().replaceAll("[^A-Z]", "X") : "PROD";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int sequence = 1;
        String sqlCount = "SELECT COUNT(*) FROM production_batches WHERE product_id = ? AND YEAR(created_at) = ?";
        try (PreparedStatement psC = conn.prepareStatement(sqlCount)) {
            psC.setInt(1, productId);
            psC.setInt(2, year);
            try (ResultSet rsC = psC.executeQuery()) {
                if (rsC.next()) sequence = rsC.getInt(1) + 1;
            }
        }
        return String.format("BATCH-%s-%d-%03d", productCode, year, sequence);
    }

    private String getProductName(Connection conn, int productId) {
        String name = "";
        try (PreparedStatement psN = conn.prepareStatement("SELECT name FROM farm_products WHERE id = ?")) {
            psN.setInt(1, productId);
            try (ResultSet rsN = psN.executeQuery()) {
                if (rsN.next()) name = rsN.getString("name");
            }
        } catch (Exception e) {}
        return name;
    }
}