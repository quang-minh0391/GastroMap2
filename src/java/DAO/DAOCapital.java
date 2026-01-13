package DAO;

import DAL.DBContext;
import model.CapitalContribution;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOCapital extends DBContext {

    // 1. Hàm lấy danh sách theo id nông dân và tìm kiếm
    public List<CapitalContribution> getList(int farmerId, String date, String receipt) {
        List<CapitalContribution> list = new ArrayList<>();
        String sql = "SELECT * FROM capital_contributions WHERE member_id = ? "
                   + "AND contribution_date LIKE ? AND receipt_number LIKE ?";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, farmerId);
            pre.setString(2, "%" + (date == null ? "" : date) + "%");
            pre.setString(3, "%" + (receipt == null ? "" : receipt) + "%");
            ResultSet rs = pre.executeQuery();
            while (rs.next()) {
                list.add(new CapitalContribution(
                    rs.getInt(1), rs.getInt(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6)
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. Hàm thêm mới
    public int insert(CapitalContribution c) {
        String sql = "INSERT INTO capital_contributions (member_id, contribution, contribution_date, receipt_number, note) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pre = conn.prepareStatement(sql);
            pre.setInt(1, c.getMemberId());
            pre.setString(2, c.getContribution());
            pre.setString(3, c.getContributionDate());
            pre.setString(4, c.getReceiptNumber());
            pre.setString(5, c.getNote());
            return pre.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}