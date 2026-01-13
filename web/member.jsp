<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Gọi Header (Đã bao gồm Sidebar và mở thẻ main-area) --%>
<%@include file="common/header.jsp" %>

<div class="container-fluid">
    <div class="row justify-content-center">
        <div class="col-md-11">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h4 class="m-0 fw-bold text-success">
                    <i class="bi bi-people-fill me-2"></i>Quản lý thành viên HTX
                </h4>
                <a href="memberManager?service=add" class="btn btn-primary px-4 shadow-sm">
                    <i class="bi bi-person-plus-fill me-2"></i>Thêm thành viên
                </a>
            </div>

            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-3">
                    <form action="memberManager" method="get" class="row g-2">
                        <input type="hidden" name="service" value="list">
                        <div class="col-md-10">
                            <div class="input-group">
                                <span class="input-group-text bg-white border-end-0"><i class="bi bi-search text-muted"></i></span>
                                <input type="text" name="searchName" class="form-control border-start-0 ps-0" 
                                       placeholder="Nhập tên thành viên cần tìm..." value="${searchName}">
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
                                    <th class="ps-4 py-3">ID</th>
                                    <th>Thành viên</th>
                                    <th>Tên đăng nhập</th>
                                    <th>Liên hệ</th>
                                    <th>Trạng thái</th>
                                    <th class="text-center pe-4" style="width: 250px;">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${listM}" var="m">
                                    <tr>
                                        <td class="ps-4">${m.id}</td>
                                        <td>
                                            <div class="fw-bold text-dark">${m.full_name}</div>
                                            <small class="text-muted">Ngày tham gia: ${m.joined_date}</small>
                                        </td>
                                        <td><span class="badge bg-light text-dark border">@${m.username}</span></td>
                                        <td>
                                            <div><i class="bi bi-telephone me-1"></i>${m.phone}</div>
                                            <small class="text-muted"><i class="bi bi-envelope me-1"></i>${m.email}</small>
                                        </td>
                                        <td>
                                            <span class="badge ${m.status == 'Active' ? 'bg-success' : 'bg-warning'}">
                                                ${m.status}
                                            </span>
                                        </td>
                                        <td class="text-center pe-4">
                                            <div class="d-flex justify-content-center gap-2">
                                                <a href="capitalManager?service=list&id=${m.id}" 
   class="btn btn-outline-primary btn-sm px-3 shadow-sm"
   title="Xem chi tiết vốn góp">
    <i class="bi bi-cash-stack me-1"></i> Vốn góp
</a>
                                                
                                                <a href="memberManager?service=delete&id=${m.id}" 
                                                   class="btn btn-outline-danger btn-sm px-3 shadow-sm"
                                                   title="Ẩn thành viên này"
                                                   onclick="return confirm('Bạn có chắc chắn muốn ẩn thành viên [${m.full_name}] khỏi danh sách? Dữ liệu vẫn sẽ được lưu trữ trong hệ thống.')">
                                                    <i class="bi bi-trash me-1"></i> Xóa
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty listM}">
                                    <tr>
                                        <td colspan="6" class="text-center py-5 text-muted">
                                            <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                            Không tìm thấy thành viên nào phù hợp.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <nav class="mt-4">
                <ul class="pagination justify-content-center">
                    <c:forEach begin="1" end="${endP}" var="i">
                        <li class="page-item ${tag == i ? 'active' : ''}">
                            <a class="page-link shadow-sm mx-1 rounded" 
                               href="memberManager?service=list&index=${i}&searchName=${searchName}">${i}</a>
                        </li>
                    </c:forEach>
                </ul>
            </nav>
        </div>
    </div>
</div>

<%-- Gọi Footer (Đã bao gồm đóng thẻ main-area và các file JS) --%>
<%@include file="common/footer.jsp" %>