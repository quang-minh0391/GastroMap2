package memberController;

import DAO.DAOContract;
import model.Contract1;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "contractController", urlPatterns = {"/contractManager"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class contractController extends HttpServlet {

    private String getFolderUpload() {
        String userHome = System.getProperty("user.home");
        File folder = new File(userHome + File.separator + "FarmerData" + File.separator + "uploads");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Lấy thông tin phân quyền từ session
        Integer userId = (Integer) session.getAttribute("id");
        Integer memberType = (Integer) session.getAttribute("member_type");
        Integer coopId = (Integer) session.getAttribute("coop_id");

        // Kiểm tra đăng nhập
        if (userId == null) {
            response.sendRedirect("login/login.jsp"); // Đảm bảo đường dẫn này đúng
            return;
        }

        String service = request.getParameter("service");
        DAOContract dao = new DAOContract();
        String sCode = request.getParameter("searchCode");
        String sType = request.getParameter("searchType");

        // 2. Mặc định hiển thị danh sách
        if (service == null || service.equals("list")) {
            
            // Gọi hàm getListSmart đã sửa logic (m.coop_id = ? OR m.id = ?)
            // Điều này giúp Type 1, 3 thấy được hợp đồng của Type 2 (chủ HTX)
            List<Contract1> list = dao.getListSmart(userId, memberType, coopId, sCode, sType);

            request.setAttribute("listContract", list);
            request.setAttribute("memberId", userId); 
            request.getRequestDispatcher("contract.jsp").forward(request, response);
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    Integer userId = (Integer) session.getAttribute("id");

    // Kiểm tra đăng nhập
    if (userId == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String service = request.getParameter("service");
    DAOContract dao = new DAOContract();

    // 1. CHỨC NĂNG THÊM MỚI (INSERT)
    if ("insert".equals(service)) {
        try {
            String contractCode = request.getParameter("contractCode");
            String contractType = request.getParameter("contractType");
            String signingDate = request.getParameter("signingDate");
            String expiryDate = request.getParameter("expiryDate");

            double totalValue = 0;
            try {
                String valStr = request.getParameter("totalValue");
                totalValue = (valStr != null && !valStr.isEmpty()) ? Double.parseDouble(valStr) : 0;
            } catch (NumberFormatException e) {
                totalValue = 0;
            }

            String status = request.getParameter("status");

            // Xử lý file minh chứng
            Part filePart = request.getPart("documentFile");
            String fileName = "";
            if (filePart != null && filePart.getSize() > 0) {
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                fileName = System.currentTimeMillis() + "_" + originalFileName;
                filePart.write(getFolderUpload() + File.separator + fileName);
            }

            // Tạo đối tượng và chèn vào DB
            Contract1 c = new Contract1(0, contractCode, userId, contractType,
                    signingDate, expiryDate, totalValue, status, fileName);

            dao.insert(c);
            response.sendRedirect("contractManager?service=list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi thêm mới: " + e.getMessage());
            doGet(request, response);
        }
    } 
    
    // 2. CHỨC NĂNG CẬP NHẬT (UPDATE)
    else if ("update".equals(service)) {
        try {
            // Lấy ID từ input hidden trong modal sửa
            int id = Integer.parseInt(request.getParameter("id"));
            String signingDate = request.getParameter("signingDate");
            String expiryDate = request.getParameter("expiryDate");
            String status = request.getParameter("status");

            // Gọi hàm update trong DAO (chỉ sửa 3 trường theo yêu cầu)
            int result = dao.update(id, signingDate, expiryDate, status);

            if (result > 0) {
                // Thành công thì quay về danh sách
                response.sendRedirect("contractManager?service=list");
            } else {
                request.setAttribute("errorMessage", "Không thể cập nhật hợp đồng!");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi cập nhật: " + e.getMessage());
            doGet(request, response);
        }
    }
}
}