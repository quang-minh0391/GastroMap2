<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">✏️ Sửa Nông sản</h2>
    <p class="text-muted">Cập nhật thông tin nông sản</p>
</div>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/farm-products" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${product.id}">
                    
                    <div class="mb-3">
                        <label for="name" class="form-label fw-bold">Tên nông sản <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" required
                               value="${product.name}">
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="unit" class="form-label fw-bold">Đơn vị tính</label>
                                <select class="form-select" id="unit" name="unit">
                                    <option value="kg" ${product.unit == 'kg' ? 'selected' : ''}>kg (Kilogram)</option>
                                    <option value="tấn" ${product.unit == 'tấn' ? 'selected' : ''}>tấn</option>
                                    <option value="tạ" ${product.unit == 'tạ' ? 'selected' : ''}>tạ</option>
                                    <option value="yến" ${product.unit == 'yến' ? 'selected' : ''}>yến</option>
                                    <option value="lít" ${product.unit == 'lít' ? 'selected' : ''}>lít</option>
                                    <option value="quả" ${product.unit == 'quả' ? 'selected' : ''}>quả</option>
                                    <option value="bó" ${product.unit == 'bó' ? 'selected' : ''}>bó</option>
                                    <option value="túi" ${product.unit == 'túi' ? 'selected' : ''}>túi</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="status" class="form-label fw-bold">Trạng thái</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="ACTIVE" ${product.status == 'ACTIVE' ? 'selected' : ''}>Hoạt động</option>
                                    <option value="INACTIVE" ${product.status == 'INACTIVE' ? 'selected' : ''}>Ngừng hoạt động</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label fw-bold">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="3">${product.description}</textarea>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/farm-products" class="btn btn-secondary">
                            ← Quay lại
                        </a>
                        <button type="submit" class="btn btn-success">
                            ✓ Cập nhật
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
