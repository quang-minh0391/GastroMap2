package DAO;

import DAL.DBContext;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Contract;

public class ContractDAO extends DBContext {
    public static final ContractDAO INSTANCE = new ContractDAO();

    private ContractDAO() { super(); }

    public List<Contract> searchContracts(String keyword, int coopId) {
    List<Contract> list = new ArrayList<>();
    // Tìm hợp đồng:
    // 1. Hợp đồng của thành viên thuộc HTX (m.coop_id = coopId)
    // 2. Hoặc hợp đồng được ký trực tiếp bởi HTX (member_id = coopId)
    String sql = "SELECT DISTINCT c.id, c.contract_code, c.contract_type, c.signing_date, c.expiry_date, c.status, c.document_path " +
                 "FROM contracts c " +
                 "LEFT JOIN members m ON c.member_id = m.id " +
                 "WHERE c.contract_code LIKE ? " +
                 "AND (c.member_id = ? OR m.coop_id = ?) " +
                 "LIMIT 10";
                 
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        ps.setInt(2, coopId); // Hợp đồng ký trực tiếp với HTX
        ps.setInt(3, coopId); // Hợp đồng của thành viên thuộc HTX
        
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Contract c = new Contract();
            c.setId(rs.getInt("id"));
            c.setContractCode(rs.getString("contract_code"));
            c.setContractType(rs.getString("contract_type"));
            c.setSigningDate(rs.getDate("signing_date"));
            c.setExpiryDate(rs.getDate("expiry_date"));
            c.setStatus(rs.getString("status"));
            c.setDocumentPath(rs.getString("document_path"));
            list.add(c);
        }
    } catch (Exception e) { 
        e.printStackTrace(); 
    }
    return list;
}
}