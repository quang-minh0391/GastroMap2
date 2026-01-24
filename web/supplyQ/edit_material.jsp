<%@page contentType="text/html" pageEncoding="UTF-8"%>
<body class="bg-light">
    <%@include file="/common/header.jsp" %>
    <div class="container mt-5">
        <div class="card shadow-sm border-0 mx-auto" style="max-width: 700px;">
            <div class="card-header bg-warning text-dark fw-bold">
                <i class="bi bi-pencil-square me-2"></i>CHỈNH SỬA THÔNG TIN VẬT TƯ
            </div>
            <div class="card-body">
                <form action="EditMaterialServlet" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${m.id}">
                    <input type="hidden" name="oldImage" value="${m.image}">

                    <div class="row">
                        <div class="col-md-4 text-center">
                            <label class="small fw-bold d-block mb-2">Ảnh hiện tại</label>
                            <img src="${pageContext.request.contextPath}/${m.image}" 
                                 class="img-thumbnail mb-2" style="max-height: 150px;">
                            <input type="file" name="image" class="form-control form-control-sm" accept="image/*">
                        </div>
                        <div class="col-md-8">
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Tên vật tư</label>
                                <input type="text" name="name" class="form-control" value="${m.name}" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold">Đơn vị tính</label>
                                <input type="text" name="unit" class="form-control" value="${m.unit}" required>
                            </div>
                        </div>
                    </div>

                    <div class="mb-3 mt-3">
                        <label class="form-label small fw-bold">Mô tả vật tư</label>
                        <textarea name="description" class="form-control" rows="4">${m.description}</textarea>
                    </div>

                    <div class="d-flex justify-content-between border-top pt-3">
                        <a href="SearchMaterialServlet" class="btn btn-secondary">Hủy bỏ</a>
                        <button type="submit" class="btn btn-warning fw-bold">LƯU THAY ĐỔI</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>