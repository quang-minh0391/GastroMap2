<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-11">
            
            <%-- Hiển thị thông báo từ Session --%>
            <c:if test="${not empty sessionScope.success_msg}">
                <div class="alert alert-success alert-dismissible fade show shadow-sm border-0 mb-4" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.success_msg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="success_msg" scope="session" />
            </c:if>

            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center border-bottom">
                    <h5 class="m-0 fw-bold text-success">
                        <i class="bi bi-calendar3 me-2"></i>Danh sách cuộc họp HTX
                    </h5>
                    <button type="button" class="btn btn-success shadow-sm px-4 fw-bold" data-bs-toggle="modal" data-bs-target="#addMeetingModal">
                        <i class="bi bi-plus-circle me-2"></i>Tạo cuộc họp mới
                    </button>
                </div>
                
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th class="ps-4 py-3">Tên cuộc họp</th>
                                    <th>Thời gian</th>
                                    <th>Địa điểm</th>
                                    <%-- Cột Trạng thái đã được lược bỏ --%>
                                    <th class="text-center">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listMeetings}" var="m">
                                    <tr>
                                        <td class="ps-4">
                                            <div class="fw-bold text-dark">${m.title}</div>
                                            <small class="text-muted text-truncate d-block" style="max-width: 400px;">
                                                ${m.description}
                                            </small>
                                        </td>
                                        <td class="text-secondary">
                                            <i class="bi bi-clock me-1"></i> ${m.meeting_date}
                                        </td>
                                        <td>
                                            <i class="bi bi-geo-alt me-1 text-danger"></i> ${m.location}
                                        </td>
                                        <td class="text-center">
                                            <a href="meetingManager?service=viewDetail&id=${m.id}" class="btn btn-sm btn-primary px-4 rounded-pill shadow-sm">
                                                <i class="bi bi-box-arrow-in-right me-1"></i> Vào họp
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                
                                <c:if test="${empty listMeetings}">
                                    <tr>
                                        <%-- Sửa colspan từ 5 xuống 4 vì đã bỏ 1 cột --%>
                                        <td colspan="4" class="text-center py-5 text-muted">
                                            <i class="bi bi-inbox fs-1 d-block mb-2 opacity-25"></i>
                                            Hiện tại chưa có cuộc họp nào được lên lịch.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%-- Modal Thêm cuộc họp mới --%>
<div class="modal fade" id="addMeetingModal" tabindex="-1" aria-labelledby="addMeetingModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content border-0 shadow-lg">
            <div class="modal-header bg-success text-white py-3">
                <h5 class="modal-title fw-bold" id="addMeetingModalLabel">
                    <i class="bi bi-plus-circle me-2"></i>Thiết lập cuộc họp mới
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="meetingManager" method="POST">
                <input type="hidden" name="service" value="addMeeting">
                
                <div class="modal-body p-4">
                    <div class="mb-3">
                        <label class="form-label fw-bold text-dark">Tiêu đề cuộc họp</label>
                        <input type="text" name="title" class="form-control" placeholder="Ví dụ: Đại hội thường niên" required>
                    </div>
                    
                    <div class="mb-3">
                        <label class="form-label fw-bold text-dark">Nội dung tóm tắt</label>
                        <textarea name="description" class="form-control" rows="3" placeholder="Ghi chú nội dung chính..." required></textarea>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold text-dark">Ngày giờ họp</label>
                            <input type="datetime-local" name="meeting_date" class="form-control" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label fw-bold text-dark">Địa điểm</label>
                            <input type="text" name="location" class="form-control" placeholder="Phòng họp..." required>
                        </div>
                    </div>
                </div>
                
                <div class="modal-footer bg-light border-0 py-3">
                    <button type="button" class="btn btn-outline-secondary px-4" data-bs-dismiss="modal">Để sau</button>
                    <button type="submit" class="btn btn-success px-4 fw-bold">
                        <i class="bi bi-send me-2"></i>Lưu và Phát hành
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    if (typeof socket !== 'undefined') {
        socket.onmessage = function(event) {
            if (event.data.includes("MEETING_NEW")) {
                location.reload();
            }
        };
    }
</script>

<%-- Gọi Footer --%>
<%@include file="common/footer.jsp" %>