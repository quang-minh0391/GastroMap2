/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.supply;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import DAO.MaterialReceiptDAO;
import DAL.DBContext;
import DAO.PaymentVoucherDAO;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.PaymentVoucher;

/**
 *
 * @author Admin
 */
public class CreateMaterialReceiptServlet extends HttpServlet {

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
            out.println("<title>Servlet CreateMaterialReceiptServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CreateMaterialReceiptServlet at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
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

        request.setCharacterEncoding("UTF-8"); // Để đọc tiếng Việt
        HttpSession session = request.getSession();

        try {
            // 1. LẤY THÔNG TIN TỪ SESSION (Đã được lưu từ loginController)
            Integer userId = (Integer) session.getAttribute("id");
            Integer memberType = (Integer) session.getAttribute("member_type");
            Integer coopId = (Integer) session.getAttribute("coop_id");

            if (userId == null) {
                response.sendRedirect("login/login.jsp");
                return;
            }

            // 2. LOGIC XÁC ĐỊNH NGƯỜI CHỊU NỢ (DEBTOR)
            int debtorId = 0;
            if (memberType == 2) {
                // Nếu là HTX -> Gán nợ cho chính mình
                debtorId = userId;
            } else if (memberType == 3) {
                // Nếu là Nhân viên -> Gán nợ cho HTX chủ quản (coop_id)
                debtorId = (coopId != null) ? coopId : 0;
            }

            if (debtorId == 0) {
                throw new Exception("Không xác định được chủ thể chịu nợ (HTX).");
            }

            // 3. LẤY DỮ LIỆU TỪ JSP FORM
            int partnerId = Integer.parseInt(request.getParameter("partnerId"));
            int materialId = Integer.parseInt(request.getParameter("materialId"));

            String contractStr = request.getParameter("contractId");
            Integer contractId = (contractStr != null && !contractStr.isEmpty()) ? Integer.parseInt(contractStr) : null;

            double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
            double amountPaid = Double.parseDouble(request.getParameter("amountPaid"));
            String receiptDate = request.getParameter("receiptDate");
            String note = request.getParameter("note");

            // Lấy các mảng chi tiết kho
            String[] warehouseIds = request.getParameterValues("warehouseId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] prices = request.getParameterValues("importPrice[]");

            if (warehouseIds == null) {
                throw new Exception("Vui lòng chọn ít nhất 1 kho.");
            }

            // 4. GỌI DAO ĐỂ THỰC THI TRANSACTION (Gồm: Receipt + Detail + Inventory + Ledger)
            MaterialReceiptDAO dao = new MaterialReceiptDAO();
            boolean isSuccess = dao.saveFullReceipt(
                    materialId, partnerId, contractId, note,
                    totalAmount, amountPaid, receiptDate,
                    warehouseIds, quantities, prices, debtorId
            );

            if (isSuccess) {
                // Thêm context path và thư mục supplyQ vào trước tên file JSP
                // ================== TỰ ĐỘNG TẠO PHIẾU CHI ==================
                // Nếu có trả tiền ngay (amountPaid > 0) -> Ghi sổ
               if (amountPaid > 0) {
                    try {
                        PaymentVoucherDAO voucherDao = new PaymentVoucherDAO();
                        PaymentVoucher v = new PaymentVoucher();
                        
                        // Tạo mã phiếu chi: PC + Thời gian hiện tại
                        long timeCode = System.currentTimeMillis();
                        v.setVoucherCode("PC-MAT-" + timeCode); 
                        
                        v.setVoucherType("PAYMENT"); // Loại: PHIẾU CHI
                        v.setPartnerId(partnerId);   // Chi trả cho Nhà cung cấp
                        v.setMemberId(null);
                        
                        // Chuyển đổi double -> BigDecimal
                        v.setAmount(BigDecimal.valueOf(amountPaid)); 
                        
                        v.setPaymentMethod("Tiền mặt/Chuyển khoản");
                        v.setDescription("Chi tiền nhập vật tư (Ngày nhập: " + receiptDate + ")");
                        v.setCreatedDate(new Timestamp(System.currentTimeMillis()));

                        // Lưu vào DB (Tự động ghi vào financial_ledger)
                        voucherDao.insertVoucher(v);
                        
                    } catch (Exception e) {
                        e.printStackTrace(); // Log lỗi nhưng không chặn luồng chính
                    }
                }
                // ================== [KẾT THÚC] ==================
                response.sendRedirect(request.getContextPath() + "/supplyQ/add_materials.jsp?status=success");
            } else {
                // Thất bại
                response.sendRedirect(request.getContextPath() + "/supplyQ/add_materials.jsp?status=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Thất bại -> Chuyển hướng lại với status=error
                response.sendRedirect(request.getContextPath() + "/supplyQ/add_materials.jsp?status=error");
        }
    }

}
