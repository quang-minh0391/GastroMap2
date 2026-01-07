<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">📥 Lịch sử Nhập Kho</h2>
            <p class="text-muted">Xem lịch sử các phiếu nhập kho</p>
        </div>
        <a href="${pageContext.request.contextPath}/stock-ins?action=create" class="btn btn-success">
            ➕ Nhập kho mới
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
                        <th>Kho</th>
                        <th>Số lượng</th>
                        <th>Ngày nhận</th>
                        <th>Người nhận</th>
                        <th>Ghi chú</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="stockIn" items="${stockInList}" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 10 + loop.count}</td>
                            <td>
                                <c:forEach var="batch" items="${batchList}">
                                    <c:if test="${batch.id == stockIn.batchId}">
                                        <a href="${pageContext.request.contextPath}/batches?action=view&id=${batch.id}">
                                            <strong>${batch.batchCode}</strong>
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="batch" items="${batchList}">
                                    <c:if test="${batch.id == stockIn.batchId}">
                                        <c:forEach var="product" items="${productList}">
                                            <c:if test="${product.id == batch.productId}">
                                                ${product.name}
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="warehouse" items="${warehouseList}">
                                    <c:if test="${warehouse.id == stockIn.warehouseId}">
                                        🏭 ${warehouse.name}
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <span class="badge bg-success">+${stockIn.quantity} ${stockIn.unit}</span>
                            </td>
                            <td><fmt:formatDate value="${stockIn.receivedDate}" pattern="dd/MM/yyyy"/></td>
                            <td>${not empty stockIn.receivedBy ? stockIn.receivedBy : '-'}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty stockIn.note}">
                                        ${stockIn.note.length() > 30 ? stockIn.note.substring(0, 30).concat('...') : stockIn.note}
                                    </c:when>
                                    <c:otherwise><span class="text-muted">-</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty stockInList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">
                                Chưa có phiếu nhập kho nào. 
                                <a href="${pageContext.request.contextPath}/stock-ins?action=create">Nhập kho</a>
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
                        <a class="page-link" href="<c:url value='/stock-ins'><c:param name='page' value='${currentPage - 1}'/></c:url>">«</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/stock-ins'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='/stock-ins'><c:param name='page' value='${currentPage + 1}'/></c:url>">»</a>
                    </li>
                </ul>
            </nav>
        </c:if>

        <div class="text-muted text-center small">
            Tổng: ${totalRecords} phiếu nhập | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
