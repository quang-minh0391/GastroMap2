package memberController;

import DAO.DAOCapital;
import model.CapitalContribution;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "capitalController", urlPatterns = {"/capitalManager"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class capitalController extends HttpServlet {

    /**
     * Hàm tự động tạo và trả về đường dẫn thư mục lưu trữ ảnh cố định 
     * trong thư mục cá nhân của người dùng (User Home).
     */
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
        String service = request.getParameter("service");
        DAOCapital dao = new DAOCapital();

        // Xử lý hiển thị danh sách
        if (service == null || service.equals("list") || service.equals("viewByMember")) {
            String idRaw = request.getParameter("id");
            if (idRaw != null) {
                try {
                    int farmerId = Integer.parseInt(idRaw);
                    String sDate = request.getParameter("searchDate");
                    String sReceipt = request.getParameter("searchReceipt");

                    List<CapitalContribution> list = dao.getList(farmerId, sDate, sReceipt);

                    request.setAttribute("listC", list);
                    request.setAttribute("farmerId", farmerId);
                    request.getRequestDispatcher("capital.jsp").forward(request, response);
                } catch (NumberFormatException e) {
                    response.getWriter().println("ID không hợp lệ!");
                }
            } else {
                response.getWriter().println("Empty ID!");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");
        DAOCapital dao = new DAOCapital();

        if ("insert".equals(service)) {
            try {
                // Đọc thông tin từ Form
                int farmerId = Integer.parseInt(request.getParameter("farmerId"));
                String contribution = request.getParameter("contribution");
                String date = request.getParameter("date");
                String note = request.getParameter("note");

                // 1. Xử lý file ảnh tải lên
                Part filePart = request.getPart("imageFile");
                String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                
                // Tạo tên file duy nhất bằng timestamp để tránh trùng lặp
                String fileName = System.currentTimeMillis() + "_" + originalFileName;
                
                // 2. Lưu file vật lý vào ổ cứng (User Home)
                String uploadPath = getFolderUpload();
                filePart.write(uploadPath + File.separator + fileName);

                // 3. Lưu thông tin vào Database thông qua DAO
                // Cột ảnh chỉ lưu Tên File (fileName), không lưu đường dẫn tuyệt đối
                CapitalContribution c = new CapitalContribution(0, farmerId, contribution, date, fileName, note);
                dao.insert(c);

                // 4. Quay lại trang danh sách đóng góp của nông dân đó
                response.sendRedirect("capitalManager?service=list&id=" + farmerId);
                
            } catch (Exception e) {
                // In lỗi ra Console của NetBeans để kiểm tra nếu có vấn đề
                e.printStackTrace();
                response.getWriter().println("Lỗi hệ thống: " + e.getMessage());
            }
        }
    }
}