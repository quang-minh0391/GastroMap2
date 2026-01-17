package controller.supply;

import DAO.MaterialSupplyDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "CreateMaterialSupplyServlet", urlPatterns = {"/CreateMaterialSupplyServlet"})
public class CreateMaterialSupplyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer staffId = (Integer) session.getAttribute("id"); // ID người lập phiếu (Admin/NV)

        if (staffId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 1. Lấy thông tin cơ bản
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            String contractIdRaw = request.getParameter("contractId");
            Integer contractId = (contractIdRaw != null && !contractIdRaw.isEmpty()) ? Integer.parseInt(contractIdRaw) : null;
            
            String note = request.getParameter("note");
            double total = Double.parseDouble(request.getParameter("totalAmount"));
            double paid = Double.parseDouble(request.getParameter("amountPaid"));

            // 2. Lấy danh sách kho và số lượng (Arrays)
            String[] whIds = request.getParameterValues("warehouseId[]");
            String[] qtys = request.getParameterValues("quantity[]");
            String[] prices = request.getParameterValues("supplyPrice[]");

            // 3. Gọi DAO xử lý nghiệp vụ
            boolean success = MaterialSupplyDAO.INSTANCE.saveFullSupply(
                    memberId, staffId, materialId, contractId, note, total, paid, whIds, qtys, prices
            );

            if (success) {
                response.sendRedirect("supplyQ/mem_supply_materials.jsp?status=success");
            } else {
                response.sendRedirect("supplyQ/mem_supply_materials.jsp?status=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("supplyQ/mem_supply_materials.jsp?status=error");
        }
    }
}