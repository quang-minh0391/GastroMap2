<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa thông tin kho - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>

    <div class="container mt-5">
        <div class="card shadow-sm border-0 mx-auto" style="max-width: 600px;">
            <div class="card-header bg-warning py-3">
                <h5 class="mb-0 fw-bold text-dark">
                    <i class="bi bi-pencil-square me-2"></i>CẬP NHẬT THÔNG TIN KHO
                </h5>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="EditWarehouseServlet" method="post">
                    <input type="hidden" name="id" value="${w.id}">
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Tên kho vật tư</label>
                        <input type="text" name="name" class="form-control" value="${w.name}" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Vị trí địa lý</label>
                        <input type="text" name="location" class="form-control" value="${w.location}" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold small">Mô tả / Ghi chú</label>
                        <textarea name="description" class="form-control" rows="4">${w.description}</textarea>
                    </div>

                    <div class="d-flex justify-content-between border-top pt-3 mt-4">
                        <a href="ListWarehouseServlet" class="btn btn-secondary px-4">Hủy bỏ</a>
                        <button type="submit" class="btn btn-warning px-5 fw-bold text-dark">LƯU THAY ĐỔI</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>