<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách Nhà cung cấp - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>
         <div class="mb-3">
                        <a href="${pageContext.request.contextPath}/SearchMaterialServlet" class="text-decoration-none text-secondary small">
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách vật tư
                        </a>
                    </div>
        <div class="container mt-4 mb-5">
            <div class="d-flex justify-content-between align-items-center mb-4 pb-3 border-bottom">
                <h3 class="mb-0 fw-bold text-primary">
                    <i class="bi bi-truck me-2"></i>DANH SÁCH NHÀ CUNG CẤP
                </h3>
                <a href="CreatePartnerServlet" class="btn btn-success shadow-sm">
                    <i class="bi bi-plus-circle me-1"></i> Thêm nhà cung cấp
                </a>
            </div>

            <div class="card shadow-sm border-0">
                <div class="table-responsive">
                    <div class="card shadow-sm border-0">
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th class="ps-3">ID</th>
                                        <th>Tên nhà cung cấp</th>
                                        <th>Liên hệ</th>
                                        <th>Địa chỉ</th>
                                        <th>Ghi chú</th>
                                        <th class="text-end">Nợ hiện tại (HTX nợ)</th>
                                        <th class="text-center">Thao tác</th> </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="p" items="${partners}">
                                        <tr>
                                            <td class="ps-3 text-muted">${p.id}</td>
                                            <td>
                                                <div class="fw-bold text-dark">${p.name}</div>
                                                <small class="text-muted">MST: ${p.tax_code}</small>
                                            </td>
                                            <td>
                                                <div><i class="bi bi-telephone me-1"></i> ${p.phone}</div>
                                            </td>
                                            <td class="small text-secondary">${p.address}</td>
                                            <td class="small text-muted italic">
                                                <c:out value="${p.note}" default="---" />
                                            </td>
                                            <td class="text-end fw-bold text-danger">
                                                <fmt:formatNumber value="${p.debt}" pattern="#,###"/> đ
                                            </td>
                                            <td class="text-center">
                                                <div class="btn-group shadow-sm">
                                                    <button type="button" 
                                                            class="btn btn-sm btn-outline-info" 
                                                            onclick="handleHistory(${p.id}, ${p.debt}, '${p.name}')"
                                                            title="Lịch sử giao dịch">
                                                        <i class="bi bi-journal-text"></i>
                                                    </button>

                                                    <a href="EditPartnerServlet?id=${p.id}" class="btn btn-sm btn-outline-warning" title="Sửa thông tin">
                                                        <i class="bi bi-pencil"></i>
                                                    </a>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty partners}">
                                        <tr>
                                            <td colspan="7" class="text-center py-5 text-muted">Chưa có dữ liệu nhà cung cấp.</td>
                                        </tr>
                                    </c:if>
                                <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>


        </div>
        <script>
                                                                function handleHistory(partnerId, debtAmount, partnerName) {
                                                                    // Chuyển giá trị nợ sang số để so sánh (đề phòng trường hợp nợ là chuỗi)
                                                                    const debt = parseFloat(debtAmount);

                                                                    if (debt <= 0 || isNaN(debt)) {
                                                                        // Nếu không có nợ, hiện thông báo SweetAlert2
                                                                        Swal.fire({
                                                                            title: 'Thông báo',
                                                                            html: 'Nhà cung cấp <b>' + partnerName + '</b> hiện chưa có giao dịch phát sinh nợ.',
                                                                            icon: 'info',
                                                                            confirmButtonText: 'Đã rõ',
                                                                            confirmButtonColor: '#0dcaf0'
                                                                        });
                                                                    } else {
                                                                        // Nếu có nợ, điều hướng đến Servlet lịch sử như bình thường
                                                                        window.location.href = "PartnerDebtDetailServlet?partnerId=" + partnerId;
                                                                    }
                                                                }
        </script>
    </body>
</html>