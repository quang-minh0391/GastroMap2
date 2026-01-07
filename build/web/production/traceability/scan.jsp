<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Truy xuất Nguồn gốc - GastroMap</title>
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
        .scan-area {
            background: linear-gradient(135deg, var(--primary-green), var(--light-green));
            color: white;
            padding: 3rem;
            border-radius: 15px;
            text-align: center;
        }
        .scan-icon { font-size: 5rem; margin-bottom: 1rem; }
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
            <div class="col-md-8">
                <div class="scan-area mb-4">
                    <div class="scan-icon">
                        <i class="bi bi-qr-code-scan"></i>
                    </div>
                    <h2>Truy xuất Nguồn gốc Nông sản</h2>
                    <p class="mb-0">Quét mã QR hoặc nhập mã để xem thông tin chi tiết về nguồn gốc sản phẩm</p>
                </div>

                <div class="card">
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

                        <form action="${pageContext.request.contextPath}/traceability" method="post">
                            <input type="hidden" name="action" value="lookup">
                            
                            <div class="mb-3">
                                <label for="qrValue" class="form-label fw-bold">
                                    <i class="bi bi-upc-scan"></i> Nhập mã QR
                                </label>
                                <div class="input-group input-group-lg">
                                    <input type="text" class="form-control" id="qrValue" name="qrValue" 
                                           placeholder="VD: QR-1-ABC12345" required autofocus>
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-search"></i> Tra cứu
                                    </button>
                                </div>
                                <div class="form-text">
                                    Nhập đầy đủ mã QR được in trên bao bì sản phẩm
                                </div>
                            </div>
                        </form>

                        <hr>

                        <div class="text-center">
                            <h6 class="text-muted">Hoặc</h6>
                            <a href="${pageContext.request.contextPath}/traceability?action=history" class="btn btn-outline-secondary">
                                <i class="bi bi-clock-history"></i> Xem lịch sử quét gần đây
                            </a>
                        </div>
                    </div>
                </div>

                <div class="card mt-4">
                    <div class="card-body">
                        <h5><i class="bi bi-question-circle"></i> Hướng dẫn sử dụng</h5>
                        <ol class="mb-0">
                            <li>Tìm mã QR trên bao bì sản phẩm</li>
                            <li>Nhập mã QR vào ô tìm kiếm ở trên</li>
                            <li>Nhấn "Tra cứu" để xem thông tin nguồn gốc</li>
                            <li>Thông tin bao gồm: Lô sản xuất, Thành viên sản xuất, Ngày thu hoạch, v.v.</li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

