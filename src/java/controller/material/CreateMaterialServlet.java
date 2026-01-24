/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.material;

import DAO.MaterialDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;

/**
 *
 * @author Admin
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10)
public class CreateMaterialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Chuyển hướng đến trang tạo mới
        request.getRequestDispatcher("/supplyQ/create_material.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        // 1. Kiểm tra đăng nhập và lấy coop_id (Sử dụng đường dẫn tuyệt đối)
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = (Integer) session.getAttribute("id"); // Type 2 lấy chính ID của mình làm coopId
        }

        // 2. Lấy các thông tin từ form
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String description = request.getParameter("description");

        // 3. Xử lý upload ảnh
        Part part = request.getPart("image");
        String fileName = "mat_" + System.currentTimeMillis() + ".jpg";
        String uploadPath = getServletContext().getRealPath("/") + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String imagePath = null;
        if (part != null && part.getSize() > 0) {
            part.write(uploadPath + File.separator + fileName);
            imagePath = "uploads/" + fileName;
        }

        // 4. Gọi DAO với tham số coopId
        boolean success = MaterialDAO.INSTANCE.insertMaterial(name, unit, description, imagePath, coopId);

        if (success) {
            // Chuyển hướng sử dụng đường dẫn tuyệt đối
            response.sendRedirect(request.getContextPath() + "/SearchMaterialServlet?status=success");
        } else {
            request.setAttribute("error", "Không thể thêm vật tư!");
            request.getRequestDispatcher("/supplyQ/create_material.jsp").forward(request, response);
        }
    }
}
