<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Vật tư mới - GastroMap2</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
    <div class="container mt-5">
        <div class="card shadow-sm border-0 mx-auto" style="max-width: 600px;">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0">THÊM VẬT TƯ MỚI VÀO KHO</h5>
            </div>
            <div class="card-body">
                <form action="CreateMaterialServlet" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label class="form-label fw-bold">Tên vật tư (*)</label>
                        <input type="text" name="name" class="form-control" placeholder="Ví dụ: Phân bón NPK" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Đơn vị tính (*)</label>
                        <input type="text" name="unit" class="form-control" placeholder="Ví dụ: bao, kg, lít" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Hình ảnh minh họa</label>
                        <input type="file" name="image" class="form-control" accept="image/*">
                    </div>
                    <div class="mb-3">
                        <label class="form-label fw-bold">Mô tả chi tiết</label>
                        <textarea name="description" class="form-control" rows="4"></textarea>
                    </div>
                    <div class="d-flex justify-content-between">
                        <a href="SearchMaterialServlet" class="btn btn-secondary">Quay lại</a>
                        <button type="submit" class="btn btn-primary px-4">LƯU VẬT TƯ</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>