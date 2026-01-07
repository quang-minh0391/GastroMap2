<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">📦 Lô Sản xuất</h2>
            <p class="text-muted">Quản lý các lô sản xuất nông sản</p>
        </div>
        <a href="${pageContext.request.contextPath}/batches?action=create" class="btn btn-success">
            <i class="bi bi-plus-lg"></i> Tạo lô mới
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
                        <th>Mã lô</th>
                        <th>Nông sản</th>
                        <th>Thành viên</th>
                        <th>Ngày thu hoạch</th>
                        <th>Số lượng</th>
                        <th>Trạng thái</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="batch" items="${batchList}" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 10 + loop.count}</td>
                            <td><strong>${batch.batchCode}</strong></td>
                            <td>
                                <c:forEach var="product" items="${productList}">
                                    <c:if test="${product.id == batch.productId}">
                                        ${product.name}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="member" items="${memberList}">
                                    <c:if test="${member.id == batch.memberId}">
                                        ${member.fullName}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td><fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/></td>
                            <td>${batch.totalQuantity} ${batch.unit}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${batch.status == 'AVAILABLE'}">
                                        <span class="badge bg-success">Còn hàng</span>
                                    </c:when>
                                    <c:when test="${batch.status == 'SOLD'}">
                                        <span class="badge bg-info">Đã bán</span>
                                    </c:when>
                                    <c:when test="${batch.status == 'EXPIRED'}">
                                        <span class="badge bg-danger">Hết hạn</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${batch.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/batches?action=view&id=${batch.id}" 
                                   class="btn btn-sm btn-outline-primary" title="Xem">👁️</a>
                                <a href="${pageContext.request.contextPath}/qr-codes?action=generate&batchId=${batch.id}" 
                                   class="btn btn-sm btn-outline-success" title="Tạo QR">📱</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty batchList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">
                                Chưa có lô sản xuất nào. 
                                <a href="${pageContext.request.contextPath}/batches?action=create">Tạo lô mới</a>
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
                        <a class="page-link" href="<c:url value='/batches'><c:param name='page' value='${currentPage - 1}'/></c:url>">«</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/batches'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='/batches'><c:param name='page' value='${currentPage + 1}'/></c:url>">»</a>
                    </li>
                </ul>
            </nav>
        </c:if>

        <div class="text-muted text-center small">
            Tổng: ${totalRecords} lô | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
