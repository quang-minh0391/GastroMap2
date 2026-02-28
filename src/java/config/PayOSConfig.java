package config;
import vn.payos.PayOS;

public class PayOSConfig {
    // Thay bằng key thật của bạn
    public static final String CLIENT_ID = "65c2fa91-9ffd-4cac-80bb-779033814025";
    public static final String API_KEY = "9d233427-a88f-4cbd-a9e2-a7d7e22fed22";
    public static final String CHECKSUM_KEY = "159b5dd1524c611a581af75aecfdf59fbcacd1eaabcc60cce64fc430d54013e6";

    private static PayOS payOS;

    public static PayOS getPayOS() {
        if (payOS == null) {
            payOS = new PayOS(CLIENT_ID, API_KEY, CHECKSUM_KEY);
        }
        return payOS;
    }
}