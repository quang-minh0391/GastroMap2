<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Mã QR - GastroMap</title>
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
                <a class="nav-link" href="${pageContext.request.contextPath}/stock-ins">Nhập kho</a>
                <a class="nav-link active" href="${pageContext.request.contextPath}/qr-codes">Mã QR</a>
                <a class="nav-link" href="${pageContext.request.contextPath}/traceability">Truy xuất</a>
            </div>
        </div>
    </nav>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-white">
                        <h4 class="mb-0">
                            <i class="bi bi-qr-code"></i> Tạo Mã QR cho Lô Sản xuất
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
                            Mỗi lô sản xuất có thể có nhiều mã QR. Mã QR sẽ được tự động sinh và là duy nhất.
                        </div>

                        <form action="${pageContext.request.contextPath}/qr-codes" method="post">
                            <input type="hidden" name="action" value="save">
                            
                            <div class="mb-3">
                                <label for="batchId" class="form-label">Lô sản xuất <span class="text-danger">*</span></label>
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
                                <label for="quantity" class="form-label">Số lượng mã QR</label>
                                <input type="number" class="form-control" id="quantity" name="quantity" 
                                       value="1" min="1" max="100">
                                <div class="form-text">Tối đa 100 mã QR mỗi lần tạo</div>
                            </div>

                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/qr-codes" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Quay lại
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-qr-code"></i> Tạo mã QR
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

