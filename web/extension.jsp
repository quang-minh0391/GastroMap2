<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chọn gói thành viên</title>
        <style>
            .container { text-align: center; margin-top: 50px; font-family: Arial, sans-serif; }
            .plan-box { border: 1px solid #ddd; padding: 20px; display: inline-block; margin: 10px; border-radius: 8px; width: 200px; }
            .error { color: red; margin-bottom: 15px; }
            button { background-color: #28a745; color: white; border: none; padding: 10px 20px; cursor: pointer; border-radius: 5px; }
            button:hover { background-color: #218838; }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Vui lòng chọn gói dịch vụ để gia hạn</h2>

            <%-- Hiển thị thông báo lỗi nếu có (từ Servlet gửi về qua URL) --%>
            <% 
                String msg = request.getParameter("msg");
                if ("failed".equals(msg)) {
            %>
                <p class="error">Thanh toán không thành công hoặc đã bị hủy. Vui lòng thử lại.</p>
            <% } else if ("error".equals(msg)) { %>
                <p class="error">Có lỗi hệ thống xảy ra. Vui lòng quay lại sau.</p>
            <% } %>

            <form action="create-payment2" method="POST">
                <div class="plan-box">
                    <h3>Gói Tháng</h3>
                    <p>Giá: 100.000đ</p>
                    <button type="submit" name="plan_id" value="1">Chọn Gói Tháng</button>
                </div>

                <div class="plan-box">
                    <h3>Gói Năm</h3>
                    <p>Giá: 1.000.000đ</p>
                    <button type="submit" name="plan_id" value="2">Chọn Gói Năm</button>
                </div>
            </form>
        </div>
    </body>
</html>