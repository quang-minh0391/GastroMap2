package controller.debt;

import DAO.DebtDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import DAO.FinanceDAO;
import model.FinancialTransaction;
import java.math.BigDecimal;

@WebServlet(name = "DebtManagementServlet", urlPatterns = {"/DebtManagementServlet"})

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10)
public class DebtManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer coopId = (Integer) session.getAttribute("id");
        if (coopId == null) {             response.sendRedirect("login/login.jsp");
 return; }

        DebtDAO dao = new DebtDAO();
        request.setAttribute("memberDebts", dao.getMemberDebtList(coopId));
        request.setAttribute("partnerDebts", dao.getPartnerDebtList(coopId));
        request.getRequestDispatcher("/debt/debt_management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DebtDAO dao = new DebtDAO();
        
        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            String pIdStr = request.getParameter("partnerId");
            // Xác định đây là Nhà cung cấp hay Nông dân dựa vào PartnerId
            Integer partnerId = (pIdStr == null || pIdStr.isEmpty()) ? null : Integer.parseInt(pIdStr);
            
            double amount = Double.parseDouble(request.getParameter("amount"));
            String method = request.getParameter("paymentMethod");
            String note = request.getParameter("note");

            // Lấy loại phiếu (RECEIPT = Thu, PAYMENT = Chi)
            String vType = request.getParameter("voucherType");
            String entryType = request.getParameter("entryType");
            
            // Xử lý upload ảnh minh chứng (giữ nguyên logic cũ)
            String imagePath = "";
            Part part = request.getPart("image");
            if (part != null && part.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("/") + "uploads";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) uploadDir.mkdir();
                String fileName = "bill_" + System.currentTimeMillis() + ".jpg";
                part.write(uploadPath + File.separator + fileName);
                imagePath = "uploads/" + fileName;
            }

            boolean success;
            if (partnerId != null) {
                // TÁCH RIÊNG NHÀ CUNG CẤP: Gọi hàm chuyên biệt để LUÔN TRỪ NỢ
                success = dao.saveSupplierVoucher(memberId, partnerId, amount, method, note, imagePath);
            } else {
                // ĐỐI VỚI NÔNG DÂN: Giữ nguyên logic cũ (Thu nợ/Chi tiền)
//                String vType = request.getParameter("voucherType");
//                String entryType = request.getParameter("entryType");
                success = dao.saveVoucher(memberId, null, vType, amount, method, note, imagePath, entryType);
            }

            if (success) {
                // =========================================================
                try {
                    FinanceDAO fDao = new FinanceDAO();
                    String transType = "";
                    String catName = "";
                    String desc = "";

                    // Phân loại luồng tiền
                    if (partnerId != null) {
                        // 1. HTX Trả nợ Nhà cung cấp
                        transType = "OUT";
                        catName = "Trả nợ nhà cung cấp";
                        desc = "Thanh toán nợ cho NCC (ID: " + partnerId + ") - Bằng: " + method;
                    } else {
                        if ("RECEIPT".equals(vType)) {
                            // 2. HTX Thu nợ Nông dân
                            transType = "IN";
                            catName = "Thu nợ nông dân";
                            desc = "Thu tiền nợ từ Nông dân (ID: " + memberId + ") - Bằng: " + method;
                        } else {
                            // 3. HTX Trả nợ Nông dân
                            transType = "OUT";
                            catName = "Trả nợ nông dân";
                            desc = "Chi trả nợ cho Nông dân (ID: " + memberId + ") - Bằng: " + method;
                        }
                    }

                    // Tự động tìm/tạo Danh mục và lưu giao dịch
                    int catId = fDao.getOrCreateCategory(catName, transType);
                    
                    FinancialTransaction fTrans = new FinancialTransaction();
                    fTrans.setCategoryId(catId);
                    fTrans.setAmount(new BigDecimal(amount));
                    fTrans.setTransactionType(transType);
                    // Ghép thêm ghi chú của thủ quỹ vào description để sổ cái dễ tra cứu
                    fTrans.setDescription(desc + " | Ghi chú: " + (note != null ? note : ""));
                    
                    fDao.insertTransaction(fTrans);

                } catch (Exception ex) {
                    System.out.println("Lỗi tích hợp Sổ cái từ module Công nợ: " + ex.getMessage());
                }
                // =========================================================
                response.sendRedirect("DebtManagementServlet?status=success");
            } else {
                response.sendRedirect("DebtManagementServlet?status=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("DebtManagementServlet?status=error");
        }
    }
}