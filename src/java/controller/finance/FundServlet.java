package controller.finance;

import DAO.FundDAO;
import DAL.DBContext; 
import model.FundCategory;
import model.FundTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.sql.*;
import model.member;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


public class FundServlet extends HttpServlet {

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
            out.println("<title>Servlet FundServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet FundServlet at " + request.getContextPath() + "</h1>");
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
        
        FundDAO dao = new FundDAO();
        
        // 1. Dashboard Cards & Dropdown Data (Giữ nguyên)
        List<FundCategory> funds = dao.getAllFunds();
        List<member> members = dao.getAllMembers();
        request.setAttribute("funds", funds);
        request.setAttribute("members", members);

        // --- 2. XỬ LÝ TÌM KIẾM & PHÂN TRANG (MỚI) ---
        
        // a. Lấy trang hiện tại
        int pageIndex = 1;
        int pageSize = 10; // Số dòng mỗi trang
        try {
            if (request.getParameter("page") != null) {
                pageIndex = Integer.parseInt(request.getParameter("page"));
            }
        } catch (NumberFormatException e) { pageIndex = 1; }

        // b. Lấy tham số lọc
        String f_fund = request.getParameter("f_fund");
        String f_mem = request.getParameter("f_mem");
        String f_type = request.getParameter("f_type");
        String f_date_from = request.getParameter("f_date_from");
        String f_date_to = request.getParameter("f_date_to");

        Integer searchFundId = (f_fund != null && !f_fund.isEmpty()) ? Integer.parseInt(f_fund) : null;
        Integer searchMemId = (f_mem != null && !f_mem.isEmpty()) ? Integer.parseInt(f_mem) : null;
        java.sql.Date dateFrom = (f_date_from != null && !f_date_from.isEmpty()) ? java.sql.Date.valueOf(f_date_from) : null;
        java.sql.Date dateTo = (f_date_to != null && !f_date_to.isEmpty()) ? java.sql.Date.valueOf(f_date_to) : null;

        // c. Gọi DAO tìm kiếm
        List<FundTransaction> history = dao.searchFundTransactions(searchFundId, searchMemId, f_type, dateFrom, dateTo, pageIndex, pageSize);
        
        // d. Tính tổng trang
        int totalRecords = dao.countFundTransactions(searchFundId, searchMemId, f_type, dateFrom, dateTo);
        int totalPage = (totalRecords % pageSize == 0) ? (totalRecords / pageSize) : (totalRecords / pageSize + 1);

        // e. Đẩy dữ liệu ra JSP
        request.setAttribute("history", history);
        request.setAttribute("pageIndex", pageIndex);
        request.setAttribute("totalPage", totalPage);
        
        // f. Giữ lại giá trị lọc để hiện lại trên form
        request.setAttribute("f_fund", f_fund);
        request.setAttribute("f_mem", f_mem);
        request.setAttribute("f_type", f_type);
        request.setAttribute("f_date_from", f_date_from);
        request.setAttribute("f_date_to", f_date_to);
        
        request.getRequestDispatcher("/finance/fund_management.jsp").forward(request, response);
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
            int fundId = Integer.parseInt(request.getParameter("fund_id"));
            String type = request.getParameter("type");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String note = request.getParameter("note");
            
            // --- XỬ LÝ NGÀY GIỜ TỪ FORM ---
            String dateStr = request.getParameter("transaction_date"); // Chuỗi dạng: "2026-01-24T21:30"
            java.sql.Timestamp transDate;
            
            if (dateStr != null && !dateStr.isEmpty()) {
                // HTML5 datetime-local có chữ 'T' ở giữa, SQL cần khoảng trắng
                String formattedDate = dateStr.replace("T", " ") + ":00"; 
                transDate = java.sql.Timestamp.valueOf(formattedDate);
            } else {
                transDate = new java.sql.Timestamp(System.currentTimeMillis());
            }
            // ------------------------------
            
            int memberId = 0;
            String memStr = request.getParameter("member_id");
            if(memStr != null && !memStr.isEmpty()) {
                memberId = Integer.parseInt(memStr);
            }
            
            FundTransaction trans = new FundTransaction();
            trans.setFundId(fundId);
            trans.setMemberId(memberId);
            trans.setTransactionType(type);
            trans.setAmount(amount);
            trans.setNote(note);
            trans.setTransactionDate(transDate); // Set ngày đã chọn
            
            FundDAO dao = new FundDAO();
            boolean result = dao.insertFundTransaction(trans);
            
            if(result) {
                response.sendRedirect("fund?msg=success");
            } else {
                response.sendRedirect("fund?error=fail");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("fund?error=exception");
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
    }// </editor-fold>

}
