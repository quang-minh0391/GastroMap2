<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tồn Kho - GastroMap</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        :root {
            --primary-green: #2d5a27;
            --light-green: #4a7c43;
            --bg-cream: #f8f5f0;
        }
        body { background-color: var(--bg-cream); }
        .navbar { background-color: var(--primary-green) !important; }
        .btn-primary { background-color: var(--primary-green); border-color: var(--primary-green); }
        .btn-primary:hover { background-color: var(--light-green); border-color: var(--light-green); }
        .table thead { background-color: var(--primary-green); color: white; }
        .card { border: none; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .filter-card { background-color: #fff; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark mb-4">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">
                <i class="bi bi-leaf"></i> GastroMap
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="${pageContext.request.contextPath}/farm-products">Nông sản</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/batches">Lô sản xuất</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/warehouses">Kho</a>
                <a class="nav-link active" href="${pageContext.request.contextPath}/inventory">Tồn kho</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/stock-ins">Nhập kho</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/qr-codes">Mã QR</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/traceability">Truy xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <!-- Filter Card -->
        <div class="card filter-card mb-4">
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/inventory" method="get" class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label for="warehouseId" class="form-label">Lọc theo Kho</label>
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
                        <label for="batchId" class="form-label">Lọc theo Lô</label>
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
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-filter"></i> Lọc
                        </button>
                        <a href="${pageContext.request.contextPath}/inventory" class="btn btn-outline-secondary">
                            <i class="bi bi-x-lg"></i> Xóa bộ lọc
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <h4 class="mb-0"><i class="bi bi-box"></i> Tồn Kho theo Lô</h4>
                <a href="${pageContext.request.contextPath}/stock-ins?action=create" class="btn btn-primary">
                    <i class="bi bi-box-arrow-in-down"></i> Nhập kho
                </a>
            </div>
            <div class="card-body">
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

                <div class="table-responsive">
                    <table class="table table-hover">
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
                                                <i class="bi bi-building"></i> ${warehouse.name}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="batch" items="${batchList}">
                                            <c:if test="${batch.id == inventory.batchId}">
                                                <a href="${pageContext.request.contextPath}/batches?action=view&id=${batch.id}">
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
                                    <td>
                                        <fmt:formatDate value="${inventory.updatedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
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
                    <nav aria-label="Page navigation">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <c:url var="prevUrl" value="/inventory">
                                    <c:param name="page" value="${currentPage - 1}"/>
                                    <c:if test="${not empty selectedWarehouseId}">
                                        <c:param name="warehouseId" value="${selectedWarehouseId}"/>
                                    </c:if>
                                    <c:if test="${not empty selectedBatchId}">
                                        <c:param name="batchId" value="${selectedBatchId}"/>
                                    </c:if>
                                </c:url>
                                <a class="page-link" href="${prevUrl}">
                                    <i class="bi bi-chevron-left"></i>
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <c:url var="pageUrl" value="/inventory">
                                        <c:param name="page" value="${i}"/>
                                        <c:if test="${not empty selectedWarehouseId}">
                                            <c:param name="warehouseId" value="${selectedWarehouseId}"/>
                                        </c:if>
                                        <c:if test="${not empty selectedBatchId}">
                                            <c:param name="batchId" value="${selectedBatchId}"/>
                                        </c:if>
                                    </c:url>
                                    <a class="page-link" href="${pageUrl}">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <c:url var="nextUrl" value="/inventory">
                                    <c:param name="page" value="${currentPage + 1}"/>
                                    <c:if test="${not empty selectedWarehouseId}">
                                        <c:param name="warehouseId" value="${selectedWarehouseId}"/>
                                    </c:if>
                                    <c:if test="${not empty selectedBatchId}">
                                        <c:param name="batchId" value="${selectedBatchId}"/>
                                    </c:if>
                                </c:url>
                                <a class="page-link" href="${nextUrl}">
                                    <i class="bi bi-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>

                <div class="text-muted text-center">
                    Tổng: ${totalRecords} bản ghi | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

