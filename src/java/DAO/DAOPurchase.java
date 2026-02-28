package DAO;

import DAL.DBContext;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DAOPurchase extends DBContext {

    // HÀM CHÍNH: Xử lý Transaction
    public boolean createPurchaseTransaction(
            int memberId, int productId, Date harvestDate, Date expiryDate, String unit, String note, boolean createQR,
            String[] warehouseIds, String[] quantities, String[] buyPrices, double amountPaid) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = this.conn;
            if (conn == null) {
                return false;
            }
            conn.setAutoCommit(false);

            // MỐC THỜI GIAN THỰC: Dùng để format mã phiếu cho đồng bộ
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());

            // 1. TẠO PHIẾU THU MUA (produce_receipts)
            String receiptCode = "PN-" + new SimpleDateFormat("yyyyMMdd-HHmmss").format(currentTimestamp);
            double totalAmount = 0;
            for (int i = 0; i < quantities.length; i++) {
                totalAmount += Double.parseDouble(quantities[i]) * Double.parseDouble(buyPrices[i]);
            }

            // SỬA: Bỏ cột created_at để Database tự xử lý theo giờ hệ thống
            String sqlReceipt = "INSERT INTO produce_receipts (receipt_code, member_id, purchase_date, total_amount, amount_paid, note) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlReceipt, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, receiptCode);
            ps.setInt(2, memberId);
            ps.setDate(3, harvestDate); // purchase_date chỉ lưu Ngày (DATE)
            ps.setDouble(4, totalAmount);
            ps.setDouble(5, amountPaid);
            ps.setString(6, note);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int receiptId = 0;
            if (rs.next()) {
                receiptId = rs.getInt(1);
            }

            // 2. SINH MÃ LÔ & TẠO LÔ (production_batches)
            String productName = getProductName(conn, productId);
            String batchCode = generateBatchCode(conn, productId, productName, currentTimestamp);

            double totalQuantity = 0;
            for (String q : quantities) {
                totalQuantity += Double.parseDouble(q);
            }

            // SỬA: Bỏ cột created_at để Database tự xử lý
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
            if (rs.next()) {
                batchId = rs.getInt(1);
            }

            // 3. CHI TIẾT PHIẾU & NHẬP KHO
            String sqlDetail = "INSERT INTO produce_receipt_details (receipt_id, product_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
            // SỬA: Bỏ created_at để Database tự xử lý
            String sqlStockIn = "INSERT INTO batch_stock_ins (batch_id, warehouse_id, quantity, unit, received_date, note) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlInventory = "INSERT INTO batch_inventory (warehouse_id, batch_id, remaining_quantity, unit) VALUES (?, ?, ?, ?)";

            for (int i = 0; i < warehouseIds.length; i++) {
                int whId = Integer.parseInt(warehouseIds[i]);
                double qty = Double.parseDouble(quantities[i]);
                double price = Double.parseDouble(buyPrices[i]);

                // A. Chi tiết phiếu
                try (PreparedStatement psD = conn.prepareStatement(sqlDetail)) {
                    psD.setInt(1, receiptId);
                    psD.setInt(2, productId);
                    psD.setInt(3, whId);
                    psD.setDouble(4, qty);
                    psD.setDouble(5, price);
                    psD.setDouble(6, qty * price);
                    psD.executeUpdate();
                }

                // B. Lịch sử nhập kho - received_date lưu Ngày, giờ tạo tự sinh
                try (PreparedStatement psS = conn.prepareStatement(sqlStockIn)) {
                    psS.setInt(1, batchId);
                    psS.setInt(2, whId);
                    psS.setDouble(3, qty);
                    psS.setString(4, unit);
                    psS.setDate(5, harvestDate); 
                    psS.setString(6, "Nhập từ phiếu: " + receiptCode);
                    psS.executeUpdate();
                }

                // C. Tồn kho (Giữ nguyên)
                try (PreparedStatement psI = conn.prepareStatement(sqlInventory)) {
                    psI.setInt(1, whId);
                    psI.setInt(2, batchId);
                    psI.setDouble(3, qty);
                    psI.setString(4, unit);
                    psI.executeUpdate();
                }
            }

            // 4. QR (Đã chạy đúng - Giữ nguyên Database tự xử lý created_at)
            if (createQR) {
                String sqlQR = "INSERT INTO batch_qr_codes (batch_id, qr_value, status) VALUES (?, ?, 'CREATED')";
                try (PreparedStatement psQ = conn.prepareStatement(sqlQR)) {
                    psQ.setInt(1, batchId);
                    psQ.setString(2, "QR_" + batchCode);
                    psQ.executeUpdate();
                }
            }

            // 5. CÔNG NỢ
            updateLedger(conn, memberId, receiptId, batchCode, totalAmount, amountPaid, receiptCode);

            conn.commit();
            return true;

        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ex) {}
        }
    }

    // --- CÁC HÀM PHỤ TRỢ ---

    private String generateBatchCode(Connection conn, int productId, String productName, java.sql.Timestamp ts) throws SQLException {
        String productCode = "PROD";
        if (productName != null) {
            String cleanName = productName.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
            if (cleanName.length() >= 3) {
                productCode = cleanName.substring(0, 3);
            } else if (!cleanName.isEmpty()) {
                productCode = cleanName;
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        int year = cal.get(Calendar.YEAR);

        int sequence = 1;
        // YEAR(created_at) lấy theo giờ DB nên sẽ rất chính xác
        String sqlCount = "SELECT COUNT(*) FROM production_batches WHERE product_id = ? AND YEAR(created_at) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCount)) {
            ps.setInt(1, productId);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sequence = rs.getInt(1) + 1;
                }
            }
        }
        return String.format("BATCH-%s-%d-%03d", productCode, year, sequence);
    }

    private String getProductName(Connection conn, int productId) {
        String name = "";
        try (PreparedStatement ps = conn.prepareStatement("SELECT name FROM farm_products WHERE id = ?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) name = rs.getString("name");
            }
        } catch (Exception e) {}
        return name;
    }

    private void updateLedger(Connection conn, int memberId, int receiptId, String batchCode, double totalAmount, double amountPaid, String receiptCode) throws SQLException {
        double currentBalance = 0;
        double debt = totalAmount - amountPaid;

        String sqlGetBal = "SELECT balance_after FROM member_transaction_ledger "
                + "WHERE member_id = ? AND partner_id IS NULL "
                + "ORDER BY id DESC LIMIT 1";

        try (PreparedStatement psBal = conn.prepareStatement(sqlGetBal)) {
            psBal.setInt(1, memberId);
            try (ResultSet rs = psBal.executeQuery()) {
                if (rs.next()) {
                    currentBalance = rs.getDouble("balance_after");
                }
            }
        }

        // SỬA: Bỏ transaction_date để Database tự xử lý
        String sqlLedger = "INSERT INTO member_transaction_ledger (member_id, reference_type, reference_id, amount, entry_type, balance_after, note) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sqlLedger)) {
            ps.setInt(1, memberId);
            ps.setString(2, "FARM_PURCHASE");
            ps.setInt(3, receiptId);
            ps.setDouble(4, totalAmount);
            ps.setString(5, "CREDIT");
            ps.setDouble(6, currentBalance - debt);
            ps.setString(7, "Thu mua lô: " + batchCode + " (Phiếu: " + receiptCode + ")");
            ps.executeUpdate();
        }
    }
}