<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3 border-bottom">
                    <h5 class="m-0 fw-bold text-success d-flex align-items-center">
                        <i class="bi bi-person-plus-fill me-2 fs-4"></i>
                        Thêm thành viên hoặc quản lý mới
                    </h5>
                </div>

                <div class="card-body p-4">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    </c:if>

                    <form action="memberManager" method="post">
                        <input type="hidden" name="service" value="insert">
                        
                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <h6 class="text-uppercase text-muted fw-bold small mb-3">Thông tin đăng nhập</h6>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Tên đăng nhập <span class="text-danger">*</span></label>
                                <input type="text" name="username" class="form-control bg-light" 
                                       placeholder="Ví dụ: nva_01" required>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Mật khẩu <span class="text-danger">*</span></label>
                                <input type="password" name="password" class="form-control bg-light" 
                                       placeholder="••••••••" required>
                            </div>

                            <div class="col-md-12 mb-4">
                                <label class="form-label fw-bold text-primary">Loại thành viên <span class="text-danger">*</span></label>
                                <select name="member_type" class="form-select border-primary bg-light-primary" required>
                                    <option value="1" selected>1. Thành viên (Nông dân - Chỉ xem dữ liệu cá nhân)</option>
                                    <option value="3">2. Quản lý (Có quyền quản lý HTX và xem danh sách)</option>
                                </select>
                                <div class="form-text mt-2">
                                    <i class="bi bi-info-circle"></i> 
                                    Mặc định tài khoản sẽ có hạn dùng đến <strong>01/01/9999</strong>.
                                </div>
                            </div>

                            <hr class="my-4 text-muted">

                            <div class="col-md-12 mb-3">
                                <h6 class="text-uppercase text-muted fw-bold small mb-3">Thông tin cá nhân</h6>
                            </div>

                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-bold">Họ và tên <span class="text-danger">*</span></label>
                                <input type="text" name="full_name" class="form-control" 
                                       placeholder="Nhập tên đầy đủ..." required>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Email <span class="text-danger">*</span></label>
                                <input type="email" name="email" class="form-control" 
                                       placeholder="example@gmail.com" required>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Số điện thoại <span class="text-danger">*</span></label>
                                <input type="text" name="phone" class="form-control" 
                                       placeholder="Nhập số điện thoại..." required>
                            </div>

                            <div class="col-md-12 mb-4">
                                <label class="form-label fw-bold">Địa chỉ</label>
                                <textarea name="address" class="form-control" rows="2" 
                                          placeholder="Địa chỉ thường trú..."></textarea>
                            </div>
                        </div>

                        <div class="pt-3 border-top d-flex justify-content-between align-items-center">
                            <a href="memberManager?service=list" class="btn btn-outline-secondary px-4">
                                <i class="bi bi-arrow-left me-2"></i>Hủy bỏ
                            </a>
                            <button type="submit" class="btn btn-success px-5 fw-bold shadow-sm">
                                <i class="bi bi-check-circle me-2"></i>Lưu thông tin
                            </button>
                        </div>
                    </form>
                </div>
            </div>
            
            <div class="text-center mt-4">
                <p class="text-muted small">
                    Hệ thống sẽ tự động gán tài khoản này vào HTX của bạn. <br>
                    Mọi thay đổi sẽ được lưu nhật ký hệ thống.
                </p>
            </div>
        </div>
    </div>
</div>

<style>
    /* Style bổ sung để làm đẹp form */
    .bg-light-primary { background-color: #f0f7ff; }
    .form-control:focus, .form-select:focus {
        border-color: #198754;
        box-shadow: 0 0 0 0.25 rad rgba(25, 135, 84, 0.25);
    }
    .card { border-radius: 12px; }
    .btn { border-radius: 8px; transition: all 0.3s; }
    .btn-success:hover { transform: translateY(-1px); box-shadow: 0 4px 12px rgba(25, 135, 84, 0.3); }
</style>

<%-- Gọi Footer --%>
<%@include file="common/footer.jsp" %>