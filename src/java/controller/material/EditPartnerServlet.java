/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.material;

import DAO.PartnerDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *
 * @author Admin
 */
public class EditPartnerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            Map<String, Object> partner = PartnerDAO.INSTANCE.getPartnerById(id);
            request.setAttribute("p", partner);
            request.getRequestDispatcher("/supplyQ/edit_partner.jsp").forward(request, response);
        } else {
            response.sendRedirect("ListPartnerServlet");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String taxCode = request.getParameter("tax_code");
        String note = request.getParameter("note");

        boolean success = PartnerDAO.INSTANCE.updatePartner(id, name, phone, address, taxCode, note);
        
        if (success) {
            response.sendRedirect("ListPartnerServlet?status=update_success");
        } else {
            request.setAttribute("error", "Không thể cập nhật thông tin nhà cung cấp.");
            doGet(request, response);
        }
    }
}