<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">✏️ Sửa thông tin Kho</h2>
    <p class="text-muted">Cập nhật thông tin kho lưu trữ</p>
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

                <form action="${pageContext.request.contextPath}/warehouses" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${warehouse.id}">
                    
                    <div class="mb-3">
                        <label for="name" class="form-label fw-bold">Tên kho <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" required
                               value="${warehouse.name}">
                    </div>

                    <div class="mb-3">
                        <label for="location" class="form-label fw-bold">Vị trí</label>
                        <input type="text" class="form-control" id="location" name="location"
                               value="${warehouse.location}">
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label fw-bold">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="3">${warehouse.description}</textarea>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/warehouses" class="btn btn-secondary">
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
