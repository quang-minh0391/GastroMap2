<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3">
                    <h5 class="m-0 fw-bold text-success">
                        <i class="bi bi-pencil-square me-2"></i>Chỉnh sửa thông tin cá nhân
                    </h5>
                </div>
                <div class="card-body p-4">
                    <form action="updateProfile" method="POST">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Họ và tên / Tên HTX</label>
                                <input type="text" name="full_name" class="form-control" 
                                       value="${userProfile.full_name}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Email</label>
                                <input type="email" name="email" class="form-control" 
                                       value="${userProfile.email}" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Số điện thoại</label>
                                <input type="text" name="phone" class="form-control" 
                                       value="${userProfile.phone}">
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label fw-bold">Tên đăng nhập (Không thể sửa)</label>
                                <input type="text" class="form-control bg-light" 
                                       value="@${userProfile.username}" readonly>
                            </div>
                            <div class="col-md-12 mb-3">
                                <label class="form-label fw-bold">Địa chỉ</label>
                                <textarea name="address" class="form-control" rows="2">${userProfile.address}</textarea>
                            </div>
                        </div>

                        <hr class="my-4">
                        
                        <div class="d-flex justify-content-between">
                            <a href="profile" class="btn btn-outline-secondary px-4">Hủy bỏ</a>
                            <button type="submit" class="btn btn-success px-5 shadow-sm">Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="common/footer.jsp" %>