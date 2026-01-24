<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header (Đã bao gồm Sidebar và mở thẻ main-area) --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-10">
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0 mb-3" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show shadow-sm border-0 mb-3" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3">
                    <h5 class="m-0 fw-bold text-success">
                        <i class="bi bi-person-vcard me-2"></i>Thông tin hồ sơ
                    </h5>
                </div>
                <div class="card-body p-4">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <p class="mb-2"><strong>Hợp tác xã:</strong> ${userProfile.full_name}</p>
                            <p class="mb-2"><strong>Tên đăng nhập:</strong> <span class="badge bg-light text-dark">${userProfile.username}</span></p>
                            <p class="mb-2"><strong>Email:</strong> ${userProfile.email}</p>
                            <p class="mb-2"><strong>Số điện thoại:</strong> ${userProfile.phone}</p>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p class="mb-2"><strong>Địa chỉ:</strong> ${userProfile.address}</p>
                            
                            <%-- CHỈ HIỆN NGÀY HẾT HẠN NẾU LÀ MEMBER_TYPE = 2 --%>
                            <c:if test="${userProfile.member_type == 2}">
                                <p class="mb-2"><strong>Ngày hết hạn:</strong> <span class="text-danger">${userProfile.expiry_date}</span></p>
                            </c:if>

                            <p class="mb-2"><strong>Trạng thái:</strong> 
                                <span class="badge ${userProfile.status == 'Active' ? 'bg-success' : 'bg-warning'}">
                                    ${userProfile.status}
                                </span>
                            </p>
                        </div>
                    </div>
                    <hr>
                    <div class="text-end">
                        <%-- CHỈ HIỆN NÚT GIA HẠN NẾU LÀ MEMBER_TYPE = 2 --%>
                        <c:if test="${userProfile.member_type == 2}">
                            <a href="${pageContext.request.contextPath}/extension.jsp" class="btn btn-primary px-4 shadow-sm me-2">
                                <i class="bi bi-clock-history me-2"></i>Gia hạn tài khoản
                            </a>
                        </c:if>

                        <button type="button" class="btn btn-warning px-4 shadow-sm me-2 text-white fw-bold" data-bs-toggle="modal" data-bs-target="#changePassModal">
                            <i class="bi bi-key-fill me-2"></i>Đổi mật khẩu
                        </button>

                        <a href="${pageContext.request.contextPath}/updateProfile" class="btn btn-success px-4 shadow-sm">
                            <i class="bi bi-pencil-square me-2"></i>Chỉnh sửa thông tin
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="changePassModal" tabindex="-1" aria-labelledby="changePassModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow">
            <div class="modal-header bg-warning text-white">
                <h5 class="modal-title fw-bold" id="changePassModalLabel">
                    <i class="bi bi-shield-lock me-2"></i>Thay đổi mật khẩu
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="memberManager" method="post">
                <input type="hidden" name="service" value="updatePassword">
                
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mật khẩu hiện tại</label>
                        <input type="password" name="oldPassword" class="form-control" required placeholder="Nhập mật khẩu cũ">
                    </div>
                    <hr>
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Mật khẩu mới</label>
                        <input type="password" id="newPass" name="newPassword" class="form-control" required placeholder="Tối thiểu 6 ký tự">
                    </div>
                    <div class="mb-0">
                        <label class="form-label fw-semibold">Xác nhận mật khẩu mới</label>
                        <input type="password" id="confirmPass" name="confirmPassword" class="form-control" required placeholder="Nhập lại mật khẩu mới">
                        <div id="pwMsg" class="mt-2" style="font-size: 0.85rem;"></div>
                    </div>
                </div>
                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-warning px-4 text-white fw-bold shadow-sm" id="btnSubmit">Lưu thay đổi</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    const newPass = document.getElementById('newPass');
    const confirmPass = document.getElementById('confirmPass');
    const pwMsg = document.getElementById('pwMsg');
    const btnSubmit = document.getElementById('btnSubmit');

    function validatePassword() {
        if (confirmPass.value === "") {
            pwMsg.innerHTML = "";
            btnSubmit.disabled = false;
            return;
        }
        if (confirmPass.value === newPass.value) {
            pwMsg.innerHTML = '<span class="text-success"><i class="bi bi-check-circle-fill me-1"></i>Mật khẩu trùng khớp</span>';
            btnSubmit.disabled = false;
        } else {
            pwMsg.innerHTML = '<span class="text-danger"><i class="bi bi-x-circle-fill me-1"></i>Mật khẩu xác nhận chưa đúng</span>';
            btnSubmit.disabled = true;
        }
    }

    confirmPass.addEventListener('input', validatePassword);
    newPass.addEventListener('input', validatePassword);
</script>

<%-- Gọi Footer (Đã bao gồm đóng thẻ main-area và các file JS) --%>
<%@include file="common/footer.jsp" %>