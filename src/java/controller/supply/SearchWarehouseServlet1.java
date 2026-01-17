/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.supply;

import DAO.MaterialWarehouseDAO;
import model.WarehouseStockDTO;
import com.google.gson.Gson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Admin
 */
public class SearchWarehouseServlet1 extends HttpServlet {
    private final Gson gson = new Gson();
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet SearchWarehouseServlet1</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchWarehouseServlet1 at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Hỗ trợ cả phương thức GET nếu Select2 cấu hình mặc định
        doPost(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        // Thiết lập trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1. Lấy materialId (được gửi từ JSP qua ajax data)
        String materialIdStr = request.getParameter("materialId");
        String term = request.getParameter("term"); // Từ khóa tìm kiếm tên kho
        if (term == null) term = "";

        // Kiểm tra nếu chưa chọn vật tư
        if (materialIdStr == null || materialIdStr.trim().isEmpty()) {
            response.getWriter().write("[]"); // Trả về mảng rỗng
            return;
        }

        try {
            int materialId = Integer.parseInt(materialIdStr);
            
            // 2. Gọi DAO lấy danh sách kho kèm tồn kho thực tế của vật tư đó
            MaterialWarehouseDAO dao = new MaterialWarehouseDAO();
            List<WarehouseStockDTO> list = dao.searchWarehousesWithStock(materialId, term);

            // 3. Chuyển danh sách thành JSON và trả về cho trình duyệt
            String json = this.gson.toJson(list);
            response.getWriter().write(json);
            
        } catch (NumberFormatException e) {
            response.getWriter().write("[]");
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
