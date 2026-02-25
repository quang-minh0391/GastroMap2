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
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // =================================================================================
            // 1. TẠO PHIẾU THU MUA (produce_receipts)
            // =================================================================================
            String receiptCode = "PN_" + System.currentTimeMillis(); // Mã phiếu nhập vẫn để random cho nhanh

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
            if (rs.next()) {
                receiptId = rs.getInt(1);
            }

            // =================================================================================
            // 2. SINH MÃ LÔ TỰ ĐỘNG THEO CHUẨN (Batch Code Generation)
            // =================================================================================
            String productName = getProductName(conn, productId); // Hàm phụ lấy tên SP
            String batchCode = generateBatchCode(conn, productId, productName); // Hàm sinh mã lô chuẩn

            // TẠO LÔ SẢN XUẤT (production_batches)
            double totalQuantity = 0;
            for (String q : quantities) {
                totalQuantity += Double.parseDouble(q);
            }

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

            // =================================================================================
            // 3. LƯU CHI TIẾT (produce_receipt_details) & NHẬP KHO (batch_stock_ins)
            // =================================================================================
            String sqlDetail = "INSERT INTO produce_receipt_details (receipt_id, product_id, warehouse_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlStockIn = "INSERT INTO batch_stock_ins (batch_id, warehouse_id, quantity, unit, received_date, note) VALUES (?, ?, ?, ?, ?, ?)";
            String sqlInventory = "INSERT INTO batch_inventory (warehouse_id, batch_id, remaining_quantity, unit) VALUES (?, ?, ?, ?)";

            for (int i = 0; i < warehouseIds.length; i++) {
                int whId = Integer.parseInt(warehouseIds[i]);
                double qty = Double.parseDouble(quantities[i]);
                double price = Double.parseDouble(buyPrices[i]);
                double subtotal = qty * price;

                // A. Lưu chi tiết phiếu (quan trọng để lưu giá)
                ps = conn.prepareStatement(sqlDetail);
                ps.setInt(1, receiptId);
                ps.setInt(2, productId);
                ps.setInt(3, whId);
                ps.setDouble(4, qty);
                ps.setDouble(5, price);
                ps.setDouble(6, subtotal);
                ps.executeUpdate();

                // B. Lưu lịch sử nhập kho
                ps = conn.prepareStatement(sqlStockIn);
                ps.setInt(1, batchId);
                ps.setInt(2, whId);
                ps.setDouble(3, qty);
                ps.setString(4, unit);
                ps.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                ps.setString(6, "Nhập từ phiếu: " + receiptCode);
                ps.executeUpdate();

                // C. Tăng tồn kho
                ps = conn.prepareStatement(sqlInventory);
                ps.setInt(1, whId);
                ps.setInt(2, batchId);
                ps.setDouble(3, qty);
                ps.setString(4, unit);
                ps.executeUpdate();
            }

            // =================================================================================
            // 4. CÁC BƯỚC CÒN LẠI (QR & CÔNG NỢ) - GIỮ NGUYÊN NHƯ CŨ
            // =================================================================================
            if (createQR) {
                String sqlQR = "INSERT INTO batch_qr_codes (batch_id, qr_value, status) VALUES (?, ?, 'CREATED')";
                ps = conn.prepareStatement(sqlQR);
                ps.setInt(1, batchId);
                ps.setString(2, "QR_" + batchCode);
                ps.executeUpdate();
            }

            // Xử lý công nợ (ghi CÓ và ghi NỢ) - Logic giống hệt bài trước
            updateLedger(conn, memberId, receiptId, batchCode, totalAmount, amountPaid, receiptCode);
// 5. [MỚI] GHI SỔ CÁI TÀI CHÍNH (Chi trả tiền ngay)
            // =================================================================================
            if (amountPaid > 0) {
                // Giả định ID=12 là "Chi trả thành viên (Mua nông sản)" trong bảng transaction_categories
                // Bạn cần kiểm tra trong DB xem ID này có đúng là 12 không nhé!
                int financeCatId = 12;

                String sqlFinance = "INSERT INTO financial_ledger (transaction_date, category_id, amount, transaction_type, description, source_table, source_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(sqlFinance);
                ps.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis())); // Thời gian chi tiền
                ps.setInt(2, financeCatId);
                ps.setDouble(3, amountPaid);
                ps.setString(4, "OUT"); // Loại giao dịch là CHI
                ps.setString(5, "Chi trả mua hàng phiếu: " + receiptCode + " (" + productName + ")");
                ps.setString(6, "produce_receipts"); // Nguồn gốc để truy vết
                ps.setInt(7, receiptId); // ID của phiếu nhập
                ps.executeUpdate();
            }
            conn.commit(); // Lưu tất cả
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception ex) {
            }
        }
    }

    // --- CÁC HÀM PHỤ TRỢ (HELPER METHODS) ---
    // 1. Hàm sinh mã BATCH-{CODE}-{YEAR}-{SEQ}
    private String generateBatchCode(Connection conn, int productId, String productName) throws SQLException {
        // Tạo mã tắt từ tên (VD: Lúa ST25 -> LUA)
        String productCode = "PROD";
        if (productName != null && productName.length() >= 3) {
            // Lấy 3 ký tự đầu viết hoa, bỏ dấu tiếng Việt (nếu cần xử lý kỹ hơn thì dùng thư viện)
            productCode = productName.substring(0, 3).toUpperCase().replaceAll("[^A-Z]", "X");
        }

        // Lấy năm hiện tại
        int year = Calendar.getInstance().get(Calendar.YEAR);

        // Đếm số lượng lô của sản phẩm này trong năm nay
        int sequence = 1;
        String sqlCount = "SELECT COUNT(*) FROM production_batches WHERE product_id = ? AND YEAR(created_at) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCount)) {
            ps.setInt(1, productId);
            ps.setInt(2, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sequence = rs.getInt(1) + 1; // Tăng lên 1
                }
            }
        }

        // Format: BATCH-LUA-2026-001
        return String.format("BATCH-%s-%d-%03d", productCode, year, sequence);
    }

    // 2. Lấy tên sản phẩm
    private String getProductName(Connection conn, int productId) {
        String name = "";
        try (PreparedStatement ps = conn.prepareStatement("SELECT name FROM farm_products WHERE id = ?")) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    name = rs.getString("name");
                }
            }
        } catch (Exception e) {
        }
        return name;
    }

    // 3. Hàm cập nhật công nợ (Tách ra cho gọn)
    private void updateLedger(Connection conn, int memberId, int receiptId, String batchCode, double totalAmount, double amountPaid, String receiptCode) throws SQLException {

        double currentBalance = 0;
        double debt = totalAmount - amountPaid;
        // Bước: Truy vấn để lấy balance_after mới nhất của thành viên này
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

        String sqlLedger = "INSERT INTO member_transaction_ledger (member_id, transaction_date, reference_type, reference_id, amount, entry_type, balance_after, note) VALUES (?, NOW(), ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sqlLedger);
        ps.setInt(1, memberId);
        ps.setString(2, "FARM_PURCHASE");
        ps.setInt(3, receiptId);
        ps.setDouble(4, totalAmount);
        ps.setString(5, "CREDIT");
        ps.setDouble(6, currentBalance - debt);
        ps.setString(7, "Thu mua lô: " + batchCode);
        ps.executeUpdate();

    }
}
