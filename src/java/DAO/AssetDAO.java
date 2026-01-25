package DAO;

import DAL.DBContext;
import java.math.BigDecimal;
import model.AssetCategory;
import model.FixedAsset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import model.AssetMaintenance;

public class AssetDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. Lấy tất cả danh mục tài sản (Để hiển thị vào Dropdown Box khi thêm mới)
    public List<AssetCategory> getAllCategories() {
        List<AssetCategory> list = new ArrayList<>();
        String query = "SELECT * FROM asset_categories";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new AssetCategory(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBigDecimal("depreciation_rate"),
                        rs.getString("description")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // Hàm lọc đa năng: Mã, Tên, Trạng thái, Vị trí, Khoảng giá, Khoảng ngày
    public List<FixedAsset> filterAssets(String code, String name, String status, String location,
            Date dateFrom, Date dateTo,
            BigDecimal priceFrom, BigDecimal priceTo,
            String sortBy, String sortOrder,
            int pageIndex, int pageSize) {

        List<FixedAsset> list = new ArrayList<>();
        // Kỹ thuật WHERE 1=1 để dễ dàng cộng chuỗi AND phía sau
        StringBuilder sql = new StringBuilder("SELECT * FROM fixed_assets WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // 1. Lọc theo Mã (Gần đúng)
        if (code != null && !code.trim().isEmpty()) {
            sql.append(" AND code LIKE ?");
            params.add("%" + code.trim() + "%");
        }
        // 2. Lọc theo Tên (Gần đúng)
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name.trim() + "%");
        }
        // 3. Lọc theo Trạng thái (Chính xác)
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        // 4. Lọc theo Vị trí (Gần đúng)
        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        // 5. Lọc Khoảng Giá
        if (priceFrom != null) {
            sql.append(" AND current_value >= ?");
            params.add(priceFrom);
        }
        if (priceTo != null) {
            sql.append(" AND current_value <= ?");
            params.add(priceTo);
        }
        // 6. Lọc Khoảng Ngày
        if (dateFrom != null) {
            sql.append(" AND purchase_date >= ?");
            params.add(dateFrom);
        }
        if (dateTo != null) {
            sql.append(" AND purchase_date <= ?");
            params.add(dateTo);
        }

        String orderCol = "code"; // Mặc định
        if ("name".equals(sortBy)) {
            orderCol = "name";
        } else if ("purchase_date".equals(sortBy)) {
            orderCol = "purchase_date";
        } else if ("current_value".equals(sortBy)) {
            orderCol = "current_value";
        } else if ("status".equals(sortBy)) {
            orderCol = "status";
        }

        String direction = "ASC"; // Mặc định
        if ("DESC".equalsIgnoreCase(sortOrder)) {
            direction = "DESC";
        }

        // Sắp xếp: Hàng thanh lý xuống cuối, mới nhất lên đầu
//        sql.append(" ORDER BY CASE WHEN status = 'LIQUIDATED' THEN 1 ELSE 0 END, created_at DESC");
//        sql.append(" ORDER BY CASE WHEN status = 'LIQUIDATED' THEN 1 ELSE 0 END, code ASC");
        sql.append(" ORDER BY CASE WHEN status = 'LIQUIDATED' THEN 1 ELSE 0 END, ");
        sql.append(orderCol).append(" ").append(direction);
        sql.append(", id DESC");

        // --- THÊM PHÂN TRANG (LIMIT OFFSET) ---
        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add((pageIndex - 1) * pageSize);

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new FixedAsset(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        rs.getInt("purchase_receipt_id"),
                        rs.getDate("purchase_date"),
                        rs.getBigDecimal("initial_value"),
                        rs.getBigDecimal("current_value"),
                        rs.getString("status"),
                        rs.getString("location"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    // 3. Thêm mới tài sản
    public void insertAsset(FixedAsset asset) {
        String query = "INSERT INTO fixed_assets (code, name, category_id, purchase_date, initial_value, current_value, status, location) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, asset.getCode());
            ps.setString(2, asset.getName());
            ps.setInt(3, asset.getCategoryId());
            ps.setDate(4, asset.getPurchaseDate());
            ps.setBigDecimal(5, asset.getInitialValue());
            ps.setBigDecimal(6, asset.getCurrentValue());
            ps.setString(7, asset.getStatus());
            ps.setString(8, asset.getLocation());

            System.out.println("Dang them tai san: " + asset.getCode() + " - Trang thai: " + asset.getStatus());

            ps.executeUpdate();
            System.out.println("Them thanh cong!");

        } catch (Exception e) {
            System.out.println("LOI KHI THEM TAI SAN:");
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 4. Lấy lịch sử bảo trì của một tài sản cụ thể
    public List<AssetMaintenance> getMaintenanceHistory(int assetId) {
        List<AssetMaintenance> list = new ArrayList<>();
        String query = "SELECT * FROM asset_maintenance_log WHERE asset_id = ? ORDER BY maintenance_date DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, assetId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new AssetMaintenance(
                        rs.getInt("id"),
                        rs.getInt("asset_id"),
                        rs.getDate("maintenance_date"),
                        rs.getBigDecimal("cost"),
                        rs.getString("performer"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // 5. Thêm mới phiếu bảo trì
    public void insertMaintenance(AssetMaintenance log) {
        String query = "INSERT INTO asset_maintenance_log (asset_id, maintenance_date, cost, performer, description) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, log.getAssetId());
            ps.setDate(2, log.getMaintenanceDate());
            ps.setBigDecimal(3, log.getCost());
            ps.setString(4, log.getPerformer());
            ps.setString(5, log.getDescription());
            ps.executeUpdate();

            // (Tùy chọn) Cập nhật trạng thái tài sản thành "MAINTENANCE" hoặc "ACTIVE" nếu muốn
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 6. Lấy thông tin 1 tài sản theo ID (để hiển thị tên trên trang bảo trì)
    public FixedAsset getAssetById(int id) {
        String query = "SELECT * FROM fixed_assets WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new FixedAsset(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getInt("category_id"),
                        rs.getInt("purchase_receipt_id"),
                        rs.getDate("purchase_date"),
                        rs.getBigDecimal("initial_value"),
                        rs.getBigDecimal("current_value"),
                        rs.getString("status"),
                        rs.getString("location"),
                        rs.getTimestamp("created_at")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void updateAssetStatus(int assetId, String newStatus) {
        String sql = "UPDATE fixed_assets SET status = ? WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, newStatus);
            ps.setInt(2, assetId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int countAssets(String code, String name, String status, String location,
            Date dateFrom, Date dateTo, BigDecimal priceFrom, BigDecimal priceTo) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM fixed_assets WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // [COPY LẠI Y HỆT CÁC IF LỌC TỪ HÀM TRÊN VÀO ĐÂY]
        if (code != null && !code.trim().isEmpty()) {
            sql.append(" AND code LIKE ?");
            params.add("%" + code.trim() + "%");
        }
        if (name != null && !name.trim().isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name.trim() + "%");
        }
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND status = ?");
            params.add(status);
        }
        if (location != null && !location.trim().isEmpty()) {
            sql.append(" AND location LIKE ?");
            params.add("%" + location.trim() + "%");
        }
        if (priceFrom != null) {
            sql.append(" AND current_value >= ?");
            params.add(priceFrom);
        }
        if (priceTo != null) {
            sql.append(" AND current_value <= ?");
            params.add(priceTo);
        }
        if (dateFrom != null) {
            sql.append(" AND purchase_date >= ?");
            params.add(dateFrom);
        }
        if (dateTo != null) {
            sql.append(" AND purchase_date <= ?");
            params.add(dateTo);
        }

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }
    // [MỚI] Đếm tổng tất cả tài sản trong DB (Không quan tâm bộ lọc)
    public int countAllAssets() {
        String sql = "SELECT COUNT(*) FROM fixed_assets";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); if (ps != null) ps.close(); if (rs != null) rs.close(); } catch (Exception e) {}
        }
        return 0;
    }

    // [MỚI] Lấy thống kê trạng thái (Để hiện lên Dashboard cho đẹp giống Finance)
    // Trả về Map: Key là Status, Value là Số lượng
    public java.util.Map<String, Integer> getAssetStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        stats.put("ACTIVE", 0);
        stats.put("MAINTENANCE", 0);
        stats.put("BROKEN", 0);
        stats.put("LIQUIDATED", 0);
        
        String sql = "SELECT status, COUNT(*) FROM fixed_assets GROUP BY status";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                stats.put(rs.getString(1), rs.getInt(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); if (ps != null) ps.close(); if (rs != null) rs.close(); } catch (Exception e) {}
        }
        return stats;
    }
}
