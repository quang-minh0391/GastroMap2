<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Nhà cung cấp mới - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
    <div class="container mt-5">
        <div class="card shadow-sm border-0 mx-auto" style="max-width: 700px;">
            <div class="card-header bg-success text-white py-3">
                <h5 class="mb-0 fw-bold"><i class="bi bi-plus-circle me-2"></i>THÊM NHÀ CUNG CẤP MỚI</h5>
            </div>
            <div class="card-body p-4">
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>
                
                <form action="CreatePartnerServlet" method="post">
                    <div class="row mb-3">
                        <div class="col-md-7">
                            <label class="form-label fw-bold small">Tên nhà cung cấp (*)</label>
                            <input type="text" name="name" class="form-control" placeholder="Ví dụ: Công ty Vật tư Nông nghiệp X" required>
                        </div>
                        <div class="col-md-5">
                            <label class="form-label fw-bold small">Mã số thuế</label>
                            <input type="text" name="tax_code" class="form-control" placeholder="Mã số thuế doanh nghiệp">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Số điện thoại liên hệ</label>
                        <input type="text" name="phone" class="form-control" placeholder="Số điện thoại hoặc Hotline">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Địa chỉ trụ sở</label>
                        <input type="text" name="address" class="form-control" placeholder="Địa chỉ đăng ký kinh doanh">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Ghi chú (Note)</label>
                        <textarea name="note" class="form-control" rows="3" placeholder="Thông tin bổ sung về NCC này..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between border-top pt-3 mt-4">
                        <a href="ListPartnerServlet" class="btn btn-secondary px-4">Quay lại danh sách</a>
                        <button type="submit" class="btn btn-success px-5 fw-bold">XÁC NHẬN THÊM</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>