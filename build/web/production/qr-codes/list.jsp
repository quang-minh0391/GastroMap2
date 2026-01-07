<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mã QR - GastroMap</title>
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
        .qr-value { font-family: monospace; font-size: 0.9rem; }
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
                <a class="nav-link" href="${pageContext.request.contextPath}/stock-ins">Nhập kho</a>
                <a class="nav-link active" href="${pageContext.request.contextPath}/qr-codes">Mã QR</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/traceability">Truy xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="card">
            <div class="card-header bg-white d-flex justify-content-between align-items-center">
                <h4 class="mb-0"><i class="bi bi-qr-code"></i> Quản lý Mã QR</h4>
                <a href="${pageContext.request.contextPath}/qr-codes?action=generate" class="btn btn-primary">
                    <i class="bi bi-plus-lg"></i> Tạo mã QR
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
                                <th>Mã QR</th>
                                <th>Mã lô</th>
                                <th>Nông sản</th>
                                <th>Trạng thái</th>
                                <th>Ngày tạo</th>
                                <th class="text-center">Thao tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="qrCode" items="${qrCodeList}" varStatus="loop">
                                <tr>
                                    <td>${(currentPage - 1) * 10 + loop.count}</td>
                                    <td>
                                        <span class="qr-value">${qrCode.qrValue}</span>
                                    </td>
                                    <td>
                                        <c:forEach var="batch" items="${batchList}">
                                            <c:if test="${batch.id == qrCode.batchId}">
                                                <a href="${pageContext.request.contextPath}/batches?action=view&id=${batch.id}">
                                                    ${batch.batchCode}
                                                </a>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:forEach var="batch" items="${batchList}">
                                            <c:if test="${batch.id == qrCode.batchId}">
                                                <c:forEach var="product" items="${productList}">
                                                    <c:if test="${product.id == batch.productId}">
                                                        ${product.name}
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                        </c:forEach>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${qrCode.status == 'CREATED'}">
                                                <span class="badge bg-success">Đã tạo</span>
                                            </c:when>
                                            <c:when test="${qrCode.status == 'PRINTED'}">
                                                <span class="badge bg-info">Đã in</span>
                                            </c:when>
                                            <c:when test="${qrCode.status == 'USED'}">
                                                <span class="badge bg-warning">Đã dùng</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${qrCode.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><fmt:formatDate value="${qrCode.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/qr-codes?action=view&id=${qrCode.id}" 
                                           class="btn btn-sm btn-outline-primary" title="Xem QR">
                                            <i class="bi bi-eye"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/traceability?action=result&qrId=${qrCode.id}" 
                                           class="btn btn-sm btn-outline-success" title="Truy xuất">
                                            <i class="bi bi-search"></i>
                                        </a>
                                        <a href="${pageContext.request.contextPath}/qr-codes?action=delete&id=${qrCode.id}" 
                                           class="btn btn-sm btn-outline-danger" title="Xóa"
                                           onclick="return confirm('Bạn có chắc muốn xóa mã QR này?')">
                                            <i class="bi bi-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty qrCodeList}">
                                <tr>
                                    <td colspan="7" class="text-center text-muted py-4">
                                        Chưa có mã QR nào. 
                                        <a href="${pageContext.request.contextPath}/qr-codes?action=generate">Tạo mã QR</a>
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
                                <a class="page-link" href="<c:url value='/qr-codes'><c:param name='page' value='${currentPage - 1}'/></c:url>">
                                    <i class="bi bi-chevron-left"></i>
                                </a>
                            </li>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                    <a class="page-link" href="<c:url value='/qr-codes'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="<c:url value='/qr-codes'><c:param name='page' value='${currentPage + 1}'/></c:url>">
                                    <i class="bi bi-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>

                <div class="text-muted text-center">
                    Tổng: ${totalRecords} mã QR | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

