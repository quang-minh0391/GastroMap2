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

    public List<Contract> searchContracts(String keyword) {
    List<Contract> list = new ArrayList<>();
    // Thêm document_path vào câu SELECT
    String sql = "SELECT id, contract_code, contract_type, signing_date, expiry_date, status, document_path " +
                 "FROM contracts WHERE contract_code LIKE ? LIMIT 10";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Contract c = new Contract();
            c.setId(rs.getInt("id"));
            c.setContractCode(rs.getString("contract_code"));
            c.setContractType(rs.getString("contract_type"));
            c.setSigningDate(rs.getDate("signing_date"));
            c.setExpiryDate(rs.getDate("expiry_date"));
            c.setStatus(rs.getString("status"));
            c.setDocumentPath(rs.getString("document_path")); // Lấy đường dẫn file
            list.add(c);
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}
}