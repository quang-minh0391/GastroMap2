<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách Vật tư - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>

        <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3><i class="bi bi-box-seam"></i> Kho Vật Tư</h3>

                <div class="btn-group shadow-sm" role="group">
                    <a href="supplyQ/add_materials.jsp" class="btn btn-success">
                        <i class="bi bi-plus-circle"></i> Nhập kho
                    </a>
                    <a href="supplyQ/mem_supply_materials.jsp" class="btn btn-outline-primary">
                        <i class="bi bi-person-up"></i> Cung ứng thành viên
                    </a>
                </div>
            </div>

            <div class="card mb-4 shadow-sm">
                <div class="card-body">
                    <form class="row g-3" id="searchForm" action="SearchMaterialServlet" method="GET">
                        <div class="col-md-6">
                            <input type="text" class="form-control" id="searchInput" name="keyword" placeholder="Tìm kiếm tên vật tư..." value="${keyword}">
                        </div>
                        <div class="col-md-4">

                            <select class="form-select" id="sort" name="sort" onchange="document.getElementById('searchForm').submit();">
                                <option value="" <c:if test="${sort == ''}">selected</c:if>>Tất cả trạng thái</option>
                                <option value="low" <c:if test="${sort == 'low'}">selected</c:if>>Sắp hết hàng (<10)</option>
                                <option value="out" <c:if test="${sort == 'out'}">selected</c:if>>Hết hàng</option>
                                <option value="in" <c:if test="${sort == 'in'}">selected</c:if>>Còn hàng</option>

                                </select>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-secondary w-100"><i class="bi bi-search"></i> Tìm</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="card shadow-sm">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-dark">
                                <tr>
                                    <th>#</th>
                                    <th>Ảnh</th>
                                    <th>Tên Vật Tư</th>
                                    <th>Đơn Vị</th>

                                    <th class="text-center">Tồn Kho</th>
                                    <th>Trạng Thái</th>
                                    <th class="text-center">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>

                            <c:forEach var="i" items="${materialList}">
                                <tr>
                                    <td>${i.id}</td>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/${i.image}"
                                             alt="Ảnh sản phẩm"
                                             width="50"
                                             height="50">
                                    </td>
                                    <td class="fw-bold text-primary">${i.name}</td>
                                    <td>${i.unit}</td>

                                    <td class="text-center fw-bold">
                                        <fmt:formatNumber value="${i.stockQuantity}" maxFractionDigits="0"/>
                                    </td>

                                    <td>
                                        <c:choose>
                                            <c:when test="${i.stockQuantity == 0}">
                                                <span class="badge bg-danger">Hết hàng</span>
                                            </c:when>
                                            <c:when test="${i.stockQuantity > 0 && i.stockQuantity < 10}">
                                                <span class="badge bg-warning text-dark">Sắp hết</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-success">Còn hàng</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/MaterialDetailServlet?id=${i.id}" class="btn btn-sm btn-outline-info" title="Xem chi tiết"><i class="bi bi-eye"></i></a>
                                        <button class="btn btn-sm btn-outline-warning" title="Sửa"><i class="bi bi-pencil"></i></button>
                                    </td>
                                </tr>
                            </c:forEach>


                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
