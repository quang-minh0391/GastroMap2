<%-- 1. Thêm dòng này để sửa lỗi font chữ tiếng Việt --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- 2. Kiểm tra lại tên file include: Nếu bạn đặt tên là admin_header.jsp thì sửa lại cho đúng --%>
<%@include file="common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">Chào mừng trở lại! 👋</h2>
    <p class="text-muted">Dưới đây là tóm tắt tình hình hệ thống GastroMap hôm nay.</p>
</div>

<div class="row">
    <div class="col-md-3 mb-4">
        <div class="card shadow-sm border-0 bg-primary text-white p-3">
            <h6 class="opacity-75">Tổng Thành Viên</h6>
            <h3 class="fw-bold">1,250</h3>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card shadow-sm border-0 bg-success text-white p-3">
            <h6 class="opacity-75">Doanh Thu Gói VIP</h6>
            <h3 class="fw-bold">45,000,000đ</h3>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card shadow-sm border-0 bg-warning text-dark p-3">
            <h6 class="opacity-75">Địa Điểm Mới</h6>
            <h3 class="fw-bold">12</h3>
        </div>
    </div>
    <div class="col-md-3 mb-4">
        <div class="card shadow-sm border-0 bg-info text-white p-3">
            <h6 class="opacity-75">Yêu Cầu Hỗ Trợ</h6>
            <h3 class="fw-bold">5</h3>
        </div>
    </div>
</div>

<div class="mt-4 p-5 bg-white rounded shadow-sm text-center border">
    <img src="https://img.freepik.com/free-vector/data-report-concept-illustration_114360-883.jpg" 
         style="max-width: 300px;" alt="Intro">
    <h4 class="mt-3 fw-bold">Hệ thống GastroMap2</h4>
    <p class="text-muted">Hệ thống quản lý dữ liệu thông minh dành cho quản trị viên.</p>
    <p class="small text-secondary">Vui lòng chọn các mục ở menu bên trái để bắt đầu làm việc.</p>
</div>

<%-- 3. Đóng bằng footer --%>
<%@include file="common/footer.jsp" %>