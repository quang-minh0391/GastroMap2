<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách Kho vật tư - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
     <div class="mb-3">
                        <a href="${pageContext.request.contextPath}/SearchMaterialServlet" class="text-decoration-none text-secondary small">
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách vật tư
                        </a>
                    </div>
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <h3 class="mb-0 fw-bold text-primary">
                <i class="bi bi-houses me-2"></i>HỆ THỐNG KHO VẬT TƯ
            </h3>
            <a href="CreateWarehouseServlet" class="btn btn-success shadow-sm">
                <i class="bi bi-plus-circle me-1"></i> Thêm kho mới
            </a>
        </div>

        <div class="row">
            <c:forEach var="w" items="${warehouses}">
                <div class="col-md-4 mb-4">
                    <div class="card h-100 shadow-sm border-0">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start mb-2">
                                <h5 class="card-title fw-bold text-dark">${w.name}</h5>
                                <span class="badge bg-info text-dark">${w.total_items} loại vật tư</span>
                            </div>
                            <p class="text-muted small mb-2">
                                <i class="bi bi-geo-alt-fill text-danger"></i> ${w.location}
                            </p>
                            <p class="card-text small text-secondary">${w.description}</p>
                        </div>
                        <div class="card-footer bg-white border-top-0 d-flex justify-content-end gap-2 pb-3">
                            <a href="WarehouseInventoryServlet?id=${w.id}" class="btn btn-sm btn-outline-primary">
                                <i class="bi bi-box-seam"></i> Xem tồn kho
                            </a>
                            <a href="EditWarehouseServlet?id=${w.id}" class="btn btn-sm btn-outline-warning">
                                <i class="bi bi-pencil"></i> Sửa
                            </a>
                        </div>
                    </div>
                </div>
            </c:forEach>
            
            <c:if test="${empty warehouses}">
                <div class="col-12 text-center py-5">
                    <i class="bi bi-inbox text-muted" style="font-size: 3rem;"></i>
                    <p class="mt-2 text-muted">Chưa có dữ liệu kho vật tư nào được tạo.</p>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>