<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h2 class="fw-bold">📋 Chi tiết Lô Sản xuất</h2>
            <p class="text-muted">Thông tin chi tiết về lô sản xuất</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/qr-codes?action=generate&batchId=${batch.id}" 
               class="btn btn-success">📱 Tạo mã QR</a>
            <a href="${pageContext.request.contextPath}/stock-ins?action=create&batchId=${batch.id}" 
               class="btn btn-primary">📥 Nhập kho</a>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-body text-center py-4">
                <h3 class="text-success fw-bold">${batch.batchCode}</h3>
                <c:choose>
                    <c:when test="${batch.status == 'AVAILABLE'}">
                        <span class="badge bg-success fs-6">Còn hàng</span>
                    </c:when>
                    <c:when test="${batch.status == 'SOLD'}">
                        <span class="badge bg-info fs-6">Đã bán</span>
                    </c:when>
                    <c:when test="${batch.status == 'EXPIRED'}">
                        <span class="badge bg-danger fs-6">Hết hạn</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge bg-secondary fs-6">${batch.status}</span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<div class="row">
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">📦 Thông tin sản phẩm</h6>
            </div>
            <div class="card-body">
                <p class="mb-2"><strong>Nông sản:</strong> ${product.name}</p>
                <p class="mb-2"><strong>Sản lượng:</strong> ${batch.totalQuantity} ${batch.unit}</p>
                <p class="mb-0"><strong>Đơn vị mặc định:</strong> ${product.unit}</p>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">👤 Thành viên sản xuất</h6>
            </div>
            <div class="card-body">
                <p class="mb-2"><strong>Họ tên:</strong> ${member.fullName}</p>
                <p class="mb-2"><strong>Điện thoại:</strong> ${not empty member.phone ? member.phone : 'N/A'}</p>
                <p class="mb-0"><strong>Địa chỉ:</strong> ${not empty member.address ? member.address : 'N/A'}</p>
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
                <p class="mb-2"><strong>Ngày thu hoạch:</strong> <fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/></p>
                <p class="mb-2"><strong>Ngày hết hạn:</strong> 
                    <c:choose>
                        <c:when test="${not empty batch.expiryDate}">
                            <fmt:formatDate value="${batch.expiryDate}" pattern="dd/MM/yyyy"/>
                        </c:when>
                        <c:otherwise>Không xác định</c:otherwise>
                    </c:choose>
                </p>
                <p class="mb-0"><strong>Ngày tạo:</strong> <fmt:formatDate value="${batch.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card shadow-sm border-0 mb-4">
            <div class="card-header bg-light">
                <h6 class="mb-0 fw-bold">⚡ Thao tác nhanh</h6>
            </div>
            <div class="card-body">
                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/qr-codes?batchId=${batch.id}" 
                       class="btn btn-outline-primary">📱 Xem mã QR của lô</a>
                    <a href="${pageContext.request.contextPath}/inventory?batchId=${batch.id}" 
                       class="btn btn-outline-info">📊 Xem tồn kho</a>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="mt-3">
    <a href="${pageContext.request.contextPath}/batches" class="btn btn-secondary">
        ← Quay lại danh sách
    </a>
</div>

<%@include file="../../common/footer.jsp" %>
