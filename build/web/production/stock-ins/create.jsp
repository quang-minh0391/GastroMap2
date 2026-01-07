<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">📥 Phiếu Nhập Kho</h2>
    <p class="text-muted">Tạo phiếu nhập kho nông sản</p>
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

                <div class="alert alert-info">
                    ℹ️ Nhập kho sẽ tự động cập nhật tồn kho theo lô và kho đã chọn.
                </div>

                <form action="${pageContext.request.contextPath}/stock-ins" method="post">
                    <input type="hidden" name="action" value="save">
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="batchId" class="form-label fw-bold">Lô sản xuất <span class="text-danger">*</span></label>
                                <select class="form-select" id="batchId" name="batchId" required>
                                    <option value="">-- Chọn lô --</option>
                                    <c:forEach var="batch" items="${batchList}">
                                        <c:forEach var="product" items="${productList}">
                                            <c:if test="${product.id == batch.productId}">
                                                <option value="${batch.id}">${batch.batchCode} - ${product.name}</option>
                                            </c:if>
                                        </c:forEach>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="mb-3">
                                <label for="warehouseId" class="form-label fw-bold">Kho nhập <span class="text-danger">*</span></label>
                                <select class="form-select" id="warehouseId" name="warehouseId" required>
                                    <option value="">-- Chọn kho --</option>
                                    <c:forEach var="warehouse" items="${warehouseList}">
                                        <option value="${warehouse.id}">${warehouse.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-4">
                            <div class="mb-3">
                                <label for="quantity" class="form-label fw-bold">Số lượng nhập <span class="text-danger">*</span></label>
                                <input type="number" step="0.01" min="0.01" class="form-control" id="quantity" 
                                       name="quantity" required placeholder="VD: 100">
                            </div>
                        </div>
                        <div class="col-md-4">
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
                        <div class="col-md-4">
                            <div class="mb-3">
                                <label for="receivedDate" class="form-label fw-bold">Ngày nhận <span class="text-danger">*</span></label>
                                <input type="date" class="form-control" id="receivedDate" name="receivedDate" required>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="receivedBy" class="form-label fw-bold">Người nhận</label>
                        <input type="text" class="form-control" id="receivedBy" name="receivedBy"
                               placeholder="Họ tên người nhận kho">
                    </div>

                    <div class="mb-3">
                        <label for="note" class="form-label fw-bold">Ghi chú</label>
                        <textarea class="form-control" id="note" name="note" rows="2"
                                  placeholder="Ghi chú thêm về phiếu nhập..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/stock-ins" class="btn btn-secondary">
                            ← Quay lại
                        </a>
                        <button type="submit" class="btn btn-success">
                            ✓ Nhập kho
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById('receivedDate').valueAsDate = new Date();
</script>

<%@include file="../../common/footer.jsp" %>
