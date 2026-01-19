<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Tồn kho: ${warehouse.name} - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
    
    <div class="container mt-4 mb-5">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="ListWarehouseServlet">Danh sách kho</a></li>
                <li class="breadcrumb-item active">Chi tiết tồn kho</li>
            </ol>
        </nav>

        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body d-flex justify-content-between align-items-center">
                <div>
                    <h3 class="fw-bold text-primary mb-1"><i class="bi bi-box-seam me-2"></i>${warehouse.name}</h3>
                    <p class="text-muted mb-0"><i class="bi bi-geo-alt-fill text-danger"></i> ${warehouse.location}</p>
                </div>
                <div class="text-end">
                    <span class="badge bg-dark p-2">${items.size()} mặt hàng</span>
                </div>
            </div>
        </div>

        <div class="card shadow-sm border-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th class="ps-3" style="width: 80px;">Ảnh</th>
                            <th>Tên vật tư</th>
                            <th class="text-center">Số lượng tồn</th>
                            <th class="text-center">Đơn vị</th>
                            <th class="text-center">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${items}">
                            <tr>
                                <td class="ps-3">
                                    <img src="${pageContext.request.contextPath}/${item.image}" 
                                         class="rounded border" style="width: 50px; height: 50px; object-fit: cover;">
                                </td>
                                <td>
                                    <div class="fw-bold">${item.name}</div>
                                    <small class="text-muted">ID: ${item.id}</small>
                                </td>
                                <td class="text-center fw-bold ${item.quantity < 10 ? 'text-danger' : 'text-success'}">
                                    <fmt:formatNumber value="${item.quantity}" pattern="#,###.#"/>
                                </td>
                                <td class="text-center text-secondary">${item.unit}</td>
                                <td class="text-center">
                                    <a href="MaterialDetailServlet?id=${item.id}" class="btn btn-sm btn-outline-info" title="Xem lịch sử">
                                        <i class="bi bi-clock-history"></i> Lịch sử
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty items}">
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted">Kho này hiện đang trống.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
        <div class="mt-3">
            <a href="ListWarehouseServlet" class="btn btn-secondary"><i class="bi bi-arrow-left"></i> Quay lại danh sách</a>
        </div>
    </div>
</body>
</html>