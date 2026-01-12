package websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/notificationServer")
public class NotificationWebSocket {
    // Sử dụng CopyOnWriteArraySet là rất chính xác để tránh lỗi xung đột luồng (Thread-safe)
    private static final CopyOnWriteArraySet<NotificationWebSocket> sessions = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        sessions.add(this);
        System.out.println("WebSocket mở: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(this);
        System.out.println("WebSocket đóng: " + session.getId());
    }

    // Nên thêm OnError để log lỗi nếu có vấn đề về mạng
    @OnError
    public void onError(Throwable throwable) {
        System.err.println("WebSocket lỗi: " + throwable.getMessage());
    }

    // Hàm tĩnh để gửi thông báo
    public static void broadcast(String message) {
        for (NotificationWebSocket client : sessions) {
            try {
                // KIỂM TRA QUAN TRỌNG: Chỉ gửi nếu session vẫn đang mở (isOpen)
                if (client.session != null && client.session.isOpen()) {
                    client.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}