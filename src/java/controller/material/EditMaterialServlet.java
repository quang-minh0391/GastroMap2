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
import jakarta.servlet.http.Part;
import java.io.File;
import model.Material;

/**
 *
 * @author Admin
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10)
public class EditMaterialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        // Sử dụng hàm getMaterialById đã có sẵn trong DAO của bạn
        Material m = MaterialDAO.INSTANCE.getMaterialById(id);
        request.setAttribute("m", m);
        request.getRequestDispatcher("/supplyQ/edit_material.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String description = request.getParameter("description");
        String oldImage = request.getParameter("oldImage");

        // Xử lý ảnh: Nếu có chọn ảnh mới thì lưu, không thì dùng lại ảnh cũ
        Part part = request.getPart("image");
        String imagePath = oldImage;
        if (part != null && part.getSize() > 0) {
            String fileName = "mat_" + System.currentTimeMillis() + ".jpg";
            String uploadPath = getServletContext().getRealPath("/") + "uploads";
            part.write(uploadPath + File.separator + fileName);
            imagePath = "uploads/" + fileName;
        }

        boolean success = MaterialDAO.INSTANCE.updateMaterial(id, name, unit, description, imagePath);
        if (success) {
            response.sendRedirect("SearchMaterialServlet?status=update_success");
        } else {
            response.sendRedirect("EditMaterialServlet?id=" + id + "&status=error");
        }
    }
}