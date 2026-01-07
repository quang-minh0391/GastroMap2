<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Lô Sản xuất - GastroMap</title>
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
        .card { border: none; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        .info-label { font-weight: 600; color: #666; }
        .info-value { font-size: 1.1rem; }
        .batch-code { font-size: 1.5rem; font-weight: 700; color: var(--primary-green); }
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
                <a class="nav-link active" href="${pageContext.request.contextPath}/batches">Lô sản xuất</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/warehouses">Kho</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/inventory">Tồn kho</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/stock-ins">Nhập kho</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/qr-codes">Mã QR</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/traceability">Truy xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">
                            <i class="bi bi-info-circle"></i> Chi tiết Lô Sản xuất
                        </h4>
                        <div>
                            <a href="${pageContext.request.contextPath}/qr-codes?action=generate&batchId=${batch.id}" 
                               class="btn btn-success">
                                <i class="bi bi-qr-code"></i> Tạo mã QR
                            </a>
                            <a href="${pageContext.request.contextPath}/stock-ins?action=create&batchId=${batch.id}" 
                               class="btn btn-primary">
                                <i class="bi bi-box-arrow-in-down"></i> Nhập kho
                            </a>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="text-center mb-4">
                            <span class="batch-code">${batch.batchCode}</span>
                            <br>
                            <c:choose>
                                <c:when test="${batch.status == 'AVAILABLE'}">
                                    <span class="badge bg-success fs-6 mt-2">Còn hàng</span>
                                </c:when>
                                <c:when test="${batch.status == 'SOLD'}">
                                    <span class="badge bg-info fs-6 mt-2">Đã bán</span>
                                </c:when>
                                <c:when test="${batch.status == 'EXPIRED'}">
                                    <span class="badge bg-danger fs-6 mt-2">Hết hạn</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary fs-6 mt-2">${batch.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="card bg-light mb-3">
                                    <div class="card-body">
                                        <h5 class="card-title"><i class="bi bi-box-seam"></i> Thông tin sản phẩm</h5>
                                        <hr>
                                        <p class="mb-2">
                                            <span class="info-label">Nông sản:</span>
                                            <span class="info-value">${product.name}</span>
                                        </p>
                                        <p class="mb-2">
                                            <span class="info-label">Sản lượng:</span>
                                            <span class="info-value">${batch.totalQuantity} ${batch.unit}</span>
                                        </p>
                                        <p class="mb-0">
                                            <span class="info-label">Đơn vị mặc định:</span>
                                            <span class="info-value">${product.unit}</span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="card bg-light mb-3">
                                    <div class="card-body">
                                        <h5 class="card-title"><i class="bi bi-person"></i> Thành viên sản xuất</h5>
                                        <hr>
                                        <p class="mb-2">
                                            <span class="info-label">Họ tên:</span>
                                            <span class="info-value">${member.fullName}</span>
                                        </p>
                                        <p class="mb-2">
                                            <span class="info-label">Điện thoại:</span>
                                            <span class="info-value">${not empty member.phone ? member.phone : 'N/A'}</span>
                                        </p>
                                        <p class="mb-0">
                                            <span class="info-label">Địa chỉ:</span>
                                            <span class="info-value">${not empty member.address ? member.address : 'N/A'}</span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6">
                                <div class="card bg-light mb-3">
                                    <div class="card-body">
                                        <h5 class="card-title"><i class="bi bi-calendar"></i> Thời gian</h5>
                                        <hr>
                                        <p class="mb-2">
                                            <span class="info-label">Ngày thu hoạch:</span>
                                            <span class="info-value"><fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/></span>
                                        </p>
                                        <p class="mb-2">
                                            <span class="info-label">Ngày hết hạn:</span>
                                            <span class="info-value">
                                                <c:choose>
                                                    <c:when test="${not empty batch.expiryDate}">
                                                        <fmt:formatDate value="${batch.expiryDate}" pattern="dd/MM/yyyy"/>
                                                    </c:when>
                                                    <c:otherwise>Không xác định</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </p>
                                        <p class="mb-0">
                                            <span class="info-label">Ngày tạo:</span>
                                            <span class="info-value"><fmt:formatDate value="${batch.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="card bg-light mb-3">
                                    <div class="card-body">
                                        <h5 class="card-title"><i class="bi bi-gear"></i> Thao tác nhanh</h5>
                                        <hr>
                                        <div class="d-grid gap-2">
                                            <a href="${pageContext.request.contextPath}/qr-codes?action=list&batchId=${batch.id}" 
                                               class="btn btn-outline-primary">
                                                <i class="bi bi-qr-code"></i> Xem mã QR của lô
                                            </a>
                                            <a href="${pageContext.request.contextPath}/inventory?batchId=${batch.id}" 
                                               class="btn btn-outline-info">
                                                <i class="bi bi-box"></i> Xem tồn kho
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between mt-3">
                            <a href="${pageContext.request.contextPath}/batches" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Quay lại danh sách
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

