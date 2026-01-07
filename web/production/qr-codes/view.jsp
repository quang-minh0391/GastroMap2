<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Mã QR - GastroMap</title>
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
        .qr-display { 
            background: white; 
            padding: 20px; 
            border-radius: 10px; 
            text-align: center;
            border: 2px dashed #ddd;
        }
        .qr-value { 
            font-family: monospace; 
            font-size: 1.5rem; 
            font-weight: bold;
            color: var(--primary-green);
        }
        .info-label { font-weight: 600; color: #666; }
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
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card">
                    <div class="card-header bg-white d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">
                            <i class="bi bi-qr-code"></i> Chi tiết Mã QR
                        </h4>
                        <a href="${pageContext.request.contextPath}/traceability?action=result&qrId=${qrCode.id}" 
                           class="btn btn-success">
                            <i class="bi bi-search"></i> Xem Truy xuất
                        </a>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-5">
                                <div class="qr-display">
                                    <i class="bi bi-qr-code" style="font-size: 8rem; color: #333;"></i>
                                    <hr>
                                    <div class="qr-value">${qrCode.qrValue}</div>
                                    <div class="mt-2">
                                        <c:choose>
                                            <c:when test="${qrCode.status == 'CREATED'}">
                                                <span class="badge bg-success fs-6">Đã tạo</span>
                                            </c:when>
                                            <c:when test="${qrCode.status == 'PRINTED'}">
                                                <span class="badge bg-info fs-6">Đã in</span>
                                            </c:when>
                                            <c:when test="${qrCode.status == 'USED'}">
                                                <span class="badge bg-warning fs-6">Đã dùng</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary fs-6">${qrCode.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="text-muted mt-2">
                                        <small>Ngày tạo: <fmt:formatDate value="${qrCode.createdAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-7">
                                <h5><i class="bi bi-info-circle"></i> Thông tin liên kết</h5>
                                <hr>
                                
                                <c:if test="${not empty batch}">
                                    <div class="card bg-light mb-3">
                                        <div class="card-body">
                                            <h6><i class="bi bi-layers"></i> Lô sản xuất</h6>
                                            <p class="mb-1">
                                                <span class="info-label">Mã lô:</span> 
                                                <a href="${pageContext.request.contextPath}/batches?action=view&id=${batch.id}">
                                                    <strong>${batch.batchCode}</strong>
                                                </a>
                                            </p>
                                            <p class="mb-1">
                                                <span class="info-label">Sản lượng:</span> 
                                                ${batch.totalQuantity} ${batch.unit}
                                            </p>
                                            <p class="mb-1">
                                                <span class="info-label">Ngày thu hoạch:</span> 
                                                <fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/>
                                            </p>
                                            <c:if test="${not empty batch.expiryDate}">
                                                <p class="mb-0">
                                                    <span class="info-label">Hạn sử dụng:</span> 
                                                    <fmt:formatDate value="${batch.expiryDate}" pattern="dd/MM/yyyy"/>
                                                </p>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${not empty product}">
                                    <div class="card bg-light mb-3">
                                        <div class="card-body">
                                            <h6><i class="bi bi-box-seam"></i> Nông sản</h6>
                                            <p class="mb-1">
                                                <span class="info-label">Tên:</span> 
                                                <strong>${product.name}</strong>
                                            </p>
                                            <p class="mb-0">
                                                <span class="info-label">Đơn vị:</span> ${product.unit}
                                            </p>
                                        </div>
                                    </div>
                                </c:if>

                                <c:if test="${not empty member}">
                                    <div class="card bg-light mb-3">
                                        <div class="card-body">
                                            <h6><i class="bi bi-person"></i> Thành viên sản xuất</h6>
                                            <p class="mb-1">
                                                <span class="info-label">Họ tên:</span> 
                                                <strong>${member.fullName}</strong>
                                            </p>
                                            <c:if test="${not empty member.phone}">
                                                <p class="mb-1">
                                                    <span class="info-label">Điện thoại:</span> ${member.phone}
                                                </p>
                                            </c:if>
                                            <c:if test="${not empty member.address}">
                                                <p class="mb-0">
                                                    <span class="info-label">Địa chỉ:</span> ${member.address}
                                                </p>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between mt-4">
                            <a href="${pageContext.request.contextPath}/qr-codes" class="btn btn-secondary">
                                <i class="bi bi-arrow-left"></i> Quay lại danh sách
                            </a>
                            <div>
                                <button class="btn btn-outline-primary" onclick="window.print()">
                                    <i class="bi bi-printer"></i> In mã QR
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

