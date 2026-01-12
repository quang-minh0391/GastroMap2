/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.supply;

import DAO.MaterialDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Material;

/**
 *
 * @author Admin
 */
public class SearchMaterialServlet extends HttpServlet {
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
            out.println("<title>Servlet SearchMaterialServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchMaterialServlet at " + request.getContextPath() + "</h1>");
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

        // Lấy từ khóa tìm kiếm từ request
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = ""; // Nếu không có từ khóa, tìm tất cả
        }
        String sort = request.getParameter("sort");
        if (sort == null || sort.isEmpty()) {
            sort = "";
        }
        
        List<Material> MaterialList = new ArrayList<>();
        MaterialList = MaterialDAO.INSTANCE.listMaterials(keyword, sort);
                
        
        request.setAttribute("keyword", keyword);
        request.setAttribute("sort", sort);
        request.setAttribute("materialList", MaterialList);

        request.getRequestDispatcher("supplyQ/list_materials.jsp").forward(request, response);

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
        // 1. Cấu hình tiếng Việt cho dữ liệu nhận vào (quan trọng với POST)
        request.setCharacterEncoding("UTF-8");
        
        // 2. Lấy từ khóa từ Select2 gửi lên
        String keyword = request.getParameter("term");
        if (keyword == null) keyword = "";

        // 3. Gọi DAO để lấy danh sách vật tư (đã có trường unit)
        List<Material> materials = MaterialDAO.INSTANCE.searchMaterials(keyword);

        // 4. Cấu hình phản hồi JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 5. Trả về chuỗi JSON
        String json = gson.toJson(materials);
        response.getWriter().write(json);
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
