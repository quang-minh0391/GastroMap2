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
    String f_fund_name = request.getParameter("f_fund_name"); // Thay đổi tên tham số
    String f_member_name = request.getParameter("f_member_name");
    String f_type = request.getParameter("f_type");
    String f_date_from = request.getParameter("f_date_from");
    String f_date_to = request.getParameter("f_date_to");

    // Xử lý ngày tháng
    java.sql.Date dateFrom = (f_date_from != null && !f_date_from.isEmpty()) ? java.sql.Date.valueOf(f_date_from) : null;
    java.sql.Date dateTo = (f_date_to != null && !f_date_to.isEmpty()) ? java.sql.Date.valueOf(f_date_to) : null;

    // c. Gọi DAO tìm kiếm (Truyền chuỗi tên quỹ vào)
    List<FundTransaction> history = dao.searchFundTransactions(f_fund_name, f_member_name, f_type, dateFrom, dateTo, pageIndex, pageSize);
    
    // d. Tính tổng trang
    int totalRecords = dao.countFundTransactions(f_fund_name, f_member_name, f_type, dateFrom, dateTo);
    int totalPage = (totalRecords % pageSize == 0) ? (totalRecords / pageSize) : (totalRecords / pageSize + 1);

    // e. Đẩy dữ liệu ra JSP
    request.setAttribute("history", history);
    request.setAttribute("pageIndex", pageIndex);
    request.setAttribute("totalPage", totalPage);
    
    // f. Giữ lại giá trị lọc
    request.setAttribute("f_fund_name", f_fund_name); // Gửi lại tên quỹ
    request.setAttribute("f_member_name", f_member_name);
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
        
        // 1. Lấy action để biết người dùng đang muốn "Tạo quỹ" hay "Giao dịch"
        String action = request.getParameter("action"); 
        FundDAO dao = new FundDAO();

        try {
            // --- TRƯỜNG HỢP 1: TẠO QUỸ MỚI ---
            // Kiểm tra action TRƯỚC, nếu đúng là tạo quỹ thì xử lý ngay và return
            if ("create_fund".equals(action)) {
                String newName = request.getParameter("new_fund_name");
                String newDesc = request.getParameter("new_fund_desc");
                
                // Mặc định tạo quỹ mới thì tiền = 0
                boolean created = dao.insertFundCategory(newName, newDesc, BigDecimal.ZERO);
                
                if (created) {
                    response.sendRedirect("fund?msg=created");
                } else {
                    response.sendRedirect("fund?error=create_fail");
                }
                return; // QUAN TRỌNG: Dừng lại ở đây, không chạy xuống phần dưới
            }

            // --- TRƯỜNG HỢP 2: GIAO DỊCH (NỘP/RÚT) ---
            // Chỉ khi không phải tạo quỹ thì mới lấy các thông số này
            int fundId = Integer.parseInt(request.getParameter("fund_id"));
            String type = request.getParameter("type");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String note = request.getParameter("note");
            
            // Xử lý ngày tháng
            String dateStr = request.getParameter("transaction_date");
            java.sql.Timestamp transDate;
            
            if (dateStr != null && !dateStr.isEmpty()) {
                String formattedDate = dateStr.replace("T", " ") + ":00"; 
                transDate = java.sql.Timestamp.valueOf(formattedDate);
            } else {
                transDate = new java.sql.Timestamp(System.currentTimeMillis());
            }

            // Xử lý thành viên (có thể null)
            int memberId = 0;
            String memStr = request.getParameter("member_id");
            if(memStr != null && !memStr.isEmpty()) {
                try {
                    memberId = Integer.parseInt(memStr);
                } catch (NumberFormatException e) {
                    memberId = 0; // Nếu lỗi parse thì coi như là 0 (Quỹ chung)
                }
            }
            
            FundTransaction trans = new FundTransaction();
            trans.setFundId(fundId);
            trans.setMemberId(memberId);
            trans.setTransactionType(type);
            trans.setAmount(amount);
            trans.setNote(note);
            trans.setTransactionDate(transDate);
            
            boolean result = dao.insertFundTransaction(trans);
            
            if(result) {
                response.sendRedirect("fund?msg=success");
            } else {
                response.sendRedirect("fund?error=fail");
            }
            
        } catch (Exception e) {
            e.printStackTrace(); // Xem lỗi chi tiết ở cửa sổ Output của Netbean
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
