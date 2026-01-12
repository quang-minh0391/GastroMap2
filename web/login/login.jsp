<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Đăng Nhập Hệ Thống</title>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    
    <style>
        * {
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            background-image: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url('<%=request.getContextPath()%>/image2/anhlogoRice.jpg');
            background-size: cover;
            background-position: center;
            background-attachment: fixed;
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .login-container {
            background: rgba(255, 255, 255, 0.95);
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 400px;
            text-align: center;
        }

        h2 {
            color: #333;
            margin-bottom: 30px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .input-group {
            margin-bottom: 20px;
            text-align: left;
        }

        input[type="email"],
        input[type="password"] {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 14px;
            transition: all 0.3s;
            background: #f9f9f9;
        }

        input[type="email"]:focus,
        input[type="password"]:focus {
            border-color: #ff8c00;
            background: #fff;
            outline: none;
            box-shadow: 0 0 8px rgba(255, 140, 0, 0.2);
        }

        .btn-login {
            background: linear-gradient(135deg, #ff8c00, #ed6a0a);
            color: white;
            border: none;
            padding: 12px;
            border-radius: 8px;
            cursor: pointer;
            width: 100%;
            font-size: 16px;
            font-weight: 600;
            transition: transform 0.2s, box-shadow 0.2s;
            margin-top: 10px;
        }

        .btn-login:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(237, 106, 10, 0.4);
        }

        .secondary-actions {
            margin-top: 20px;
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .btn-link {
            background: none;
            border: none;
            color: #666;
            text-decoration: none;
            font-size: 13px;
            cursor: pointer;
            transition: color 0.3s;
        }

        .btn-link:hover {
            color: #ff8c00;
            text-decoration: underline;
        }

        .register-section {
            margin-top: 25px;
            padding-top: 20px;
            border-top: 1px solid #eee;
        }

        .btn-register {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 10px;
            border-radius: 8px;
            cursor: pointer;
            width: 100%;
            font-size: 14px;
            font-weight: 500;
            transition: background 0.3s;
        }

        .btn-register:hover {
            background-color: #218838;
        }

        .message {
            background-color: #ffebee;
            color: #c62828;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 13px;
            border: 1px solid #ffcdd2;
        }

        @media (max-width: 480px) {
            .login-container {
                width: 90%;
                padding: 25px;
            }
        }
    </style>
</head>
<body>
    <div class="login-container">
        <h2>Đăng Nhập</h2>
        
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
        %>
            <div class="message"><%= message %></div>
        <% } %>

        <form action="<%=request.getContextPath()%>/loginURL" method="POST">            
            <div class="input-group">
                <input type="email" name="user" placeholder="Email công việc" required>
            </div>
            <div class="input-group">
                <input type="password" name="pass" placeholder="Mật khẩu" required>
            </div>
            
            <input type="submit" name="submit" class="btn-login" value="ĐĂNG NHẬP">
            <input type="hidden" name="service" value="loginUser">
            
            <div class="secondary-actions">
                <a href="<%= request.getContextPath() %>/forgetpass/ForgetPassword.jsp" class="btn-link">Quên mật khẩu?</a>
            </div>
        </form>

        <div class="register-section">
            <p style="font-size: 13px; color: #777; margin-bottom: 10px;">Tạo tài khoản mới </p>
            <button type="button" class="btn-register" onclick="window.location.href='<%= request.getContextPath() %>/select_plan.jsp'">
                Đăng ký tài khoản mới
            </button>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <% 
        // Lấy các thuộc tính kiểm tra từ Controller
        Boolean coopExpired = (Boolean) request.getAttribute("coopExpired");
        Boolean accountExpired = (Boolean) request.getAttribute("accountExpired");
        
        if (coopExpired != null && coopExpired) { 
            // TRƯỜNG HỢP: Tài khoản loại 1 hoặc 3 bị chặn do HTX hết hạn
    %>
        <script>
            Swal.fire({
                title: 'Hợp tác xã hết hạn!',
                text: 'Tài khoản của hợp tác xã đã hết hạn dịch vụ, bạn không thể vào hệ thống lúc này. Vui lòng liên hệ quản trị viên HTX.',
                icon: 'error',
                confirmButtonColor: '#ed6a0a',
                confirmButtonText: 'Đã hiểu',
                allowOutsideClick: false
            });
        </script>
    <% 
        } else if (accountExpired != null && accountExpired) { 
            // TRƯỜNG HỢP: Chính tài khoản loại 2 hết hạn
    %>
        <script>
            Swal.fire({
                title: 'Tài khoản hết hạn!',
                text: 'Gói dịch vụ của bạn đã hết hạn. Xin mời gia hạn để tiếp tục sử dụng.',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#ff8c00',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Gia hạn ngay',
                cancelButtonText: 'Đóng',
                allowOutsideClick: false
            }).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = '<%= request.getContextPath() %>/extension.jsp';
                }
            });
        </script>
    <% } %>
</body>
</html>