<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">✅ Kết quả Truy xuất Nguồn gốc</h2>
    <p class="text-muted">Thông tin chi tiết về nguồn gốc sản phẩm</p>
</div>

<div class="card shadow-sm border-0 bg-success text-white mb-4">
    <div class="card-body text-center py-4">
        <div class="row align-items-center">
            <div class="col-md-4">
                <!-- QR Code Image -->
                <div class="bg-white p-2 d-inline-block rounded">
                    <img src="${pageContext.request.contextPath}/qr-image?id=${qrCode.id}" 
                         alt="QR Code" width="150" height="150"
                         style="image-rendering: pixelated;">
                </div>
            </div>
            <div class="col-md-8 text-md-start">
                <h4 class="mt-2 mb-2">✅ Thông tin Nguồn gốc Xác thực</h4>
                <p class="mb-2">Mã QR: <strong>${qrCode.qrValue}</strong></p>
                <span class="badge bg-light text-success fs-6">✓ Đã xác minh</span>
                <div class="mt-2">
                    <a href="${pageContext.request.contextPath}/qr-image?id=${qrCode.id}" 
                       download="QR_${qrCode.qrValue}.png"
                       class="btn btn-sm btn-light">
                        <i class="bi bi-download"></i> Tải QR
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">🌾 Thông tin Sản phẩm</h6>
            </div>
            <div class="card-body">
                <p class="mb-2"><strong>Tên nông sản:</strong> ${not empty product ? product.name : 'N/A'}</p>
                <p class="mb-0"><strong>Đơn vị:</strong> ${not empty product ? product.unit : 'N/A'}</p>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">📦 Thông tin Lô Sản xuất</h6>
            </div>
            <div class="card-body">
                <p class="mb-2"><strong>Mã lô:</strong> ${not empty batch ? batch.batchCode : 'N/A'}</p>
                <p class="mb-2"><strong>Sản lượng:</strong> ${not empty batch ? batch.totalQuantity : '0'} ${not empty batch ? batch.unit : ''}</p>
                <p class="mb-2"><strong>Trạng thái:</strong>
                    <c:choose>
                        <c:when test="${batch.status == 'AVAILABLE'}"><span class="badge bg-success">Còn hàng</span></c:when>
                        <c:when test="${batch.status == 'SOLD'}"><span class="badge bg-info">Đã bán</span></c:when>
                        <c:when test="${batch.status == 'EXPIRED'}"><span class="badge bg-danger">Hết hạn</span></c:when>
                        <c:otherwise><span class="badge bg-secondary">${batch.status}</span></c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">📅 Thời gian</h6>
            </div>
            <div class="card-body">
                <p class="mb-2"><strong>Ngày thu hoạch:</strong> 
                    <c:if test="${not empty batch}"><fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/></c:if>
                </p>
                <p class="mb-0"><strong>Hạn sử dụng:</strong>
                    <c:choose>
                        <c:when test="${not empty batch.expiryDate}"><fmt:formatDate value="${batch.expiryDate}" pattern="dd/MM/yyyy"/></c:when>
                        <c:otherwise>Không xác định</c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">👤 Thành viên Sản xuất</h6>
            </div>
            <div class="card-body">
                <p class="mb-2"><strong>Họ tên:</strong> ${not empty member ? member.full_name : 'N/A'}</p>
                <p class="mb-2"><strong>Điện thoại:</strong> ${not empty member.phone ? member.phone : 'N/A'}</p>
                <p class="mb-0"><strong>Địa chỉ:</strong> ${not empty member.address ? member.address : 'N/A'}</p>
            </div>
        </div>
    </div>
</div>

<!-- Scan History -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-header bg-light">
        <h6 class="mb-0 fw-bold">📜 Lịch sử Quét</h6>
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${not empty scanHistory}">
                <c:forEach var="scan" items="${scanHistory}">
                    <div class="border-start border-success border-3 ps-3 mb-3">
                        <strong><fmt:formatDate value="${scan.scanTime}" pattern="dd/MM/yyyy" timeZone="Asia/Ho_Chi_Minh"/></strong><br>
                        <span class="text-muted">
                            📍 ${not empty scan.scanLocation ? scan.scanLocation : 'Không xác định'}
                            | 👤 ${not empty scan.scanActor ? scan.scanActor : 'Anonymous'}
                        </span>
                        <c:if test="${not empty scan.note}">
                            <br><small class="text-muted">${scan.note}</small>
                        </c:if>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="text-muted mb-0">Chưa có lịch sử quét</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<!-- Record New Scan -->
<div class="card shadow-sm border-0 mb-4">
    <div class="card-header bg-light">
        <h6 class="mb-0 fw-bold">➕ Ghi nhận lượt quét mới</h6>
    </div>
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/traceability" method="post" class="row g-2 align-items-end">
            <input type="hidden" name="action" value="recordScan">
            <input type="hidden" name="qrId" value="${qrCode.id}">
            <div class="col-md-4">
                <label class="form-label">Vị trí quét</label>
                <input type="text" class="form-control" name="scanLocation" placeholder="VD: Siêu thị ABC">
            </div>
            <div class="col-md-3">
                <label class="form-label">Người quét</label>
                <input type="text" class="form-control" name="scanActor" placeholder="Họ tên">
            </div>
            <div class="col-md-3">
                <label class="form-label">Ghi chú</label>
                <input type="text" class="form-control" name="note" placeholder="Ghi chú">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-success w-100">✓ Ghi nhận</button>
            </div>
        </form>
    </div>
</div>

<div class="text-center mt-4">
    <a href="${pageContext.request.contextPath}/traceability" class="btn btn-secondary">
        ← Tra cứu mã khác
    </a>
</div>

<%@include file="../../common/footer.jsp" %>
