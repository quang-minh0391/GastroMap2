<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="common/header.jsp" %>

<style>
    /* Nền nhẹ nhàng và font chữ hiện đại */
    body {
        background-color: #f8f9fa;
    }
    .welcome-card {
        border: none;
        border-radius: 20px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.05);
        background: #ffffff;
        padding: 60px 40px;
        margin-top: 50px;
    }
    .main-title {
        color: #2c3e50;
        font-weight: 800;
        letter-spacing: -1px;
    }
    .sub-title {
        color: #198754; /* Màu xanh lá đặc trưng nông nghiệp */
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 2px;
        font-size: 0.9rem;
    }
    .description {
        color: #6c757d;
        line-height: 1.8;
        font-size: 1.1rem;
        max-width: 600px;
        margin: 0 auto;
    }
    .illustration {
        max-width: 320px;
        margin-bottom: 40px;
        transition: transform 0.3s ease;
    }
    .illustration:hover {
        transform: translateY(-5px);
    }
</style>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-lg-9 text-center">
            <div class="welcome-card animate__animated animate__fadeIn">
                <img src="https://img.freepik.com/free-vector/data-report-concept-illustration_114360-883.jpg" 
                     alt="GastroMap" class="illustration img-fluid">

                <p class="sub-title mb-2">Hệ thống GastroMap v2.0</p>
                
                <h1 class="main-title display-5 mb-4">Chào mừng bạn trở lại! 👋</h1>
                
                <p class="description">
                    GastroMap là nền tảng quản trị thông minh dành riêng cho 
                    <strong>Hợp tác xã và Nông dân</strong>. Chúng tôi cung cấp giải pháp 
                    số hóa hiện đại giúp bạn tối ưu hóa quy trình sản xuất và quản lý 
                    nông sản một cách bền vững.
                </p>

                <div class="mt-5 pt-4 border-top">
                    <small class="text-muted italic">
                        Cùng nhau kiến tạo tương lai nông nghiệp số Việt Nam.
                    </small>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="common/footer.jsp" %>