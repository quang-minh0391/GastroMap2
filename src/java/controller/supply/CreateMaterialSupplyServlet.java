package controller.supply;

import DAO.MaterialSupplyDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CreateMaterialSupplyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Integer staffId = (Integer) session.getAttribute("id");

        if (staffId == null) {
            response.sendRedirect("login/login.jsp");
            return;
        }

        // Xác định ID của HTX chủ quản
        Integer coopId = (Integer) session.getAttribute("coop_id");
        if (coopId == null || coopId == 0) {
            coopId = staffId; // Nếu là tài khoản HTX (Type 2)
        }

        try {
            int memberId = Integer.parseInt(request.getParameter("memberId"));
            int materialId = Integer.parseInt(request.getParameter("materialId"));
            String contractIdRaw = request.getParameter("contractId");
            Integer contractId = (contractIdRaw != null && !contractIdRaw.isEmpty()) ? Integer.parseInt(contractIdRaw) : null;
            
            String note = request.getParameter("note");
            double total = Double.parseDouble(request.getParameter("totalAmount"));
            double paid = Double.parseDouble(request.getParameter("amountPaid"));

            String[] whIds = request.getParameterValues("warehouseId[]");
            String[] qtys = request.getParameterValues("quantity[]");
            String[] prices = request.getParameterValues("supplyPrice[]");

            if (whIds == null) throw new Exception("Vui lòng chọn ít nhất 1 kho.");

            // Gọi DAO kèm theo tham số coopId
            boolean success = MaterialSupplyDAO.INSTANCE.saveFullSupply(
                    memberId, staffId, materialId, contractId, note, total, paid, whIds, qtys, prices, coopId
            );

            // Chuyển hướng sử dụng đường dẫn tuyệt đối
            String status = success ? "success" : "error";
            response.sendRedirect(request.getContextPath() + "/supplyQ/mem_supply_materials.jsp?status=" + status);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/supplyQ/mem_supply_materials.jsp?status=error");
        }
    }
}