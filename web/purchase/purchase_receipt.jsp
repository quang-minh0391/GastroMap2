<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thu Mua Nông Sản - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" rel="stylesheet" />
        <style>
            .purchase-card { border: none; box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15); }
            .header-bg { background: linear-gradient(135deg, #198754 0%, #20c997 100%); color: white; }
            .total-display-box { background-color: #f8f9fa; border: 2px dashed #198754; border-radius: 8px; padding: 10px; text-align: center; }
            .total-value { font-size: 1.6rem; font-weight: 800; color: #198754; }
            .info-box { font-size: 0.9rem; }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>

        <div class="container mt-5 mb-5">
            <div class="row justify-content-center">
                <div class="col-lg-11">
                    <div class="mb-3">
                        <a href="DebtManagementServlet" class="text-decoration-none text-secondary small">
                            <i class="bi bi-arrow-left"></i> Quay lại quản lý công nợ
                        </a>
                    </div>

                    <form action="${pageContext.request.contextPath}/CreatePurchaseReceiptServlet" method="post" id="purchaseForm">
                        <div class="card purchase-card">
                            <div class="card-header header-bg py-3">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h4 class="mb-0 fw-bold"><i class="bi bi-cart-check-fill"></i> PHIẾU THU MUA NÔNG SẢN</h4>
                                        <small class="opacity-75">Ghi nhận nhập kho nông sản thu mua từ nông dân</small>
                                    </div>
                                    <div class="badge bg-white text-success p-2">Mã: TM-TỰ ĐỘNG</div>
                                </div>
                            </div>

                            <div class="card-body p-4">
                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">1. Thông tin người bán (Nông dân)</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Ngày thu mua (*)</label>
                                        <input type="date" class="form-control" name="purchaseDate" id="purchaseDate" required>
                                    </div>
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Chọn thành viên bán hàng (*)</label>
                                        <select class="form-select" name="memberId" id="memberSelect" required></select>
                                    </div>

                                    <div class="col-md-12 d-none" id="memberInfoBox">
                                        <div class="card border-0 bg-light info-box p-3 border-start border-success border-4">
                                            <div class="row">
                                                <div class="col-md-8">
                                                    <div class="fw-bold text-success">Thông tin nông dân:</div>
                                                    <div class="small">Họ tên: <span id="mInfoName" class="fw-bold"></span> | SĐT: <span id="mInfoPhone"></span></div>
                                                    <div class="small">Địa chỉ: <span id="mInfoAddress"></span></div>
                                                </div>
                                                <div class="col-md-4 text-end border-start">
                                                    <div class="small fw-bold text-danger">Công nợ hiện tại:</div>
                                                    <div class="h5 mb-0 fw-bold text-danger" id="mInfoDebt">0 đ</div>
                                                    <small class="text-muted">(Số tiền đang nợ HTX)</small>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">2. Thông tin nông sản thu mua</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Chọn loại nông sản thu mua (*)</label>
                                        <select class="form-select" name="productId" id="productSelect" required></select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Đơn vị tính</label>
                                        <input type="text" id="unitDisplay" class="form-control bg-light" readonly placeholder="---">
                                    </div>
                                </div>

                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">3. Chi tiết kho và Đơn giá mua</h6>
                                <div class="table-responsive mb-3">
                                    <table class="table table-bordered align-middle" id="distributionTable">
                                        <thead class="bg-light">
                                            <tr class="text-center small fw-bold text-secondary">
                                                <th style="width: 40%;">Nhập vào kho (*)</th>
                                                <th style="width: 15%;">Số lượng mua (*)</th>
                                                <th style="width: 20%;">Đơn giá mua (VNĐ) (*)</th>
                                                <th style="width: 20%;">Thành tiền</th>
                                                <th style="width: 5%;"></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="distribution-row">
                                                <td><select class="form-select warehouse-select" name="warehouseId[]" required></select></td>
                                                <td><input type="number" class="form-control qty-input text-center" name="quantity[]" step="0.01" min="0.01" required></td>
                                                <td><input type="number" class="form-control price-input text-end" name="purchasePrice[]" min="1" required></td>
                                                <td class="subtotal text-end fw-bold text-secondary">0 đ</td>
                                                <td class="text-center">
                                                    <button type="button" class="btn btn-link text-danger p-0 remove-row"><i class="bi bi-trash"></i></button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <button type="button" class="btn btn-outline-success btn-sm fw-bold" id="btnAddRow">
                                        <i class="bi bi-plus-circle"></i> Thêm vị trí kho nhập
                                    </button>
                                </div>

                                <div class="total-display-box mb-4 shadow-sm">
                                    <div class="total-label fw-bold small text-muted">TỔNG GIÁ TRỊ THU MUA</div>
                                    <div class="total-value fw-bold" id="displayTotal">0 đ</div>
                                    <input type="hidden" name="totalAmount" id="hiddenTotal">
                                </div>

                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">4. Thanh toán & Trừ nợ</h6>
                                <div class="row g-3 p-3 border rounded bg-white mb-4 shadow-sm">
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold">HTX trả ngay cho Nông dân (VNĐ):</label>
                                        <input type="text" id="amountPaid" class="form-control form-control-lg fw-bold text-primary" 
                                               name="amountPaidDisplay" value="0" oninput="formatPaymentInput(this)">
                                        <input type="hidden" name="amountPaid" id="hiddenAmountPaid" value="0">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold">Số tiền trừ vào Nợ (Số dư mới):</label>
                                        <input type="text" id="remainingBalance" class="form-control form-control-lg bg-light fw-bold text-success" readonly value="0">
                                    </div>
                                    <div class="col-12 mt-2" id="paymentLogicContainer"></div>
                                </div>

                                <div class="col-12">
                                    <label class="form-label fw-bold small">Ghi chú phiếu thu mua</label>
                                    <textarea class="form-control" name="note" rows="2" placeholder="Ví dụ: Nông sản loại 1, thu mua tại ruộng..."></textarea>
                                </div>
                            </div>

                            <div class="card-footer bg-light p-3 text-end">
                                <button type="submit" class="btn btn-success btn-lg px-5 fw-bold">HOÀN TẤT THU MUA</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
            const formatter = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'});

            function formatPaymentInput(input) {
                let value = parseInt(input.value.replace(/\D/g, "")) || 0;
                const total = parseFloat($('#hiddenTotal').val()) || 0;
                if (value > total && total > 0) {
                    value = total;
                    Swal.fire({ icon: 'warning', title: 'Vượt quá giá trị phiếu!', text: 'Trả tối đa: ' + formatter.format(total), timer: 1500, showConfirmButton: false, toast: true, position: 'top-end' });
                }
                input.value = new Intl.NumberFormat('vi-VN').format(value);
                $('#hiddenAmountPaid').val(value);
                updateDebtLogic();
            }

            function calculateGrandTotal() {
                let grandTotal = 0;
                $('.distribution-row').each(function () {
                    const qty = parseFloat($(this).find('.qty-input').val()) || 0;
                    const price = parseFloat($(this).find('.price-input').val()) || 0;
                    const subtotal = qty * price;
                    $(this).find('.subtotal').text(formatter.format(subtotal));
                    grandTotal += subtotal;
                });
                $('#displayTotal').text(formatter.format(grandTotal));
                $('#hiddenTotal').val(grandTotal);
                updateDebtLogic();
            }

            function updateDebtLogic() {
                const total = parseFloat($('#hiddenTotal').val()) || 0;
                const paid = parseFloat($('#hiddenAmountPaid').val()) || 0;
                const balance = total - paid;
                $('#remainingBalance').val(formatter.format(balance));

                let html = '';
                if (total === 0) {
                    html = '<div class="p-2 rounded border bg-light text-muted small"><i class="bi bi-info-circle me-2"></i>Chọn sản phẩm và nhập giá để tính toán.</div>';
                } else {
                    html = `<div class="alert alert-success d-flex align-items-center mb-0 py-2"><i class="bi bi-info-circle-fill me-2"></i><div class="small fw-bold">Hành động: Giảm nợ của nông dân xuống: -\${formatter.format(balance)}</div></div>`;
                }
                $('#paymentLogicContainer').html(html);
            }

            function initWarehouseSelect(element) {
                $(element).select2({
                    theme: 'bootstrap-5',
                    placeholder: '-- Tìm kho nhập --',
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchWarehouseServlet',
                        type: 'POST', dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({ results: $.map(data, item => ({ id: item.id, text: item.name })) })
                    }
                });
            }

            $(document).ready(function () {
                $('#purchaseDate').val(new Date().toISOString().split('T')[0]);

                // SELECT2: NÔNG DÂN
                $('#memberSelect').select2({
                    theme: 'bootstrap-5', placeholder: '-- Tìm nông dân bán hàng --', minimumInputLength: 1,
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchMemberServlet',
                        dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({ results: $.map(data, item => ({ id: item.id, text: item.full_name, phone: item.phone, address: item.address, debt: item.current_debt || 0 })) })
                    }
                }).on('select2:select', function (e) {
                    const data = e.params.data;
                    $('#mInfoName').text(data.text); $('#mInfoPhone').text(data.phone); $('#mInfoAddress').text(data.address); $('#mInfoDebt').text(formatter.format(data.debt));
                    $('#memberInfoBox').removeClass('d-none');
                });

                // SELECT2: SẢN PHẨM NÔNG SẢN
                $('#productSelect').select2({
                    theme: 'bootstrap-5', placeholder: '-- Tìm nông sản thu mua --',
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchProductServlet',
                        type: 'POST', dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({ results: $.map(data, item => ({ id: item.id, text: item.name, unit: item.unit })) })
                    }
                }).on('select2:select', function (e) { $('#unitDisplay').val(e.params.data.unit); });

                initWarehouseSelect('.warehouse-select');

                $('#btnAddRow').click(() => {
                    const row = `<tr class="distribution-row">
                        <td><select class="form-select warehouse-select" name="warehouseId[]" required></select></td>
                        <td><input type="number" class="form-control qty-input text-center" name="quantity[]" step="0.01" required></td>
                        <td><input type="number" class="form-control price-input text-end" name="purchasePrice[]" required></td>
                        <td class="subtotal text-end fw-bold">0 đ</td>
                        <td class="text-center"><button type="button" class="btn btn-link text-danger remove-row"><i class="bi bi-trash"></i></button></td>
                    </tr>`;
                    const $row = $(row).appendTo('#distributionTable tbody');
                    initWarehouseSelect($row.find('.warehouse-select'));
                });

                $(document).on('click', '.remove-row', function() { $(this).closest('tr').remove(); calculateGrandTotal(); });
                $(document).on('input', '.qty-input, .price-input', calculateGrandTotal);
            });
        </script>
    </body>
</html>