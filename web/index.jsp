<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="mb-4">
        <h2 class="fw-bold">Chào mừng trở lại! 👋</h2>
        <p class="text-muted">Dưới đây là tóm tắt tình hình hệ thống GastroMap hôm nay.</p>
    </div>

    <div class="row">
        <div class="col-md-3 mb-4">
            <div class="card shadow-sm border-0 bg-primary text-white p-4 h-100">
                <h6 class="opacity-75">Tổng Thành Viên</h6>
                <h3 class="fw-bold mb-0">1,250</h3>
                <small>+5% tháng này</small>
            </div>
        </div>
        <div class="col-md-3 mb-4">
            <div class="card shadow-sm border-0 bg-success text-white p-4 h-100">
                <h6 class="opacity-75">Doanh Thu Gói VIP</h6>
                <h3 class="fw-bold mb-0">45,000,000đ</h3>
                <small>Cập nhật: 5p trước</small>
            </div>
        </div>
        <div class="col-md-3 mb-4">
            <div class="card shadow-sm border-0 bg-warning text-dark p-4 h-100">
                <h6 class="opacity-75">Địa Điểm Mới</h6>
                <h3 class="fw-bold mb-0">12</h3>
                <small>Đang chờ duyệt: 2</small>
            </div>
        </div>
        <div class="col-md-3 mb-4">
            <div class="card shadow-sm border-0 bg-info text-white p-4 h-100">
                <h6 class="opacity-75">Yêu Cầu Hỗ Trợ</h6>
                <h3 class="fw-bold mb-0">5</h3>
                <small>Chưa xử lý: 1</small>
            </div>
        </div>
    </div>

    <div class="mt-4 p-5 bg-white rounded shadow-sm text-center border">
        <img src="https://img.freepik.com/free-vector/data-report-concept-illustration_114360-883.jpg" 
             style="max-width: 250px;" alt="Intro" class="img-fluid">
        <h4 class="mt-4 fw-bold text-success">Hệ thống GastroMap v2.0</h4>
        <p class="text-muted mx-auto" style="max-width: 600px;">
            Hệ thống quản trị thông minh dành cho HTX và Nông dân.
        </p>
    </div>
</div>

<%@include file="common/footer.jsp" %>