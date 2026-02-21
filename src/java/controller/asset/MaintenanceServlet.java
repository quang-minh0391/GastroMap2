package controller.asset;

import DAO.AssetDAO;
import DAO.FinanceDAO;
import model.AssetMaintenance;
import model.FixedAsset;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FinancialTransaction;

@WebServlet(name = "MaintenanceServlet", urlPatterns = {"/maintenance"})
public class MaintenanceServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Servlet MaintenanceServlet</title></head><body>");
            out.println("<h1>Servlet MaintenanceServlet at " + request.getContextPath() + "</h1>");
            out.println("</body></html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("asset_id");
        
        if (idRaw != null) {
            try {
                int assetId = Integer.parseInt(idRaw);
                AssetDAO dao = new AssetDAO();
                
                // 1. Lấy thông tin tài sản
                FixedAsset asset = dao.getAssetById(assetId);
                
                // 2. Lấy danh sách lịch sử
                List<AssetMaintenance> history = dao.getMaintenanceHistory(assetId);
                
                request.setAttribute("asset", asset);
                request.setAttribute("history", history);
                
                request.getRequestDispatcher("/asset/maintenance.jsp").forward(request, response);
            } catch (Exception e) {
                response.sendRedirect("AssetServlet");
            }
        } else {
            response.sendRedirect("AssetServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        // --- LOGIC 1: CẬP NHẬT TRẠNG THÁI THỦ CÔNG ---
        if ("update_status".equals(action)) {
            try {
                int assetId = Integer.parseInt(request.getParameter("asset_id"));
                String newStatus = request.getParameter("new_status");
                
                AssetDAO dao = new AssetDAO();
                dao.updateAssetStatus(assetId, newStatus);
                
                response.sendRedirect("maintenance?asset_id=" + assetId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return; 
        }

        // --- LOGIC 2: THÊM PHIẾU BẢO TRÌ & GHI SỔ TÀI CHÍNH ---
        try {
            int assetId = Integer.parseInt(request.getParameter("asset_id"));
            // Lấy ngày từ form (String -> Date)
            String dateStr = request.getParameter("maintenance_date");
            java.sql.Date mDate = java.sql.Date.valueOf(dateStr);
            
            BigDecimal cost = new BigDecimal(request.getParameter("cost"));
            String performer = request.getParameter("performer");
            String description = request.getParameter("description");
            
            // 1. Lưu vào bảng Bảo trì (Asset Maintenance)
            AssetMaintenance log = new AssetMaintenance();
            log.setAssetId(assetId);
            log.setMaintenanceDate(mDate);
            log.setCost(cost);
            log.setPerformer(performer);
            log.setDescription(description);
            
            AssetDAO dao = new AssetDAO();
            dao.insertMaintenance(log);
            
            // 2. Cập nhật trạng thái tài sản thành MAINTENANCE
            dao.updateAssetStatus(assetId, "MAINTENANCE");
            
            // 3. [MỚI] TỰ ĐỘNG GHI SỔ CÁI TÀI CHÍNH
        if (cost.compareTo(BigDecimal.ZERO) > 0) {
            FinanceDAO financeDao = new FinanceDAO();
            FinancialTransaction trans = new FinancialTransaction();
            
            // Lấy thời gian hiện tại cho giao dịch tài chính
            trans.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
            
            // ID = 4 (Giả sử là 'Chi bảo trì tài sản' trong bảng transaction_categories)
            // Bạn cần chắc chắn ID này tồn tại trong DB, nếu không hãy dùng ID 3 (Chi khác)
            trans.setCategoryId(4); 
            
            trans.setAmount(cost);
            trans.setTransactionType("OUT"); // Chi tiền
            trans.setDescription("Phí bảo trì tài sản ID " + assetId + " (" + description + ")");
            trans.setSourceTable("asset_maintenance_log"); // (Tùy chọn) Để biết nguồn gốc
            // trans.setSourceId(...); // Nếu muốn lưu ID log
            
            financeDao.insertTransaction(trans);
        }
            
            response.sendRedirect("maintenance?asset_id=" + assetId);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AssetServlet?error=maintenance");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}