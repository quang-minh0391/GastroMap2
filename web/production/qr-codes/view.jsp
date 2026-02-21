<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="../../common/header.jsp" %>

<!-- Print Styles -->
<style>
    /* Hide everything except print area when printing */
    @media print {
        body * {
            visibility: hidden;
        }
        
        .print-area, .print-area * {
            visibility: visible;
        }
        
        .print-area {
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            padding: 20px;
        }
        
        .no-print {
            display: none !important;
        }
        
        /* Remove margins and shadows for clean print */
        .print-area .card {
            box-shadow: none !important;
            border: 2px solid #198754 !important;
        }
        
        @page {
            size: auto;
            margin: 10mm;
        }
    }
    
    /* Print preview card styling */
    .print-card {
        max-width: 350px;
        margin: 0 auto;
        border: 3px solid #198754;
        border-radius: 16px;
        overflow: hidden;
    }
    
    .print-card-header {
        background: linear-gradient(135deg, #198754 0%, #20c997 100%);
        padding: 15px 20px;
        text-align: center;
    }
    
    .print-card-header .logo {
        font-size: 1.8rem;
        font-weight: bold;
        color: white;
        text-shadow: 1px 1px 2px rgba(0,0,0,0.2);
    }
    
    .print-card-body {
        padding: 25px;
        text-align: center;
        background: white;
    }
    
    .print-qr-frame {
        display: inline-block;
        padding: 15px;
        background: white;
        border: 2px solid #e9ecef;
        border-radius: 12px;
        box-shadow: 0 4px 15px rgba(0,0,0,0.1);
    }
    
    .print-qr-code {
        font-family: 'Courier New', monospace;
        font-size: 1.1rem;
        font-weight: bold;
        color: #198754;
        letter-spacing: 1px;
        margin: 15px 0 10px 0;
    }
    
    .print-product-name {
        font-size: 1rem;
        color: #495057;
        margin: 5px 0;
    }
    
    .print-batch-code {
        font-size: 0.9rem;
        color: #6c757d;
        margin: 5px 0;
    }
    
    .print-card-footer {
        background: #f8f9fa;
        padding: 10px 20px;
        text-align: center;
        border-top: 1px solid #e9ecef;
    }
    
    .print-footer-text {
        font-size: 0.75rem;
        color: #6c757d;
        margin: 0;
    }
    
    .print-scan-text {
        font-size: 0.8rem;
        color: #198754;
        font-weight: 500;
    }
</style>

<div class="no-print">
    <div class="mb-4">
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h2 class="fw-bold"><i class="bi bi-qr-code me-2"></i>Chi tiết Mã QR</h2>
                <p class="text-muted">Thông tin chi tiết về mã QR và lô sản xuất</p>
            </div>
            <a href="${pageContext.request.contextPath}/traceability?action=result&qrId=${qrCode.id}" 
               class="btn btn-success">
                <i class="bi bi-search me-1"></i> Xem Truy xuất
            </a>
        </div>
    </div>
</div>

<div class="row">
    <!-- Left Column - QR Code Display & Print Preview -->
    <div class="col-md-5">
        <!-- Print Area - This will be printed -->
        <div class="print-area">
            <div class="print-card card shadow-sm mb-4">
                <!-- Header with Logo -->
                <div class="print-card-header">
                    <div class="logo">
                        🍀 GastroMap
                    </div>
                    <div style="color: rgba(255,255,255,0.9); font-size: 0.8rem; margin-top: 5px;">
                        Hệ thống Truy xuất Nguồn gốc
                    </div>
                </div>
                
                <!-- QR Code Body -->
                <div class="print-card-body">
                    <div class="print-qr-frame">
                        <img src="${pageContext.request.contextPath}/qr-image?id=${qrCode.id}" 
                             alt="QR Code: ${qrCode.qrValue}"
                             width="200" height="200"
                             style="image-rendering: pixelated;">
                    </div>
                    
                    <div class="print-qr-code">${qrCode.qrValue}</div>
                    
                    <c:if test="${not empty product}">
                        <div class="print-product-name">
                            <strong>${product.name}</strong>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty batch}">
                        <div class="print-batch-code">
                            Lô: ${batch.batchCode}
                        </div>
                    </c:if>
                </div>
                
                <!-- Footer -->
                <div class="print-card-footer">
                    <p class="print-scan-text mb-1">
                        <i class="bi bi-phone"></i> Quét mã để xem nguồn gốc
                    </p>
                    <p class="print-footer-text">
                        gastromap.vn | Nông sản sạch - Nguồn gốc rõ ràng
                    </p>
                </div>
            </div>
        </div>
        
        <!-- Action Buttons - No Print -->
        <div class="no-print">
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-body">
                    <div class="d-flex gap-2 justify-content-center mb-3">
                        <button class="btn btn-primary" onclick="window.print()">
                            <i class="bi bi-printer me-1"></i> In mã QR
                        </button>
                        <a href="${pageContext.request.contextPath}/qr-image?id=${qrCode.id}" 
                           download="QR_${qrCode.qrValue}.png"
                           class="btn btn-success">
                            <i class="bi bi-download me-1"></i> Tải xuống
                        </a>
                    </div>
                    
                    <div class="text-center text-muted small">
                        <div class="mb-1">
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
                        </div>
                        <div>Ngày tạo: <fmt:formatDate value="${qrCode.createdAt}" pattern="dd/MM/yyyy HH:mm"/></div>
                    </div>
                </div>
            </div>
            
            <!-- Scan Instructions -->
            <div class="card shadow-sm border-0 bg-light">
                <div class="card-body">
                    <h6 class="fw-bold mb-2"><i class="bi bi-phone me-2"></i>Hướng dẫn quét</h6>
                    <ol class="small mb-0 ps-3">
                        <li>Mở ứng dụng Camera hoặc quét QR trên điện thoại</li>
                        <li>Hướng camera vào mã QR ở trên</li>
                        <li>Nhấn vào link hiện ra để xem thông tin truy xuất</li>
                    </ol>
                </div>
            </div>
        </div>
    </div>
    
    <!-- Right Column - Linked Information (No Print) -->
    <div class="col-md-7 no-print">
        <h5 class="fw-bold mb-3"><i class="bi bi-link-45deg me-2"></i>Thông tin liên kết</h5>
        
        <c:if test="${not empty batch}">
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-header bg-light">
                    <h6 class="mb-0 fw-bold"><i class="bi bi-box-seam me-2"></i>Lô sản xuất</h6>
                </div>
                <div class="card-body">
                    <p class="mb-1">
                        <strong>Mã lô:</strong> 
                        <a href="${pageContext.request.contextPath}/production-batches?action=view&id=${batch.id}">
                            ${batch.batchCode}
                        </a>
                    </p>
                    <p class="mb-1"><strong>Sản lượng:</strong> ${batch.totalQuantity} ${batch.unit}</p>
                    <p class="mb-1"><strong>Ngày thu hoạch:</strong> <fmt:formatDate value="${batch.harvestDate}" pattern="dd/MM/yyyy"/></p>
                    <c:if test="${not empty batch.expiryDate}">
                        <p class="mb-0"><strong>Hạn sử dụng:</strong> <fmt:formatDate value="${batch.expiryDate}" pattern="dd/MM/yyyy"/></p>
                    </c:if>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty product}">
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-header bg-light">
                    <h6 class="mb-0 fw-bold"><i class="bi bi-basket me-2"></i>Nông sản</h6>
                </div>
                <div class="card-body">
                    <p class="mb-1"><strong>Tên:</strong> ${product.name}</p>
                    <p class="mb-0"><strong>Đơn vị:</strong> ${product.unit}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty member}">
            <div class="card shadow-sm border-0 mb-3">
                <div class="card-header bg-light">
                    <h6 class="mb-0 fw-bold"><i class="bi bi-person me-2"></i>Thành viên sản xuất</h6>
                </div>
                <div class="card-body">
                    <p class="mb-1"><strong>Họ tên:</strong> ${member.full_name}</p>
                    <c:if test="${not empty member.phone}">
                        <p class="mb-1"><strong>Điện thoại:</strong> ${member.phone}</p>
                    </c:if>
                    <c:if test="${not empty member.address}">
                        <p class="mb-0"><strong>Địa chỉ:</strong> ${member.address}</p>
                    </c:if>
                </div>
            </div>
        </c:if>
    </div>
</div>

<div class="mt-3 no-print">
    <a href="${pageContext.request.contextPath}/qr-codes" class="btn btn-secondary">
        <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách
    </a>
</div>

<%@include file="../../common/footer.jsp" %>
