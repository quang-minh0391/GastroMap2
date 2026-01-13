<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Đăng ký hệ thống Rice</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; font-family: 'Poppins', sans-serif; }
        body {
            background-image: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url('<%=request.getContextPath()%>/image2/anhlogoRice.jpg');
            background-size: cover; background-position: center; background-attachment: fixed;
            margin: 0; display: flex; justify-content: center; align-items: center; min-height: 100vh; padding: 20px;
        }
        .register-container {
            background: rgba(255, 255, 255, 0.98);
            padding: 35px; border-radius: 15px; box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
            width: 100%; max-width: 450px; text-align: center;
        }
        h2 { color: #333; margin-bottom: 25px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; }
        .input-group { margin-bottom: 15px; text-align: left; }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%; padding: 12px 15px; border: 1px solid #ddd; border-radius: 8px;
            font-size: 14px; transition: all 0.3s; background: #fdfdfd;
        }
        input:focus { border-color: #ff8c00; outline: none; box-shadow: 0 0 8px rgba(255, 140, 0, 0.2); }
        .btn-register {
            background: linear-gradient(135deg, #28a745, #218838); color: white; border: none;
            padding: 13px; border-radius: 8px; cursor: pointer; width: 100%;
            font-size: 16px; font-weight: 600; margin-top: 10px; transition: 0.3s;
        }
        .btn-register:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(40, 167, 69, 0.3); }
        .message { 
            background-color: #ffebee; color: #c62828; padding: 10px; 
            border-radius: 5px; margin-bottom: 20px; font-size: 13px; border-left: 5px solid #c62828;
        }
        .login-link { margin-top: 20px; font-size: 14px; color: #666; }
        .login-link a { color: #ff8c00; text-decoration: none; font-weight: 600; }
        .login-link a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="register-container">
        <h2>Tạo tài khoản mới</h2>
        
        <% String message = (String) request.getAttribute("message");
           if (message != null) { %>
            <div class="message"><%= message %></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/Register" method="POST">
            <input type="hidden" name="service" value="registerUser">
            <input type="hidden" name="orderCode" value="${orderCode}">
            <div class="input-group">
                <input type="text" name="username" placeholder="Tên đăng nhập" required>
            </div>
            <div class="input-group">
                <input type="email" name="email" placeholder="Địa chỉ Email" required>
            </div>
            <div class="input-group">
                <input type="password" name="pass" placeholder="Mật khẩu" required>
            </div>
            <div class="input-group">
                <input type="password" name="repass" placeholder="Xác nhận mật khẩu" required>
            </div>
            <div class="input-group">
                <input type="text" name="full_name" placeholder="Họ và tên" required>
            </div>
            <div class="input-group">
                <input type="text" name="phone" placeholder="Số điện thoại" required>
            </div>
            <div class="input-group">
                <input type="text" name="address" placeholder="Địa chỉ thường trú">
            </div>

            <button type="submit" class="btn-register">HOÀN TẤT ĐĂNG KÝ</button>
        </form>

        <div class="login-link">
            Đã có tài khoản? <a href="<%=request.getContextPath()%>/login/login.jsp">Đăng nhập ngay</a>
        </div>
    </div>
</body>
</html>