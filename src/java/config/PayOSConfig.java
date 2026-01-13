package config;
import vn.payos.PayOS;

public class PayOSConfig {
    // Thay bằng key thật của bạn
    public static final String CLIENT_ID = "d77b4944-d641-47c4-b226-870281cf96f5";
    public static final String API_KEY = "94494536-8492-485d-bc66-030d2f85d97e";
    public static final String CHECKSUM_KEY = "f979648c360769c6f51e049a6b31a319795987851ef9d615ddf3d8ba2f7354b6";

    private static PayOS payOS;

    public static PayOS getPayOS() {
        if (payOS == null) {
            payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        }
        return payOS;
    }
}