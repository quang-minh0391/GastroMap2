package controller.asset;

import DAO.AssetDAO;
import DAO.FinanceDAO;
import model.AssetCategory;
import model.FinancialTransaction;
import model.FixedAsset;
import java.util.*;
import java.sql.Date;
import java.math.BigDecimal;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FinancialTransaction;

public class AssetServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AssetServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AssetServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        AssetDAO dao = new AssetDAO();
//
//        // 1. Lấy thông tin phân trang
//        int pageIndex = 1;
//        int pageSize = 10; // Giới hạn 10 dòng/trang
//        try {
//            if (request.getParameter("page") != null) {
//                pageIndex = Integer.parseInt(request.getParameter("page"));
//            }
//        } catch (Exception e) {
//        }
//
//        // Lấy các tham số lọc từ JSP gửi lên
//        String fCode = request.getParameter("f_code");
//        String fName = request.getParameter("f_name");
//        String fStatus = request.getParameter("f_status");
//        String fLocation = request.getParameter("f_location");
//
//        // 1. Lấy tham số sắp xếp
//        String sortBy = request.getParameter("sortBy");
//        String sortOrder = request.getParameter("sortOrder");
//
//        // Mặc định nếu chưa có
//        if (sortBy == null) {
//            sortBy = "code";
//        }
//        if (sortOrder == null) {
//            sortOrder = "ASC";
//        }
//
//        // Xử lý ngày tháng (Từ ngày - Đến ngày)
//        java.sql.Date fDateFrom = null;
//        java.sql.Date fDateTo = null;
//        try {
//            String d1 = request.getParameter("f_date_from");
//            String d2 = request.getParameter("f_date_to");
//            if (d1 != null && !d1.isEmpty()) {
//                fDateFrom = java.sql.Date.valueOf(d1);
//            }
//            if (d2 != null && !d2.isEmpty()) {
//                fDateTo = java.sql.Date.valueOf(d2);
//            }
//        } catch (Exception e) {
//        } // Lờ đi lỗi parse date
//
//        // Xử lý giá tiền (Từ giá - Đến giá)
//        BigDecimal fPriceFrom = null;
//        BigDecimal fPriceTo = null;
//        try {
//            String p1 = request.getParameter("f_price_from");
//            String p2 = request.getParameter("f_price_to");
//            if (p1 != null && !p1.isEmpty()) {
//                fPriceFrom = new BigDecimal(p1);
//            }
//            if (p2 != null && !p2.isEmpty()) {
//                fPriceTo = new BigDecimal(p2);
//            }
//        } catch (Exception e) {
//        }
//
//        // 2. Gọi hàm lọc thông minh
//        List<FixedAsset> listAssets = dao.filterAssets(fCode, fName, fStatus, fLocation,
//                fDateFrom, fDateTo, fPriceFrom, fPriceTo,
//                sortBy, sortOrder, pageIndex, pageSize);
//        List<AssetCategory> listCategories = dao.getAllCategories();
//// 3. Tính tổng trang
//        int totalRecords = dao.countAssets(fCode, fName, fStatus, fLocation, fDateFrom, fDateTo, fPriceFrom, fPriceTo);
//        int totalPage = (totalRecords % pageSize == 0) ? (totalRecords / pageSize) : (totalRecords / pageSize + 1);
//        //  Gửi dữ liệu về JSP
//        request.setAttribute("sortBy", sortBy);
//        request.setAttribute("sortOrder", sortOrder);
//
//        request.setAttribute("assets", listAssets);
//        request.setAttribute("categories", listCategories);
//        request.setAttribute("pageIndex", pageIndex);
//        request.setAttribute("totalPage", totalPage);
//
//        // 4. Lưu lại các giá trị đã nhập để hiển thị lại trên ô input (Giữ trạng thái form)
//        request.setAttribute("f_code", fCode);
//        request.setAttribute("f_name", fName);
//        request.setAttribute("f_status", fStatus);
//        request.setAttribute("f_location", fLocation);
//        request.setAttribute("f_date_from", request.getParameter("f_date_from"));
//        request.setAttribute("f_date_to", request.getParameter("f_date_to"));
//        request.setAttribute("f_price_from", request.getParameter("f_price_from"));
//        request.setAttribute("f_price_to", request.getParameter("f_price_to"));
//
//        request.getRequestDispatcher("/asset/asset_management.jsp").forward(request, response);
//    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AssetDAO dao = new AssetDAO();

        // 1. Logic Phân trang & Lọc (Giữ nguyên logic cũ của bạn)
        int pageIndex = 1;
        int pageSize = 10;
        try {
            if (request.getParameter("page") != null) pageIndex = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {}

        String fCode = request.getParameter("f_code");
        String fName = request.getParameter("f_name");
        String fStatus = request.getParameter("f_status");
        String fLocation = request.getParameter("f_location");
        // --- CẤU HÌNH SẮP XẾP MẶC ĐỊNH (SỬA Ở ĐÂY) ---
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        // Nếu người dùng chưa chọn sắp xếp (mới vào trang), ta set mặc định:
        if (sortBy == null) {
            sortBy = "purchase_date"; // <--- Mặc định theo NGÀY MUA
        }
        if (sortOrder == null) {
            sortOrder = "DESC";        // <--- Mặc định giảm dần
        }
        
        java.sql.Date fDateFrom = null;
        java.sql.Date fDateTo = null;
        try {
             if (request.getParameter("f_date_from") != null && !request.getParameter("f_date_from").isEmpty())
                fDateFrom = java.sql.Date.valueOf(request.getParameter("f_date_from"));
             if (request.getParameter("f_date_to") != null && !request.getParameter("f_date_to").isEmpty())
                fDateTo = java.sql.Date.valueOf(request.getParameter("f_date_to"));
        } catch (Exception e) {}

        BigDecimal fPriceFrom = null; 
        BigDecimal fPriceTo = null;
        try {
             if (request.getParameter("f_price_from") != null && !request.getParameter("f_price_from").isEmpty())
                fPriceFrom = new BigDecimal(request.getParameter("f_price_from"));
             if (request.getParameter("f_price_to") != null && !request.getParameter("f_price_to").isEmpty())
                fPriceTo = new BigDecimal(request.getParameter("f_price_to"));
        } catch (Exception e) {}

        // 2. Lấy danh sách tài sản theo bộ lọc
        List<FixedAsset> listAssets = dao.filterAssets(fCode, fName, fStatus, fLocation, fDateFrom, fDateTo, fPriceFrom, fPriceTo, sortBy, sortOrder, pageIndex, pageSize);
        List<AssetCategory> listCategories = dao.getAllCategories();
        
        // Tính tổng trang cho bộ lọc
        int totalRecordsFiltered = dao.countAssets(fCode, fName, fStatus, fLocation, fDateFrom, fDateTo, fPriceFrom, fPriceTo);
        int totalPage = (totalRecordsFiltered % pageSize == 0) ? (totalRecordsFiltered / pageSize) : (totalRecordsFiltered / pageSize + 1);

        // --- [MỚI] 3. LẤY SỐ LIỆU TỔNG QUÁT (Không phụ thuộc bộ lọc) ---
        // Yêu cầu: "khi mới mở hiện ra tổng số máy móc thiết bị (tất cả trạng thái kể cả đã thanh lí)"
        int grandTotalAssets = dao.countAllAssets();
        java.util.Map<String, Integer> stats = dao.getAssetStatistics();

        // 4. Gửi dữ liệu sang JSP
        request.setAttribute("grandTotalAssets", grandTotalAssets); // Số tổng toàn bộ
        request.setAttribute("stats", stats); // Thống kê từng loại
        
        request.setAttribute("assets", listAssets);
        request.setAttribute("categories", listCategories);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("totalPage", totalPage);
        
        // Lưu lại giá trị bộ lọc
        request.setAttribute("f_code", fCode);
        request.setAttribute("f_name", fName);
        request.setAttribute("f_status", fStatus);
        request.setAttribute("f_location", fLocation);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);
        
        // --- THÊM 2 DÒNG NÀY ---
        request.setAttribute("f_date_from", request.getParameter("f_date_from"));
        request.setAttribute("f_date_to", request.getParameter("f_date_to"));
        // --- THÊM 2 DÒNG NÀY CHO GIÁ ---
        request.setAttribute("f_price_from", request.getParameter("f_price_from"));
        request.setAttribute("f_price_to", request.getParameter("f_price_to"));

        request.getRequestDispatcher("/asset/asset_management.jsp").forward(request, response);
    }
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            // --- LOGIC 1: BÁN THANH LÝ (THU TIỀN) ---
            if ("liquidate".equals(action)) {
                int assetId = Integer.parseInt(request.getParameter("asset_id"));
                String assetName = request.getParameter("asset_name");
                BigDecimal price = new BigDecimal(request.getParameter("price"));

                AssetDAO assetDao = new AssetDAO();
                FinanceDAO financeDao = new FinanceDAO();

                // a. Đổi trạng thái sang LIQUIDATED
                assetDao.updateAssetStatus(assetId, "LIQUIDATED");

                // b. Ghi sổ cái: THU TIỀN (IN)
                FinancialTransaction trans = new FinancialTransaction();
                trans.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis())); // Lấy giờ hiện tại
                trans.setCategoryId(1); // Giả sử ID=1 là Doanh thu khác (REVENUE) - Check DB của bạn!
                trans.setAmount(price);
                trans.setTransactionType("IN");
                trans.setDescription("Thu từ thanh lý tài sản: " + assetName);
                financeDao.insertTransaction(trans);

                response.sendRedirect("AssetServlet");
                return; // Kết thúc để không chạy xuống phần Insert
            }

            // --- LOGIC 2: MUA LẠI TÀI SẢN THANH LÝ (CHI TIỀN) ---
            if ("repurchase".equals(action)) {
                int assetId = Integer.parseInt(request.getParameter("asset_id"));
                String assetName = request.getParameter("asset_name");
                String targetStatus = request.getParameter("target_status");
                BigDecimal price = new BigDecimal(request.getParameter("price"));

                AssetDAO assetDao = new AssetDAO();
                FinanceDAO financeDao = new FinanceDAO();

                // a. Khôi phục trạng thái cũ
                assetDao.updateAssetStatus(assetId, targetStatus);

                // b. Ghi sổ cái: CHI TIỀN (OUT)
                FinancialTransaction trans = new FinancialTransaction();
                trans.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
                trans.setCategoryId(3); // Giả sử ID=3 là Chi mua vật tư (EXPENSE) - Check DB của bạn!
                trans.setAmount(price);
                trans.setTransactionType("OUT");
                trans.setDescription("Mua lại tài sản thanh lý: " + assetName);
                financeDao.insertTransaction(trans);

                response.sendRedirect("AssetServlet");
                return;
            }

            // Chỉ chạy khi không có action đặc biệt
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            // --- SỬA ĐOẠN NÀY: Thay vì lấy ID, ta lấy Tên và tự xử lý ---
            String categoryName = request.getParameter("category_name"); 
            AssetDAO dao = new AssetDAO();
            int categoryId = dao.getOrCreateCategoryId(categoryName);
            String dateStr = request.getParameter("purchase_date");
            Date purchaseDate = Date.valueOf(dateStr);
            BigDecimal price = new BigDecimal(request.getParameter("price"));
            String status = request.getParameter("status");
            String location = request.getParameter("location");

            FixedAsset newAsset = new FixedAsset();
            newAsset.setCode(code);
            newAsset.setName(name);
            newAsset.setCategoryId(categoryId);
            newAsset.setPurchaseDate(purchaseDate);
            newAsset.setInitialValue(price);
            newAsset.setCurrentValue(price);
            newAsset.setStatus(status);
            newAsset.setLocation(location);

            dao.insertAsset(newAsset);

            // --- GHI SỔ CÁI KHI MUA MỚI (TÙY CHỌN) ---
            FinanceDAO fDao = new FinanceDAO();
            FinancialTransaction fTrans = new FinancialTransaction();
            fTrans.setTransactionDate(new java.sql.Timestamp(System.currentTimeMillis()));
            fTrans.setCategoryId(3); // Chi mua sắm
            fTrans.setAmount(price);
            fTrans.setTransactionType("OUT");
            fTrans.setDescription("Mua mới tài sản: " + name);
            fDao.insertTransaction(fTrans);
            // ------------------------------------------

            response.sendRedirect("AssetServlet");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AssetServlet?error=1");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
