<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Gọi Header --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-11">
            <%-- Tiêu đề trang mới --%>
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="m-0 fw-bold text-success">
                    <i class="bi bi-file-earmark-text me-2"></i>
                    Quản lý danh sách Hợp đồng
                </h4>
                <button type="button" class="btn btn-primary px-4 shadow-sm" data-bs-toggle="modal" data-bs-target="#addContractModal">
                    <i class="bi bi-plus-circle-fill me-2"></i>Thêm hợp đồng
                </button>
            </div>

            <%-- Form tìm kiếm --%>
            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-3">
                    <form action="contractManager" method="get" class="row g-2">
                        <input type="hidden" name="service" value="list">
                        <div class="col-md-5">
                            <div class="input-group">
                                <span class="input-group-text bg-white border-end-0 text-muted small">Mã HĐ</span>
                                <input type="text" name="searchCode" class="form-control border-start-0" 
                                       placeholder="Tìm theo mã hợp đồng..." value="${param.searchCode}">
                            </div>
                        </div>
                        <div class="col-md-5">
                            <div class="input-group">
                                <span class="input-group-text bg-white border-end-0 text-muted small">Loại HĐ</span>
                                <input type="text" name="searchType" class="form-control border-start-0" 
                                       placeholder="Tìm theo loại hợp đồng..." value="${param.searchType}">
                            </div>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-success w-100 shadow-sm">Tìm kiếm</button>
                        </div>
                    </form>
                </div>
            </div>

            <%-- Bảng danh sách --%>
            <div class="card shadow-sm border-0">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="bg-light">
                                <tr>
                                    <th class="ps-4 py-3">Mã HĐ</th>
                                    <th>Loại HĐ</th>
                                    <th>Ngày ký - Hết hạn</th>
                                    <th>Tổng giá trị</th>
                                    <th>Trạng thái</th>
                                    <th>Minh chứng</th>
                                    <th class="text-center">Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listContract}" var="con">
                                    <tr>
                                        <td class="ps-4">
                                            <span class="text-dark fw-bold">${con.contractCode}</span>
                                        </td>
                                        <td>
                                            <span class="badge bg-info text-dark border">${con.contractType}</span>
                                        </td>
                                        <td>
                                            <small class="d-block text-muted">Ký: ${con.signingDate}</small>
                                            <small class="d-block text-danger">Hết: ${con.expiryDate}</small>
                                        </td>
                                        <td>
                                            <span class="fw-medium text-success">
                                                <fmt:formatNumber value="${con.totalValue}" pattern="#,##0"/> đ
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${con.status == 'Hiệu lực'}">
                                                    <span class="badge bg-success">Hiệu lực</span>
                                                </c:when>
                                                <c:when test="${con.status == 'Hết hạn'}">
                                                    <span class="badge bg-danger">Hết hạn</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${con.status}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <a href="displayImage?fname=${con.documentPath}" target="_blank">
                                                <img src="displayImage?fname=${con.documentPath}" 
                                                     class="rounded border shadow-sm" 
                                                     style="width: 45px; height: 45px; object-fit: cover;"
                                                     onerror="this.src='https://placehold.co/45x45?text=File'">
                                            </a>
                                        </td>
                                        <td class="text-center">
                                            <button type="button" class="btn btn-sm btn-outline-primary shadow-sm" 
                                                    data-bs-toggle="modal" data-bs-target="#editContractModal"
                                                    onclick="loadEditData('${con.id}', '${con.signingDate}', '${con.expiryDate}', '${con.status}')">
                                                <i class="bi bi-pencil-square me-1"></i> Sửa
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty listContract}">
                                    <tr>
                                        <td colspan="7" class="text-center py-5 text-muted">
                                            <i class="bi bi-file-earmark-x fs-1 d-block mb-2"></i>
                                            Chưa có dữ liệu hợp đồng nào được tìm thấy.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <div class="mt-3">
                <a href="javascript:history.back()" class="text-decoration-none text-muted small">
                    <i class="bi bi-arrow-left me-1"></i> Quay lại trang trước
                </a>
            </div>
        </div>
    </div>
</div>

<%-- Modal Thêm mới --%>
<div class="modal fade" id="addContractModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <form action="contractManager" method="post" enctype="multipart/form-data" class="modal-content border-0 shadow">
            <input type="hidden" name="service" value="insert">
            <div class="modal-header bg-success text-white">
                <h5 class="modal-title"><i class="bi bi-file-earmark-plus me-2"></i>Thêm mới hợp đồng</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold small">Mã hợp đồng</label>
                        <input type="text" name="contractCode" class="form-control" placeholder="Ví dụ: HĐ-001" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold small">Loại hợp đồng</label>
                        <input type="text" name="contractType" class="form-control" placeholder="Góp vốn, thuê đất..." required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold small">Ngày ký kết</label>
                        <input type="date" name="signingDate" class="form-control" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold small">Ngày hết hạn</label>
                        <input type="date" name="expiryDate" class="form-control" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold small">Tổng giá trị (VNĐ)</label>
                        <input type="number" name="totalValue" class="form-control" placeholder="0" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold small">Trạng thái</label>
                        <select name="status" class="form-select">
                            <option value="Hiệu lực">Hiệu lực</option>
                            <option value="Chờ ký">Chờ ký</option>
                            <option value="Tạm ngưng">Tạm ngưng</option>
                        </select>
                    </div>
                </div>
                <div class="mb-0">
                    <label class="form-label fw-bold small">Ảnh minh chứng</label>
                    <input type="file" name="documentFile" class="form-control" accept="image/*" required>
                </div>
            </div>
            <div class="modal-footer bg-light border-0">
                <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-success px-4">Lưu lại</button>
            </div>
        </form>
    </div>
</div>

<%-- Modal Chỉnh sửa --%>
<div class="modal fade" id="editContractModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <form action="contractManager" method="post" class="modal-content border-0 shadow">
            <input type="hidden" name="service" value="update">
            <input type="hidden" name="id" id="edit_id">
            
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title"><i class="bi bi-pencil-square me-2"></i>Cập nhật Hợp đồng</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body p-4">
                <div class="mb-3">
                    <label class="form-label fw-bold small">Ngày ký mới</label>
                    <input type="date" name="signingDate" id="edit_signingDate" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold small">Ngày hết hạn mới</label>
                    <input type="date" name="expiryDate" id="edit_expiryDate" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label fw-bold small">Trạng thái hiện tại</label>
                    <select name="status" id="edit_status" class="form-select">
                        <option value="Hiệu lực">Hiệu lực</option>
                        <option value="Hết hạn">Hết hạn</option>
                        <option value="Tạm ngưng">Tạm ngưng</option>
                        <option value="Chờ ký">Chờ ký</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer bg-light border-0">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-primary px-4">Lưu thay đổi</button>
            </div>
        </form>
    </div>
</div>

<%-- Script xử lý gán dữ liệu vào Modal --%>
<script>
    function loadEditData(id, signDate, expiryDate, status) {
        document.getElementById('edit_id').value = id;
        document.getElementById('edit_signingDate').value = signDate;
        document.getElementById('edit_expiryDate').value = expiryDate;
        document.getElementById('edit_status').value = status;
    }
</script>

<%@include file="common/footer.jsp" %>