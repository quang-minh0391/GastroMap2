package DAO;

import DAL.DBContext;
import model.PaymentVoucher;
import model.FinancialTransaction; // Import từ module Finance
import java.sql.*;
import java.math.BigDecimal;

public class PaymentVoucherDAO {
    
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Hàm tạo Phiếu Thu/Chi và Tự động ghi Sổ cái
    public boolean insertVoucher(PaymentVoucher v) {
        boolean success = false;
        String sql = "INSERT INTO payment_vouchers (voucher_code, voucher_type, member_id, partner_id, amount, payment_method, description, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Chuẩn bị ghi sổ cái (FinanceDAO logic)
        String sqlLedger = "INSERT INTO financial_ledger (transaction_date, category_id, amount, transaction_type, description, source_table, source_id) VALUES (?, ?, ?, ?, ?, 'payment_vouchers', ?)";

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Transaction

            // 1. Insert Payment Voucher
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, v.getVoucherCode());
            ps.setString(2, v.getVoucherType());
            
            if(v.getMemberId() != null && v.getMemberId() > 0) ps.setInt(3, v.getMemberId());
            else ps.setNull(3, Types.INTEGER);
            
            if(v.getPartnerId() != null && v.getPartnerId() > 0) ps.setInt(4, v.getPartnerId());
            else ps.setNull(4, Types.INTEGER);
            
            ps.setBigDecimal(5, v.getAmount());
            ps.setString(6, v.getPaymentMethod());
            ps.setString(7, v.getDescription());
            ps.setTimestamp(8, v.getCreatedDate());
            
            ps.executeUpdate();
            
            // Lấy ID vừa tạo
            ResultSet gk = ps.getGeneratedKeys();
            int voucherId = 0;
            if(gk.next()) voucherId = gk.getInt(1);
            ps.close();

            // 2. Tự động ghi vào Sổ Cái Tài Chính (Finance Ledger)
            ps = conn.prepareStatement(sqlLedger);
            ps.setTimestamp(1, v.getCreatedDate());
            
            // Logic xác định Category ID (Dựa trên SQL ở bước 2)
            int catId = 3; // Mặc định chi khác
            String transType = "OUT";
            
            if("RECEIPT".equals(v.getVoucherType())) {
                transType = "IN";
                // Nếu thu từ Member (ví dụ bán vật tư) -> ID 11
                // Nếu thu khác -> ID 13
                catId = (v.getMemberId() != null && v.getMemberId() > 0) ? 11 : 13;
            } else {
                transType = "OUT";
                // Nếu chi cho Partner (Nhà cung cấp) -> ID 10
                // Nếu chi cho Member (Mua nông sản) -> ID 12
                if (v.getPartnerId() != null && v.getPartnerId() > 0) catId = 10;
                else if (v.getMemberId() != null && v.getMemberId() > 0) catId = 12;
            }
            
            ps.setInt(2, catId);
            ps.setBigDecimal(3, v.getAmount());
            ps.setString(4, transType);
            ps.setString(5, "Tự động từ Phiếu " + v.getVoucherCode() + ": " + v.getDescription());
            ps.setInt(6, voucherId); // Link nguồn
            
            ps.executeUpdate();

            conn.commit();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            try { if(conn!=null) conn.rollback(); } catch(Exception ex){}
        } finally {
            try { if(conn!=null) conn.setAutoCommit(true); conn.close(); } catch(Exception ex){}
        }
        return success;
    }
}