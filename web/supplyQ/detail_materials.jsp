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
                    <li class="breadcrumb-item"><a href="#">Trang chủ</a></li>
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
                                <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                                    <span class="stat-label">Giá vốn</span>
                                    <span class="stat-value text-primary">
                                        <fmt:formatNumber value="${material.unitPrice}" type="number" groupingUsed="true" />
                                    </span>
                                </li>

                                <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                                    <span class="stat-label">Giá cung ứng</span>
                                    <span class="stat-value text-primary">
                                        <fmt:formatNumber value="${material.salePrice}" type="number" groupingUsed="true" />
                                    </span>
                                </li>

                                <li class="list-group-item d-flex justify-content-between align-items-center px-0">
                                    <span class="stat-label">Tổng giá trị tồn</span>
                                    <span class="stat-value text-danger">
                                        <fmt:formatNumber value="${material.unitPrice * material.stockQuantity}" type="number" groupingUsed="true" />
                                    </span>
                                </li>

                            </ul>

                            <div class="d-grid gap-2">
                                <button class="btn btn-primary"><i class="bi bi-pencil-square"></i> Cập nhật</button>
                                <button class="btn btn-outline-danger"><i class="bi bi-trash"></i> Xóa</button>
                            </div>
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
                                        <div class="btn-group">
                                            <button class="btn btn-sm btn-outline-success">Xuất Excel</button>
                                        </div>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Ngày nhập</th>
                                                    <th>Phiếu nhập</th>
                                                    <th>Nhà cung cấp (Partner)</th>
                                                    <th>Hợp đồng nhập (Contract)</th>
                                                    <th class="text-center">Số lượng</th>
                                                    <th class="text-end">Giá vốn/Đơn vị</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>10/05/2024</td>
                                                    <td><a href="#" class="fw-bold text-dark">PN-2024-001</a></td>
                                                    <td>Cty VTNN Bình Điền</td>
                                                    <td>
                                                        <a href="contract-detail.jsp?id=5" class="badge contract-badge-in p-2">
                                                            <i class="bi bi-file-earmark-check"></i> HĐ-MUA-BD01
                                                        </a>
                                                        <div class="small text-muted mt-1">HĐ Cung ứng vật tư 2024</div>
                                                    </td>
                                                    <td class="text-center text-success fw-bold">+ 200</td>
                                                    <td class="text-end">820,000 đ</td>
                                                </tr>

                                                <tr>
                                                    <td>01/04/2024</td>
                                                    <td><a href="#" class="fw-bold text-dark">PN-2024-005</a></td>
                                                    <td>Đại lý Cấp 1 An Giang</td>
                                                    <td>
                                                        <span class="badge bg-secondary bg-opacity-25 text-dark border">
                                                            <i class="bi bi-shop"></i> Mua lẻ/Ngoài HĐ
                                                        </span>
                                                    </td>
                                                    <td class="text-center text-success fw-bold">+ 50</td>
                                                    <td class="text-end">850,000 đ</td>
                                                </tr>

                                                <tr>
                                                    <td>15/03/2024</td>
                                                    <td><a href="#" class="fw-bold text-dark">PN-2024-008</a></td>
                                                    <td>Tập đoàn Lộc Trời</td>
                                                    <td>
                                                        <a href="contract-detail.jsp?id=8" class="badge contract-badge-in p-2">
                                                            <i class="bi bi-file-earmark-check"></i> HĐ-LT-2024
                                                        </a>
                                                    </td>
                                                    <td class="text-center text-success fw-bold">+ 100</td>
                                                    <td class="text-end">830,000 đ</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>

                                <div class="tab-pane fade" id="outbound-pane" role="tabpanel">
                                    <div class="d-flex justify-content-between align-items-center mb-3">
                                        <h5 class="mb-0 text-danger">Nhật ký cung ứng cho Thành viên</h5>
                                        <div class="btn-group">
                                            <button class="btn btn-sm btn-outline-secondary">Tất cả</button>
                                            <button class="btn btn-sm btn-outline-secondary">Có hợp đồng</button>
                                        </div>
                                    </div>

                                    <div class="table-responsive">
                                        <table class="table table-hover align-middle">
                                            <thead class="table-light">
                                                <tr>
                                                    <th>Ngày</th>
                                                    <th>Đơn hàng (SO)</th>
                                                    <th>Thành viên (Member)</th>
                                                    <th>Hợp đồng bao tiêu (Contract)</th>
                                                    <th class="text-center">Số lượng</th>
                                                    <th class="text-end">Giá bán</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <tr>
                                                    <td>22/05/2024</td>
                                                    <td><a href="#" class="fw-bold text-dark">SO-2024-050</a></td>
                                                    <td>Nguyễn Văn A</td>
                                                    <td>
                                                        <a href="contract-detail.jsp?id=10" class="badge contract-badge-out p-2">
                                                            <i class="bi bi-file-earmark-text"></i> HĐ-LUA-01
                                                        </a>
                                                        <div class="small text-muted mt-1">HĐ Bao tiêu lúa</div>
                                                    </td>
                                                    <td class="text-center text-danger fw-bold">- 20</td>
                                                    <td class="text-end">850,000 đ</td>
                                                </tr>

                                                <tr>
                                                    <td>20/05/2024</td>
                                                    <td><a href="#" class="fw-bold text-dark">SO-2024-048</a></td>
                                                    <td>Trần Thị B</td>
                                                    <td>
                                                        <span class="badge bg-secondary bg-opacity-25 text-dark border">
                                                            <i class="bi bi-cart"></i> Mua lẻ
                                                        </span>
                                                    </td>
                                                    <td class="text-center text-danger fw-bold">- 5</td>
                                                    <td class="text-end">880,000 đ</td>
                                                </tr>
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