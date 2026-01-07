<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nhập Kho - GastroMap</title>
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
                <a class="nav-link" href="${pageContext.request.contextPath}/inventory">Tồn kho</a>
                <a class="nav-link active" href="${pageContext.request.contextPath}/stock-ins">Nhập kho</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/qr-codes">Mã QR</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/traceability">Truy xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <h4 class="mb-0"><i class="bi bi-box-arrow-in-down"></i> Lịch sử Nhập Kho</h4>
                <a href="${pageContext.request.contextPath}/stock-ins?action=create" class="btn btn-primary">
                    <i class="bi bi-plus-lg"></i> Nhập kho mới
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
                                <th>Mã lô</th>
                                <th>Nông sản</th>
                                <th>Kho</th>
                                <th>Số lượng</th>
                                <th>Ngày nhận</th>
                                <th>Người nhận</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="stockIn" items="${stockInList}" varStatus="loop">
                                <tr>
                                    <td>${(currentPage - 1) * 10 + loop.count}</td>
                                    <td>
                                        <c:forEach var="batch" items="${batchList}">
                                            <c:if test="${batch.id == stockIn.batchId}">
                                                <a href="${pageContext.request.contextPath}/batches?action=view&id=${batch.id}">
                                                    <strong>${batch.batchCode}</strong>
                                                </a>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="batch" items="${batchList}">
                                            <c:if test="${batch.id == stockIn.batchId}">
                                                <c:forEach var="product" items="${productList}">
                                                    <c:if test="${product.id == batch.productId}">
                                                        ${product.name}
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="warehouse" items="${warehouseList}">
                                            <c:if test="${warehouse.id == stockIn.warehouseId}">
                                                <i class="bi bi-building"></i> ${warehouse.name}
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <span class="badge bg-success">
                                            +${stockIn.quantity} ${stockIn.unit}
                                        </span>
                                    </td>
                                    <td><fmt:formatDate value="${stockIn.receivedDate}" pattern="dd/MM/yyyy"/></td>
                                    <td>${not empty stockIn.receivedBy ? stockIn.receivedBy : '-'}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${not empty stockIn.note}">
                                                ${stockIn.note.length() > 30 ? stockIn.note.substring(0, 30).concat('...') : stockIn.note}
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted">-</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty stockInList}">
                                <tr>
                                    <td colspan="8" class="text-center text-muted py-4">
                                        Chưa có phiếu nhập kho nào. 
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
                                <a class="page-link" href="<c:url value='/stock-ins'><c:param name='page' value='${currentPage - 1}'/></c:url>">
                                    <i class="bi bi-chevron-left"></i>
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="<c:url value='/stock-ins'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="<c:url value='/stock-ins'><c:param name='page' value='${currentPage + 1}'/></c:url>">
                                    <i class="bi bi-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>

                <div class="text-muted text-center">
                    Tổng: ${totalRecords} phiếu nhập | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

