<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thu Mua & Nhập Kho - GastroMap2</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <link href="https://cdn.jsdelivr.net/npm/select2-bootstrap-5-theme@1.3.0/dist/select2-bootstrap-5-theme.min.css" rel="stylesheet" />

        <style>
            .receipt-card {
                border: none;
                box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            }
            .header-bg {
                background: linear-gradient(135deg, #198754 0%, #157347 100%);
                color: white;
            }
            .step-badge {
                font-size: 0.7rem;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
            .total-display-box {
                background-color: #e8f5e9;
                border: 2px dashed #198754;
                border-radius: 8px;
                padding: 15px;
                text-align: center;
            }
            .total-value {
                font-size: 1.8rem;
                font-weight: 800;
                color: #198754;
            }
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
                        <div class="card receipt-card">
                            <div class="card-header header-bg py-3">
                                <div class="d-flex justify-content-between align-items-center">
                                    <div>
                                        <h4 class="mb-0 fw-bold"><i class="bi bi-box-seam"></i> PHIẾU THU MUA & TẠO LÔ</h4>
                                        <small class="opacity-75">Quy trình: Mua -> Tạo Lô -> Nhập kho -> Tồn kho -> QR</small>
                                    </div>
                                    <div class="text-end">
                                        <div class="badge bg-warning text-dark mb-1">TRẠNG THÁI: MỚI</div>
                                        <div class="small opacity-75">Ngày lập: <span id="currentDateDisplay"></span></div>
                                    </div>
                                </div>
                            </div>

                            <div class="card-body p-4">
                                <div class="d-flex align-items-center mb-3">
                                    <span class="badge bg-success step-badge me-2">BƯỚC 1 & 2</span>
                                    <h6 class="text-success fw-bold mb-0">THÔNG TIN LÔ HÀNG & NGUỒN GỐC</h6>
                                </div>

                                <div class="row g-3 mb-4 border rounded p-3 bg-white shadow-sm">
                                    <div class="col-md-3">
                                        <label class="form-label fw-bold small text-muted">Mã Lô sản xuất (Auto)</label>
                                        <input type="text" class="form-control fw-bold text-success bg-light" value="BATCH-AUTO" readonly>
                                        <small class="text-muted" style="font-size: 11px;">*Mã sẽ sinh tự động khi lưu</small>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label fw-bold small">Ngày thu hoạch (*)</label>
                                        <input type="date" class="form-control" name="purchaseDate" id="purchaseDate" required>
                                    </div>
                                    <div class="col-md-3">
                                        <label class="form-label fw-bold small text-danger">Ngày hết hạn (*)</label>
                                        <input type="date" class="form-control" name="expiryDate" id="expiryDate" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold small">Nông dân / Thành viên (*)</label>
                                        <select class="form-select" name="memberId" id="farmerSelect" required></select>
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
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Sản phẩm nông sản (*)</label>
                                        <select class="form-select" name="productId" id="productSelect" required></select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Đơn vị tính</label>
                                        <input type="text" id="unitDisplay" name="unit" class="form-control bg-light" readonly placeholder="---">
                                    </div>
                                    <div class="col-12 d-none" id="memberInfoBox">
                                        <div class="alert alert-light border border-success d-flex justify-content-between align-items-center py-2 mb-0">
                                            <div><strong><i class="bi bi-person-check-fill text-success"></i> <span id="mInfoName"></span></strong> | SĐT: <span id="mInfoPhone"></span></div>
                                            <div class="text-danger fw-bold"> <span id="mInfoDebt">0</span></div>
                                        </div>
                                    </div>
                                </div>

                                <div class="d-flex align-items-center mb-3">
                                    <span class="badge bg-primary step-badge me-2">BƯỚC 3 & 4</span>
                                    <h6 class="text-primary fw-bold mb-0">PHÂN BỔ NHẬP KHO</h6>
                                </div>

                                <div class="table-responsive mb-3">
                                    <table class="table table-bordered align-middle table-hover" id="distributionTable">
                                        <thead class="table-light">
                                            <tr class="text-center small fw-bold text-secondary">
                                                <th style="width: 35%;">Kho lưu trữ (*)</th>
                                                <th style="width: 15%;">Số lượng nhập (*)</th>
                                                <th style="width: 20%;">Đơn giá mua (VNĐ)</th>
                                                <th style="width: 20%;">Thành tiền</th>
                                                <th style="width: 5%;">Xóa</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr class="distribution-row">
                                                <td><select class="form-select warehouse-select" name="warehouseId[]" required disabled></select></td>
                                                <td><input type="number" class="form-control qty-input text-center fw-bold" name="quantity[]" step="0.01" min="0.01" required></td>
                                                <td>
                                                    <input type="text" class="form-control price-input text-end fw-bold" name="buyPriceRaw[]" placeholder="0" required oninput="formatBuyPrice(this)">
                                                    <input type="hidden" name="buyPrice[]" class="price-hidden">
                                                </td>
                                                <td class="subtotal text-end fw-bold text-secondary">0 đ</td>
                                                <td class="text-center"><button type="button" class="btn btn-sm btn-outline-danger remove-row"><i class="bi bi-trash"></i></button></td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <button type="button" class="btn btn-outline-primary btn-sm fw-bold" id="btnAddRow" disabled><i class="bi bi-plus-circle"></i> Thêm vị trí kho</button>
                                </div>

                                <div class="form-check form-switch mb-4 p-3 bg-white border rounded shadow-sm">
                                    <input class="form-check-input" type="checkbox" id="createQR" name="createQR" checked>
                                    <label class="form-check-label fw-bold text-dark" for="createQR">
                                        <i class="bi bi-qr-code-scan text-primary me-2"></i>
                                        BƯỚC 5: Tự động tạo mã QR truy xuất nguồn gốc
                                    </label>
                                    <div class="small text-muted ms-4">Hệ thống sẽ sinh mã QR và lưu vào bảng <code>batch_qr_codes</code>.</div>
                                </div>

                                <div class="row">
                                    <div class="col-md-5">
                                        <div class="total-display-box shadow-sm h-100 d-flex flex-column justify-content-center">
                                            <div class="total-label fw-bold">TỔNG GIÁ TRỊ LÔ HÀNG</div>
                                            <div class="total-value my-2" id="displayTotal">0 đ</div>
                                            <input type="hidden" name="totalAmount" id="hiddenTotal">
                                        </div>
                                    </div>
                                    <div class="col-md-7">
                                        <div class="card border h-100 shadow-sm">
                                            <div class="card-header bg-light fw-bold small text-muted">THANH TOÁN & ĐỐI TRỪ</div>
                                            <div class="card-body">
                                                <div class="mb-3">
                                                    <label class="form-label fw-bold small">HTX trả ngay:</label>
                                                    <div class="input-group">
                                                        <input type="text" id="amountPaid" class="form-control fw-bold text-primary" value="0" oninput="formatPaymentInput(this)">
                                                        <span class="input-group-text">VNĐ</span>
                                                    </div>
                                                    <input type="hidden" name="amountPaid" id="hiddenAmountPaid" value="0">
                                                </div>
                                                <div class="d-flex justify-content-between align-items-center p-2 rounded bg-light border">
                                                    <span class="small fw-bold">Dư nợ còn lại (Ghi vào sổ nợ):</span>
                                                    <span class="fw-bold text-danger" id="debtDisplay">0 đ</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="mt-4">
                                    <label class="form-label fw-bold small">Ghi chú phiếu:</label>
                                    <textarea class="form-control" name="note" rows="2" placeholder="Ví dụ: Lúa thu hoạch vụ Đông Xuân, độ ẩm 14.5%..."></textarea>
                                </div>
                            </div>

                            <div class="card-footer bg-white p-3 text-end">
                                <button type="button" class="btn btn-secondary me-2" onclick="history.back()">Hủy bỏ</button>
                                <button type="submit" class="btn btn-success btn-lg px-4 fw-bold shadow">
                                    <i class="bi bi-check-circle-fill me-2"></i>HOÀN TẤT THU MUA
                                </button>
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

                                    function formatBuyPrice(input) {
                                        let value = input.value.replace(/\D/g, "");
                                        if (value === "")
                                            value = 0;
                                        let numValue = parseInt(value);
                                        input.value = new Intl.NumberFormat('vi-VN').format(numValue);
                                        $(input).siblings('.price-hidden').val(numValue);
                                        calculateTotals();
                                    }

                                    function calculateTotals() {
                                        let grandTotal = 0;
                                        $('.distribution-row').each(function () {
                                            const qty = parseFloat($(this).find('.qty-input').val()) || 0;
                                            const price = parseFloat($(this).find('.price-hidden').val()) || 0;
                                            const subtotal = qty * price;
                                            $(this).find('.subtotal').text(formatter.format(subtotal));
                                            grandTotal += subtotal;
                                        });
                                        $('#displayTotal').text(formatter.format(grandTotal));
                                        $('#hiddenTotal').val(grandTotal);
                                        let paid = parseFloat($('#hiddenAmountPaid').val()) || 0;
                                        if (paid > grandTotal) {
                                            paid = grandTotal;
                                            $('#hiddenAmountPaid').val(paid);
                                            $('#amountPaid').val(new Intl.NumberFormat('vi-VN').format(paid));
                                        }
                                        $('#debtDisplay').text(formatter.format(grandTotal - paid));
                                    }

                                    function formatPaymentInput(input) {
                                        let val = parseInt(input.value.replace(/\D/g, '')) || 0;
                                        let total = parseFloat($('#hiddenTotal').val()) || 0;
                                        if (val > total)
                                            val = total;
                                        input.value = new Intl.NumberFormat('vi-VN').format(val);
                                        $('#hiddenAmountPaid').val(val);
                                        calculateTotals();
                                    }

                                    function initWarehouseSelect(el) {
                                        $(el).select2({
                                            theme: 'bootstrap-5', placeholder: 'Chọn kho nhập', width: '100%',
                                            ajax: {
                                                url: '${pageContext.request.contextPath}/SearchProductWarehouseServlet',
                                                type: 'POST', dataType: 'json',
                                                processResults: data => ({results: $.map(data, item => ({id: item.id, text: item.name}))})
                                            }
                                        });
                                    }

                                    $(document).ready(function () {
                                        $('#currentDateDisplay').text(new Date().toLocaleDateString('vi-VN'));
                                        $('#purchaseDate').val(new Date().toISOString().split('T')[0]);

                                        $('#farmerSelect').select2({
                                            theme: 'bootstrap-5', placeholder: 'Tìm nông dân...',
                                            ajax: {
                                                url: '${pageContext.request.contextPath}/SearchMemberServlet',
                                                dataType: 'json', data: params => ({term: params.term}),
                                                processResults: data => ({results: $.map(data, item => ({id: item.id, text: item.full_name, phone: item.phone, debt: item.current_debt || 0}))})
                                            }
                                        }).on('select2:select', function (e) {
                                            const d = e.params.data;

                                            $('#mInfoName').text(d.text);
                                            $('#mInfoPhone').text(d.phone);

                                            const debt = d.debt || 0;

                                            if (debt < 0) {
                                                $('#mInfoDebt').text('HTX nợ nông dân: ' + formatter.format(Math.abs(debt)));
                                            } else if (debt > 0) {
                                                $('#mInfoDebt').text('Nông dân nợ HTX: ' + formatter.format(debt));
                                            } else {
                                                $('#mInfoDebt').text('Không có công nợ');
                                            }

                                            $('#memberInfoBox').removeClass('d-none');
                                        });


                                        $('#productSelect').select2({
                                            theme: 'bootstrap-5', placeholder: 'Chọn nông sản...',
                                            ajax: {
                                                url: '${pageContext.request.contextPath}/SearchProductServlet',
                                                type: 'POST', dataType: 'json', data: params => ({term: params.term}),
                                                processResults: data => ({results: $.map(data, item => ({id: item.id, text: item.name, unit: item.unit}))})
                                            }
                                        }).on('select2:select', function (e) {
                                            $('#unitDisplay').val(e.params.data.unit);
                                            $('#btnAddRow').prop('disabled', false);
                                            $('.warehouse-select').prop('disabled', false).empty().append('<option value="">Chọn kho nhập</option>');
                                            initWarehouseSelect('.warehouse-select');
                                        });

                                        $('#btnAddRow').click(function () {
                                            const row = `
                        <tr class="distribution-row">
                            <td><select class="form-select warehouse-select" name="warehouseId[]" required></select></td>
                            <td><input type="number" class="form-control qty-input text-center fw-bold" name="quantity[]" step="0.01" min="0.01" required></td>
                            <td>
                                <input type="text" class="form-control price-input text-end fw-bold" name="buyPriceRaw[]" placeholder="0" required oninput="formatBuyPrice(this)">
                                <input type="hidden" name="buyPrice[]" class="price-hidden">
                            </td>
                            <td class="subtotal text-end fw-bold text-secondary">0 đ</td>
                            <td class="text-center"><button type="button" class="btn btn-sm btn-outline-danger remove-row"><i class="bi bi-trash"></i></button></td>
                        </tr>`;
                                            const $el = $(row).appendTo('#distributionTable tbody');
                                            initWarehouseSelect($el.find('.warehouse-select'));
                                        });

                                        $(document).on('click', '.remove-row', function () {
                                            if ($('#distributionTable tbody tr').length > 1) {
                                                $(this).closest('tr').remove();
                                                calculateTotals();
                                            }
                                        });

                                        $(document).on('input', '.qty-input', calculateTotals);

                                        // KIỂM TRA TRƯỚC KHI LƯU
                                        $('#purchaseForm').on('submit', function (e) {
                                            let prices = [];
                                            let isValidPrice = true;
                                            let isNegative = false;

                                            const harvestDate = new Date($('#purchaseDate').val());
                                            const expiryDate = new Date($('#expiryDate').val());

                                            if (expiryDate <= harvestDate) {
                                                e.preventDefault();
                                                Swal.fire({icon: 'error', title: 'Ngày không hợp lệ', text: 'Ngày hết hạn phải sau ngày thu hoạch!'});
                                                return false;
                                            }

                                            $('.price-hidden').each(function () {
                                                let val = parseFloat($(this).val());
                                                if (val <= 0)
                                                    isNegative = true;
                                                prices.push(val);
                                            });
                                            $('.qty-input').each(function () {
                                                if (parseFloat($(this).val()) <= 0)
                                                    isNegative = true;
                                            });

                                            if (isNegative) {
                                                e.preventDefault();
                                                Swal.fire({icon: 'warning', title: 'Dữ liệu lỗi', text: 'Số lượng và đơn giá phải lớn hơn 0!'});
                                                return false;
                                            }

                                            if (prices.length > 0) {
                                                const firstPrice = prices[0];
                                                for (let i = 1; i < prices.length; i++) {
                                                    if (prices[i] !== firstPrice) {
                                                        isValidPrice = false;
                                                        break;
                                                    }
                                                }
                                            }

                                            if (!isValidPrice) {
                                                e.preventDefault();
                                                Swal.fire({icon: 'error', title: 'Đơn giá khác nhau', text: 'Trong một lô hàng, đơn giá mua vào các kho phải giống nhau!'});
                                                return false;
                                            }
                                        });

                                        // --- XỬ LÝ THÔNG BÁO POPUP (Đã bổ sung phần báo Lỗi) ---
                                        const urlParams = new URLSearchParams(window.location.search);
                                        const status = urlParams.get('status');

                                        if (status === 'success') {
                                            Swal.fire({
                                                icon: 'success',
                                                title: 'Hoàn tất!',
                                                text: 'Phiếu thu mua đã được tạo thành công.',
                                                confirmButtonColor: '#198754'
                                            });
                                            // Xóa param trên URL để F5 không hiện lại
                                            window.history.replaceState({}, document.title, window.location.pathname);
                                        } else if (status === 'error') {
                                            Swal.fire({
                                                icon: 'error',
                                                title: 'Thất bại!',
                                                text: 'Có lỗi xảy ra khi lưu phiếu. Vui lòng kiểm tra lại!',
                                                confirmButtonColor: '#dc3545'
                                            });
                                            window.history.replaceState({}, document.title, window.location.pathname);
                                        }
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
        </script>
    </body>
</html>