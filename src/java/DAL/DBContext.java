package DAL;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBContext {
    public Connection conn = null;

    public DBContext(String URL, String username, String pass) {
        try {
            // 1. Đổi Driver sang MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. Kết nối
            conn = DriverManager.getConnection(URL, username, pass);
            System.out.println("Connected to MySQL successfully!");
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("Connection failed!");
            ex.printStackTrace();
        }
    }

    public DBContext() {
    // Thử mật khẩu là "root" thay vì để trống ""
    this("jdbc:mysql://localhost:3306/gastromap2?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "1234");
}

    public void dispatch(HttpServletRequest request, HttpServletResponse response, String page) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(page);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getData(String sql) {
        ResultSet rs = null;
        try {
            // MySQL hỗ trợ tốt hơn với Statement mặc định hoặc cấu hình dưới đây
            Statement state = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = state.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rs;
    }

    public Connection getConnection() {
        return conn;
    }
    public static void main(String[] args) {
    // 1. Khởi tạo đối tượng DBContext
    DBContext db = new DBContext();

    // 2. Kiểm tra xem kết nối có thành công không
    if (db.getConnection() != null) {
        System.out.println("Chúc mừng! Bạn đã kết nối MySQL thành công.");

        // 3. Chạy thử một câu lệnh truy vấn
        // Giả sử bạn đã tạo bảng subscription_plans
//        String testSql = "SELECT * FROM subscription_plans";
//        ResultSet rs = db.getData(testSql);
//
//        try {
//            System.out.println("--- Danh sách gói cước ---");
//            while (rs != null && rs.next()) {
//                // Lấy dữ liệu theo tên cột trong Database
//                int id = rs.getInt("id");
//                String name = rs.getString("plan_name");
//                double price = rs.getDouble("price");
//                
//                System.out.println("ID: " + id + " | Tên: " + name + " | Giá: " + price);
//            }
//        } catch (SQLException e) {
//            System.out.println("Lỗi khi đọc dữ liệu: " + e.getMessage());
//        }
    } else {
        System.out.println("Kết nối thất bại! Vui lòng kiểm tra lại:");
        System.out.println("- MySQL đã bật chưa? (XAMPP/WampServer)");
        System.out.println("- Tên Database 'gastromap2' có đúng không?");
        System.out.println("- Đã thêm thư viện mysql-connector-j-x.x.x.jar vào dự án chưa?");
    }
}
}