<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết Vật tư - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <style>
            .material-img-box {
                background-color: #e9ecef;
                height: 200px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 8px 8px 0 0;
                overflow: hidden; /* THÊM */
            }

            .material-img-box img {
                max-width: 100%;   /* THÊM */
                max-height: 100%;  /* THÊM */
                object-fit: contain; /* THÊM (hoặc cover) */
            }

            .stat-label {
                font-size: 0.9rem;
                color: #6c757d;
            }
            .stat-value {
                font-size: 1.1rem;
                font-weight: 600;
            }
            /* Style cho Badge Hợp đồng bán (Member) */
            .contract-badge-out {
                cursor: pointer;
                text-decoration: none;
                background-color: #0d6efd; /* Blue */
                color: white;
                transition: all 0.2s;
            }
            /* Style cho Badge Hợp đồng mua (Partner) */
            .contract-badge-in {
                cursor: pointer;
                text-decoration: none;
                background-color: #198754; /* Green */
                color: white;
                transition: all 0.2s;
            }
            .contract-badge-out:hover, .contract-badge-in:hover {
                opacity: 0.9;
                transform: scale(1.05);
                color: white;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>

        <div class="container mt-3">
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="SearchMaterialServlet">Kho vật tư</a></li>
                    <li class="breadcrumb-item active" aria-current="page">${i.name}</li>
                </ol>
            </nav>
        </div>

        <div class="container mb-5">
            <div class="row">

                <div class="col-lg-4 mb-4">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="material-img-box">
                            <img src="${pageContext.request.contextPath}/${material.image}">
                        </div>


                        <div class="card-body">
                            <h4 class="card-title text-primary fw-bold">${material.name}</h4>
                            <p class="text-muted mb-3">Mã: <span class="badge bg-secondary">${material.id}</span></p>

                            <div class="mb-4">
                                <div class="d-flex justify-content-between mb-1">
                                    <span class="fw-bold">Tồn kho hiện tại</span>
                                    <span class="text-success fw-bold">${material.stockQuantity}</span>
                                </div>

                                <div class="progress" style="height: 10px;">
                                    <div class="progress-bar bg-success"
                                         role="progressbar"
                                         style="width: ${material.stockQuantity}%;"
                                         aria-valuenow="${material.stockQuantity}"
                                         aria-valuemin="0"
                                         aria-valuemax="100">
                                    </div>
                                </div>

                                <small class="text-muted">Định mức an toàn: &gt;10 </small>
                            </div>

                            <ul class="list-group list-group-flush mb-4">
                                <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                                    <span class="stat-label">Đơn vị tính</span>
                                    <span class="stat-value">${material.unit}</span>
                                </li>




                            </ul>

<!--                            <div class="d-grid gap-2">
                                <button class="btn btn-primary"><i class="bi bi-pencil-square"></i> Cập nhật</button>
                                <button class="btn btn-outline-danger"><i class="bi bi-trash"></i> Tạm Ngưng</button>
                            </div>-->
                        </div>
                    </div>
                </div>

                <div class="col-lg-8">
                    <div class="card shadow-sm border-0">
                        <div class="card-header bg-white border-bottom-0 pt-3 ps-3">
                            <ul class="nav nav-tabs card-header-tabs" id="materialTab" role="tablist">
                                <li class="nav-item">
                                    <button class="nav-link active" id="info-tab" data-bs-toggle="tab" data-bs-target="#info-pane" type="button">Thông tin chi tiết</button>
                                </li>
                                <li class="nav-item">
                                    <button class="nav-link text-success" id="inbound-tab" data-bs-toggle="tab" data-bs-target="#inbound-pane" type="button">
                                        <i class="bi bi-box-arrow-in-down"></i> Lịch sử nhập hàng
                                    </button>
                                </li>
                                <li class="nav-item">
                                    <button class="nav-link text-danger" id="outbound-tab" data-bs-toggle="tab" data-bs-target="#outbound-pane" type="button">
                                        <i class="bi bi-box-arrow-up"></i> Lịch sử cung ứng
                                    </button>
                                </li>
                            </ul>
                        </div>

                        <div class="card-body">
                            <div class="tab-content" id="materialTabContent">

                                <div class="tab-pane fade show active" id="info-pane" role="tabpanel">
                                    <h5 class="mb-3">Mô tả sản phẩm</h5>
                                    <p>${material.description}</p>

                                    <div class="row mt-4">

                                        <div class="alert alert-light border">
                                            <h6><i class="bi bi-geo-alt"></i> Vị trí kho</h6>
                                            <c:forEach var="inv" items="${inventories}">
                                                <div>
                                                    ${inv.warehouse.name} - ${inv.warehouse.location} : ${inv.quantity} ${inv.unit}
                                                </div>
                                            </c:forEach>
                                        </div>



                                    </div>
                                </div>

                                <div class="tab-pane fade" id="inbound-pane" role="tabpanel">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h5 class="mb-0 text-success">Nhật ký nhập từ Nhà cung cấp</h5>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Ngày nhập</th>
                                                    <th>Phiếu nhập</th>
                                                    <th>Nhà cung cấp</th>
                                                    <th>Hợp đồng nhập</th>
                                                    <th class="text-center">Số lượng</th>
                                                    <th class="text-center">ĐVT</th>
                                                    <th class="text-end">Giá vốn/Đơn vị</th>                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${inboundHistory}">
                                                    <tr>
                                                        <td><fmt:formatDate value="${item.receipt_date}" pattern="dd/MM/yyyy"/></td>
                                                        <td><span class="fw-bold text-dark">${item.receipt_code}</span></td>
                                                        <td>${item.partner_name}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${not empty item.contract_code}">
                                                                    <a href="${pageContext.request.contextPath}/contractManager" class="badge contract-badge-in p-2">
                                                                        <i class="bi bi-file-earmark-check"></i> ${item.contract_code}
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge bg-secondary bg-opacity-25 text-dark border small">Mua lẻ</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class="text-center text-success fw-bold">+ ${item.quantity}</td>
                                                        <td class="text-center text-muted">${item.unit}</td> 
                                                        <td class="text-end"><fmt:formatNumber value="${item.unit_price}" pattern="#,###"/> đ</td>                                                    </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty inboundHistory}">
                                                    <tr><td colspan="6" class="text-center text-muted">Chưa có lịch sử nhập hàng cho vật tư này.</td></tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <div class="tab-pane fade" id="outbound-pane" role="tabpanel">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h5 class="mb-0 text-danger">Nhật ký cung ứng cho Thành viên</h5>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Ngày</th>
                                                    <th>Mã cung ứng</th>
                                                    <th>Thành viên</th>
                                                    <th>Hợp đồng</th>
                                                    <th class="text-center">Số lượng</th>
                                                    <th class="text-center">ĐVT</th> 
                                                    <th class="text-end">Giá bán</th>                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="item" items="${outboundHistory}">
                                                    <tr>
                                                        <td><fmt:formatDate value="${item.supply_date}" pattern="dd/MM/yyyy"/></td>
                                                        <td><span class="fw-bold text-dark">${item.supply_code}</span></td>
                                                        <td>${item.member_name}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${not empty item.contract_code}">
                                                                    <a href="${pageContext.request.contextPath}/contractManager" class="badge contract-badge-out p-2">
                                                                        <i class="bi bi-file-earmark-text"></i> ${item.contract_code}
                                                                    </a>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge bg-secondary bg-opacity-25 text-dark border small">Cấp lẻ</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td class="text-center text-danger fw-bold">- ${item.quantity}</td>
                                                        <td class="text-center text-muted">${item.unit}</td> 
                                                        <td class="text-end"><fmt:formatNumber value="${item.unit_price}" pattern="#,###"/> đ</td>                                                    </tr>
                                                    </c:forEach>
                                                    <c:if test="${empty outboundHistory}">
                                                    <tr><td colspan="6" class="text-center text-muted">Chưa có lịch sử cung ứng cho vật tư này.</td></tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>