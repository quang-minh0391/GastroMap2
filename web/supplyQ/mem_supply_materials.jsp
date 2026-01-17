<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Cung Ứng Vật Tư - GastroMap2</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" rel="stylesheet" />
        <style>
            .supply-card {
                border: none;
                box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            }
            .header-bg {
                background: linear-gradient(135deg, #0d6efd 0%, #0dcaf0 100%);
                color: white;
            }
            .total-display-box {
                background-color: #f8f9fa;
                border: 2px dashed #0d6efd;
                border-radius: 8px;
                padding: 10px;
                text-align: center;
            }
            .total-value {
                font-size: 1.6rem;
                font-weight: 800;
                color: #0d6efd;
            }
            /* Style mới cho hiển thị tồn kho */
            .stock-info-tag {
                font-size: 0.75rem;
                font-weight: bold;
                color: #dc3545;
                display: block;
                margin-top: 2px;
            }
        </style>
    </head>
    <body class="bg-light">
        <%@include file="/common/header.jsp" %>

        <div class="container mt-5 mb-5">
            <div class="row justify-content-center">
                <div class="col-lg-11">
                    <div class="mb-3">
                        <a href="${pageContext.request.contextPath}/SearchMaterialServlet" class="text-decoration-none text-secondary small">
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách vật tư
                        </a>
                    </div>

                    <form action="${pageContext.request.contextPath}/CreateMaterialSupplyServlet" method="post" id="supplyForm">
                        <div class="card supply-card">
                            <div class="card-header header-bg py-3">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h4 class="mb-0 fw-bold"><i class="bi bi-person-up"></i> PHIẾU CUNG ỨNG VẬT TƯ</h4>
                                        <small class="opacity-75">Xuất vật tư từ kho cho thành viên hợp tác xã</small>
                                    </div>
                                    <div class="badge bg-white text-primary p-2">MÃ: PX-TỰ ĐỘNG</div>
                                </div>
                            </div>

                            <div class="card-body p-4">
                                <h6 class="text-primary fw-bold border-bottom pb-2 mb-3">1. Thông tin người nhận</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Ngày xuất (*)</label>
                                        <input type="date" class="form-control" name="supplyDate" id="supplyDate" readonly required>
                                    </div>
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Chọn thành viên nhận vật tư (*)</label>
                                        <select class="form-select" name="memberId" id="memberSelect" required></select>
                                    </div>

                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Hợp đồng liên kết</label>
                                        <select class="form-select" name="contractId" id="contractSelect"></select>
                                    </div>

                                    <div class="col-md-6 d-none" id="contractInfoBox">
                                        <div class="card border-0 bg-light info-box">
                                            <div class="card-body p-2 border-start border-info border-4">
                                                <p class="mb-0 small text-info fw-bold">Chi tiết hợp đồng:</p>
                                                <div class="small">Loại: <span id="dtType"></span> | Trạng thái: <span id="dtStatus" class="badge bg-secondary"></span></div>
                                                <div class="small">Thời hạn: <span id="dtSign"></span> - <span id="dtExpiry"></span></div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-12 d-none" id="memberInfoBox">
                                        <div class="card border-0 bg-light info-box">
                                            <div class="card-body p-2 border-start border-primary border-4">
                                                <div class="row">
                                                    <div class="col-md-4">
                                                        <p class="mb-0 small text-primary fw-bold">Thông tin cá nhân:</p>
                                                        <div class="small">Họ tên: <span id="mInfoName" class="fw-bold"></span></div>
                                                        <div class="small">SĐT: <span id="mInfoPhone"></span></div>
                                                    </div>
                                                    <div class="col-md-4">
                                                        <p class="mb-0 small text-primary fw-bold">Địa chỉ:</p>
                                                        <div class="small" id="mInfoAddress">---</div>
                                                    </div>
                                                    <div class="col-md-4 text-end">
                                                        <p class="mb-0 small text-danger fw-bold">Dư nợ hiện tại:</p>
                                                        <div class="h5 mb-0 fw-bold text-danger" id="mInfoDebt">0 đ</div>
                                                        <small class="text-muted">(Số tiền thành viên đang nợ HTX)</small>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <h6 class="text-primary fw-bold border-bottom pb-2 mb-3">2. Thông tin vật tư xuất kho</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Bước 1: Chọn vật tư xuất (*)</label>
                                        <select class="form-select" name="materialId" id="materialSelect" required></select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Đơn vị tính</label>
                                        <input type="text" id="unitDisplay" class="form-control bg-light" readonly placeholder="---">
                                    </div>
                                </div>

                                <h6 class="text-primary fw-bold border-bottom pb-2 mb-3">3. Chi tiết kho và Số lượng</h6>
                                <div class="table-responsive mb-3">
                                    <table class="table table-bordered align-middle" id="distributionTable">
                                        <thead class="bg-light">
                                            <tr class="text-center small fw-bold text-secondary">
                                                <th style="width: 40%;">Bước 2: Xuất từ kho (*)</th>
                                                <th style="width: 15%;">Số lượng xuất (*)</th>
                                                <th style="width: 20%;">Đơn giá bán (VNĐ) (*)</th>
                                                <th style="width: 20%;">Thành tiền</th>
                                                <th style="width: 5%;"></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="distribution-row">
                                                <td>
                                                    <select class="form-select warehouse-select" name="warehouseId[]" required disabled>
                                                        <option value="">-- Vui lòng chọn vật tư trước --</option>
                                                    </select>
                                                    <span class="stock-info-tag d-none">Tồn thực tế: <span class="stock-val">0</span></span>
                                                </td>
                                                <td><input type="number" class="form-control qty-input text-center" name="quantity[]" step="0.01" min="0.01" required></td>
                                                <td><input type="number" class="form-control price-input text-end" name="supplyPrice[]" min="1" required></td>
                                                <td class="subtotal text-end fw-bold text-secondary">0 đ</td>
                                                <td class="text-center">
                                                    <button type="button" class="btn btn-link text-danger p-0 remove-row"><i class="bi bi-trash"></i></button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <button type="button" class="btn btn-outline-primary btn-sm fw-bold" id="btnAddRow" disabled>
                                        <i class="bi bi-plus-circle"></i> Thêm vị trí kho xuất
                                    </button>
                                </div>

                                <div class="total-display-box mb-4 shadow-sm">
                                    <div class="total-label fw-bold">TỔNG GIÁ TRỊ CUNG ỨNG</div>
                                    <div class="total-value fw-bold" id="displayTotal">0 đ</div>
                                    <input type="hidden" name="totalAmount" id="hiddenTotal">
                                </div>

                                <div class="row g-3 p-3 border rounded bg-white mb-4 shadow-sm">
                                    <h6 class="text-primary fw-bold border-bottom pb-2 mb-3">4. Thanh toán & Công nợ</h6>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold">Thành viên thanh toán (VNĐ):</label>
                                        <input type="text" id="amountPaid" class="form-control form-control-lg fw-bold text-success" 
                                               name="amountPaidDisplay" value="0" oninput="formatPaymentInput(this)">
                                        <input type="hidden" name="amountPaid" id="hiddenAmountPaid" value="0">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold">Ghi vào nợ thành viên:</label>
                                        <input type="text" id="remainingBalance" class="form-control form-control-lg bg-light fw-bold text-danger" readonly value="0">
                                    </div>
                                    <div class="col-12 mt-2" id="paymentLogicContainer"></div>
                                </div>

                                <div class="col-12">
                                    <label class="form-label fw-bold small">Ghi chú phiếu xuất</label>
                                    <textarea class="form-control" name="note" rows="2" placeholder="Ví dụ: Cung ứng phân bón đợt 1 vụ đông xuân..."></textarea>
                                </div>
                            </div>

                            <div class="card-footer bg-light p-3 text-end">
                                <button type="submit" class="btn btn-primary btn-lg px-5 fw-bold">XÁC NHẬN XUẤT KHO</button>
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

            // HÀM 1: Định dạng tiền và chặn trả lố
            function formatPaymentInput(input) {
                let value = parseInt(input.value.replace(/\D/g, "")) || 0;
                const total = parseFloat($('#hiddenTotal').val()) || 0;

                if (value > total && total > 0) {
                    value = total;
                    Swal.fire({
                        icon: 'warning',
                        title: 'Vượt quá giá trị phiếu!',
                        text: 'Thành viên chỉ thanh toán tối đa: ' + formatter.format(total),
                        timer: 1500, showConfirmButton: false, toast: true, position: 'top-end'
                    });
                }
                input.value = new Intl.NumberFormat('vi-VN').format(value);
                $('#hiddenAmountPaid').val(value);
                updateDebtLogic();
            }

            // HÀM 2: Tính tổng và chặn bám đuổi (nếu giảm số lượng hàng)
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

                let currentPaid = parseFloat($('#hiddenAmountPaid').val()) || 0;
                if (currentPaid > grandTotal) {
                    $('#hiddenAmountPaid').val(grandTotal);
                    $('#amountPaid').val(new Intl.NumberFormat('vi-VN').format(grandTotal));
                }
                updateDebtLogic();
            }

            // HÀM 3: Hiển thị nợ chi tiết (Fix lỗi \${})
            function updateDebtLogic() {
                const total = parseFloat($('#hiddenTotal').val()) || 0;
                const paid = parseFloat($('#hiddenAmountPaid').val()) || 0;
                const debt = total - paid;

                $('#remainingBalance').val(formatter.format(debt));

                let html = '';
                if (total === 0) {
                    html = '<div class="p-2 rounded border bg-light text-muted small"><i class="bi bi-info-circle me-2"></i>Chọn vật tư và nhập kho để tính toán.</div>';
                } else if (debt > 0) {
                    html = `<div class="alert alert-warning d-flex align-items-center mb-0 py-2"><i class="bi bi-exclamation-triangle-fill me-2"></i><div class="small fw-bold">Ghi vào công nợ (Thành viên nợ HTX: \${formatter.format(debt)})</div></div>`;
                } else {
                    html = '<div class="alert alert-success d-flex align-items-center mb-0 py-2"><i class="bi bi-check-circle-fill me-2"></i><div class="small fw-bold">Thành viên đã thanh toán đủ.</div></div>';
                }
                $('#paymentLogicContainer').html(html);
            }

            // HÀM 4: Tìm kho theo đúng vật tư (Dùng Servlet 1)
            function initWH(el) {
                const materialId = $('#materialSelect').val();
                if (!materialId) {
                    $(el).prop('disabled', true).html('<option value="">-- Vui lòng chọn vật tư trước --</option>');
                    return;
                }

                $(el).prop('disabled', false);
                $(el).select2({
                    theme: 'bootstrap-5',
                    placeholder: '-- Chọn kho xuất --',
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchWarehouseServlet1',
                        type: 'POST',
                        dataType: 'json',
                        data: params => ({ term: params.term, materialId: materialId }),
                        processResults: data => ({
                            results: $.map(data, item => ({
                                id: item.id, 
                                text: item.name + " (Tồn: " + item.current_stock + ")",
                                stock: item.current_stock
                            }))
                        })
                    }
                }).on('select2:select', function(e) {
                    const data = e.params.data;
                    const $row = $(this).closest('tr');
                    $row.find('.qty-input').attr('max', data.stock);
                    $row.find('.stock-val').text(data.stock);
                    $row.find('.stock-info-tag').removeClass('d-none');
                });
            }

            $(document).ready(function () {
                $('#supplyDate').val(new Date().toISOString().split('T')[0]);

                // SELECT2: THÀNH VIÊN
                $('#memberSelect').select2({
                    theme: 'bootstrap-5',
                    placeholder: '-- Tìm thành viên nhận hàng --',
                    minimumInputLength: 1,
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchMemberServlet',
                        dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({
                            results: $.map(data, item => ({
                                id: item.id, text: item.full_name + " (" + item.phone + ")",
                                phone: item.phone, address: item.address, debt: item.current_debt || 0
                            }))
                        })
                    }
                }).on('select2:select', function (e) {
                    const data = e.params.data;
                    $('#mInfoName').text(data.text);
                    $('#mInfoPhone').text(data.phone || 'Chưa cập nhật');
                    $('#mInfoAddress').text(data.address || 'Chưa cập nhật');
                    $('#mInfoDebt').text(formatter.format(data.debt));
                    $('#memberInfoBox').removeClass('d-none');
                }).on('select2:clear', () => $('#memberInfoBox').addClass('d-none'));

                // SELECT2: VẬT TƯ (MỞ KHÓA KHO)
                $('#materialSelect').select2({
                    theme: 'bootstrap-5',
                    placeholder: '-- Chọn vật tư trước khi chọn kho --',
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchMaterialServlet',
                        type: 'POST', dataType: 'json',
                        data: params => ({ term: params.term }),
                        processResults: data => ({ results: $.map(data, item => ({ id: item.id, text: item.name, unit: item.unit })) })
                    }
                }).on('select2:select', function(e) {
                    $('#unitDisplay').val(e.params.data.unit);
                    $('#btnAddRow').prop('disabled', false);
                    $('.distribution-row:not(:first)').remove();
                    const $firstRow = $('.distribution-row').first();
                    $firstRow.find('.warehouse-select').val(null).trigger('change');
                    $firstRow.find('.qty-input').val('').removeAttr('max');
                    $firstRow.find('.stock-info-tag').addClass('d-none');
                    initWH('.warehouse-select');
                });

                // CHẶN NHẬP QUÁ TỒN KHO TỨC THÌ
                $(document).on('input', '.qty-input', function() {
                    const qty = parseFloat($(this).val()) || 0;
                    const max = parseFloat($(this).attr('max')) || 0;
                    if (qty > max && max >= 0) {
                        Swal.fire({ icon: 'error', title: 'Không đủ hàng!', text: 'Kho chỉ còn tồn: ' + max, timer: 1500 });
                        $(this).val(max);
                    }
                    calculateGrandTotal();
                });

                // THÊM DÒNG MỚI
                $('#btnAddRow').click(() => {
                    const row = `<tr class="distribution-row">
                        <td><select class="form-select warehouse-select" name="warehouseId[]" required></select><span class="stock-info-tag d-none">Tồn: <span class="stock-val">0</span></span></td>
                        <td><input type="number" class="form-control qty-input text-center" name="quantity[]" step="0.01" required></td>
                        <td><input type="number" class="form-control price-input text-end" name="supplyPrice[]" required></td>
                        <td class="subtotal text-end fw-bold">0 đ</td>
                        <td class="text-center"><button type="button" class="btn btn-link text-danger remove-row"><i class="bi bi-trash"></i></button></td>
                    </tr>`;
                    const $row = $(row).appendTo('#distributionTable tbody');
                    initWH($row.find('.warehouse-select'));
                });

                $(document).on('click', '.remove-row', function() { $(this).closest('tr').remove(); calculateGrandTotal(); });
                $(document).on('input', '.price-input', calculateGrandTotal);

                // HỢP ĐỒNG
                $('#contractSelect').select2({
                    theme: 'bootstrap-5', placeholder: '-- Tìm hợp đồng --', allowClear: true,
                    ajax: {
                        url: '${pageContext.request.contextPath}/ContractServlet', dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({ results: $.map(data, item => ({ id: item.id, text: item.contractCode, type: item.contractType, sign: item.signingDate, expiry: item.expiryDate, status: item.status })) })
                    }
                }).on('select2:select', function (e) {
                    const d = e.params.data;
                    $('#dtType').text(d.type); $('#dtStatus').text(d.status); $('#dtSign').text(d.sign); $('#dtExpiry').text(d.expiry);
                    $('#contractInfoBox').removeClass('d-none');
                }).on('select2:clear', () => $('#contractInfoBox').addClass('d-none'));

                // THÔNG BÁO THÀNH CÔNG
                if (new URLSearchParams(window.location.search).get('status') === 'success') {
                    Swal.fire('Thành công', 'Phiếu cung ứng đã được lưu và trừ kho.', 'success');
                    window.history.replaceState({}, document.title, window.location.pathname);
                }
            });
        </script>
    </body>
</html>