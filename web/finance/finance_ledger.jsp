<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../common/header.jsp" %>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.9.1/chart.min.js"></script>

<style>
    /* CSS Riêng cho Finance */
    .dashboard-fin { display: flex; gap: 20px; margin-bottom: 30px; }
    .card-fin { flex: 1; padding: 20px; border-radius: 8px; color: white; text-align: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    .card-fin h3 { margin: 0; font-size: 1.2rem; opacity: 0.9; }
    .card-fin .money { font-size: 2rem; font-weight: bold; margin-top: 10px; }
    .bg-green { background-color: #28a745; }
    .bg-red { background-color: #dc3545; }
    .bg-blue { background-color: #17a2b8; }
    
    .main-content-fin { display: flex; gap: 30px; }
    .form-section-fin { flex: 1; background: white; padding: 20px; border-radius: 8px; height: fit-content; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    .table-section-fin { flex: 2; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    
    .table-fin { width: 100%; border-collapse: collapse; background: white; }
    .table-fin th, .table-fin td { padding: 12px; border-bottom: 1px solid #eee; text-align: left; }
    .table-fin th { background-color: #f8f9fa; cursor: pointer; user-select: none; }
    .badge-in { background: #d4edda; color: #155724; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 12px; }
    .badge-out { background: #f8d7da; color: #721c24; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 12px; }
    
    input, select, textarea { width: 100%; padding: 8px; margin: 8px 0 15px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
    .btn-save { width: 100%; padding: 10px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; }
    .btn-submit-fin { width: 100%; padding: 10px; border: none; border-radius: 4px; color: white; cursor: pointer; font-size: 16px; margin-top: 10px; }
    .btn-in { background: #28a745; }
    .btn-out { background: #dc3545; }
    /* CSS cho Dropdown Tìm kiếm (Copy style từ bên Fund qua) */
    .search-dropdown-container { position: relative; width: 100%; margin-bottom: 15px; }
    
    /* Ô nhập liệu */
    .search-input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
    
    /* Khung danh sách thả xuống */
    .dropdown-list { 
        display: none; 
        position: absolute; 
        width: 100%; 
        max-height: 200px; 
        overflow-y: auto; 
        background: white; 
        border: 1px solid #ddd; 
        border-top: none; 
        z-index: 1000; 
        box-shadow: 0 4px 6px rgba(0,0,0,0.1); 
        border-radius: 0 0 4px 4px;
    }
    
    /* Từng dòng trong danh sách */
    .dropdown-item { 
        padding: 10px; 
        cursor: pointer; 
        border-bottom: 1px solid #f1f1f1; 
        font-size: 14px; 
        color: #007bff; /* Chữ màu xanh như yêu cầu */
        font-weight: 500;
    }
    
    .dropdown-item:hover { background-color: #f8f9fa; color: #0056b3; }
    
    /* Style chung cho phần Thu/Chi */
    .dropdown-item span { 
        font-size: 0.9em; 
        font-weight: bold; 
        float: right; 
    }

    /* [MỚI] Class riêng cho màu sắc */
    .type-in { color: #28a745; /* Xanh lá */ }
    .type-out { color: #dc3545; /* Đỏ */ }
</style>

<script>
    function sortTable(column) {
        var currentSort = document.getElementById('sortBy').value;
        var currentOrder = document.getElementById('sortOrder').value;
        if (currentSort === column) {
            document.getElementById('sortOrder').value = (currentOrder === 'ASC' ? 'DESC' : 'ASC');
        } else {
            document.getElementById('sortBy').value = column;
            document.getElementById('sortOrder').value = 'DESC'; 
        }
        document.getElementById('filterForm').submit();
    }
</script>

<div class="container-fluid p-4">
    <h2 class="mb-4 text-dark fw-bold">Sổ Cái Tài Chính</h2>

    <div class="dashboard-fin">
        <div class="card-fin bg-green"><h3>Tổng Thu (IN)</h3><div class="money"><fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/></div></div>
        <div class="card-fin bg-red"><h3>Tổng Chi (OUT)</h3><div class="money"><fmt:formatNumber value="${totalExpense}" type="currency" currencySymbol="₫"/></div></div>
        <div class="card-fin bg-blue"><h3>Số Dư Hiện Tại</h3><div class="money"><fmt:formatNumber value="${balance}" type="currency" currencySymbol="₫"/></div></div>
    </div>

    <div style="background: white; padding: 20px; margin-bottom: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
            <h4 style="margin: 0;">📊 Biểu đồ: <span id="chartTitle" style="color: #007bff;">Theo Tháng (Năm ${currentYear})</span></h4>
            <div class="btn-group" role="group">
                <button onclick="switchChart('MONTH')" id="btnMonth" style="width:auto; padding: 6px 15px; background: #007bff; color: white; border: 1px solid #007bff; cursor: pointer; border-radius: 4px 0 0 4px;">Tháng</button>
                <button onclick="switchChart('YEAR')" id="btnYear" style="width:auto; padding: 6px 15px; background: white; color: #007bff; border: 1px solid #007bff; cursor: pointer; border-radius: 0 4px 4px 0;">Năm</button>
            </div>
        </div>
        <div style="height: 350px;"><canvas id="financeChart"></canvas></div>
    </div>

    <script>
        const dataYear = { labels: ${yLabels}, rev: ${yRev}, exp: ${yExp}, bal: ${yBal} };
        const dataMonth = { labels: ${mLabels}, rev: ${mRev}, exp: ${mExp}, bal: ${mBal} };

        const ctx = document.getElementById('financeChart').getContext('2d');
        let myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: dataMonth.labels,
                datasets: [
                    {label: 'Tổng Thu', data: dataMonth.rev, borderColor: '#28a745', backgroundColor: '#28a745', tension: 0.3},
                    {label: 'Tổng Chi', data: dataMonth.exp, borderColor: '#dc3545', backgroundColor: '#dc3545', tension: 0.3},
                    {label: 'Số dư', data: dataMonth.bal, borderColor: '#17a2b8', backgroundColor: 'rgba(23, 162, 184, 0.1)', fill: true, borderDash: [5, 5], tension: 0.3}
                ]
            },
            options: {
                responsive: true, maintainAspectRatio: false, interaction: {mode: 'index', intersect: false},
                plugins: { tooltip: { callbacks: { label: function (context) {
                    let label = context.dataset.label || '';
                    if (label) label += ': ';
                    if (context.parsed.y !== null) label += new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(context.parsed.y);
                    return label;
                }}}}
            }
        });

        function switchChart(mode) {
            const btnMonth = document.getElementById('btnMonth');
            const btnYear = document.getElementById('btnYear');
            const title = document.getElementById('chartTitle');
            if (mode === 'MONTH') {
                btnMonth.style.background = '#007bff'; btnMonth.style.color = 'white';
                btnYear.style.background = 'white'; btnYear.style.color = '#007bff';
                title.innerText = "Theo Tháng (Năm ${currentYear})";
                myChart.data.labels = dataMonth.labels;
                myChart.data.datasets[0].data = dataMonth.rev;
                myChart.data.datasets[1].data = dataMonth.exp;
                myChart.data.datasets[2].data = dataMonth.bal;
            } else {
                btnYear.style.background = '#007bff'; btnYear.style.color = 'white';
                btnMonth.style.background = 'white'; btnMonth.style.color = '#007bff';
                title.innerText = "Theo 5 Năm Gần Nhất";
                myChart.data.labels = dataYear.labels;
                myChart.data.datasets[0].data = dataYear.rev;
                myChart.data.datasets[1].data = dataYear.exp;
                myChart.data.datasets[2].data = dataYear.bal;
            }
            myChart.update();
        }
    </script>
<script>
    // JS ĐỂ CHUYỂN ĐỔI GIỮA THU/CHI
    function setFormType(type) {
        document.getElementById('transTypeInput').value = type;
        var btn = document.getElementById('submitBtn');
        var typeLabel = document.getElementById('typeLabel');
        
        if (type === 'IN') {
            btn.className = 'btn-submit-fin btn-in';
            btn.innerText = 'Xác nhận Thu (+)'
            typeLabel.innerHTML = 'Đang chọn: <span style="color:green; font-weight:bold;">THU VÀO</span>';
        } else {
            btn.className = 'btn-submit-fin btn-out';
            btn.innerText = 'Xác nhận Chi (-)'
            typeLabel.innerHTML = 'Đang chọn: <span style="color:red; font-weight:bold;">CHI RA</span>';
        }
    }
    // 1. Hàm hiện dropdown khi click vào ô input
    function showCatDropdown() {
        document.getElementById("catDropdown").style.display = "block";
    }

    // 2. Hàm lọc danh sách khi gõ chữ
    function filterCategories() {
        var input, filter, div, items, i, txtValue;
        input = document.getElementById("catSearchInput");
        filter = input.value.toLowerCase();
        div = document.getElementById("catDropdown");
        items = div.getElementsByClassName("cat-item");
        
        div.style.display = "block"; // Luôn hiện khi đang gõ

        for (i = 0; i < items.length; i++) {
            txtValue = items[i].getAttribute("data-search");
            if (txtValue.indexOf(filter) > -1) {
                items[i].style.display = "";
            } else {
                items[i].style.display = "none";
            }
        }
    }

    // 3. Hàm chọn item -> Điền vào ô input và ẩn list
    function selectCategory(name) {
        document.getElementById("catSearchInput").value = name;
        document.getElementById("catDropdown").style.display = "none";
    }

    // 4. Click ra ngoài thì đóng dropdown
    document.addEventListener('click', function(event) {
        var container = document.querySelector('.search-dropdown-container');
        if (!container.contains(event.target)) {
            document.getElementById("catDropdown").style.display = "none";
        }
    });
</script>
    <div class="main-content-fin">
        <div class="form-section-fin">
            <h4 class="mt-0 mb-3 border-bottom pb-2">✍️ Ghi nhận Giao dịch mới</h4>
            
            <div style="display: flex; gap: 10px; margin-bottom: 15px;">
                <button onclick="setFormType('IN')" style="flex:1; padding:8px; background:#28a745; color:white; border:none; cursor:pointer; border-radius:4px;">Thu vào (+)</button>
                <button onclick="setFormType('OUT')" style="flex:1; padding:8px; background:#dc3545; color:white; border:none; cursor:pointer; border-radius:4px;">Chi ra (-)</button>
            </div>
            <div id="typeLabel" style="margin-bottom: 10px; font-size: 14px; text-align: center;">Đang chọn: <span style="color:green; font-weight:bold;">THU VÀO</span></div>

            <form action="finance" method="POST">
                <input type="hidden" id="transTypeInput" name="type" value="IN">
                
                <label>Thời gian:</label>
                <input type="datetime-local" name="transaction_date" style="color:#555;" id="transDateInput">
                <script>
                    const now = new Date();
                    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
                    document.getElementById('transDateInput').value = now.toISOString().slice(0, 16);
                </script>
                
                <label>Tên giao dịch (Chọn hoặc Gõ mới):</label>
                
                <div class="search-dropdown-container">
                    <input type="text" name="category_name" id="catSearchInput" 
                           class="search-input" 
                           placeholder="Gõ tên loại giao dịch..." 
                           autocomplete="off" 
                           required
                           onkeyup="filterCategories()" 
                           onfocus="showCatDropdown()">
                    
                    <div id="catDropdown" class="dropdown-list">
                        <c:forEach items="${catList}" var="c">
                            <div class="dropdown-item cat-item" 
                                 onclick="selectCategory('${c.name}')"
                                 data-search="${c.name.toLowerCase()}">
                                
                                ${c.name}
                                
                                <c:choose>
                                    <c:when test="${c.type == 'REVENUE'}">
                                        <span class="type-in">(Thu)</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="type-out">(Chi)</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <label>Số tiền (VNĐ):</label>
                <input type="number" name="amount" required placeholder="VD: 5000000" min="0">
                
                <label>Mô tả chi tiết:</label>
                <textarea name="description" rows="3" placeholder="Ghi chú thêm..."></textarea>
                
                <button type="submit" id="submitBtn" class="btn-submit-fin btn-in">Xác nhận Thu (+)</button>
            </form>
        </div>

        <div class="table-section-fin">
            <h4 class="mt-0 mb-3">📜 Sổ cái giao dịch</h4>
            
            <form id="filterForm" action="finance" method="GET" style="background:#f8f9fa; padding:10px; margin-bottom:15px; display:flex; flex-wrap:wrap; gap:10px; align-items:center; border: 1px solid #dee2e6; border-radius: 4px;">
                <input type="hidden" id="sortBy" name="sortBy" value="${sortBy}">
                <input type="hidden" id="sortOrder" name="sortOrder" value="${sortOrder}">
                
                <%-- Lọc ngày (Giữ nguyên) --%>
                <div style="display:flex; align-items:center; gap:5px;">
                    <input type="date" name="f_date_from" value="${param.f_date_from}" style="margin:0; padding: 5px;">
                    <span>➜</span>
                    <input type="date" name="f_date_to" value="${param.f_date_to}" style="margin:0; padding: 5px;">
                </div>

                <%-- [MỚI] Lọc giá tiền --%>
                <div style="display:flex; align-items:center; gap:5px; border:1px solid #ddd; padding:2px 5px; border-radius:4px; background:white;">
                    <span style="font-size:12px; color:#555;">Giá:</span>
                    <input type="number" name="f_amount_from" value="${f_amount_from}" placeholder="Min" min="0" step="1000" style="width: 80px; border:none; outline:none;">
                    <span>-</span>
                    <input type="number" name="f_amount_to" value="${f_amount_to}" placeholder="Max" min="0" step="1000" style="width: 80px; border:none; outline:none;">
                </div>

                <%-- Lọc tên & loại (Giữ nguyên) --%>
                <input type="text" name="f_cat_name" value="${f_cat_name}" placeholder="Tìm theo tên danh mục..." style="margin:0; padding: 5px; width: 180px;">

                <select name="f_type" style="width: auto; margin:0; padding: 5px;">
                    <option value="">-- Loại --</option>
                    <option value="IN" ${f_type=='IN'?'selected':''}>Thu</option>
                    <option value="OUT" ${f_type=='OUT'?'selected':''}>Chi</option>
                </select>
                
                <button type="submit" style="width:auto; padding:5px 15px; background: #007bff; color: white; border: none; border-radius: 4px;">🔍 Tìm</button>
                <a href="finance" style="color:red; text-decoration:none; font-size:12px;">[Xóa lọc]</a>
            </form>

            <table class="table-fin">
                <thead>
                    <tr>
                        <th onclick="sortTable('transaction_date')">Thời gian ${sortBy=='transaction_date'?(sortOrder=='ASC'?'▲':'▼'):''}</th>
                        <th onclick="sortTable('cat_name')">Danh mục ${sortBy=='cat_name'?(sortOrder=='ASC'?'▲':'▼'):''}</th>
                        <th>Diễn giải</th>
                        <th onclick="sortTable('type')">Loại</th>
                        <th onclick="sortTable('amount')">Số tiền ${sortBy=='amount'?(sortOrder=='ASC'?'▲':'▼'):''}</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${transList}" var="t">
                        <tr>
                            <td><fmt:formatDate value="${t.transactionDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td><strong>${t.categoryName}</strong></td>
                            <td style="color:#555;">${t.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.transactionType == 'IN'}"><span class="badge-in">THU</span></c:when>
                                    <c:otherwise><span class="badge-out">CHI</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td style="font-weight:bold; color:${t.transactionType == 'IN' ? 'green' : 'red'}; text-align:right;">
                                ${t.transactionType == 'IN' ? '+' : '-'}<fmt:formatNumber value="${t.amount}" type="currency" currencySymbol="₫"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty transList}"><tr><td colspan="5" style="text-align: center; padding: 20px;">Chưa có giao dịch nào.</td></tr></c:if>
                </tbody>
            </table>

            <c:url value="finance" var="baseUrl">
                 <c:if test="${not empty param.f_date_from}"><c:param name="f_date_from" value="${param.f_date_from}"/></c:if>
                 <c:if test="${not empty param.f_date_to}"><c:param name="f_date_to" value="${param.f_date_to}"/></c:if>
                 
                 <%-- [MỚI] Thêm tham số giá --%>
                 <c:if test="${not empty param.f_amount_from}"><c:param name="f_amount_from" value="${param.f_amount_from}"/></c:if>
                 <c:if test="${not empty param.f_amount_to}"><c:param name="f_amount_to" value="${param.f_amount_to}"/></c:if>
                 
                 <c:if test="${not empty f_cat_name}"><c:param name="f_cat_name" value="${f_cat_name}"/></c:if>
                 <c:if test="${not empty f_type}"><c:param name="f_type" value="${f_type}"/></c:if>
                 <c:param name="sortBy" value="${sortBy}"/>
                 <c:param name="sortOrder" value="${sortOrder}"/>
            </c:url>

            <div style="margin-top: 15px; text-align: center;">
                <c:if test="${totalPage > 1}">
                    <c:if test="${pageIndex > 1}">
                        <a href="${baseUrl}&page=${pageIndex-1}" style="padding: 6px 12px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">« Trước</a>
                    </c:if>
                    <span style="margin:0 10px; font-weight:bold;">Trang ${pageIndex} / ${totalPage}</span>
                    <c:if test="${pageIndex < totalPage}">
                        <a href="${baseUrl}&page=${pageIndex+1}" style="padding: 6px 12px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">Sau »</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
    </div>

</div> </body>
</html>