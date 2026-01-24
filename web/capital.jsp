<%-- 
    Document   : capital
    Sửa đổi: Kết nối với ImageDisplayServlet để hiển thị ảnh từ thư mục FarmerData
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header (Đã bao gồm Sidebar và mở thẻ main-area) --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-11">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="m-0 fw-bold text-success">
                    <i class="bi bi-bank2 me-2"></i>Lịch sử góp vốn: Nông dân #${farmerId}
                </h4>
                <button type="button" class="btn btn-primary px-4 shadow-sm" data-bs-toggle="modal" data-bs-target="#addCapitalModal">
                    <i class="bi bi-plus-circle-fill me-2"></i>Thêm vốn góp
                </button>
            </div>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-3">
                    <form action="capitalManager" method="get" class="row g-2">
                        <input type="hidden" name="service" value="list">
                        <input type="hidden" name="id" value="${farmerId}">
                        <div class="col-md-5">
                            <div class="input-group">
                                <span class="input-group-text bg-white border-end-0 text-muted small">Ngày góp</span>
                                <input type="date" name="searchDate" class="form-control border-start-0" value="${param.searchDate}">
                            </div>
                        </div>
                        <div class="col-md-5">
                            <div class="input-group">
                                <span class="input-group-text bg-white border-end-0"><i class="bi bi-image text-muted"></i></span>
                                <input type="text" name="searchReceipt" class="form-control border-start-0 ps-0" 
                                       placeholder="Tìm theo tên file ảnh (receipt)..." value="${param.searchReceipt}">
                            </div>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-success w-100 shadow-sm">Tìm kiếm</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="card shadow-sm border-0">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="bg-light">
                                <tr>
                                    <th class="ps-4 py-3">Ngày đóng góp</th>
                                    <th>Nội dung đóng góp</th>
                                    <th>Minh chứng (Ảnh)</th>
                                    <th>Ghi chú</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listC}" var="c">
                                    <tr>
                                        <td class="ps-4">
                                            <span class="text-dark fw-medium">${c.contributionDate}</span>
                                        </td>
                                        <td>
                                            <span class="badge bg-info text-dark border">${c.contribution}</span>
                                        </td>
                                        <td>
                                            <a href="displayImage?fname=${c.receiptNumber}" target="_blank">
                                                <img src="displayImage?fname=${c.receiptNumber}" class="rounded border shadow-sm" 
                                                     style="width: 50px; height: 50px; object-fit: cover;"
                                                     onerror="this.src='https://placehold.co/50x50?text=No+Img'">
                                            </a>
                                        </td>
                                        <td class="text-muted small">${c.note}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty listC}">
                                    <tr>
                                        <td colspan="4" class="text-center py-5 text-muted">
                                            <i class="bi bi-clipboard-x fs-1 d-block mb-2"></i>
                                            Chưa có dữ liệu đóng góp vốn cho thành viên này.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="mt-3">
                <a href="memberManager?service=list" class="btn btn-link text-decoration-none p-0 text-muted">
                    <i class="bi bi-arrow-left me-1"></i> Quay lại danh sách thành viên
                </a>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="addCapitalModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <form action="capitalManager" method="post" enctype="multipart/form-data" class="modal-content border-0 shadow">
            <input type="hidden" name="service" value="insert">
            <input type="hidden" name="farmerId" value="${farmerId}">
            
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title"><i class="bi bi-plus-circle me-2"></i>Ghi nhận vốn góp</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            
            <div class="modal-body p-4">
                <div class="mb-3">
                    <label class="form-label fw-bold small">Loại tài sản đóng góp</label>
                    <input type="text" name="contribution" class="form-control" placeholder="Ví dụ: 10.000.000đ, 2 sào đất..." required>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold small">Ngày thực hiện</label>
                    <input type="date" name="date" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold small">Ảnh minh chứng (Phiếu thu/Giấy tờ)</label>
                    <input type="file" name="imageFile" class="form-control" accept="image/*" required>
                </div>
                <div class="mb-0">
                    <label class="form-label fw-bold small">Ghi chú (nếu có)</label>
                    <textarea name="note" class="form-control" rows="3" placeholder="Thông tin chi tiết thêm..."></textarea>
                </div>
            </div>
            
            <div class="modal-footer bg-light border-0">
                <button type="button" class="btn btn-secondary px-4" data-bs-modal="modal">Hủy bỏ</button>
                <button type="submit" class="btn btn-success px-4">Lưu dữ liệu</button>
            </div>
        </form>
    </div>
</div>

<%-- Gọi Footer (Đã bao gồm đóng thẻ main-area và các file JS) --%>
<%@include file="common/footer.jsp" %>