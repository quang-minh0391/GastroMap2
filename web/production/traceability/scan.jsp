<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="../../common/header.jsp" %>

<div class="mb-4">
    <h2 class="fw-bold">🔍 Truy xuất Nguồn gốc Nông sản</h2>
    <p class="text-muted">Quét mã QR hoặc nhập mã để xem thông tin nguồn gốc sản phẩm</p>
</div>

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card shadow-sm border-0 bg-success text-white mb-4">
            <div class="card-body text-center py-5">
                <div style="font-size: 5rem;">🔍</div>
                <h3 class="mt-3">Truy xuất Nguồn gốc</h3>
                <p class="mb-0">Nhập mã QR để xem thông tin chi tiết về nguồn gốc nông sản</p>
            </div>
        </div>

        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
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

                <form action="${pageContext.request.contextPath}/traceability" method="post">
                    <input type="hidden" name="action" value="lookup">
                    
                    <div class="mb-3">
                        <label for="qrValue" class="form-label fw-bold">📱 Nhập mã QR</label>
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="qrValue" name="qrValue" 
                                   placeholder="VD: QR-1-ABC12345" required autofocus>
                            <button type="submit" class="btn btn-success">🔍 Tra cứu</button>
                        </div>
                        <div class="form-text">Nhập đầy đủ mã QR được in trên bao bì sản phẩm</div>
                    </div>
                </form>

                <hr>

                <div class="text-center">
                    <h6 class="text-muted">Hoặc</h6>
                    <a href="${pageContext.request.contextPath}/traceability?action=history" class="btn btn-outline-secondary">
                        📜 Xem lịch sử quét gần đây
                    </a>
                </div>
            </div>
        </div>

        <div class="card shadow-sm border-0 mt-4">
            <div class="card-body">
                <h5 class="fw-bold">❓ Hướng dẫn sử dụng</h5>
                <ol class="mb-0">
                    <li>Tìm mã QR trên bao bì sản phẩm</li>
                    <li>Nhập mã QR vào ô tìm kiếm ở trên</li>
                    <li>Nhấn "Tra cứu" để xem thông tin nguồn gốc</li>
                    <li>Thông tin bao gồm: Lô sản xuất, Thành viên sản xuất, Ngày thu hoạch, v.v.</li>
                </ol>
            </div>
        </div>
    </div>
</div>

<%@include file="../../common/footer.jsp" %>
