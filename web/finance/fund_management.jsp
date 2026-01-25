<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../common/header.jsp" %>

<style>
    /* CSS Quỹ */
    .fund-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; margin-bottom: 30px; }
    .fund-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); border-top: 4px solid #007bff; }
    .fund-name { font-size: 1.1rem; font-weight: bold; color: #555; }
    .fund-balance { font-size: 1.8rem; font-weight: bold; color: #28a745; margin: 10px 0; }
    .fund-desc { font-size: 0.9rem; color: #777; }

    .content-row-fund { display: flex; gap: 20px; }
    .form-box-fund { flex: 1; background: white; padding: 20px; border-radius: 8px; height: fit-content; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    .table-box-fund { flex: 2; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }

    .btn-submit-fund { width: 100%; padding: 10px; border: none; border-radius: 4px; color: white; cursor: pointer; font-size: 16px; }
    .btn-deposit { background: #28a745; }
    .btn-withdraw { background: #dc3545; }
    
    .table-fund { width: 100%; border-collapse: collapse; }
    .table-fund th, .table-fund td { padding: 10px; border-bottom: 1px solid #eee; text-align: left; }
    .table-fund th { background: #f8f9fa; }
    
    .badge-deposit { background: #d4edda; color: #155724; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 11px;}
    .badge-withdraw { background: #f8d7da; color: #721c24; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 11px;}
</style>

<script>
    function setFormType(type) {
        document.getElementById('transType').value = type;
        var btn = document.getElementById('submitBtn');
        if (type === 'DEPOSIT') {
            btn.className = 'btn-submit-fund btn-deposit';
            btn.innerText = 'Xác nhận Nộp Quỹ (+)'
        } else {
            btn.className = 'btn-submit-fund btn-withdraw';
            btn.innerText = 'Xác nhận Rút Quỹ (-)'
        }
    }
</script>

<div class="container-fluid p-4">
    <a href="finance" style="text-decoration: none; color: #007bff; margin-bottom: 10px; display: inline-block;">← Quay lại Sổ cái Tài chính</a>
    <h2 class="mb-4 fw-bold">Quản lý Quỹ Chung HTX</h2>

    <div class="fund-grid">
        <c:forEach items="${funds}" var="f">
            <div class="fund-card">
                <div class="fund-name">${f.fundName}</div>
                <div class="fund-balance">
                    <fmt:formatNumber value="${f.currentBalance}" type="currency" currencySymbol="₫"/>
                </div>
                <div class="fund-desc">${f.description}</div>
            </div>
        </c:forEach>
    </div>

    <div class="content-row-fund">
        <div class="form-box-fund">
            <h4 class="mt-0 mb-3 border-bottom pb-2">Giao dịch Quỹ</h4>

            <div style="display: flex; gap: 10px; margin-bottom: 15px;">
                <button onclick="setFormType('DEPOSIT')" style="flex:1; padding:8px; background:#28a745; color:white; border:none; cursor:pointer; border-radius:4px;">Nộp vào (+)</button>
                <button onclick="setFormType('WITHDRAW')" style="flex:1; padding:8px; background:#dc3545; color:white; border:none; cursor:pointer; border-radius:4px;">Chi ra (-)</button>
            </div>

            <form action="fund" method="POST">
                <input type="hidden" id="transType" name="type" value="DEPOSIT">

                <label style="font-weight:bold; display:block; margin-top:10px;">Chọn Quỹ:</label>
                <select name="fund_id" required style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">
                    <c:if test="${empty funds}">
                        <option value="">(Chưa có quỹ nào trong hệ thống)</option>
                    </c:if>
                    <c:forEach items="${funds}" var="f">
                        <option value="${f.id}">${f.fundName} (Dư: <fmt:formatNumber value="${f.currentBalance}" type="currency"/>)</option>
                    </c:forEach>
                </select>

                <label style="font-weight:bold; display:block; margin-top:10px;">Thành viên:</label>
                <select name="member_id" style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">
                    <option value="">-- Quỹ chung (Không chọn thành viên) --</option>
                    <c:forEach items="${members}" var="m">
                        <option value="${m.id}">${m.full_name} - ${m.phone}</option>
                    </c:forEach>
                </select>

                <label style="font-weight:bold; display:block; margin-top:10px;">Thời gian:</label>
                <input type="datetime-local" name="transaction_date" required id="transDateInput" style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">
                <script>
                    const now = new Date();
                    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
                    document.getElementById('transDateInput').value = now.toISOString().slice(0, 16);
                </script>

                <label style="font-weight:bold; display:block; margin-top:10px;">Số tiền (VNĐ):</label>
                <input type="number" name="amount" required min="1000" step="1000" style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">

                <label style="font-weight:bold; display:block; margin-top:10px;">Ghi chú:</label>
                <textarea name="note" rows="3" required placeholder="VD: Thu phí tháng 1..." style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;"></textarea>

                <button type="submit" id="submitBtn" class="btn-submit-fund btn-deposit" style="margin-top:15px;">Xác nhận Nộp Quỹ (+)</button>
            </form>
        </div>

        <div class="table-box-fund">
            <h4 class="mt-0 mb-3">📜 Lịch sử Giao dịch</h4>

            <form action="fund" method="GET" style="background:#f8f9fa; padding:10px; margin-bottom:15px; border-radius:4px; border:1px solid #dee2e6;">
                <div style="display: flex; gap: 10px; flex-wrap: wrap; align-items:center;">
                    <input type="date" name="f_date_from" value="${param.f_date_from}" style="padding: 5px; border:1px solid #ccc; border-radius:3px;">
                    <span>➜</span>
                    <input type="date" name="f_date_to" value="${param.f_date_to}" style="padding: 5px; border:1px solid #ccc; border-radius:3px;">
                    <select name="f_fund" style="flex: 1; padding: 5px; border:1px solid #ccc; border-radius:3px;">
                        <option value="">-- Tất cả Quỹ --</option>
                        <c:forEach items="${funds}" var="f">
                            <option value="${f.id}" ${param.f_fund == f.id ? 'selected' : ''}>${f.fundName}</option>
                        </c:forEach>
                    </select>
                    <select name="f_type" style="width: 100px; padding: 5px; border:1px solid #ccc; border-radius:3px;">
                        <option value="">-- Loại --</option>
                        <option value="DEPOSIT" ${param.f_type == 'DEPOSIT' ? 'selected' : ''}>Nộp (+)</option>
                        <option value="WITHDRAW" ${param.f_type == 'WITHDRAW' ? 'selected' : ''}>Rút (-)</option>
                    </select>
                    <button type="submit" style="padding: 5px 15px; background: #007bff; color: white; border: none; cursor: pointer; border-radius:3px;">🔍 Tìm</button>
                    <a href="fund" style="font-size: 12px; color: red; text-decoration: none;">[Xóa]</a>
                </div>
            </form>

            <table class="table-fund">
                <thead>
                    <tr>
                        <th>Thời gian</th>
                        <th>Quỹ</th>
                        <th>Loại</th>
                        <th>Thành viên/Ghi chú</th>
                        <th>Số tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${history}" var="h">
                        <tr>
                            <td><fmt:formatDate value="${h.transactionDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td><strong>${h.fundName}</strong></td>
                            <td>
                                <c:choose>
                                    <c:when test="${h.transactionType == 'DEPOSIT'}"><span class="badge-deposit">Nộp vào</span></c:when>
                                    <c:otherwise><span class="badge-withdraw">Rút ra</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${not empty h.memberName}">👤 <b>${h.memberName}</b><br></c:if>
                                <span style="color:gray; font-size: 0.9em;">${h.note}</span>
                            </td>
                            <td style="font-weight: bold; color: ${h.transactionType == 'DEPOSIT' ? 'green' : 'red'}; text-align: right;">
                                <c:if test="${h.transactionType == 'DEPOSIT'}">+</c:if>
                                <c:if test="${h.transactionType == 'WITHDRAW'}">-</c:if>
                                <fmt:formatNumber value="${h.amount}" type="currency" currencySymbol="₫"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty history}">
                        <tr><td colspan="5" style="text-align: center; color: red; padding: 20px;">Không tìm thấy giao dịch nào!</td></tr>
                    </c:if>
                </tbody>
            </table>

            <div style="margin-top: 15px; text-align: center;">
                <c:if test="${totalPage > 1}">
                    <c:if test="${pageIndex > 1}">
                        <a href="fund?page=${pageIndex - 1}&f_fund=${param.f_fund}&f_type=${param.f_type}" style="padding: 5px 10px; background: #e9ecef; text-decoration: none; border-radius: 4px; color: black; border:1px solid #ddd;">« Trước</a>
                    </c:if>
                    <span style="margin: 0 10px; font-weight: bold;">Trang ${pageIndex} / ${totalPage}</span>
                    <c:if test="${pageIndex < totalPage}">
                        <a href="fund?page=${pageIndex + 1}&f_fund=${param.f_fund}&f_type=${param.f_type}" style="padding: 5px 10px; background: #e9ecef; text-decoration: none; border-radius: 4px; color: black; border:1px solid #ddd;">Sau »</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>

</div> </body>
</html>