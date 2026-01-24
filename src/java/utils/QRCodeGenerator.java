package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating QR codes using ZXing library
 * Configuration:
 * - Width: 250px
 * - Height: 250px
 * - Format: PNG
 * - Error Correction: HIGH (can recover up to 30% damage)
 */
public class QRCodeGenerator {
    
    private static final int DEFAULT_WIDTH = 250;
    private static final int DEFAULT_HEIGHT = 250;
    private static final String DEFAULT_FORMAT = "PNG";
    
    /**
     * Generate QR code image as byte array
     * @param content The content to encode in QR code
     * @return byte array of PNG image
     */
    public static byte[] generateQRCodeImage(String content) throws WriterException, IOException {
        return generateQRCodeImage(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    /**
     * Generate QR code image with custom dimensions
     * @param content The content to encode
     * @param width Image width in pixels
     * @param height Image height in pixels
     * @return byte array of PNG image
     */
    public static byte[] generateQRCodeImage(String content, int width, int height) 
            throws WriterException, IOException {
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        // Configure encoding hints for better quality
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // High error correction
        hints.put(EncodeHintType.MARGIN, 2); // Margin around QR code
        
        // Generate the QR code matrix
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        
        // Convert to PNG image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, DEFAULT_FORMAT, outputStream);
        
        return outputStream.toByteArray();
    }
    
    /**
     * Write QR code directly to an output stream
     * @param content The content to encode
     * @param outputStream The stream to write to
     */
    public static void writeQRCodeToStream(String content, OutputStream outputStream) 
            throws WriterException, IOException {
        writeQRCodeToStream(content, DEFAULT_WIDTH, DEFAULT_HEIGHT, outputStream);
    }
    
    /**
     * Write QR code directly to an output stream with custom dimensions
     * @param content The content to encode
     * @param width Image width
     * @param height Image height
     * @param outputStream The stream to write to
     */
    public static void writeQRCodeToStream(String content, int width, int height, OutputStream outputStream) 
            throws WriterException, IOException {
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 2);
        
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, DEFAULT_FORMAT, outputStream);
    }
    
    /**
     * Generate QR code content URL for traceability
     * This URL will be encoded in the QR code for scanning
     * @param baseUrl The base URL of the application (e.g., http://localhost:9999/GastroMap2)
     * @param qrValue The unique QR value
     * @return Full URL for traceability lookup
     */
    public static String generateTraceabilityUrl(String baseUrl, String qrValue) {
        return baseUrl + "/traceability?action=scan&qr=" + qrValue;
    }
}

