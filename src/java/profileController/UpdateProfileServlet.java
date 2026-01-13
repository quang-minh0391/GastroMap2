/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package profileController;
import DAO.DAOMember1;
import model.member;  
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author ADMIN
 */
@WebServlet(name="updateProfile", urlPatterns={"/updateProfile"})
public class UpdateProfileServlet extends HttpServlet {
   
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
            out.println("<title>Servlet UpdateProfileServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProfileServlet at " + request.getContextPath () + "</h1>");
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
        HttpSession session = request.getSession();
        Integer memberId = (Integer) session.getAttribute("id");

        if (memberId != null) {
            DAOMember1 dao = new DAOMember1();
            member user = dao.getmemberbyID(memberId);
            request.setAttribute("userProfile", user);
            request.getRequestDispatcher("updateProfile.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    // 2. Xử lý khi người dùng nhấn "Lưu thay đổi"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // Để không bị lỗi font tiếng Việt
        
        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("id");
        
        String fullName = request.getParameter("full_name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        DAOMember1 dao = new DAOMember1();
        // Gọi hàm update trong DAO (bạn cần viết hàm này trong DAOMember1)
        boolean success = dao.updateMember(id, fullName, email, phone, address);

        if (success) {
            // Cập nhật lại tên hiển thị trên session nếu có thay đổi
            session.setAttribute("full_name", fullName);
            // Chuyển hướng về trang profile kèm thông báo thành công
            response.sendRedirect("profile?message=success");
        } else {
            response.sendRedirect("updateProfile?message=error");
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
