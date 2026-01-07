<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">🌾 Danh mục Nông sản</h2>
            <p class="text-muted">Quản lý các loại nông sản trong hệ thống</p>
        </div>
        <a href="${pageContext.request.contextPath}/farm-products?action=create" class="btn btn-success">
            <i class="bi bi-plus-lg"></i> Thêm mới
        </a>
    </div>
</div>

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

<div class="card shadow-sm border-0">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Tên nông sản</th>
                        <th>Đơn vị</th>
                        <th>Mô tả</th>
                        <th>Trạng thái</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${productList}" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 10 + loop.count}</td>
                            <td><strong>${product.name}</strong></td>
                            <td>${product.unit}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty product.description}">
                                        ${product.description.length() > 50 ? product.description.substring(0, 50).concat('...') : product.description}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted">-</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.status == 'ACTIVE'}">
                                        <span class="badge bg-success">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Ngừng</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/farm-products?action=edit&id=${product.id}" 
                                   class="btn btn-sm btn-outline-primary" title="Sửa">✏️</a>
                                <a href="${pageContext.request.contextPath}/farm-products?action=delete&id=${product.id}" 
                                   class="btn btn-sm btn-outline-danger" title="Xóa"
                                   onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">🗑️</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty productList}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-4">
                                Chưa có nông sản nào. 
                                <a href="${pageContext.request.contextPath}/farm-products?action=create">Thêm mới</a>
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation" class="mt-3">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='/farm-products'><c:param name='page' value='${currentPage - 1}'/></c:url>">«</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/farm-products'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='/farm-products'><c:param name='page' value='${currentPage + 1}'/></c:url>">»</a>
                    </li>
                </ul>
            </nav>
        </c:if>

        <div class="text-muted text-center small">
            Tổng: ${totalRecords} sản phẩm | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
