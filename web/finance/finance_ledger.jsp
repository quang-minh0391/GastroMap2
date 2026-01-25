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

    <div class="main-content-fin">
        <div class="form-section-fin">
            <h4 class="mt-0 mb-3 border-bottom pb-2">✍️ Ghi nhận Giao dịch mới</h4>
            <form action="finance" method="POST">
                <label>Thời gian giao dịch:</label>
                <input type="datetime-local" name="transaction_date" style="color:#555;" title="Để trống sẽ lấy giờ hiện tại">
                
                <label>Loại giao dịch:</label>
                <select name="category_id" required>
                    <c:forEach items="${catList}" var="c">
                        <option value="${c.id}">${c.name} (${c.type=='REVENUE'?'+':'-'})</option>
                    </c:forEach>
                </select>
                <label>Số tiền (VNĐ):</label>
                <input type="number" name="amount" required placeholder="VD: 5000000">
                <label>Mô tả chi tiết:</label>
                <textarea name="description" rows="3" placeholder="VD: Bán 2 tấn thóc..."></textarea>
                <button type="submit" class="btn-save">💾 Lưu Giao dịch</button>
            </form>
        </div>

        <div class="table-section-fin">
            <h4 class="mt-0 mb-3">📜 Sổ cái giao dịch</h4>
            <form id="filterForm" action="finance" method="GET" style="background:#f8f9fa; padding:10px; margin-bottom:15px; display:flex; gap:10px; align-items:center; border: 1px solid #dee2e6; border-radius: 4px;">
                <input type="hidden" id="sortBy" name="sortBy" value="${sortBy}">
                <input type="hidden" id="sortOrder" name="sortOrder" value="${sortOrder}">
                <input type="date" name="f_date_from" value="${param.f_date_from}" style="width: auto; margin:0; padding: 5px;">
                <span>đến</span>
                <input type="date" name="f_date_to" value="${param.f_date_to}" style="width: auto; margin:0; padding: 5px;">
                <select name="f_type" style="width: auto; margin:0; padding: 5px;">
                    <option value="">-- Loại --</option>
                    <option value="IN" ${f_type=='IN'?'selected':''}>Thu</option>
                    <option value="OUT" ${f_type=='OUT'?'selected':''}>Chi</option>
                </select>
                <button type="submit" style="width:auto; padding:5px 10px; background: #007bff; color: white; border: none; border-radius: 4px;">🔍 Tìm kiếm</button>
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
                            <td><fmt:formatDate value="${t.transactionDate}" pattern="dd/MM/yyyy HH:mm" timeZone="GMT+7"/></td>
                            <td>${t.categoryName}</td>
                            <td>${t.description}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.transactionType == 'IN'}"><span class="badge-in">THU</span></c:when>
                                    <c:otherwise><span class="badge-out">CHI</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td style="font-weight:bold; color:${t.transactionType == 'IN' ? 'green' : 'red'};">
                                ${t.transactionType == 'IN' ? '+' : '-'}<fmt:formatNumber value="${t.amount}" type="currency" currencySymbol="₫"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty transList}"><tr><td colspan="5" style="text-align: center; padding: 20px;">Chưa có giao dịch nào.</td></tr></c:if>
                </tbody>
            </table>

            <div style="margin-top: 15px; text-align: center;">
                <c:if test="${totalPage > 1}">
                    <c:if test="${pageIndex > 1}">
                        <a href="finance?page=${pageIndex-1}&f_type=${f_type}&sortBy=${sortBy}&sortOrder=${sortOrder}" style="padding: 6px 12px; background: #e9ecef; color: black; text-decoration: none; border-radius: 4px; border: 1px solid #ddd;">« Trước</a>
                    </c:if>
                    <span style="margin:0 10px; font-weight:bold;">Trang ${pageIndex} / ${totalPage}</span>
                    <c:if test="${pageIndex < totalPage}">
                        <a href="finance?page=${pageIndex+1}&f_type=${f_type}&sortBy=${sortBy}&sortOrder=${sortOrder}" style="padding: 6px 12px; background: #e9ecef; color: black; text-decoration: none; border-radius: 4px; border: 1px solid #ddd;">Sau »</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>

</div> </body>
</html>