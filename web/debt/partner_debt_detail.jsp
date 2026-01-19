<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đối chiếu công nợ - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <style>
            .table-hover tbody tr:hover {
                background-color: rgba(13, 110, 253, 0.05);
            }
            .badge-receipt {
                background-color: #6c757d;
            }
            .badge-voucher {
                background-color: #0dcaf0;
                color: #000;
            }
            #imgTarget {
                max-height: 80vh;
                object-fit: contain;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>

        <div class="container mt-4 mb-5">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="DebtManagementServlet" class="text-decoration-none">Quản lý nợ</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Chi tiết đối chiếu</li>
                </ol>
            </nav>

            <div class="card shadow-sm border-0">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h5 class="fw-bold mb-0 text-primary">
                        <i class="bi bi-journal-text me-2"></i>LỊCH SỬ GIAO DỊCH & ĐỐI CHIẾU NỢ
                    </h5>
                    <span class="badge bg-secondary">Đối tác ID: ${partnerId}</span>
                </div>

                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered align-middle table-hover">
                            <thead class="table-dark text-center">
                                <tr>
                                    <th style="width: 15%;">Ngày giao dịch</th>
                                    <th style="width: 15%;">Mã chứng từ</th>
                                    <th style="width: 25%;">Nội dung / Ghi chú</th>
                                    <th style="width: 12%;" class="text-end">HTX nợ thêm</th>
                                    <th style="width: 12%;" class="text-end">HTX trả nợ</th>
                                    <th style="width: 12%;" class="text-end">Số dư cuối</th>
                                    <th style="width: 9%;">Minh chứng</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${history}">
                                    <tr>
                                        <td class="text-center small"><fmt:formatDate value="${item.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td class="fw-bold text-center text-primary">${item.code}</td>
                                        <td>
                                            <span class="badge ${item.type == 'MATERIAL_RECEIPT' ? 'badge-receipt' : 'badge-voucher'} mb-1">
                                                ${item.type == 'MATERIAL_RECEIPT' ? 'Nhập vật tư' : 'Thanh toán lẻ'}
                                            </span>
                                            <div class="small text-muted">${item.note}</div>
                                        </td>

                                        <%-- Cột 4: HTX NỢ THÊM (Chỉ hiện các loại phiếu Nhập hàng) --%>
                                        <td class="text-end text-danger fw-bold">
                                            <c:if test="${item.type == 'MATERIAL_RECEIPT' || item.type == 'FARM_PURCHASE'}">
                                                +<fmt:formatNumber value="${item.amount}" pattern="#,###"/>
                                            </c:if>
                                        </td>

                                        <%-- Cột 5: HTX TRẢ NỢ (Chỉ hiện các loại phiếu Chi tiền/Thanh toán) --%>
                                        <td class="text-end text-success fw-bold">
                                            <c:if test="${item.type == 'CASH_PAYMENT' || item.type == 'CASH_RECEIPT'}">
                                                -<fmt:formatNumber value="${item.amount}" pattern="#,###"/>
                                            </c:if>
                                        </td>

                                        <td class="text-end bg-light fw-bold">
                                            <fmt:formatNumber value="${item.balance}" pattern="#,###"/>
                                        </td>
                                        <td class="text-center">
                                            <c:if test="${not empty item.img}">
                                                <button class="btn btn-sm btn-outline-info" onclick="viewImg('${pageContext.request.contextPath}/${item.img}')">
                                                    <i class="bi bi-image"></i> Xem
                                                </button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty history}">
                                    <tr>
                                        <td colspan="7" class="text-center py-4 text-muted">Chưa có giao dịch nào được ghi nhận.</td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="card-footer bg-white text-end">
                    <button class="btn btn-secondary btn-sm" onclick="window.print()">
                        <i class="bi bi-printer me-1"></i> In sao kê
                    </button>
                </div>
            </div>
        </div>

        <div class="modal fade" id="imgModal" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content border-0 shadow-lg bg-dark">
                    <div class="modal-header border-0">
                        <h6 class="modal-title text-white small">Hình ảnh minh chứng giao dịch</h6>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-0 text-center">
                        <img id="imgTarget" src="" class="img-fluid rounded-bottom">
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                        function viewImg(url) {
                            // Gán đường dẫn đã bao gồm context path vào ảnh
                            document.getElementById('imgTarget').src = url;
                            var myModal = new bootstrap.Modal(document.getElementById('imgModal'));
                            myModal.show();
                        }
        </script>
    </body>
</html>