<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">📜 Lịch sử Quét QR</h2>
            <p class="text-muted">Xem lịch sử các lượt quét mã QR</p>
        </div>
        <a href="${pageContext.request.contextPath}/traceability" class="btn btn-success">
            🔍 Tra cứu mới
        </a>
    </div>
</div>

<div class="card shadow-sm border-0">
    <div class="card-body">
        <div class="table-responsive">
            <table class="table table-hover data-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Mã QR</th>
                        <th>Mã lô</th>
                        <th>Thời gian quét</th>
                        <th>Vị trí</th>
                        <th>Người quét</th>
                        <th>Ghi chú</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="history" items="${historyList}" varStatus="loop">
                        <tr>
                            <td>${(currentPage - 1) * 20 + loop.count}</td>
                            <td>
                                <c:forEach var="qr" items="${qrCodeList}">
                                    <c:if test="${qr.id == history.qrId}">
                                        <a href="${pageContext.request.contextPath}/traceability?action=result&qrId=${qr.id}">
                                            <code>${qr.qrValue}</code>
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <c:forEach var="qr" items="${qrCodeList}">
                                    <c:if test="${qr.id == history.qrId}">
                                        <c:forEach var="batch" items="${batchList}">
                                            <c:if test="${batch.id == qr.batchId}">
                                                ${batch.batchCode}
                                            </c:if>
                                        </c:forEach>
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td><fmt:formatDate value="${history.scanTime}" pattern="dd/MM/yyyy HH:mm" timeZone="Asia/Ho_Chi_Minh"/></td>
                            <td>${not empty history.scanLocation ? history.scanLocation : '-'}</td>
                            <td>${not empty history.scanActor ? history.scanActor : 'Anonymous'}</td>
                            <td>${not empty history.note ? history.note : '-'}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty historyList}">
                        <tr>
                            <td colspan="7" class="text-center text-muted py-4">
                                Chưa có lịch sử quét nào.
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
                        <a class="page-link" href="<c:url value='/traceability'><c:param name='action' value='history'/><c:param name='page' value='${currentPage - 1}'/></c:url>">«</a>
                    </li>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${currentPage == i ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/traceability'><c:param name='action' value='history'/><c:param name='page' value='${i}'/></c:url>">${i}</a>
                        </li>
                    </c:forEach>
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="<c:url value='/traceability'><c:param name='action' value='history'/><c:param name='page' value='${currentPage + 1}'/></c:url>">»</a>
                    </li>
                </ul>
            </nav>
        </c:if>

        <div class="text-muted text-center small">
            Tổng: ${totalRecords} lượt quét | Trang ${currentPage}/${totalPages > 0 ? totalPages : 1}
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
