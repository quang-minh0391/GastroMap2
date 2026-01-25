<%-- Đồng bộ contentType với header.jsp --%>
<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch sử công nợ - GastroMap</title>
        <style>
            .summary-card { border: 1px solid #ddd; padding: 20px; border-radius: 12px; background: #fff; margin: 20px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
            .debt-val { font-size: 24px; font-weight: bold; }
            .pos { color: #e74c3c; } .neg { color: #27ae60; }
            table { width: 100%; border-collapse: collapse; background: white; }
            th { background: #f8f9fa; color: #333; font-weight: 600; text-transform: uppercase; font-size: 11px; }
            td, th { padding: 12px; border: 1px solid #eee; }
            .clickable-code { cursor: pointer; font-weight: bold; color: #0d6efd; text-decoration: underline; }
            .clickable-code:hover { color: #0a58ca; }
        </style>
    </head>
    <body>
        <%@include file="/common/header.jsp" %>

        <div class="container-fluid px-4">
            <div class="summary-card">
                <div class="row align-items-center">
                    <div class="col-md-8">
                        <h3 class="m-0">Báo cáo công nợ: ${farmer.full_name}</h3>
                        <p class="text-muted mb-0">SĐT: ${farmer.phone} | Địa chỉ: ${farmer.address}</p>
                    </div>
                    <div class="col-md-4 text-end">
                        <span class="text-muted d-block">Số dư hiện tại</span>
                        <span class="debt-val ${farmer.current_debt > 0 ? 'pos' : 'neg'}">
                            <fmt:formatNumber value="${farmer.current_debt}" /> VNĐ
                        </span>
                    </div>
                </div>
            </div>
                        <div class="alert alert-info d-flex align-items-center py-2 mb-3" role="alert">
                        <i class="bi bi-info-circle-fill me-2"></i>
                        <div>
                            <small class="me-4"><b>Số dương (+):</b> Nông dân đang nợ HTX</small>
                            <small><b>Số âm (-):</b> HTX đang nợ Nông dân</small>
                        </div>
                    </div>

            <div class="card shadow-sm">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0"><i class="bi bi-clock-history me-2"></i>Lịch sử giao dịch</h5>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover m-0">
                        <thead>
                            <tr>
                                <th>Ngày</th> <%-- Đã bỏ cột giờ --%>
                                <th>Mã chứng từ</th>
                                <th>Hoạt động</th>
                                <th>Số tiền</th>
                                <th>Biến động</th>
                                <th>Dư nợ sau GD</th>
                                <th>Ghi chú</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${history}">
                                <tr>
                                    <%-- Chỉ hiển thị ngày, không hiện giờ --%>
                                    <td class="text-nowrap"><fmt:formatDate value="${item.date}" pattern="dd/MM/yyyy"/></td>
                                    <td>
                                        <span class="clickable-code" onclick="showPopupDetail('${item.refId}', '${item.type}', '${item.code}')">
                                            ${item.code}
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.type == 'MATERIAL_EXPORT'}">Cung ứng vật tư</c:when>
                                            <c:when test="${item.type == 'FARM_PURCHASE'}">Thu mua sản phẩm</c:when>
                                            <c:when test="${item.type == 'CASH_RECEIPT'}">Nộp tiền mặt</c:when>
                                            <c:when test="${item.type == 'CASH_PAYMENT'}">HTX chi trả lẻ</c:when>
                                        </c:choose>
                                    </td>
                                    <td class="fw-bold"><fmt:formatNumber value="${item.amount}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${item.entry == 'DEBIT'}"><span class="text-danger small">+ Tăng nợ</span></c:when>
                                            <c:otherwise><span class="text-success small">- Giảm nợ</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><strong class="text-primary"><fmt:formatNumber value="${item.balance}" /></strong></td>
                                    <td class="small text-muted">${item.note}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="modal fade" id="transactionModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header bg-success text-white">
                        <h5 class="modal-title">Chi tiết phiếu: <span id="modalCode"></span></h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body p-0">
                        <table class="table table-striped m-0">
                            <thead>
                                <tr class="table-light">
                                    <th class="ps-3">Nội dung</th>
                                    <th id="modalQtyHeader">Số lượng</th>
                                    <th>Đơn giá</th>
                                    <th class="pe-3 text-end">Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody id="modalBody"></tbody>
                        </table>
                        <%-- Vùng hiển thị minh chứng ảnh trong Popup --%>
                        <div id="modalImageArea" class="text-center p-3 border-top d-none"></div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            function showPopupDetail(refId, type, code) {
                document.getElementById('modalCode').innerText = code;
                const modalBody = document.getElementById('modalBody');
                const qtyHeader = document.getElementById('modalQtyHeader');
                const imgArea = document.getElementById('modalImageArea');
                
                modalBody.innerHTML = '<tr><td colspan="4" class="text-center py-4">Đang lấy thông tin...</td></tr>';
                imgArea.classList.add('d-none');
                imgArea.innerHTML = '';

                qtyHeader.innerText = (type.includes('CASH')) ? 'Phương thức' : 'Số lượng';

                fetch('${pageContext.request.contextPath}/FarmerTransactionDetailServlet?refId=' + refId + '&type=' + type)
                .then(res => res.json())
                .then(data => {
                    let html = '';
                    data.items.forEach(item => {
                        let qtyCol = (type.includes('CASH')) ? item.unit : (item.qty + ' ' + item.unit);
                        let priceCol = (type.includes('CASH')) ? '-' : parseFloat(item.price).toLocaleString();

                        html += `<tr>
                            <td class="ps-3">\${item.name}</td>
                            <td>\${qtyCol}</td>
                            <td>\${priceCol}</td>
                            <td class="pe-3 text-end fw-bold">\${parseFloat(item.total).toLocaleString()} VNĐ</td>
                        </tr>`;
                    });
                    modalBody.innerHTML = html;
                    
                    // Hiển thị minh chứng ảnh trong Popup nếu có
                    if (data.img && data.img.trim() !== "") {
                        imgArea.innerHTML = `<h6>Ảnh minh chứng:</h6>
                            <img src="${pageContext.request.contextPath}/\${data.img}" class="img-fluid border shadow-sm" style="max-height: 400px;">`;
                        imgArea.classList.remove('d-none');
                    }

                    new bootstrap.Modal(document.getElementById('transactionModal')).show();
                });
            }
        </script>
    </body>
</html>