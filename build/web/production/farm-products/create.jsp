<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">➕ Thêm Nông sản mới</h2>
    <p class="text-muted">Thêm danh mục nông sản vào hệ thống</p>
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
                    <input type="hidden" name="action" value="save">
                    
                    <div class="mb-3">
                        <label for="name" class="form-label fw-bold">Tên nông sản <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="name" name="name" required
                               placeholder="VD: Gạo ST25, Xoài cát Hòa Lộc...">
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="unit" class="form-label fw-bold">Đơn vị tính</label>
                                <select class="form-select" id="unit" name="unit">
                                    <option value="kg" selected>kg (Kilogram)</option>
                                    <option value="tấn">tấn</option>
                                    <option value="tạ">tạ</option>
                                    <option value="yến">yến</option>
                                    <option value="lít">lít</option>
                                    <option value="quả">quả</option>
                                    <option value="bó">bó</option>
                                    <option value="túi">túi</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="status" class="form-label fw-bold">Trạng thái</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="ACTIVE" selected>Hoạt động</option>
                                    <option value="INACTIVE">Ngừng hoạt động</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="description" class="form-label fw-bold">Mô tả</label>
                        <textarea class="form-control" id="description" name="description" rows="3"
                                  placeholder="Mô tả chi tiết về nông sản..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/farm-products" class="btn btn-secondary">
                            ← Quay lại
                        </a>
                        <button type="submit" class="btn btn-success">
                            ✓ Lưu
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
