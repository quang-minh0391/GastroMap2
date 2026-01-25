/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.finance;

import DAO.FinanceDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.FinancialTransaction;
import model.TransactionCategory;

/**
 *
 * @author Viet Duc
 */
public class FinanceServlet extends HttpServlet {

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
            out.println("<title>Servlet FinanceServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FinanceServlet at " + request.getContextPath() + "</h1>");
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FinanceDAO dao = new FinanceDAO();

        // 1. Phân trang
        int pageIndex = 1;
        int pageSize = 10;
        try {
            if (request.getParameter("page") != null) {
                pageIndex = Integer.parseInt(request.getParameter("page"));
            }
        } catch (Exception e) {
        }

        // 2. Lấy tham số Lọc & Sắp xếp
        String fType = request.getParameter("f_type");
        String fCat = request.getParameter("f_cat"); // Category ID
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        Integer catId = null;
        try {
            if (fCat != null && !fCat.isEmpty()) {
                catId = Integer.parseInt(fCat);
            }
        } catch (Exception e) {
        }

        java.sql.Date fFrom = null;
        java.sql.Date fTo = null;
        try {
            if (request.getParameter("f_date_from") != null && !request.getParameter("f_date_from").isEmpty()) {
                fFrom = java.sql.Date.valueOf(request.getParameter("f_date_from"));
            }
            if (request.getParameter("f_date_to") != null && !request.getParameter("f_date_to").isEmpty()) {
                fTo = java.sql.Date.valueOf(request.getParameter("f_date_to"));
            }
        } catch (Exception e) {
        }
        
        
        // 3. Lấy dữ liệu hiển thị (QUAN TRỌNG: Phải lấy danh mục để hiện Dropdown)
        List<FinancialTransaction> listTrans = dao.searchTransactions(fFrom, fTo, fType, catId, sortBy, sortOrder, pageIndex, pageSize);
        List<TransactionCategory> listCats = dao.getAllCategories(); // <--- ĐÃ THÊM DÒNG NÀY (Trước đây bị thiếu)
        
        int totalRecords = dao.countTransactions(fFrom, fTo, fType, catId);
        int totalPage = (totalRecords % pageSize == 0) ? (totalRecords / pageSize) : (totalRecords / pageSize + 1);

        // 4. CHUẨN BỊ DỮ LIỆU BIỂU ĐỒ (CẢ NĂM VÀ THÁNG)
        
        // A. Dữ liệu Theo Năm (5 Năm gần nhất)
        List<double[]> yearlyStats = dao.getYearlyStatistics();
        StringBuilder yLabels = new StringBuilder("[");
        StringBuilder yRev = new StringBuilder("[");
        StringBuilder yExp = new StringBuilder("[");
        StringBuilder yBal = new StringBuilder("[");

        for(double[] s : yearlyStats) {
            yLabels.append("'Năm ").append((int)s[0]).append("',");
            yRev.append(s[1]).append(",");
            yExp.append(s[2]).append(",");
            yBal.append(s[1] - s[2]).append(",");
        }
        fixJson(yLabels); fixJson(yRev); fixJson(yExp); fixJson(yBal);

        // B. Dữ liệu Theo Tháng (Của năm hiện tại)
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        List<double[]> monthlyStats = dao.getMonthlyStatistics(currentYear);
        StringBuilder mLabels = new StringBuilder("[");
        StringBuilder mRev = new StringBuilder("[");
        StringBuilder mExp = new StringBuilder("[");
        StringBuilder mBal = new StringBuilder("[");
        
        for(double[] s : monthlyStats) {
            mLabels.append("'Tháng ").append((int)s[0]).append("',");
            mRev.append(s[1]).append(",");
            mExp.append(s[2]).append(",");
            mBal.append(s[1] - s[2]).append(",");
        }
        fixJson(mLabels); fixJson(mRev); fixJson(mExp); fixJson(mBal);

        // 5. Gửi dữ liệu sang JSP
        request.setAttribute("transList", listTrans);
        request.setAttribute("catList", listCats); // Biến này giờ đã có dữ liệu
        
        // Dashboard Stats
        request.setAttribute("totalRevenue", dao.getTotalAmount("IN"));
        request.setAttribute("totalExpense", dao.getTotalAmount("OUT"));
        request.setAttribute("balance", dao.getTotalAmount("IN") - dao.getTotalAmount("OUT"));
        
        // Chart Data (Year)
        request.setAttribute("yLabels", yLabels.toString());
        request.setAttribute("yRev", yRev.toString());
        request.setAttribute("yExp", yExp.toString());
        request.setAttribute("yBal", yBal.toString());
        
        // Chart Data (Month)
        request.setAttribute("mLabels", mLabels.toString());
        request.setAttribute("mRev", mRev.toString());
        request.setAttribute("mExp", mExp.toString());
        request.setAttribute("mBal", mBal.toString());
        request.setAttribute("currentYear", currentYear);

        // Pagination & Filter State
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("f_type", fType);
        request.setAttribute("f_cat", fCat);
        request.setAttribute("sortBy", sortBy);
        request.setAttribute("sortOrder", sortOrder);

        request.getRequestDispatcher("/finance/finance_ledger.jsp").forward(request, response);
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

        try {
            // 1. Lấy dữ liệu cơ bản
            int categoryId = Integer.parseInt(request.getParameter("category_id"));
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String description = request.getParameter("description");

            // 2. XỬ LÝ NGÀY GIỜ (Logic mới thêm)
            String dateStr = request.getParameter("transaction_date"); // Chuỗi dạng: "2026-01-24T21:30"
            java.sql.Timestamp transDate;
            
            try {
                if (dateStr != null && !dateStr.isEmpty()) {
                    // HTML5 datetime-local trả về 'T' ở giữa, SQL cần khoảng trắng
                    String formattedDate = dateStr.replace("T", " ") + ":00";
                    transDate = java.sql.Timestamp.valueOf(formattedDate);
                } else {
                    transDate = new java.sql.Timestamp(System.currentTimeMillis());
                }
            } catch (Exception e) {
                // Phòng trường hợp lỗi format, lấy giờ hiện tại
                transDate = new java.sql.Timestamp(System.currentTimeMillis());
            }

            // 3. Gọi DAO
            FinanceDAO dao = new FinanceDAO();
            
            // Tự động xác định IN/OUT dựa vào loại danh mục (Logic cũ giữ nguyên)
            String catType = dao.getCategoryType(categoryId); 
            String transType = "REVENUE".equals(catType) ? "IN" : "OUT";

            // 4. Tạo đối tượng và set đủ thông tin
            FinancialTransaction trans = new FinancialTransaction();
            trans.setTransactionDate(transDate); // <--- QUAN TRỌNG: Set ngày tự chọn
            trans.setCategoryId(categoryId);
            trans.setAmount(amount);
            trans.setTransactionType(transType);
            trans.setDescription(description);

            dao.insertTransaction(trans);

            // 5. Load lại trang
            response.sendRedirect("finance");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("finance?error=1");
        }
    }

    private void fixJson(StringBuilder sb) {
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1); // Xóa dấu phẩy cuối
        }
        sb.append("]");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
