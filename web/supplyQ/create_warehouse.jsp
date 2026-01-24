<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Kho mới - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>

    <div class="container mt-5">
        <div class="card shadow-sm border-0 mx-auto" style="max-width: 600px;">
            <div class="card-header bg-success text-white py-3">
                <h5 class="mb-0 fw-bold"><i class="bi bi-plus-circle me-2"></i>THÊM KHO VẬT TƯ MỚI</h5>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="CreateWarehouseServlet" method="post">
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Tên kho (*)</label>
                        <input type="text" name="name" class="form-control" placeholder="Ví dụ: Kho Vật Tư Số 2" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Vị trí / Địa chỉ (*)</label>
                        <input type="text" name="location" class="form-control" placeholder="Ví dụ: Khu B - Tân Xã" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Mô tả kho</label>
                        <textarea name="description" class="form-control" rows="3" placeholder="Ghi chú về mục đích sử dụng kho..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between border-top pt-3 mt-4">
                        <a href="ListWarehouseServlet" class="btn btn-secondary px-4">Quay lại</a>
                        <button type="submit" class="btn btn-success px-5 fw-bold">XÁC NHẬN TẠO</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>