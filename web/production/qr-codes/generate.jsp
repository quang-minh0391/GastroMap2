<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">📱 Tạo Mã QR cho Lô Sản xuất</h2>
    <p class="text-muted">Sinh mã QR để truy xuất nguồn gốc sản phẩm</p>
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

                <div class="alert alert-info">
                    ℹ️ Mỗi lô sản xuất có thể có nhiều mã QR. Mã QR sẽ được tự động sinh và là duy nhất.
                </div>

                <form action="${pageContext.request.contextPath}/qr-codes" method="post">
                    <input type="hidden" name="action" value="save">
                    
                    <div class="mb-3">
                        <label for="batchId" class="form-label fw-bold">Lô sản xuất <span class="text-danger">*</span></label>
                        <select class="form-select" id="batchId" name="batchId" required>
                            <option value="">-- Chọn lô sản xuất --</option>
                            <c:forEach var="batch" items="${batchList}">
                                <c:forEach var="product" items="${productList}">
                                    <c:if test="${product.id == batch.productId}">
                                        <option value="${batch.id}">
                                            ${batch.batchCode} - ${product.name} (${batch.totalQuantity} ${batch.unit})
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </select>
                        <div class="form-text">Chọn lô cần tạo mã QR</div>
                    </div>

                    <div class="mb-3">
                        <label for="quantity" class="form-label fw-bold">Số lượng mã QR</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" 
                               value="1" min="1" max="100">
                        <div class="form-text">Tối đa 100 mã QR mỗi lần tạo</div>
                    </div>

                    <div class="d-flex justify-content-between mt-4">
                        <a href="${pageContext.request.contextPath}/qr-codes" class="btn btn-secondary">
                            ← Quay lại
                        </a>
                        <button type="submit" class="btn btn-success">
                            📱 Tạo mã QR
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
