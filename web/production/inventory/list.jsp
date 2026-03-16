<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">📊 Tồn Kho theo Lô</h2>
            <p class="text-muted">Xem tình trạng tồn kho theo từng lô sản xuất</p>
        </div>
        <a href="${pageContext.request.contextPath}/stock-ins?action=create" class="btn btn-success">
            📥 Nhập kho
        </a>
    </div>
</div>

<!-- Filter -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/batch-inventory" method="get" class="row g-3 align-items-end">
            <div class="col-md-4">
                <label for="warehouseId" class="form-label fw-bold">Lọc theo Kho</label>
                <select class="form-select" id="warehouseId" name="warehouseId">
                    <option value="">-- Tất cả kho --</option>
                    <c:forEach var="warehouse" items="${warehouseList}">
                        <option value="${warehouse.id}" ${selectedWarehouseId == warehouse.id ? 'selected' : ''}>
                            ${warehouse.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <label for="batchId" class="form-label fw-bold">Lọc theo Lô</label>
                <select class="form-select" id="batchId" name="batchId">
                    <option value="">-- Tất cả lô --</option>
                    <c:forEach var="batch" items="${batchList}">
                        <option value="${batch.id}" ${selectedBatchId == batch.id ? 'selected' : ''}>
                            ${batch.batchCode}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-4">
                <button type="submit" class="btn btn-primary">🔍 Lọc</button>
                <a href="${pageContext.request.contextPath}/inventory" class="btn btn-outline-secondary">✕ Xóa bộ lọc</a>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty success}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>
<c:if test="${not empty error}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
        ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
</c:if>

<div class="card shadow-sm border-0">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Kho</th>
                        <th>Mã lô</th>
                        <th>Nông sản</th>
                        <th>Số lượng tồn</th>
                        <th>Cập nhật lần cuối</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="inventory" items="${inventoryList}" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 10 + loop.count}</td>
                            <td>
                                <c:forEach var="warehouse" items="${warehouseList}">
                                    <c:if test="${warehouse.id == inventory.warehouseId}">
                                        🏭 ${warehouse.name}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="batch" items="${batchList}">
                                    <c:if test="${batch.id == inventory.batchId}">
                                        <a href="${pageContext.request.contextPath}/production-batches?action=view&id=${batch.id}">
                                            <strong>${batch.batchCode}</strong>
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="batch" items="${batchList}">
                                    <c:if test="${batch.id == inventory.batchId}">
                                        <c:forEach var="product" items="${productList}">
                                            <c:if test="${product.id == batch.productId}">
                                                ${product.name}
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <span class="badge bg-info fs-6">
                                    ${inventory.remainingQuantity} ${inventory.unit}
                                </span>
                            </td>
                            <%-- Cập nhật lần cuối: chỉ hiện ngày, ẩn giờ --%>
                            <td><fmt:formatDate value="${inventory.updatedAt}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty inventoryList}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                Chưa có dữ liệu tồn kho. 
                                <a href="${pageContext.request.contextPath}/stock-ins?action=create">Nhập kho</a>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation" class="mt-3">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <c:url var="prevUrl" value="/inventory">
                            <c:param name="page" value="${currentPage - 1}"/>
                            <c:if test="${not empty selectedWarehouseId}"><c:param name="warehouseId" value="${selectedWarehouseId}"/></c:if>
                            <c:if test="${not empty selectedBatchId}"><c:param name="batchId" value="${selectedBatchId}"/></c:if>
                        </c:url>
                        <a class="page-link" href="${prevUrl}">«</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <c:url var="pageUrl" value="/inventory">
                                <c:param name="page" value="${i}"/>
                                <c:if test="${not empty selectedWarehouseId}"><c:param name="warehouseId" value="${selectedWarehouseId}"/></c:if>
                                <c:if test="${not empty selectedBatchId}"><c:param name="batchId" value="${selectedBatchId}"/></c:if>
                            </c:url>
                            <a class="page-link" href="${pageUrl}">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <c:url var="nextUrl" value="/inventory">
                            <c:param name="page" value="${currentPage + 1}"/>
                            <c:if test="${not empty selectedWarehouseId}"><c:param name="warehouseId" value="${selectedWarehouseId}"/></c:if>
                            <c:if test="${not empty selectedBatchId}"><c:param name="batchId" value="${selectedBatchId}"/></c:if>
                        </c:url>
                        <a class="page-link" href="${nextUrl}">»</a>
                    </li>
                </ul>
            </nav>
        </c:if>

        <div class="text-muted text-center small">
            Tổng: ${totalRecords} bản ghi | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
