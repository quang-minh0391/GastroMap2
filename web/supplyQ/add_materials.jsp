<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nhập Kho Vật Tư - GastroMap2</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" rel="stylesheet" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <style>
            .receipt-card {
                border: none;
                box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            }
            .header-bg {
                background: linear-gradient(135deg, #198754 0%, #20c997 100%);
                color: white;
            }
            .total-display-box {
                background-color: #f8f9fa;
                border: 2px dashed #198754;
                border-radius: 8px;
                padding: 10px;
                text-align: center;
            }
            .total-label {
                font-size: 0.85rem;
                color: #6c757d;
                text-transform: uppercase;
            }
            .total-value {
                font-size: 1.6rem;
                font-weight: 800;
                color: #198754;
            }
            .info-box {
                font-size: 0.9rem;
            }
        </style>
    </head>
    <body class="bg-light">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <%@include file="/common/header.jsp" %>

        <div class="container mt-5 mb-5">
            <div class="row justify-content-center">
                <div class="col-lg-11">
                    <div class="mb-3">
                        <a href="material-detail.jsp" class="text-decoration-none text-secondary small">
                            <i class="bi bi-arrow-left"></i> Quay lại kho vật tư
                        </a>
                    </div>

                    <form action="${pageContext.request.contextPath}/CreateMaterialReceiptServlet" method="post" id="receiptForm">
                        <div class="card receipt-card">
                            <div class="card-header header-bg py-3">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h4 class="mb-0 fw-bold"><i class="bi bi-box-seam-fill"></i> PHIẾU NHẬP KHO VẬT TƯ</h4>
                                        <small class="opacity-75">Quản lý nhập 01 vật tư vào nhiều vị trí kho</small>
                                    </div>
                                    <div class="badge bg-white text-success p-2">MÃ: TỰ ĐỘNG</div>
                                </div>
                            </div>

                            <div class="card-body p-4">
                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">1. Thông tin giao dịch</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Ngày nhập (*)</label>
                                        <input type="date" class="form-control" name="receiptDate" id="receiptDate" readonly required style="background-color: #f8f9fa;">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Nhà cung cấp (*)</label>
                                        <select class="form-select" name="partnerId" id="partnerSelect" required></select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Hợp đồng liên kết</label>
                                        <select class="form-select" name="contractId" id="contractSelect"></select>
                                    </div>

                                    <div class="col-md-6 d-none" id="partnerInfoBox">
                                        <div class="card border-0 bg-light info-box">
                                            <div class="card-body p-2 border-start border-primary border-4">
                                                <p class="mb-0 small text-primary fw-bold">Chi tiết đối tác:</p>
                                                <div class="small">Tên: <span id="infoName" class="fw-bold"></span> | SĐT: <span id="infoPhone"></span></div>
                                                <div class="small">Địa chỉ: <span id="infoAddress"></span></div>
                                            </div>
                                        </div>
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
                                </div>

                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">2. Thông tin vật tư</h6>
                                <div class="row g-3 mb-4">
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Chọn vật tư nhập (*)</label>
                                        <select class="form-select" name="materialId" id="materialSelect" required></select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Đơn vị tính</label>
                                        <input type="text" id="unitDisplay" class="form-control bg-light" readonly placeholder="---">
                                    </div>
                                </div>

                                <h6 class="text-success fw-bold border-bottom pb-2 mb-3">3. Phân bổ kho và Đơn giá (Giá nhập ở các kho phải giống nhau)</h6>
                                <div class="table-responsive mb-3">
                                    <table class="table table-bordered align-middle" id="distributionTable">
                                        <thead class="bg-light">
                                            <tr class="text-center small fw-bold text-secondary">
                                                <th style="width: 40%;">Kho lưu trữ (*)</th>
                                                <th style="width: 15%;">Số lượng (*)</th>
                                                <th style="width: 20%;">Đơn giá (VNĐ) (*)</th>
                                                <th style="width: 20%;">Thành tiền</th>
                                                <th style="width: 5%;"></th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="distribution-row">
                                                <td>
                                                    <select class="form-select warehouse-select" name="warehouseId[]" required>
                                                        <option value="">-- Tìm kho --</option>
                                                    </select>
                                                </td>
                                                <td><input type="number" class="form-control qty-input text-center" name="quantity[]" step="0.01" min="0.01" placeholder="0.0" required></td>
                                                <td><input type="number" class="form-control price-input text-end" name="importPrice[]" min="1" placeholder="0" required></td>
                                                <td class="subtotal text-end fw-bold text-secondary">0 đ</td>
                                                <td class="text-center">
                                                    <button type="button" class="btn btn-link text-danger p-0 remove-row" title="Xóa dòng"><i class="bi bi-trash"></i></button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <button type="button" class="btn btn-outline-success btn-sm fw-bold" id="btnAddRow">
                                        <i class="bi bi-plus-circle"></i> Thêm vị trí kho
                                    </button>
                                </div>

                                <div class="total-display-box mb-4 shadow-sm">
                                    <div class="total-label fw-bold">Tổng giá trị toàn bộ lô hàng</div>
                                    <div class="total-value fw-bold" id="displayTotal">0 đ</div>
                                    <input type="hidden" name="totalAmount" id="hiddenTotal">
                                </div>
                                <div class="row g-3 p-3 border rounded bg-white mb-4 shadow-sm">
                                    <h6 class="text-success fw-bold border-bottom pb-2 mb-3">4. Thông tin thanh toán</h6>

                                    <div class="col-md-6">
                                        <label class="form-label fw-bold">HTX thanh toán (VNĐ) (*):</label>
                                        <input type="text" id="amountPaid" class="form-control form-control-lg fw-bold text-primary" 
                                               name="amountPaidDisplay" value="0" placeholder="0" oninput="formatPaymentInput(this)">

                                        <input type="hidden" name="amountPaid" id="hiddenAmountPaid" value="0">
                                    </div>

                                    <div class="col-md-6">
                                        <label class="form-label fw-bold">Số tiền còn lại:</label>
                                        <input type="text" id="remainingBalance" class="form-control form-control-lg bg-light fw-bold" readonly value="0">
                                        <input type="hidden" name="balanceValue" id="balanceValue" value="0">
                                    </div>

                                    <div class="col-12 mt-3" id="paymentLogicContainer">
                                        <div class="p-2 rounded border bg-light">
                                            <i class="bi bi-info-circle me-2"></i>Vui lòng nhập số tiền thanh toán.
                                        </div>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <label class="form-label fw-bold small">Ghi chú</label>
                                    <textarea class="form-control" name="note" rows="2" placeholder="Thông tin thêm về lô hàng..."></textarea>
                                </div>
                            </div>

                            <div class="card-footer bg-light p-3 text-end">
                                <button type="submit" class="btn btn-success btn-lg px-5 fw-bold">LƯU PHIẾU</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

        <script>
            const formatter = new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'});

            function formatPaymentInput(input) {
                // 1. Lấy giá trị đang nhập (loại bỏ ký tự không phải số)
                let value = parseInt(input.value.replace(/\D/g, "")) || 0;

                // 2. Lấy tổng giá trị phiếu hiện tại từ input ẩn
                const total = parseFloat($('#hiddenTotal').val()) || 0;

                // 3. KIỂM TRA VÀ TỰ ĐỘNG CHỈNH SỬA
                if (value > total) {
                    value = total; // Ép về bằng giá trị phiếu

                    // Thông báo nhanh cho người dùng (tự đóng sau 1.2 giây)
                    Swal.fire({
                        icon: 'warning',
                        title: 'Vượt quá giá trị phiếu!',
                        text: 'Số tiền thanh toán tự động chỉnh về: ' + formatter.format(total),
                        timer: 1200,
                        showConfirmButton: false,
                        position: 'top-end',
                        toast: true
                    });
                }

                // 4. Cập nhật hiển thị (có dấu chấm phân cách)
                input.value = new Intl.NumberFormat('vi-VN').format(value);

                // 5. Gán giá trị số nguyên vào input ẩn để gửi về Servlet
                $('#hiddenAmountPaid').val(value);

                // 6. Cập nhật lại phần tính toán công nợ bên dưới
                updatePaymentLogic();
            }

            function calculateGrandTotal() {
                let grandTotal = 0;
                $('.distribution-row').each(function () {
                    const $row = $(this);
                    let qty = parseFloat($row.find('.qty-input').val()) || 0;
                    let price = parseFloat($row.find('.price-input').val()) || 0;

                    const subtotal = qty * price;
                    $row.find('.subtotal').text(formatter.format(subtotal));
                    grandTotal += subtotal;
                });

                // Cập nhật tổng tiền vào UI và input ẩn
                $('#displayTotal').text(formatter.format(grandTotal));
                $('#hiddenTotal').val(grandTotal);

                // KIỂM TRA LẠI SỐ TIỀN ĐÃ NHẬP THANH TOÁN
                let currentPaid = parseFloat($('#hiddenAmountPaid').val()) || 0;
                if (currentPaid > grandTotal) {
                    // Nếu tiền đã trả đang lớn hơn tổng tiền mới -> Ép về bằng tổng tiền mới
                    $('#hiddenAmountPaid').val(grandTotal);
                    $('#amountPaid').val(new Intl.NumberFormat('vi-VN').format(grandTotal));
                }

                updatePaymentLogic();
            }

            function updatePaymentLogic() {
                const total = parseFloat($('#hiddenTotal').val()) || 0;
                const paid = parseFloat($('#hiddenAmountPaid').val()) || 0;
                const diff = total - paid; // Hiệu số này luôn >= 0

                // Hiển thị số tiền còn nợ (Số tiền còn lại)
                $('#remainingBalance').val(formatter.format(diff));
                $('#balanceValue').val(diff);

                let html = '';
                const container = $('#paymentLogicContainer');

                if (total === 0) {
                    html = `<div class="p-2 rounded border bg-light text-muted small"><i class="bi bi-info-circle me-2"></i>Vui lòng nhập vật tư và số lượng để tính toán.</div>`;
                } else if (diff > 0) {
                    html = `
        <div class="alert alert-warning d-flex align-items-center mb-0 py-2">
            <i class="bi bi-exclamation-triangle-fill me-2"></i>
            <div class="small fw-bold">
                Ghi vào công nợ (HTX nợ NCC: \${formatter.format(diff)}
            </div>
        </div>`;
                } else {
                    html = `
        <div class="alert alert-success d-flex align-items-center mb-0 py-2">
            <i class="bi bi-check-circle-fill me-2"></i>
            <div class="small fw-bold">Đã thanh toán đủ (Không phát sinh nợ).</div>
        </div>`;
                }

                container.html(html);
            }

            function initWarehouseSelect(element) {
                $(element).select2({
                    theme: 'bootstrap-5',
                    placeholder: '-- Tìm kho --',
                    minimumInputLength: 1,
                    width: '100%',
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchWarehouseServlet',
                        type: 'POST',
                        dataType: 'json',
                        delay: 250,
                        data: params => ({term: params.term}),
                        processResults: data => ({
                                results: $.map(data, item => ({
                                        id: item.id,
                                        text: item.name + (item.location ? " (" + item.location + ")" : "")
                                    }))
                            }),
                        cache: true
                    },
                    language: {inputTooShort: () => "", noResults: () => "Không tìm thấy kho", searching: () => "Đang tìm..."}
                });
            }

            $(document).ready(function () {
                // A. NGÀY HIỆN TẠI
                const today = new Date();
                const localDate = new Date(today.getTime() - (today.getTimezoneOffset() * 60000)).toISOString().split('T')[0];
                $('#receiptDate').val(localDate);

                // B. SELECT2 - ĐỐI TÁC (Có hiển thị thông tin)
                $('#partnerSelect').select2({
                    theme: 'bootstrap-5',
                    minimumInputLength: 1,
                    placeholder: '-- Tìm đối tác --',
                    ajax: {
                        url: '${pageContext.request.contextPath}/PartnerServlet',
                        dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({
                                results: $.map(data, item => ({
                                        id: item.id, text: item.name,
                                        phone: item.phone || 'Chưa cập nhật',
                                        address: item.address || 'Chưa cập nhật'
                                    }))
                            })
                    }
                }).on('select2:select', function (e) {
                    const d = e.params.data;
                    $('#infoName').text(d.text);
                    $('#infoPhone').text(d.phone);
                    $('#infoAddress').text(d.address);
                    $('#partnerInfoBox').removeClass('d-none');
                }).on('select2:clear', function () {
                    $('#partnerInfoBox').addClass('d-none');
                });

                // C. SELECT2 - HỢP ĐỒNG (Có hiển thị thông tin)
                $('#contractSelect').select2({
                    theme: 'bootstrap-5',
                    minimumInputLength: 1,
                    placeholder: '-- Tìm hợp đồng --',
                    allowClear: true,
                    ajax: {
                        url: '${pageContext.request.contextPath}/ContractServlet',
                        dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({
                                results: $.map(data, item => ({
                                        id: item.id, text: item.contractCode,
                                        type: item.contractType, sign: item.signingDate,
                                        expiry: item.expiryDate, status: item.status
                                    }))
                            })
                    }
                }).on('select2:select', function (e) {
                    const d = e.params.data;
                    $('#dtType').text(d.type);
                    $('#dtStatus').text(d.status);
                    $('#dtSign').text(d.sign);
                    $('#dtExpiry').text(d.expiry);
                    $('#contractInfoBox').removeClass('d-none');
                }).on('select2:clear', function () {
                    $('#contractInfoBox').addClass('d-none');
                });

                // D. SELECT2 - VẬT TƯ
                $('#materialSelect').select2({
                    theme: 'bootstrap-5', minimumInputLength: 1,
                    placeholder: '-- Tìm vật tư --',
                    ajax: {
                        url: '${pageContext.request.contextPath}/SearchMaterialServlet',
                        type: 'POST', dataType: 'json',
                        data: params => ({term: params.term}),
                        processResults: data => ({results: $.map(data, item => ({id: item.id, text: item.name, unit: item.unit}))})
                    }
                }).on('select2:select', function (e) {
                    $('#unitDisplay').val(e.params.data.unit || '---');
                });

                // E. BẢNG PHÂN BỔ KHO
                initWarehouseSelect('.warehouse-select');

                $('#btnAddRow').click(function () {
                    const newRow = `
                        <tr class="distribution-row">
                            <td><select class="form-select warehouse-select" name="warehouseId[]" required><option value="">-- Tìm kho --</option></select></td>
                            <td><input type="number" class="form-control qty-input text-center" name="quantity[]" step="0.01" min="0.01" required></td>
                            <td><input type="number" class="form-control price-input text-end" name="importPrice[]" min="1" required></td>
                            <td class="subtotal text-end fw-bold text-secondary">0 đ</td>
                            <td class="text-center"><button type="button" class="btn btn-link text-danger p-0 remove-row"><i class="bi bi-trash"></i></button></td>
                        </tr>`;
                    const $row = $(newRow).appendTo('#distributionTable tbody');
                    initWarehouseSelect($row.find('.warehouse-select'));
                });

                $(document).on('click', '.remove-row', function () {
                    if ($('.distribution-row').length > 1) {
                        $(this).closest('tr').remove();
                        calculateGrandTotal();
                    }
                });

                $(document).on('input', '.qty-input, .price-input', calculateGrandTotal);
            });
            // BẮT SỰ KIỆN KHI BẤM LƯU PHIẾU
            $('#receiptForm').on('submit', function (e) {
                let firstPrice = null;
                let isConsistent = true;

                // Duyệt qua tất cả các ô đơn giá trong bảng
                $('.price-input').each(function () {
                    // Lấy giá trị (ép kiểu về số để so sánh chính xác)
                    let val = parseFloat($(this).val());

                    // Nếu ô đó trống thì bỏ qua (để thuộc tính required của HTML lo)
                    if (isNaN(val))
                        return;

                    if (firstPrice === null) {
                        // Gán giá trị đầu tiên làm chuẩn
                        firstPrice = val;
                    } else if (val !== firstPrice) {
                        // Nếu phát hiện giá khác với giá chuẩn -> Lỗi
                        isConsistent = false;

                        // Tô đỏ ô bị sai để người dùng dễ thấy
                        $(this).addClass('is-invalid');

                        // Dừng vòng lặp
                        return false;
                    }
                });

                if (!isConsistent) {
                    e.preventDefault(); // Chặn không cho gửi form
                    alert("LỖI: Đơn giá nhập của các kho phải GIỐNG NHAU!\nVui lòng kiểm tra lại.");

                    // Xóa class đỏ sau 3 giây cho đẹp
                    setTimeout(() => $('.price-input').removeClass('is-invalid'), 3000);
                }
            });
        </script>
        <script>
            $(document).ready(function () {
                const urlParams = new URLSearchParams(window.location.search);
                const status = urlParams.get('status');

                if (status === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Phiếu nhập kho đã được lưu và cập nhật tồn kho.',
                        confirmButtonColor: '#198754'
                    });

                    // --- DÒNG QUAN TRỌNG NHẤT: Xóa tham số trên URL mà không load lại trang ---
                    window.history.replaceState({}, document.title, window.location.pathname);
                } else if (status === 'error') {
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi!',
                        text: 'Không thể lưu phiếu nhập. Vui lòng thử lại!',
                        confirmButtonColor: '#d33'
                    });

                    // Xóa tham số lỗi luôn để load lại không hiện lại
                    window.history.replaceState({}, document.title, window.location.pathname);
                }
            });
        </script>
    </body>
</html>