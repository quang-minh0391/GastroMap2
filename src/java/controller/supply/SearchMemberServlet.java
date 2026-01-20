/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.supply;

import DAO.DAOMember1;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.member;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
public class SearchMemberServlet extends HttpServlet {

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
            out.println("<title>Servlet SearchMemberServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchMemberServlet at " + request.getContextPath() + "</h1>");
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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 1. Lấy Session hiện tại (dùng false để không tạo session mới nếu đã hết hạn)
        HttpSession session = request.getSession(false);

// 2. Kiểm tra nếu session không tồn tại hoặc thông tin đăng nhập bị trống
        if (session == null || session.getAttribute("id") == null) {
            // Chuyển hướng về trang login.jsp (sử dụng getContextPath để đảm bảo đường dẫn đúng)
            response.sendRedirect("login/login.jsp");
            return; // Dừng xử lý các lệnh phía dưới
        }

// 3. Nếu session hợp lệ, tiếp tục lấy thông tin
        Integer currentUserId = (Integer) session.getAttribute("id");
        Integer memberType = (Integer) session.getAttribute("member_type");
        Integer userCoopId = (Integer) session.getAttribute("coop_id");

        // 2. LOGIC XÁC ĐỊNH COOP_ID MỤC TIÊU
        int targetCoopId = 0;
        if (memberType == 2) {
            // Nếu là Chủ nhiệm (Type 2) -> coop_id của nông dân phải = ID của chính ông này
            targetCoopId = currentUserId;
        } else if (memberType == 3) {
            // Nếu là Nhân viên (Type 3) -> coop_id của nông dân phải = coop_id của nhân viên này
            targetCoopId = (userCoopId != null) ? userCoopId : 0;
        }

        // 3. Lấy từ khóa tìm kiếm từ Select2 (tham số mặc định là 'term')
        String keyword = request.getParameter("term");
        if (keyword == null) {
            keyword = "";
        }

        // 4. Gọi DAO tìm kiếm
        DAOMember1 dao = new DAOMember1();
        List<member> list = dao.searchMembersByCoop(targetCoopId, keyword);

        // 5. Trả về kết quả JSON
        String json = gson.toJson(list);
        response.getWriter().write(json);
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
        processRequest(request, response);
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
