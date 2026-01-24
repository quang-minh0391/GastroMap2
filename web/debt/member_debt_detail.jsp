<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sao kê nợ Nông dân - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
    <div class="container mt-4 mb-5">
        <nav aria-label="breadcrumb"><ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="DebtManagementServlet">Quản lý nợ</a></li>
            <li class="breadcrumb-item active">Sao kê Nông dân</li>
        </ol></nav>

        <div class="card shadow-sm border-0">
            <div class="card-header bg-success text-white py-3">
                <h5 class="fw-bold mb-0"><i class="bi bi-person-badge me-2"></i>CHI TIẾT BIẾN ĐỘNG CÔNG NỢ</h5>
            </div>
            <div class="card-body">
                 <div class="alert alert-info d-flex align-items-center py-2 mb-3" role="alert">
                        <i class="bi bi-info-circle-fill me-2"></i>
                        <div>
                            <small class="me-4"><b>Số dương (+):</b> Nông dân đang nợ HTX</small>
                            <small><b>Số âm (-):</b> HTX đang nợ Nông dân</small>
                        </div>
                    </div>
                <table class="table table-bordered align-middle">
                    <thead class="table-light text-center">
                        <tr>
                            <th>Ngày</th>
                            <th>Mã/Loại</th>
                            <th>Ghi chú nội dung</th>
                            <th class="text-end">Phát sinh tăng</th>
                            <th class="text-end">Phát sinh giảm</th>
                            <th class="text-end">Số dư lũy kế</th>
                            <th>Minh chứng</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${history}">
                            <tr>
                                <td class="small text-center"><fmt:formatDate value="${item.date}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td class="text-center fw-bold text-primary">${item.code}</td>
                                <td>${item.note}</td>
                                
                                <td class="text-end text-danger fw-bold">
                                    <c:if test="${item.entry == 'DEBIT'}">
                                        +<fmt:formatNumber value="${item.amount}" pattern="#,###"/>
                                    </c:if>
                                </td>

                                <td class="text-end text-success fw-bold">
                                    <c:if test="${item.entry == 'CREDIT'}">
                                        -<fmt:formatNumber value="${item.amount}" pattern="#,###"/>
                                    </c:if>
                                </td>

                                <td class="text-end bg-light fw-bold">
                                    <fmt:formatNumber value="${item.balance}" pattern="#,###"/>
                                </td>
                                <td class="text-center">
                                    <c:if test="${not empty item.img}">
                                        <button class="btn btn-sm btn-outline-dark" onclick="viewImg('${pageContext.request.contextPath}/${item.img}')">
                                            <i class="bi bi-image"></i>
                                        </button>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <div class="modal fade" id="imgModal" tabindex="-1"><div class="modal-dialog modal-lg modal-dialog-centered"><div class="modal-content bg-dark">
        <div class="modal-body p-0 text-center"><img id="imgTarget" src="" class="img-fluid"></div>
    </div></div></div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function viewImg(url) {
            document.getElementById('imgTarget').src = url;
            new bootstrap.Modal(document.getElementById('imgModal')).show();
        }
    </script>
</body>
</html>