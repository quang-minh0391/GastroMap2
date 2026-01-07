<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <div class="row justify-content-center">
            <div class="col-md-10">
                <div class="card">
                    <div class="card-header bg-white">
                        <h4 class="mb-0">
                            <i class="bi bi-box-arrow-in-down"></i> Phiếu Nhập Kho
                        </h4>
                    </div>
                    <div class="card-body">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                            </div>
                        </c:if>

                        <div class="alert alert-info">
                            <i class="bi bi-info-circle"></i> 
                            Nhập kho sẽ tự động cập nhật tồn kho theo lô và kho đã chọn.
                        </div>

                        <form action="${pageContext.request.contextPath}/stock-ins" method="post">
                            <input type="hidden" name="action" value="save">
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="batchId" class="form-label">Lô sản xuất <span class="text-danger">*</span></label>
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
                                        <label for="warehouseId" class="form-label">Kho nhập <span class="text-danger">*</span></label>
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
                                        <label for="quantity" class="form-label">Số lượng nhập <span class="text-danger">*</span></label>
                                        <input type="number" step="0.01" min="0.01" class="form-control" id="quantity" 
                                               name="quantity" required placeholder="VD: 100">
                                    </div>
                                </div>
                                <div class="col-md-4">
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
                                <div class="col-md-4">
                                    <div class="mb-3">
                                        <label for="receivedDate" class="form-label">Ngày nhận <span class="text-danger">*</span></label>
                                        <input type="date" class="form-control" id="receivedDate" name="receivedDate" required>
                                    </div>
                                </div>
                            </div>

                            <div class="mb-3">
                                <label for="receivedBy" class="form-label">Người nhận</label>
                                <input type="text" class="form-control" id="receivedBy" name="receivedBy"
                                       placeholder="Họ tên người nhận kho">
                            </div>

                            <div class="mb-3">
                                <label for="note" class="form-label">Ghi chú</label>
                                <textarea class="form-control" id="note" name="note" rows="2"
                                          placeholder="Ghi chú thêm về phiếu nhập..."></textarea>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/stock-ins" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-check-lg"></i> Nhập kho
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Set default date to today
        document.getElementById('receivedDate').valueAsDate = new Date();
    </script>
</body>
</html>

