<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa Nhà cung cấp - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
    <div class="container mt-5">
        <div class="card shadow-sm border-0 mx-auto" style="max-width: 700px;">
            <div class="card-header bg-warning text-dark fw-bold py-3">
                <i class="bi bi-pencil-square me-2"></i>CẬP NHẬT THÔNG TIN NHÀ CUNG CẤP
            </div>
            <div class="card-body p-4">
                <form action="EditPartnerServlet" method="post">
                    <input type="hidden" name="id" value="${p.id}">
                    
                    <div class="row mb-3">
                        <div class="col-md-7">
                            <label class="form-label fw-bold small">Tên nhà cung cấp (*)</label>
                            <input type="text" name="name" class="form-control" value="${p.name}" required>
                        </div>
                        <div class="col-md-5">
                            <label class="form-label fw-bold small">Mã số thuế</label>
                            <input type="text" name="tax_code" class="form-control" value="${p.tax_code}">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Số điện thoại liên hệ</label>
                        <input type="text" name="phone" class="form-control" value="${p.phone}">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Địa chỉ trụ sở</label>
                        <input type="text" name="address" class="form-control" value="${p.address}">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-bold small">Ghi chú đặc biệt (Note)</label>
                        <textarea name="note" class="form-control" rows="3">${p.note}</textarea>
                    </div>

                    <div class="d-flex justify-content-between border-top pt-3 mt-4">
                        <a href="ListPartnerServlet" class="btn btn-secondary px-4">Hủy bỏ</a>
                        <button type="submit" class="btn btn-warning px-5 fw-bold text-dark">LƯU THAY ĐỔI</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>