package controller.production;

import DAO.DAOBatchQRCode;
import model.BatchQRCode;
import utils.QRCodeGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet to generate and serve QR code images
 * URL Pattern: /qr-image
 * 
 * Usage: /qr-image?id=123 or /qr-image?value=QR-1-ABC12345
 * 
 * Configuration:
 * - Width: 250px
 * - Height: 250px
 * - Format: PNG
 */
@WebServlet(name = "QRImageServlet", urlPatterns = {"/qr-image"})
public class QRImageServlet extends HttpServlet {

    private static final int QR_WIDTH = 250;
    private static final int QR_HEIGHT = 250;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");
        String qrValue = request.getParameter("value");
        
        String content = null;
        
        // Determine QR content
        if (idStr != null && !idStr.isEmpty()) {
            // Get QR value from database by ID
            try {
                Integer id = Integer.parseInt(idStr);
                DAOBatchQRCode dao = new DAOBatchQRCode();
                BatchQRCode qrCode = dao.getById(id);
                
                if (qrCode != null) {
                    // Build traceability URL
                    String baseUrl = getBaseUrl(request);
                    content = QRCodeGenerator.generateTraceabilityUrl(baseUrl, qrCode.getQrValue());
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID");
                return;
            }
        } else if (qrValue != null && !qrValue.isEmpty()) {
            // Use provided QR value directly
            String baseUrl = getBaseUrl(request);
            content = QRCodeGenerator.generateTraceabilityUrl(baseUrl, qrValue);
        }
        
        if (content == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "QR Code not found");
            return;
        }
        
        try {
            // Set response headers for PNG image
            response.setContentType("image/png");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            
            // Generate and write QR code image
            OutputStream outputStream = response.getOutputStream();
            QRCodeGenerator.writeQRCodeToStream(content, QR_WIDTH, QR_HEIGHT, outputStream);
            outputStream.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating QR code");
        }
    }
    
    /**
     * Get base URL of the application
     */
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        // Add port if not default
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        url.append(contextPath);
        return url.toString();
    }
}

