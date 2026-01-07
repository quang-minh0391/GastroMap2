<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Lô Sản xuất - GastroMap</title>
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
        .form-label { font-weight: 500; }
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
                    <div class="card-header bg-white">
                        <h4 class="mb-0">
                            <i class="bi bi-plus-circle"></i> Tạo Lô Sản xuất mới
                        </h4>
                    </div>
                    <div class="card-body">
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
                                        <label for="batchCode" class="form-label">Mã lô <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="batchCode" name="batchCode" required
                                               value="${suggestedBatchCode}" placeholder="VD: BATCH-000001">
                                        <div class="form-text">Mã lô phải là duy nhất</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="productId" class="form-label">Nông sản <span class="text-danger">*</span></label>
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
                                        <label for="memberId" class="form-label">Thành viên sản xuất <span class="text-danger">*</span></label>
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
                                        <label for="status" class="form-label">Trạng thái</label>
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
                                        <label for="harvestDate" class="form-label">Ngày thu hoạch <span class="text-danger">*</span></label>
                                        <input type="date" class="form-control" id="harvestDate" name="harvestDate" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="expiryDate" class="form-label">Ngày hết hạn</label>
                                        <input type="date" class="form-control" id="expiryDate" name="expiryDate">
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="totalQuantity" class="form-label">Sản lượng <span class="text-danger">*</span></label>
                                        <input type="number" step="0.01" min="0" class="form-control" id="totalQuantity" 
                                               name="totalQuantity" required placeholder="VD: 100">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="unit" class="form-label">Đơn vị</label>
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

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/batches" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-check-lg"></i> Tạo lô
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

