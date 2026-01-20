package DAO;

import DAL.DBContext;
import model.Contract1;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOContract extends DBContext {

    /**
     * Hàm lấy danh sách hợp đồng thông minh đã sửa lỗi hiển thị chéo:
     * - Nếu là Type 1, 3 (Nhân viên): Lấy hợp đồng của đồng nghiệp (cùng coop_id) 
     * VÀ hợp đồng của chính Hợp tác xã chủ quản (người có ID = coop_id của mình).
     * - Nếu là Type 2 (HTX - Yuki): Lấy hợp đồng của mình VÀ của tất cả thành viên cấp dưới.
     */
    public List<Contract1> getListSmart(int sessionUserId, int memberType, Integer coopId, String code, String type) {
        List<Contract1> list = new ArrayList<>();
        
        // Logic SQL mới:
        // Nhánh 1, 3: Lọc m.coop_id = ? (đồng nghiệp) HOẶC m.id = ? (ông chủ/HTX)
        // Nhánh 2: Lọc m.id = ? (chính mình) HOẶC m.coop_id = ? (thành viên dưới quyền)
        String sql = "SELECT c.* FROM contracts c "
                   + "JOIN members m ON c.member_id = m.id "
                   + "WHERE ("
                   + "    ( ? IN (1, 3) AND (m.coop_id = ? OR m.id = ?) ) " 
                   + "    OR "
                   + "    ( ? = 2 AND (m.id = ? OR m.coop_id = ?) ) "
                   + ") "
                   + "AND c.contract_code LIKE ? AND c.contract_type LIKE ?";

        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            
            // Tham số cho nhánh Nhân viên (Type 1, 3)
            pre.setInt(1, memberType);
            pre.setInt(2, coopId != null ? coopId : -1); // Lấy đồng nghiệp
            pre.setInt(3, coopId != null ? coopId : -1); // Lấy chính chủ HTX (Yuki)

            // Tham số cho nhánh HTX (Type 2 - Yuki)
            pre.setInt(4, memberType);
            pre.setInt(5, sessionUserId); // Lấy chính mình
            pre.setInt(6, sessionUserId); // Lấy thành viên cấp dưới
            
            // Tham số tìm kiếm
            pre.setString(7, "%" + (code == null ? "" : code) + "%");
            pre.setString(8, "%" + (type == null ? "" : type) + "%");

            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                list.add(new Contract1(
                    rs.getInt("id"),
                    rs.getString("contract_code"),
                    rs.getInt("member_id"),
                    rs.getString("contract_type"),
                    rs.getString("signing_date"),
                    rs.getString("expiry_date"),
                    rs.getDouble("total_value"),
                    rs.getString("status"),
                    rs.getString("document_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Giữ nguyên hàm insert
    public int insert(Contract1 c) {
        String sql = "INSERT INTO contracts (contract_code, member_id, contract_type, "
                   + "signing_date, expiry_date, total_value, status, document_path) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setString(1, c.getContractCode());
            pre.setInt(2, c.getMemberId());
            pre.setString(3, c.getContractType());
            pre.setString(4, c.getSigningDate());
            pre.setString(5, c.getExpiryDate());
            pre.setDouble(6, c.getTotalValue());
            pre.setString(7, c.getStatus());
            pre.setString(8, c.getDocumentPath());
            
            return pre.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int update(int id, String signingDate, String expiryDate, String status) {
    String sql = "UPDATE contracts SET signing_date = ?, expiry_date = ?, status = ? WHERE id = ?";
    try {
        PreparedStatement pre = conn.prepareStatement(sql);
        pre.setString(1, signingDate);
        pre.setString(2, expiryDate);
        pre.setString(3, status);
        pre.setInt(4, id);
        return pre.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0;
}
}