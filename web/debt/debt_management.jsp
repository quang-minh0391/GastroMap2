<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Công nợ - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <style>
            .table-container { background: white; border-radius: 10px; padding: 20px; box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075); }
            .amount-positive { color: #dc3545; font-weight: 800; } /* Nợ (+) */
            .amount-negative { color: #198754; font-weight: 800; } /* Dư (-) */
        </style>
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>

        <div class="container mt-4 mb-5">
            <h3 class="fw-bold mb-4"><i class="bi bi-wallet2 text-primary"></i> QUẢN LÝ CÔNG NỢ TỔNG HỢP</h3>

            <ul class="nav nav-pills mb-4 shadow-sm bg-white p-2 rounded" id="debtTab" role="tablist">
                <li class="nav-item flex-fill"><button class="nav-link active w-100" data-bs-toggle="pill" data-bs-target="#members" type="button">NỢ NÔNG DÂN</button></li>
                <li class="nav-item flex-fill"><button class="nav-link w-100" data-bs-toggle="pill" data-bs-target="#partners" type="button">NỢ NHÀ CUNG CẤP</button></li>
            </ul>

            <div class="tab-content mt-3">
                <div id="members" class="tab-pane fade show active">
                    <div class="alert alert-info d-flex align-items-center py-2 mb-3" role="alert">
                        <i class="bi bi-info-circle-fill me-2"></i>
                        <div>
                            <small class="me-4"><b>Số dương (+):</b> Nông dân đang nợ HTX</small>
                            <small><b>Số âm (-):</b> HTX đang nợ Nông dân</small>
                        </div>
                    </div>
                    <div class="table-container shadow-sm border">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr><th>Họ tên</th><th>Số điện thoại</th><th class="text-end">Số dư hiện tại</th><th class="text-center">Thao tác</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${memberDebts}">
                                    <%-- Lọc: Chỉ hiện người có nợ khác 0 --%>
                                    <c:if test="${m.amount != 0}">
                                        <tr>
                                            <td class="fw-bold">${m.name}</td>
                                            <td>${m.phone}</td>
                                            <td class="text-end">
                                                <span class="${m.amount > 0 ? 'amount-positive' : 'amount-negative'}">
                                                    <fmt:formatNumber value="${m.amount}" pattern="#,###"/> đ
                                                </span>
                                            </td>
                                            <td class="text-center">
                                                <a href="MemberDebtDetailServlet?memberId=${m.id}" class="btn btn-sm btn-outline-info me-1"><i class="bi bi-clock-history"></i> Lịch sử</a>
                                                <%-- Chặn: Chỉ thu nợ khi nợ dương, trả nợ khi nợ âm --%>
                                                <button class="btn btn-sm btn-success" ${m.amount <= 0 ? 'disabled' : ''} onclick="openVoucherModal(${m.id}, null, '${m.name}', 'RECEIPT', 'CREDIT')">HTX Thu nợ</button>
                                                <button class="btn btn-sm btn-outline-danger" ${m.amount >= 0 ? 'disabled' : ''} onclick="openVoucherModal(${m.id}, null, '${m.name}', 'PAYMENT', 'DEBIT')">HTX Trả nợ</button>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="partners" class="tab-pane fade">
                    <div class="table-container shadow-sm border">
                        <table class="table table-bordered bg-white">
                            <thead class="table-danger">
                                <tr><th>Nhà Cung Cấp</th><th>Số điện thoại</th><th class="text-end">Số nợ HTX đang nợ (+)</th><th class="text-center">Hành động</th></tr>
                            </thead>
                            <tbody>
                                <c:forEach var="p" items="${partnerDebts}">
                                    <%-- Lọc: Chỉ hiện nhà cung cấp HTX đang nợ tiền --%>
                                    <c:if test="${p.amount > 0}">
                                        <tr>
                                            <td>${p.name}</td>
                                            <td>${p.phone}</td>
                                            <td class="text-end fw-bold text-danger"><fmt:formatNumber value="${p.amount}" pattern="#,###"/> đ</td>
                                            <td class="text-center">
                                                <a href="PartnerDebtDetailServlet?partnerId=${p.id}" class="btn btn-sm btn-outline-info me-1"><i class="bi bi-eye"></i> Lịch sử</a>
                                                <button class="btn btn-sm btn-danger fw-bold" onclick="openSupplierModal(${sessionScope.id}, ${p.id}, '${p.name}', ${p.amount})">Trả nợ</button>
                                            </td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade" id="voucherModal" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <form action="DebtManagementServlet" method="post" enctype="multipart/form-data" class="modal-content shadow-lg" id="voucherForm">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title fw-bold" id="vTitle">Lập Phiếu</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" name="memberId" id="mid"><input type="hidden" name="partnerId" id="pid">
                        <input type="hidden" name="voucherType" id="vtype"><input type="hidden" name="entryType" id="etype">
                        <div class="mb-3"><label class="small fw-bold">Đối tượng:</label><input type="text" id="objName" class="form-control bg-light fw-bold" readonly></div>
                        <div class="mb-3">
                            <label class="small fw-bold text-danger">Số tiền thanh toán (*):</label>
                            <input type="number" name="amount" id="amountInput" class="form-control form-control-lg border-danger fw-bold" required min="1000">
                        </div>
                        <div class="mb-3">
                            <label class="small fw-bold">Phương thức & Minh chứng:</label>
                            <div class="input-group">
                                <select name="paymentMethod" class="form-select"><option value="Tiền mặt">💵 Tiền mặt</option><option value="Chuyển khoản">📱 Chuyển khoản</option></select>
                                <input type="file" name="image" class="form-control" accept="image/*">
                            </div>
                        </div>
                        <div class="mb-0"><label class="small fw-bold">Ghi chú:</label><textarea name="note" class="form-control" rows="2"></textarea></div>
                    </div>
                    <div class="modal-footer"><button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button><button type="submit" class="btn btn-primary fw-bold">XÁC NHẬN LƯU</button></div>
                </form>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
            let limitDebt = 0; // Biến chặn nhập quá nợ
            
            function openVoucherModal(mId, pId, name, vType, eType) {
                document.getElementById('mid').value = mId; document.getElementById('pid').value = "";
                document.getElementById('vtype').value = vType; document.getElementById('etype').value = eType;
                document.getElementById('objName').value = name; limitDebt = 0;
                document.getElementById('vTitle').innerText = (vType === 'RECEIPT') ? "HTX THU NỢ NÔNG DÂN" : "CHI TIỀN CHO NÔNG DÂN";
                new bootstrap.Modal(document.getElementById('voucherModal')).show();
            }

            function openSupplierModal(mId, pId, name, debt) {
                document.getElementById('mid').value = mId; document.getElementById('pid').value = pId;
                document.getElementById('vtype').value = "PAYMENT"; document.getElementById('etype').value = "CREDIT";
                document.getElementById('objName').value = name; limitDebt = parseFloat(debt);
                document.getElementById('vTitle').innerText = "TRẢ NỢ NHÀ CUNG CẤP (Nợ: " + limitDebt.toLocaleString() + "đ)";
                new bootstrap.Modal(document.getElementById('voucherModal')).show();
            }

            // CHẶN NHẬP QUÁ GIÁ TRỊ NỢ KHI SUBMIT
            document.getElementById('voucherForm').onsubmit = function(e) {
                const partnerId = document.getElementById('pid').value;
                const inputVal = parseFloat(document.getElementById('amountInput').value);
                if (partnerId && limitDebt > 0 && inputVal > limitDebt) {
                    Swal.fire({ icon: 'error', title: 'Vượt quá số nợ!', text: 'Bạn chỉ nợ ' + limitDebt.toLocaleString() + ' đ. Không thể trả nhiều hơn!' });
                    return false;
                }
                return true;
            };

            $(document).ready(function () {
                const status = new URLSearchParams(window.location.search).get('status');
                if (status === 'success') Swal.fire('Thành công!', 'Giao dịch đã được lưu.', 'success');
                if (status === 'error') Swal.fire('Lỗi!', 'Không thể lưu giao dịch.', 'error');
            });
        </script>
    </body>
</html>