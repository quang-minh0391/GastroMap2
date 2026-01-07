<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">➕ Tạo Lô Sản xuất mới</h2>
    <p class="text-muted">Thêm lô sản xuất mới vào hệ thống</p>
</div>

<div class="row justify-content-center">
    <div class="col-md-10">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/batches" method="post">
                    <input type="hidden" name="action" value="save">
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="batchCode" class="form-label fw-bold">Mã lô <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="batchCode" name="batchCode" required
                                       value="${suggestedBatchCode}" placeholder="VD: BATCH-000001">
                                <div class="form-text">Mã lô phải là duy nhất</div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="productId" class="form-label fw-bold">Nông sản <span class="text-danger">*</span></label>
                                <select class="form-select" id="productId" name="productId" required>
                                    <option value="">-- Chọn nông sản --</option>
                                    <c:forEach var="product" items="${productList}">
                                        <option value="${product.id}">${product.name} (${product.unit})</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="memberId" class="form-label fw-bold">Thành viên sản xuất <span class="text-danger">*</span></label>
                                <select class="form-select" id="memberId" name="memberId" required>
                                    <option value="">-- Chọn thành viên --</option>
                                    <c:forEach var="member" items="${memberList}">
                                        <option value="${member.id}">${member.fullName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="status" class="form-label fw-bold">Trạng thái</label>
                                <select class="form-select" id="status" name="status">
                                    <option value="AVAILABLE" selected>Còn hàng</option>
                                    <option value="SOLD">Đã bán</option>
                                    <option value="EXPIRED">Hết hạn</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="harvestDate" class="form-label fw-bold">Ngày thu hoạch <span class="text-danger">*</span></label>
                                <input type="date" class="form-control" id="harvestDate" name="harvestDate" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="expiryDate" class="form-label fw-bold">Ngày hết hạn</label>
                                <input type="date" class="form-control" id="expiryDate" name="expiryDate">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="totalQuantity" class="form-label fw-bold">Sản lượng <span class="text-danger">*</span></label>
                                <input type="number" step="0.01" min="0" class="form-control" id="totalQuantity" 
                                       name="totalQuantity" required placeholder="VD: 100">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="unit" class="form-label fw-bold">Đơn vị</label>
                                <select class="form-select" id="unit" name="unit">
                                    <option value="kg" selected>kg</option>
                                    <option value="tấn">tấn</option>
                                    <option value="tạ">tạ</option>
                                    <option value="yến">yến</option>
                                    <option value="lít">lít</option>
                                    <option value="quả">quả</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/batches" class="btn btn-secondary">
                            ← Quay lại
                        </a>
                        <button type="submit" class="btn btn-success">
                            ✓ Tạo lô
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
