<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid py-4">
    <div class="row justify-content-center">
        <div class="col-md-10">
            
            <%-- PHẦN HIỂN THỊ THÔNG BÁO (SUCCESS/ERROR) --%>
            <c:if test="${not empty sessionScope.success_msg}">
                <div class="alert alert-success alert-dismissible fade show shadow-sm border-0 mb-3" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> ${sessionScope.success_msg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="success_msg" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.error_msg}">
                <div class="alert alert-danger alert-dismissible fade show shadow-sm border-0 mb-3" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i> ${sessionScope.error_msg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="error_msg" scope="session" />
            </c:if>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h5 class="m-0 fw-bold text-success">
                        <i class="bi bi-calendar-event-fill me-2"></i>Chi tiết cuộc họp
                    </h5>
                    <span class="badge bg-success">Đang diễn ra</span>
                </div>
                <div class="card-body p-4">
                    <div class="row">
                        <div class="col-md-8">
                            <h2 class="fw-bold text-dark">${meeting.title}</h2>
                            <p class="text-muted fs-5">${meeting.description}</p>
                            
                            <div class="mt-4">
                                <div class="d-flex align-items-center mb-3">
                                    <i class="bi bi-clock text-success me-3 fs-4"></i>
                                    <div>
                                        <div class="fw-bold">Thời gian</div>
                                        <div class="text-secondary">${meeting.meeting_date}</div>
                                    </div>
                                </div>
                                <div class="d-flex align-items-center">
                                    <i class="bi bi-geo-alt text-success me-3 fs-4"></i>
                                    <div>
                                        <div class="fw-bold">Địa điểm</div>
                                        <div class="text-secondary">${meeting.location}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <%-- SIDEBAR: TẠO BIỂU QUYẾT --%>
                        <div class="col-md-4 border-start ps-4">
                            <div class="bg-light p-3 rounded shadow-sm border">
                                <h6 class="fw-bold mb-3 text-dark">
                                    <i class="bi bi-plus-circle-fill me-2 text-success"></i>Tạo biểu quyết mới
                                </h6>
                                <form action="voteControl" method="post">
                                    <input type="hidden" name="action" value="createVote">
                                    <input type="hidden" name="meeting_id" value="${meeting.id}">
                                    
                                    <div class="mb-3">
                                        <label class="form-label small fw-bold">Nội dung câu hỏi</label>
                                        <input type="text" name="question" class="form-control form-control-sm" placeholder="Ví dụ: Bạn có đồng ý...?" required>
                                    </div>

                                    <div id="options-container" class="mb-2">
                                        <label class="form-label small fw-bold">Các lựa chọn</label>
                                        <div class="input-group mb-2 shadow-sm">
                                            <input type="text" name="options" class="form-control form-control-sm" placeholder="Lựa chọn 1" required>
                                        </div>
                                        <div class="input-group mb-2 shadow-sm">
                                            <input type="text" name="options" class="form-control form-control-sm" placeholder="Lựa chọn 2" required>
                                        </div>
                                    </div>
                                    
                                    <button type="button" onclick="addOptionField()" class="btn btn-outline-secondary btn-sm mb-3 w-100 border-dashed">
                                        <i class="bi bi-plus-lg me-1"></i> Thêm lựa chọn
                                    </button>

                                    <button type="submit" class="btn btn-success btn-sm w-100 shadow-sm fw-bold">
                                        <i class="bi bi-megaphone me-2"></i>ĐĂNG BIỂU QUYẾT
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <%-- DANH SÁCH BIỂU QUYẾT --%>
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3">
                    <h5 class="m-0 fw-bold text-primary">
                        <i class="bi bi-check2-square me-2"></i>Danh sách biểu quyết thời gian thực
                    </h5>
                </div>
                <div class="card-body p-4" id="vote-container">
                    <c:if test="${empty listQuestions}">
                        <div class="text-center py-5">
                            <i class="bi bi-inbox text-muted fs-1 opacity-25"></i>
                            <p class="text-muted mt-2">Chưa có biểu quyết nào được tạo cho cuộc họp này.</p>
                        </div>
                    </c:if>
                    
                    <c:forEach items="${listQuestions}" var="q">
                        <div class="border rounded p-4 mb-4 bg-white shadow-sm border-start border-4 border-primary">
                            <div class="d-flex justify-content-between align-items-start mb-3">
                                <h5 class="fw-bold text-dark m-0">${q.question_text}</h5>
                                <span class="badge rounded-pill bg-primary px-3">Poll</span>
                            </div>
                            
                            <%-- FORM BÌNH CHỌN --%>
                            <form action="voteControl" method="post" class="d-flex flex-wrap gap-2">
                                <input type="hidden" name="action" value="submitVote">
                                <input type="hidden" name="meeting_id" value="${meeting.id}">
                                
                                <%-- DÒNG QUAN TRỌNG: Gửi ID câu hỏi để DB không bị NULL và chặn được vote trùng --%>
                                <input type="hidden" name="question_id" value="${q.id}">
                                
                                <c:forEach items="${q.options}" var="opt">
                                    <button type="submit" name="option_id" value="${opt.id}" class="btn btn-outline-success px-4 py-2 rounded-pill d-flex align-items-center">
                                        <span class="me-2">${opt.option_text}</span>
                                        <span class="badge bg-success rounded-circle">${opt.vote_count}</span>
                                    </button>
                                </c:forEach>
                            </form>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="text-center mt-5">
                <a href="meetingManager?service=list" class="btn btn-link text-decoration-none text-muted">
                    <i class="bi bi-arrow-left me-2"></i>Quay lại danh sách cuộc họp
                </a>
            </div>
        </div>
    </div>
</div>

<script>
    function addOptionField() {
        const container = document.getElementById('options-container');
        const div = document.createElement('div');
        div.className = 'input-group mb-2 shadow-sm animate__animated animate__fadeIn';
        div.innerHTML = `
            <input type="text" name="options" class="form-control form-control-sm" placeholder="Nhập lựa chọn tiếp theo..." required>
            <button class="btn btn-outline-danger btn-sm" type="button" onclick="this.parentElement.remove()">
                <i class="bi bi-trash"></i>
            </button>
        `;
        container.appendChild(div);
        div.querySelector('input').focus();
    }

    if (typeof socket !== 'undefined') {
        var currentOnMessage = socket.onmessage;
        socket.onmessage = function(event) {
            if (currentOnMessage) currentOnMessage(event);

            if (event.data.includes("VOTE_NEW") || 
                event.data.includes("VOTE_SUBMITTED") || 
                event.data.includes("MEETING_NEW")) {
                
                console.log("Thay đổi phát hiện, đang làm mới...");
                location.reload();
            }
        };
    }
</script>

<style>
    .border-dashed { border-style: dashed !important; border-width: 2px; }
    .badge.rounded-circle {
        width: 24px; height: 24px;
        display: flex; align-items: center; justify-content: center;
        padding: 0; font-size: 0.75rem;
    }
    .vote-item-container:hover { border-color: #0d6efd !important; transition: 0.3s; }
</style>

<%-- Gọi Footer --%>
<%@include file="common/footer.jsp" %>