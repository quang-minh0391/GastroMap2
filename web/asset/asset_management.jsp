<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
<%@include file="../common/header.jsp" %>

<style>
    /* Chỉ giữ lại CSS riêng cho trang này, bỏ CSS body */
    .dashboard-asset { display: flex; gap: 20px; margin-bottom: 30px; }
    .card-asset { flex: 1; padding: 20px; border-radius: 8px; color: white; text-align: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    .card-asset h3 { margin: 0; font-size: 1.1rem; opacity: 0.9; }
    .card-asset .number { font-size: 2rem; font-weight: bold; margin-top: 10px; }
    
    .bg-primary { background-color: #007bff; }
    .bg-success { background-color: #28a745; }
    .bg-warning { background-color: #ffc107; color: #333 !important; }
    .bg-danger { background-color: #dc3545; }
    .bg-secondary { background-color: #6c757d; }

    .main-content-asset { display: flex; gap: 20px; }
    .form-section-asset { flex: 1; background: white; padding: 20px; border-radius: 8px; height: fit-content; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    .table-section-asset { flex: 3; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }

    /* Form Styles */
    .form-asset input, .form-asset select { width: 100%; padding: 8px; margin: 5px 0 15px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
    .form-asset label { font-weight: bold; display: block; margin-top: 5px; }
    .form-asset button { width: 100%; padding: 10px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
    .form-asset button:hover { background: #0056b3; }

    /* Table Styles */
    .table-asset { width: 100%; border-collapse: collapse; margin-top: 10px; }
    .table-asset th, .table-asset td { padding: 10px; border-bottom: 1px solid #eee; text-align: left; font-size: 14px; }
    .table-asset th { background-color: #f8f9fa; cursor: pointer; }
    
    .badge { padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; display: inline-block;}
    .status-ACTIVE { background: #d4edda; color: #155724; }
    .status-MAINTENANCE { background: #fff3cd; color: #856404; }
    .status-BROKEN { background: #f8d7da; color: #721c24; }
    .status-LIQUIDATED { background: #e2e3e5; color: #383d41; text-decoration: line-through; }
</style>

<script>
    function sortTable(column) {
        var currentSort = document.getElementById('sortBy').value;
        var currentOrder = document.getElementById('sortOrder').value;
        if (currentSort === column) {
            document.getElementById('sortOrder').value = (currentOrder === 'ASC' ? 'DESC' : 'ASC');
        } else {
            document.getElementById('sortBy').value = column;
            document.getElementById('sortOrder').value = 'ASC';
        }
        document.getElementById('filterForm').submit();
    }
</script>

<div class="container-fluid p-4">
    <h2 class="mb-4 text-dark fw-bold">Quản lý Tài sản & Thiết bị</h2>

    <div class="dashboard-asset">
        <div class="card-asset bg-primary">
            <h3>Tổng Tài Sản</h3>
            <div class="number">${grandTotalAssets}</div> </div>
        <div class="card-asset bg-success">
            <h3>Đang sử dụng</h3>
            <div class="number">${stats['ACTIVE'] == null ? 0 : stats['ACTIVE']}</div>
        </div>
        <div class="card-asset bg-warning">
            <h3>Đang bảo trì</h3>
            <div class="number">${stats['MAINTENANCE'] == null ? 0 : stats['MAINTENANCE']}</div>
        </div>
        <div class="card-asset bg-danger">
            <h3>Hỏng hóc</h3>
            <div class="number">${stats['BROKEN'] == null ? 0 : stats['BROKEN']}</div>
        </div>
        <div class="card-asset bg-secondary">
            <h3>Đã thanh lý</h3>
            <div class="number">${stats['LIQUIDATED'] == null ? 0 : stats['LIQUIDATED']}</div>
        </div>
    </div>

    <div class="main-content-asset">
        
        <div class="form-section-asset">
            <h4 class="mt-0 mb-3 border-bottom pb-2">➕ Thêm Tài Sản Mới</h4>
            <form action="AssetServlet" method="POST" class="form-asset">
                <label>Mã Tài Sản:</label>
                <input type="text" name="code" required placeholder="VD: MC01">

                <label>Tên Tài Sản:</label>
                <input type="text" name="name" required placeholder="VD: Máy cày Kubota">

                <%-- Thay thế ô select cũ bằng input list --%>
                <label>Loại Tài Sản :</label>
                <input type="text" name="category_name" list="category_list" required placeholder="Gõ tên loại mới hoặc chọn..." autocomplete="off">
                <datalist id="category_list">
                    <c:forEach items="${categories}" var="cat">
                        <option value="${cat.name}"></option>
                    </c:forEach>
                </datalist>

                <label>Ngày mua:</label>
                <input type="date" name="purchase_date" required>

                <label>Giá trị (VNĐ):</label>
                <input type="number" name="price" required min="0">

                <label>Trạng thái ban đầu:</label>
                <select name="status">
                    <option value="ACTIVE">Đang sử dụng</option>
                    <option value="MAINTENANCE">Đang bảo trì</option>
                    <option value="BROKEN">Đã hỏng</option>
                </select>

                <label>Vị trí đặt:</label>
                <input type="text" name="location" placeholder="Kho A, Cánh đồng...">

                <button type="submit" style="margin-top: 10px;">Lưu thông tin</button>
            </form>
        </div>

        <div class="table-section-asset">
            <h4 class="mt-0 mb-3">
                📋 Danh sách Máy móc & Thiết bị 
            </h4>

            <form id="filterForm" action="AssetServlet" method="GET" style="background:#f8f9fa; padding:15px; border-radius:4px; margin-bottom: 15px; border: 1px solid #dee2e6;">
                <input type="hidden" id="sortBy" name="sortBy" value="${sortBy}">
                <input type="hidden" id="sortOrder" name="sortOrder" value="${sortOrder}">
                
                <div style="display: flex; gap: 10px; flex-wrap: wrap; align-items: flex-end;">
                    <%-- Các ô lọc cũ --%>
                    <div style="flex: 1; min-width: 100px;">
                         <input type="text" name="f_code" value="${f_code}" placeholder="Mã..." style="width: 100%; margin:0; padding: 6px; border: 1px solid #ccc; border-radius: 4px;">
                    </div>
                    <div style="flex: 2; min-width: 150px;">
                        <input type="text" name="f_name" value="${f_name}" placeholder="Tên thiết bị..." style="width: 100%; margin:0; padding: 6px; border: 1px solid #ccc; border-radius: 4px;">
                    </div>
                    <div style="flex: 1; min-width: 120px;">
                        <select name="f_status" style="width: 100%; margin:0; padding: 6px; border: 1px solid #ccc; border-radius: 4px;">
                            <option value="">-- Trạng thái --</option>
                            <option value="ACTIVE" ${f_status == 'ACTIVE' ? 'selected' : ''}>Đang sử dụng</option>
                            <option value="MAINTENANCE" ${f_status == 'MAINTENANCE' ? 'selected' : ''}>Bảo trì</option>
                            <option value="BROKEN" ${f_status == 'BROKEN' ? 'selected' : ''}>Hỏng</option>
                            <option value="LIQUIDATED" ${f_status == 'LIQUIDATED' ? 'selected' : ''}>Thanh lý</option>
                        </select>
                    </div>

                    <%-- --- [MỚI] THÊM LỌC NGÀY MUA --- --%>
                    <div style="display: flex; align-items: center; gap: 5px; background: white; padding: 5px; border: 1px solid #ddd; border-radius: 4px;">
                        <!--<span style="font-size: 13px; color: #555;">Ngày mua:</span>-->
                        <input type="date" name="f_date_from" value="${f_date_from}" style="padding: 4px; border: 1px solid #ccc; border-radius: 3px;">
                        <span>➜</span>
                        <input type="date" name="f_date_to" value="${f_date_to}" style="padding: 4px; border: 1px solid #ccc; border-radius: 3px;">
                    </div>
                    <%-- [MỚI] THÊM BỘ LỌC GIÁ TRỊ TẠI ĐÂY --%>
                    <div style="display: flex; align-items: center; gap: 5px; background: white; padding: 5px; border: 1px solid #ddd; border-radius: 4px;">
                        <span style="font-size: 13px; color: #555;">Giá tiền:</span>
                        <input type="number" name="f_price_from" value="${f_price_from}" placeholder="Min" min="0" step="1000" style="width: 80px; padding: 4px; border: 1px solid #ccc; border-radius: 3px;">
                        <span>-</span>
                        <input type="number" name="f_price_to" value="${f_price_to}" placeholder="Max" min="0" step="1000" style="width: 80px; padding: 4px; border: 1px solid #ccc; border-radius: 3px;">
                    </div>
                    <%-- --------------------------------- --%>

                    <button type="submit" style="width: auto; padding: 6px 15px; background: #007bff; color: white; border: none; border-radius: 4px;">🔍 Lọc</button>
                    <a href="AssetServlet" style="align-self: center; font-size: 13px; color: red; text-decoration: none; margin-left: 5px;">[Xóa lọc]</a>
                </div>
            </form>

            <table class="table-asset">
                <thead>
                    <tr>
                        <th onclick="sortTable('code')">Mã ${sortBy == 'code' ? (sortOrder == 'ASC' ? '▲' : '▼') : ''}</th>
                        <th onclick="sortTable('name')">Tên thiết bị ${sortBy == 'name' ? (sortOrder == 'ASC' ? '▲' : '▼') : ''}</th>
                        <th onclick="sortTable('purchase_date')">Ngày mua</th>
                        <th onclick="sortTable('current_value')">Giá trị</th>
                        <th onclick="sortTable('status')">Trạng thái</th>
                        <th>Vị trí</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${assets}" var="a">
                        <tr>
                            <td><strong>${a.code}</strong></td>
                            <td>${a.name}</td>
                            <td><fmt:formatDate value="${a.purchaseDate}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatNumber value="${a.currentValue}" type="currency" currencySymbol="₫"/></td>
                            <td>
                                <span class="badge status-${a.status}">
                                    <c:choose>
                                        <c:when test="${a.status == 'ACTIVE'}">Tốt</c:when>
                                        <c:when test="${a.status == 'MAINTENANCE'}">Bảo trì</c:when>
                                        <c:when test="${a.status == 'BROKEN'}">Hỏng</c:when>
                                        <c:when test="${a.status == 'LIQUIDATED'}">Đã Thanh lý</c:when>
                                        <c:otherwise>${a.status}</c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td>${a.location}</td>
                            <td>
                                <c:choose>
                                    <%-- NẾU ĐÃ THANH LÝ -> HIỆN NÚT MỞ MODAL --%>
                                        <c:when test="${a.status == 'LIQUIDATED'}">
                                            <button type="button" 
                                                    onclick="openRepurchaseModal('${a.id}', '${a.name}', ${a.currentValue})"
                                                    style="background:#17a2b8; color:white; border:none; padding:5px 10px; border-radius:4px; font-size:12px; cursor:pointer;">
                                                ↩ Mua lại
                                            </button>
                                        </c:when>

                                    <%-- NẾU CHƯA THANH LÝ -> HIỆN BẢO TRÌ & THANH LÝ --%>
                                    <c:otherwise>
                                        <a href="maintenance?asset_id=${a.id}" 
                                           style="display:inline-block; margin-bottom:5px; text-decoration: none; color: white; background: #28a745; padding: 4px 8px; border-radius: 4px; font-size: 11px;">
                                            🛠 Bảo trì
                                        </a>

                                        <form action="AssetServlet" method="POST" style="display:inline;">
                                            <input type="hidden" name="action" value="liquidate">
                                            <input type="hidden" name="asset_id" value="${a.id}">
                                            <input type="hidden" name="asset_name" value="${a.name}">
                                            <input type="hidden" name="price" value="0"> 
                                            <button type="button" onclick="let p = prompt('Giá bán thanh lý (VNĐ):', '0'); if(p){ this.form.price.value=p; this.form.submit(); }"
                                                    style="background: #6c757d; color: white; border:none; padding:4px 8px; border-radius: 4px; font-size: 11px; cursor:pointer; width: auto;">
                                                💰 Thanh lý
                                            </button>
                                        </form>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty assets}">
                        <tr><td colspan="7" style="text-align: center; color: red; padding: 20px;">Không tìm thấy tài sản nào!</td></tr>
                    </c:if>
                        <div id="repurchaseModal" class="modal" style="display:none; position:fixed; z-index:1000; left:0; top:0; width:100%; height:100%; background:rgba(0,0,0,0.5);">
    <div style="background:white; margin:10% auto; padding:20px; width:400px; border-radius:8px; box-shadow:0 5px 15px rgba(0,0,0,0.3);">
        <h3 style="margin-top:0;">↩ Mua lại Tài sản Thanh lý</h3>
        <form action="AssetServlet" method="POST">
            <input type="hidden" name="action" value="repurchase">
            <input type="hidden" name="asset_id" id="modal_asset_id">
            <input type="hidden" name="asset_name" id="modal_asset_name">
            
            <p>Bạn đang mua lại: <b id="display_name" style="color:#007bff"></b></p>
            
            <label style="display:block; margin-top:10px;">Trạng thái sau khi mua:</label>
            <select name="target_status" style="width:100%; padding:8px; margin-bottom:10px;">
                <option value="ACTIVE">✅ Tốt (Đang sử dụng)</option>
                <option value="BROKEN">❌ Hỏng (Cần sửa)</option>
            </select>
            
            <label style="display:block;">Giá mua lại (VNĐ):</label>
            <input type="number" name="price" id="modal_price" required min="0" style="width:100%; padding:8px; margin-bottom:15px;">
            
            <div style="text-align:right;">
                <button type="button" onclick="document.getElementById('repurchaseModal').style.display='none'" style="background:#ccc; border:none; padding:8px 15px; cursor:pointer; border-radius:4px;">Hủy</button>
                <button type="submit" style="background:#28a745; color:white; border:none; padding:8px 15px; cursor:pointer; border-radius:4px;">Xác nhận Mua</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openRepurchaseModal(id, name, currentValue) {
        document.getElementById('modal_asset_id').value = id;
        document.getElementById('modal_asset_name').value = name;
        document.getElementById('display_name').innerText = name;
        document.getElementById('modal_price').value = currentValue; // Gợi ý giá cũ
        document.getElementById('repurchaseModal').style.display = 'block';
    }
</script>
                </tbody>
            </table>

           <div style="margin-top: 20px; text-align: center;">
                <c:if test="${totalPage > 1}">
                    <c:if test="${pageIndex > 1}">
                        <%-- Thêm &f_price_from=${f_price_from}&f_price_to=${f_price_to} vào link --%>
                        <a href="AssetServlet?page=${pageIndex - 1}&f_code=${f_code}&f_name=${f_name}&f_status=${f_status}&sortBy=${sortBy}&sortOrder=${sortOrder}&f_date_from=${f_date_from}&f_date_to=${f_date_to}&f_price_from=${f_price_from}&f_price_to=${f_price_to}" 
                           style="padding:8px 12px; background:#007bff; text-decoration:none; border-radius:4px; color: white; border: 1px solid #ddd;">« Trước</a>
                    </c:if>
                    
                    <span style="margin:0 10px; font-weight:bold;">Trang ${pageIndex} / ${totalPage}</span>
                    
                    <c:if test="${pageIndex < totalPage}">
                        <a href="AssetServlet?page=${pageIndex + 1}&f_code=${f_code}&f_name=${f_name}&f_status=${f_status}&sortBy=${sortBy}&sortOrder=${sortOrder}&f_date_from=${f_date_from}&f_date_to=${f_date_to}&f_price_from=${f_price_from}&f_price_to=${f_price_to}" 
                           style="padding:8px 12px; background:#007bff; text-decoration:none; border-radius:4px; color: white; border: 1px solid #ddd;">Sau »</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>

</div> </body>
</html>