package memberController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "ImageDisplayServlet", urlPatterns = {"/displayImage"})
public class ImageDisplayServlet extends HttpServlet {
    
    private String getFolderUpload() {
        return System.getProperty("user.home") + File.separator + "FarmerData" + File.separator + "uploads";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fileName = request.getParameter("fname");
        if (fileName == null) return;

        File file = new File(getFolderUpload(), fileName);

        if (file.exists()) {
            String contentType = getServletContext().getMimeType(file.getName());
            response.setContentType(contentType);
            Files.copy(file.toPath(), response.getOutputStream());
        }
    }
}