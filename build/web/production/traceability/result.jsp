<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả Truy xuất - GastroMap</title>
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
        .trace-header {
            background: linear-gradient(135deg, var(--primary-green), var(--light-green));
            color: white;
            padding: 2rem;
            border-radius: 15px 15px 0 0;
            text-align: center;
        }
        .info-section { padding: 1.5rem; border-bottom: 1px solid #eee; }
        .info-section:last-child { border-bottom: none; }
        .info-label { font-weight: 600; color: #666; font-size: 0.9rem; }
        .info-value { font-size: 1.1rem; color: #333; }
        .verified-badge { 
            background: #198754; 
            color: white; 
            padding: 5px 15px; 
            border-radius: 20px;
            display: inline-block;
        }
        .timeline { position: relative; padding-left: 30px; }
        .timeline::before {
            content: '';
            position: absolute;
            left: 10px;
            top: 0;
            bottom: 0;
            width: 2px;
            background: #ddd;
        }
        .timeline-item { 
            position: relative; 
            padding-bottom: 15px;
        }
        .timeline-item::before {
            content: '';
            position: absolute;
            left: -24px;
            top: 5px;
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: var(--primary-green);
        }
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
                <a class="nav-link" href="${pageContext.request.contextPath}/qr-codes">Mã QR</a>
                <a class="nav-link active" href="${pageContext.request.contextPath}/traceability">Truy xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card">
                    <div class="trace-header">
                        <i class="bi bi-shield-check" style="font-size: 3rem;"></i>
                        <h3 class="mt-2 mb-1">Thông tin Nguồn gốc Xác thực</h3>
                        <p class="mb-2">Mã QR: <strong>${qrCode.qrValue}</strong></p>
                        <span class="verified-badge">
                            <i class="bi bi-check-circle"></i> Đã xác minh
                        </span>
                    </div>

                    <div class="card-body p-0">
                        <!-- Product Info -->
                        <div class="info-section">
                            <div class="row">
                                <div class="col-md-1 text-center">
                                    <i class="bi bi-box-seam" style="font-size: 2rem; color: var(--primary-green);"></i>
                                </div>
                                <div class="col-md-11">
                                    <h5 class="mb-3">Thông tin Sản phẩm</h5>
                                    <div class="row">
                                        <div class="col-md-6">
                                            <p class="mb-2">
                                                <span class="info-label">Tên nông sản:</span><br>
                                                <span class="info-value">${not empty product ? product.name : 'N/A'}</span>
                                            </p>
                                        </div>
                                        <div class="col-md-6">
                                            <p class="mb-2">
                                                <span class="info-label">Đơn vị:</span><br>
                                                <span class="info-value">${not empty product ? product.unit : 'N/A'}</span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Batch Info -->
                        <div class="info-section">
                            <div class="row">
                                <div class="col-md-1 text-center">
                                    <i class="bi bi-layers" style="font-size: 2rem; color: var(--primary-green);"></i>
                                </div>
                                <div class="col-md-11">
                                    <h5 class="mb-3">Thông tin Lô Sản xuất</h5>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="mb-2">
                                                <span class="info-label">Mã lô:</span><br>
                                                <span class="info-value">${not empty batch ? batch.batchCode : 'N/A'}</span>
                                            </p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-2">
                                                <span class="info-label">Sản lượng:</span><br>
                                                <span class="info-value">${not empty batch ? batch.totalQuantity : '0'} ${not empty batch ? batch.unit : ''}</span>
                                            </p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-2">
                                                <span class="info-label">Trạng thái:</span><br>
                                                <c:choose>
                                                    <c:when test="${batch.status == 'AVAILABLE'}">
                                                        <span class="badge bg-success">Còn hàng</span>
                                                    </c:when>
                                                    <c:when test="${batch.status == 'SOLD'}">
                                                        <span class="badge bg-info">Đã bán</span>
                                                    </c:when>
                                                    <c:when test="${batch.status == 'EXPIRED'}">
                                                        <span class="badge bg-danger">Hết hạn</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">${batch.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="mb-0">
                                                <span class="info-label">Ngày thu hoạch:</span><br>
                                                <span class="info-value">
                                                    <c:if test="${not empty batch}">
                                                        <fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/>
                                                    </c:if>
                                                </span>
                                            </p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-0">
                                                <span class="info-label">Hạn sử dụng:</span><br>
                                                <span class="info-value">
                                                    <c:choose>
                                                        <c:when test="${not empty batch.expiryDate}">
                                                            <fmt:formatDate value="${batch.expiryDate}" pattern="dd/MM/yyyy"/>
                                                        </c:when>
                                                        <c:otherwise>Không xác định</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Producer Info -->
                        <div class="info-section">
                            <div class="row">
                                <div class="col-md-1 text-center">
                                    <i class="bi bi-person-circle" style="font-size: 2rem; color: var(--primary-green);"></i>
                                </div>
                                <div class="col-md-11">
                                    <h5 class="mb-3">Thành viên Sản xuất</h5>
                                    <div class="row">
                                        <div class="col-md-4">
                                            <p class="mb-2">
                                                <span class="info-label">Họ tên:</span><br>
                                                <span class="info-value">${not empty member ? member.fullName : 'N/A'}</span>
                                            </p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-2">
                                                <span class="info-label">Điện thoại:</span><br>
                                                <span class="info-value">${not empty member.phone ? member.phone : 'N/A'}</span>
                                            </p>
                                        </div>
                                        <div class="col-md-4">
                                            <p class="mb-0">
                                                <span class="info-label">Địa chỉ:</span><br>
                                                <span class="info-value">${not empty member.address ? member.address : 'N/A'}</span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Scan History -->
                        <div class="info-section">
                            <div class="row">
                                <div class="col-md-1 text-center">
                                    <i class="bi bi-clock-history" style="font-size: 2rem; color: var(--primary-green);"></i>
                                </div>
                                <div class="col-md-11">
                                    <h5 class="mb-3">Lịch sử Quét</h5>
                                    <c:choose>
                                        <c:when test="${not empty scanHistory}">
                                            <div class="timeline">
                                                <c:forEach var="scan" items="${scanHistory}">
                                                    <div class="timeline-item">
                                                        <strong><fmt:formatDate value="${scan.scanTime}" pattern="dd/MM/yyyy HH:mm"/></strong><br>
                                                        <span class="text-muted">
                                                            <i class="bi bi-geo-alt"></i> ${not empty scan.scanLocation ? scan.scanLocation : 'Không xác định'}
                                                            | <i class="bi bi-person"></i> ${not empty scan.scanActor ? scan.scanActor : 'Anonymous'}
                                                        </span>
                                                        <c:if test="${not empty scan.note}">
                                                            <br><small class="text-muted">${scan.note}</small>
                                                        </c:if>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <p class="text-muted mb-0">Chưa có lịch sử quét</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>

                        <!-- Record New Scan -->
                        <div class="info-section bg-light">
                            <h6><i class="bi bi-plus-circle"></i> Ghi nhận lượt quét mới</h6>
                            <form action="${pageContext.request.contextPath}/traceability" method="post" class="row g-2 align-items-end">
                                <input type="hidden" name="action" value="recordScan">
                                <input type="hidden" name="qrId" value="${qrCode.id}">
                                <div class="col-md-4">
                                    <label class="form-label">Vị trí quét</label>
                                    <input type="text" class="form-control" name="scanLocation" placeholder="VD: Siêu thị ABC">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Người quét</label>
                                    <input type="text" class="form-control" name="scanActor" placeholder="Họ tên">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Ghi chú</label>
                                    <input type="text" class="form-control" name="note" placeholder="Ghi chú">
                                </div>
                                <div class="col-md-2">
                                    <button type="submit" class="btn btn-primary w-100">
                                        <i class="bi bi-check"></i> Ghi nhận
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/traceability" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Tra cứu mã khác
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

