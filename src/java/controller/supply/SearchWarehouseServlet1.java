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
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Admin
 */
public class SearchWarehouseServlet1 extends HttpServlet {

    private final Gson gson = new Gson();

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
            out.println("<title>Servlet SearchWarehouseServlet1</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchWarehouseServlet1 at " + request.getContextPath() + "</h1>");
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
        // Hỗ trợ cả phương thức GET nếu Select2 cấu hình mặc định
        doPost(request, response);
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
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Lấy Session và xác định coop_id
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Loại 2 (HTX) lấy id chính mình
        }

        String materialIdStr = request.getParameter("materialId");
        String term = request.getParameter("term");
        if (term == null) {
            term = "";
        }

        if (materialIdStr == null || materialIdStr.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        try {
            int materialId = Integer.parseInt(materialIdStr);

            // 2. Gọi DAO kèm theo tham số coopId
            MaterialWarehouseDAO dao = new MaterialWarehouseDAO();
            List<WarehouseStockDTO> list = dao.searchWarehousesWithStock(materialId, term, coopId);

            // 3. Trả về JSON
            response.getWriter().write(this.gson.toJson(list));

        } catch (NumberFormatException e) {
            response.getWriter().write("[]");
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
