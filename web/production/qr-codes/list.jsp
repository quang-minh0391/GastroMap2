<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">📱 Quản lý Mã QR</h2>
            <p class="text-muted">Quản lý các mã QR đã tạo cho lô sản xuất</p>
        </div>
        <a href="${pageContext.request.contextPath}/qr-codes?action=generate" class="btn btn-success">
            ➕ Tạo mã QR
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
            <table class="table table-hover data-table align-middle">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>QR Code</th>
                        <th>Mã QR</th>
                        <th>Mã lô</th>
                        <th>Nông sản</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="qrCode" items="${qrCodeList}" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 10 + loop.count}</td>
                            <td>
                                <!-- Thumbnail QR Code -->
                                <a href="${pageContext.request.contextPath}/qr-codes?action=view&id=${qrCode.id}" 
                                   title="Click để xem chi tiết">
                                    <img src="${pageContext.request.contextPath}/qr-image?id=${qrCode.id}" 
                                         alt="QR" width="50" height="50"
                                         class="border rounded"
                                         style="image-rendering: pixelated;">
                                </a>
                            </td>
                            <td><code class="bg-light p-1">${qrCode.qrValue}</code></td>
                            <td>
                                <c:forEach var="batch" items="${batchList}">
                                    <c:if test="${batch.id == qrCode.batchId}">
                                        <a href="${pageContext.request.contextPath}/production-batches?action=view&id=${batch.id}">
                                            ${batch.batchCode}
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="batch" items="${batchList}">
                                    <c:if test="${batch.id == qrCode.batchId}">
                                        <c:forEach var="product" items="${productList}">
                                            <c:if test="${product.id == batch.productId}">
                                                ${product.name}
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${qrCode.status == 'CREATED'}">
                                        <span class="badge bg-success">Đã tạo</span>
                                    </c:when>
                                    <c:when test="${qrCode.status == 'PRINTED'}">
                                        <span class="badge bg-info">Đã in</span>
                                    </c:when>
                                    <c:when test="${qrCode.status == 'USED'}">
                                        <span class="badge bg-warning">Đã dùng</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${qrCode.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${qrCode.createdAt}" pattern="dd/MM/yyyy"/></td>
                            <td class="text-center">
                                <div class="btn-group" role="group">
                                    <a href="${pageContext.request.contextPath}/qr-codes?action=view&id=${qrCode.id}" 
                                       class="btn btn-sm btn-outline-primary" title="Xem QR">
                                        <i class="bi bi-eye"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/qr-image?id=${qrCode.id}" 
                                       download="QR_${qrCode.qrValue}.png"
                                       class="btn btn-sm btn-outline-success" title="Tải xuống">
                                        <i class="bi bi-download"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/traceability?action=result&qrId=${qrCode.id}" 
                                       class="btn btn-sm btn-outline-info" title="Truy xuất">
                                        <i class="bi bi-search"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/qr-codes?action=delete&id=${qrCode.id}" 
                                       class="btn btn-sm btn-outline-danger" title="Xóa"
                                       onclick="return confirm('Bạn có chắc muốn xóa mã QR này?')">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty qrCodeList}">
                        <tr>
                            <td colspan="8" class="text-center text-muted py-4">
                                Chưa có mã QR nào. 
                                <a href="${pageContext.request.contextPath}/qr-codes?action=generate">Tạo mã QR</a>
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
                        <a class="page-link" href="<c:url value='/qr-codes'><c:param name='page' value='${currentPage - 1}'/></c:url>">«</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/qr-codes'><c:param name='page' value='${i}'/></c:url>">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='/qr-codes'><c:param name='page' value='${currentPage + 1}'/></c:url>">»</a>
                    </li>
                </ul>
            </nav>
        </c:if>

        <div class="text-muted text-center small">
            Tổng: ${totalRecords} mã QR | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
