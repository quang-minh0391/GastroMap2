<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header (Đã bao gồm Sidebar và mở thẻ main-area) --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-10">
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

                        <a href="${pageContext.request.contextPath}/updateProfile" class="btn btn-success px-4 shadow-sm">
                            <i class="bi bi-pencil-square me-2"></i>Chỉnh sửa thông tin
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%-- Gọi Footer (Đã bao gồm đóng thẻ main-area và các file JS) --%>
<%@include file="common/footer.jsp" %>