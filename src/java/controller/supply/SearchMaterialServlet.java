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
import jakarta.servlet.http.HttpSession;
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

        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập và điều hướng tuyệt đối nếu session hết hạn
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // 2. Xác định coop_id từ session (Xử lý cho cả 3 loại tài khoản)
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Type 2 dùng chính ID tài khoản
        }

        // 3. Lấy từ khóa và kiểu lọc
        String keyword = request.getParameter("keyword");
        if (keyword == null) {
            keyword = "";
        }
        String sort = request.getParameter("sort");
        if (sort == null || sort.isEmpty()) {
            sort = "";
        }

        // 4. Gọi DAO với tham số coopId
        List<Material> MaterialList = MaterialDAO.INSTANCE.listMaterials(keyword, sort, coopId);

        // 5. Đẩy dữ liệu sang JSP
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
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy Session và kiểm tra đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("id") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 2. Xác định coop_id dựa trên loại tài khoản (Type 1, 2, 3)
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Type 2 dùng chính ID của mình
        }

        // 3. Lấy từ khóa tìm kiếm
        String keyword = request.getParameter("term");
        if (keyword == null) {
            keyword = "";
        }

        // 4. Gọi DAO với tham số coopId
        List<Material> materials = MaterialDAO.INSTANCE.searchMaterials(keyword, coopId);

        // 5. Trả về JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
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
