<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="../common/header.jsp" %>

<style>
    /* CSS Grid cho Quỹ */
    .fund-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 20px; }
    .fund-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); border-top: 4px solid #007bff; position: relative; }
    .fund-card.hidden-fund { display: none; } /* Class để ẩn quỹ thứ 5 trở đi */
    
    .fund-name { font-size: 1.1rem; font-weight: bold; color: #555; height: 40px; overflow: hidden; }
    .fund-balance { font-size: 1.6rem; font-weight: bold; color: #28a745; margin: 10px 0; }
    .fund-desc { font-size: 0.85rem; color: #777; height: 30px; overflow: hidden; text-overflow: ellipsis; }

    /* Layout chính */
    .content-row-fund { display: flex; gap: 20px; }
    .form-box-fund { flex: 1; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); height: fit-content; }
    .table-box-fund { flex: 2; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }

    /* Nút bấm */
    .btn-view-all { display: block; margin: 0 auto 20px auto; background: #e9ecef; border: none; padding: 8px 20px; border-radius: 20px; cursor: pointer; color: #555; font-weight: bold; }
    .btn-view-all:hover { background: #dde2e6; }
    .btn-create-fund { float: right; background: #17a2b8; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; text-decoration: none; font-size: 14px;}

    /* Custom Searchable Dropdown (Thay thế Select box) */
    .search-dropdown-container { position: relative; width: 100%; }
    .search-input { width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
    .dropdown-list { display: none; position: absolute; width: 100%; max-height: 200px; overflow-y: auto; background: white; border: 1px solid #ddd; border-top: none; z-index: 1000; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    .dropdown-item { padding: 8px; cursor: pointer; border-bottom: 1px solid #f1f1f1; font-size: 14px; }
    .dropdown-item:hover { background-color: #f8f9fa; }
    .dropdown-item strong { color: #007bff; }

    /* Form Styles */
    .btn-submit-fund { width: 100%; padding: 10px; border: none; border-radius: 4px; color: white; cursor: pointer; font-size: 16px; margin-top: 15px; }
    .btn-deposit { background: #28a745; }
    .btn-withdraw { background: #dc3545; }
    label { font-weight:bold; display:block; margin-top:10px; font-size: 0.9rem;}
    
    /* Table Styles */
    .table-fund { width: 100%; border-collapse: collapse; }
    .table-fund th, .table-fund td { padding: 10px; border-bottom: 1px solid #eee; text-align: left; }
    .table-fund th { background: #f8f9fa; }
    .badge-deposit { background: #d4edda; color: #155724; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 11px;}
    .badge-withdraw { background: #f8d7da; color: #721c24; padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 11px;}
    
    /* Simple Modal */
    .modal { display: none; position: fixed; z-index: 1050; left: 0; top: 0; width: 100%; height: 100%; overflow: auto; background-color: rgba(0,0,0,0.4); }
    .modal-content { background-color: #fefefe; margin: 15% auto; padding: 20px; border: 1px solid #888; width: 400px; border-radius: 8px; }
    .close { color: #aaa; float: right; font-size: 28px; font-weight: bold; cursor: pointer; }
    .close:hover { color: black; }
    /* --- UNIFIED FILTER STYLES (DÙNG CHUNG CHO BỘ LỌC) --- */
.filter-wrapper {
    background-color: #f8f9fa;
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 20px;
    border: 1px solid #dee2e6;
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0,0,0,0.02);
}

.filter-input {
    padding: 8px 12px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    font-size: 14px;
    outline: none;
    transition: border-color 0.2s;
    background: white;
}

.filter-input:focus {
    border-color: #80bdff;
    box-shadow: 0 0 0 0.2rem rgba(0,123,255,.25);
}

/* Nhóm dành riêng cho Từ ngày - Đến ngày hoặc Khoảng giá */
.filter-group {
    display: flex;
    align-items: center;
    background: white;
    border: 1px solid #ced4da;
    border-radius: 4px;
    overflow: hidden;
    transition: border-color 0.2s;
}

.filter-group:focus-within {
    border-color: #80bdff;
    box-shadow: 0 0 0 0.2rem rgba(0,123,255,.25);
}

.filter-group span.group-label {
    background: #e9ecef;
    color: #495057;
    font-size: 13px;
    padding: 8px 12px;
    border-right: 1px solid #ced4da;
    font-weight: 600;
}

.filter-group span.group-divider {
    color: #6c757d;
    font-size: 12px;
    padding: 0 5px;
}

.filter-group input {
    border: none;
    padding: 8px;
    font-size: 14px;
    outline: none;
    background: transparent;
    max-width: 120px;
}

.filter-btn {
    padding: 8px 16px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.2s;
    display: flex;
    align-items: center;
    gap: 5px;
}

.filter-btn:hover {
    background-color: #0056b3;
}

.filter-clear {
    color: #dc3545;
    font-size: 13px;
    text-decoration: none;
    font-weight: bold;
    display: flex;
    align-items: center;
    gap: 3px;
}

.filter-clear:hover {
    text-decoration: underline;
    color: #a71d2a;
}
</style>

<div class="container-fluid p-4">
    <div style="display: flex; justify-content: space-between; align-items: center;">
        <div>
            <h2 class="mb-4 fw-bold" style="margin-top: 5px;">Quản lý Quỹ Chung HTX</h2>
        </div>
        <button onclick="document.getElementById('addFundModal').style.display='block'" class="btn-create-fund">
            + Tạo Quỹ Mới
        </button>
    </div>

    <div class="fund-grid" id="fundContainer">
        <c:forEach items="${funds}" var="f" varStatus="status">
            <div class="fund-card ${status.index >= 4 ? 'hidden-fund' : ''}">
                <div class="fund-name">${f.fundName}</div>
                <div class="fund-balance">
                    <fmt:formatNumber value="${f.currentBalance}" type="currency" currencySymbol="₫"/>
                </div>
                <div class="fund-desc">${f.description}</div>
            </div>
        </c:forEach>
    </div>
    
    <c:if test="${funds.size() > 4}">
        <button id="toggleBtn" class="btn-view-all" onclick="toggleFunds()">▼ Xem tất cả (${funds.size()})</button>
    </c:if>

    <div class="content-row-fund">
        
        <div class="form-box-fund">
            <h4 class="mt-0 mb-3 border-bottom pb-2">Giao dịch Quỹ</h4>
            
            <div style="display: flex; gap: 10px; margin-bottom: 15px;">
                <button type="button" onclick="setFormType('DEPOSIT')" style="flex:1; padding:8px; background:#28a745; color:white; border:none; cursor:pointer; border-radius:4px;">Nộp vào (+)</button>
                <button type="button" onclick="setFormType('WITHDRAW')" style="flex:1; padding:8px; background:#dc3545; color:white; border:none; cursor:pointer; border-radius:4px;">Chi ra (-)</button>
            </div>

            <form action="fund" method="POST">
                <input type="hidden" id="transType" name="type" value="DEPOSIT">
                
                <label>Chọn Quỹ:</label>
                <select name="fund_id" required style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">
                    <c:forEach items="${funds}" var="f">
                        <option value="${f.id}">${f.fundName} (Dư: <fmt:formatNumber value="${f.currentBalance}" groupingUsed="true"/>)</option>
                    </c:forEach>
                </select>

                <label>Thành viên:</label>
                <div class="search-dropdown-container">
                    <input type="text" id="memberSearchInput" class="search-input" placeholder="Gõ tên hoặc SĐT để tìm..." onkeyup="filterMembers()" onfocus="showDropdown()">
                    <input type="hidden" name="member_id" id="selectedMemberId" value="">
                    
                    <div id="memberDropdown" class="dropdown-list">
                        <div class="dropdown-item" onclick="selectMember('', 'Quỹ chung (Không chọn thành viên)')">
                            <i>-- Quỹ chung (Không chọn thành viên) --</i>
                        </div>
                        <c:forEach items="${members}" var="m">
                            <div class="dropdown-item member-item" 
                                 onclick="selectMember('${m.id}', '${m.full_name} - ${m.phone}')"
                                 data-search="${m.full_name.toLowerCase()} ${m.phone} ${m.username.toLowerCase()}">
                                <strong>${m.full_name}</strong> - ${m.phone}
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <label>Thời gian:</label>
                <input type="datetime-local" name="transaction_date" required id="transDateInput" style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">
                
                <label>Số tiền (VNĐ):</label>
                <input type="number" name="amount" required min="1000" step="1000" style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;">

                <label>Ghi chú:</label>
                <textarea name="note" rows="3" required placeholder="VD: Thu phí tháng 1..." style="width:100%; padding:8px; border:1px solid #ddd; border-radius:4px;"></textarea>

                <button type="submit" id="submitBtn" class="btn-submit-fund btn-deposit">Xác nhận Nộp Quỹ (+)</button>
            </form>
        </div>

        <div class="table-box-fund">
            <h4 class="mt-0 mb-3">📜 Lịch sử Giao dịch</h4>
            <form action="fund" method="GET" class="filter-wrapper">
    <input type="text" class="filter-input" name="f_member_name" value="${f_member_name}" placeholder="Tên thành viên..." style="min-width: 150px;">
    
    <input type="text" class="filter-input" name="f_fund_name" value="${f_fund_name}" placeholder="Tên quỹ..." style="min-width: 150px;">
    
    <select class="filter-input" name="f_type">
        <option value="">-- Loại Giao dịch --</option>
        <option value="DEPOSIT" ${param.f_type == 'DEPOSIT' ? 'selected' : ''}>Nộp (+)</option>
        <option value="WITHDRAW" ${param.f_type == 'WITHDRAW' ? 'selected' : ''}>Chi (-)</option>
    </select>

    <div class="filter-group">
        <span class="group-label">📅 Thời gian</span>
        <input type="date" name="f_date_from" value="${param.f_date_from}">
        <span class="group-divider">➜</span>
        <input type="date" name="f_date_to" value="${param.f_date_to}">
    </div>
    
    <button type="submit" class="filter-btn">🔍 Tìm</button>
    <a href="fund" class="filter-clear">✖ Xóa lọc</a>
</form>

            <table class="table-fund">
                <thead>
                    <tr>
                        <th>Thời gian</th>
                        <th>Quỹ</th>
                        <th>Loại</th>
                        <th>Thành viên/Ghi chú</th>
                        <th style="text-align: right;">Số tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${history}" var="h">
                        <tr>
                            <td><fmt:formatDate value="${h.transactionDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td>${h.fundName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${h.transactionType == 'DEPOSIT'}"><span class="badge-deposit">Nộp</span></c:when>
                                    <c:otherwise><span class="badge-withdraw">Chi</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:if test="${not empty h.memberName}">👤 <b>${h.memberName}</b><br></c:if>
                                <span style="color:#666; font-size: 0.9em;">${h.note}</span>
                            </td>
                            <td style="text-align: right; font-weight: bold; color: ${h.transactionType == 'DEPOSIT' ? 'green' : 'red'};">
                                <c:if test="${h.transactionType == 'DEPOSIT'}">+</c:if>
                                <c:if test="${h.transactionType == 'WITHDRAW'}">-</c:if>
                                <fmt:formatNumber value="${h.amount}" type="currency" currencySymbol="₫"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <%-- PHÂN TRANG (Cập nhật tham số f_fund_name) --%>
            <div style="margin-top: 20px; text-align: center; display: flex; justify-content: center; align-items: center; gap: 15px;">
                <c:if test="${totalPage > 1}">
                    <c:if test="${pageIndex > 1}">
                        <a href="fund?page=${pageIndex - 1}&f_fund_name=${f_fund_name}&f_member_name=${f_member_name}&f_type=${param.f_type}&f_date_from=${param.f_date_from}&f_date_to=${param.f_date_to}" 
                           style="padding: 8px 16px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">« Trước</a>
                    </c:if>

                    <span style="font-weight: bold;">Trang ${pageIndex} / ${totalPage}</span>

                    <c:if test="${pageIndex < totalPage}">
                        <a href="fund?page=${pageIndex + 1}&f_fund_name=${f_fund_name}&f_member_name=${f_member_name}&f_type=${param.f_type}&f_date_from=${param.f_date_from}&f_date_to=${param.f_date_to}" 
                           style="padding: 8px 16px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">Sau »</a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>

<div id="addFundModal" class="modal">
  <div class="modal-content">
    <span class="close" onclick="document.getElementById('addFundModal').style.display='none'">&times;</span>
    <h3 style="margin-top:0">Thêm Quỹ Mới</h3>
    <form action="fund" method="POST">
        <input type="hidden" name="action" value="create_fund">
        
        <label>Tên Quỹ:</label>
        <input type="text" name="new_fund_name" required placeholder="VD: Quỹ Khuyến Học" style="width:100%; padding:8px; margin-bottom:10px;">
        
        <label>Mô tả:</label>
        <textarea name="new_fund_desc" rows="3" placeholder="Mục đích sử dụng..." style="width:100%; padding:8px; margin-bottom:20px;"></textarea>
        
        <button type="submit" style="width:100%; padding:10px; background:#17a2b8; color:white; border:none; border-radius:4px; cursor:pointer;">Lưu Quỹ Mới</button>
    </form>
  </div>
</div>

<script>
    // 1. Script cho thời gian hiện tại
    const now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    document.getElementById('transDateInput').value = now.toISOString().slice(0, 16);

    // 2. Script đổi màu nút Nộp/Rút
    function setFormType(type) {
        document.getElementById('transType').value = type;
        var btn = document.getElementById('submitBtn');
        if (type === 'DEPOSIT') {
            btn.className = 'btn-submit-fund btn-deposit';
            btn.innerText = 'Xác nhận Nộp Quỹ (+)'
        } else {
            btn.className = 'btn-submit-fund btn-withdraw';
            btn.innerText = 'Xác nhận Chi Quỹ (-)'
        }
    }

    // 3. Script Ẩn/Hiện Quỹ (Xem tất cả)
    let isExpanded = false;
    function toggleFunds() {
        const hiddenFunds = document.querySelectorAll('.fund-card.hidden-fund'); // Những thằng đang bị ẩn gốc
        const allFunds = document.querySelectorAll('.fund-card');
        const btn = document.getElementById('toggleBtn');
        
        isExpanded = !isExpanded;
        
        if(isExpanded) {
            // Hiện hết
            allFunds.forEach(el => el.style.display = 'block');
            btn.innerText = '▲ Thu gọn';
        } else {
            // Ẩn lại những thằng index >= 4
            allFunds.forEach((el, index) => {
                if(index >= 4) el.style.display = 'none';
            });
            btn.innerText = '▼ Xem tất cả';
        }
    }

    // 4. Script Tìm kiếm Thành viên (Autocomplete Logic)
    function showDropdown() {
        document.getElementById("memberDropdown").style.display = "block";
    }

    // Đóng dropdown khi click ra ngoài
    document.addEventListener('click', function(event) {
        var container = document.querySelector('.search-dropdown-container');
        if (!container.contains(event.target)) {
            document.getElementById("memberDropdown").style.display = "none";
        }
    });

    function filterMembers() {
        var input, filter, div, items, i, txtValue;
        input = document.getElementById("memberSearchInput");
        filter = input.value.toLowerCase();
        div = document.getElementById("memberDropdown");
        items = div.getElementsByClassName("member-item");
        
        div.style.display = "block"; // Luôn hiện khi gõ

        for (i = 0; i < items.length; i++) {
            txtValue = items[i].getAttribute("data-search");
            if (txtValue.indexOf(filter) > -1) {
                items[i].style.display = "";
            } else {
                items[i].style.display = "none";
            }
        }
    }

    function selectMember(id, name) {
        document.getElementById("selectedMemberId").value = id;
        document.getElementById("memberSearchInput").value = name;
        document.getElementById("memberDropdown").style.display = "none";
    }
</script>