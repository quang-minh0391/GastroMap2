<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="Asia/Ho_Chi_Minh"/>
<%@include file="../common/header.jsp" %>

<style>
    /* CSS Riêng cho trang Maintenance */
    .maint-container { padding: 20px; font-family: sans-serif; background-color: #f4f6f9; min-height: 100vh; }
    
    .back-btn { 
        display: inline-block; 
        margin-bottom: 20px; 
        text-decoration: none; 
        color: #007bff; 
        font-weight: 500;
    }
    .back-btn:hover { text-decoration: underline; }

    .maint-header { margin-bottom: 20px; }
    .maint-title { color: #333; font-weight: bold; margin-bottom: 5px; }
    .maint-subtitle { color: #666; font-size: 0.95rem; }

    .content-layout { display: flex; gap: 30px; flex-wrap: wrap; }
    
    /* Cột trái: Form cập nhật trạng thái & Nhập bảo trì */
    .left-col { flex: 1; min-width: 300px; }
    
    /* Cột phải: Lịch sử */
    .right-col { flex: 2; min-width: 400px; }

    .form-box { 
        background: white; 
        padding: 20px; 
        border-radius: 8px; 
        box-shadow: 0 2px 4px rgba(0,0,0,0.05); 
        margin-bottom: 20px; 
    }
    
    .status-box { 
        border-left: 5px solid #ffc107; 
        background: #fff9e6; 
    }

    label { display: block; margin-top: 10px; font-weight: bold; font-size: 0.9rem; }
    input, textarea, select { 
        width: 100%; 
        padding: 8px; 
        margin-top: 5px; 
        box-sizing: border-box; 
        border: 1px solid #ddd; 
        border-radius: 4px; 
    }
    
    button { 
        margin-top: 15px; 
        padding: 10px 20px; 
        border: none; 
        border-radius: 4px; 
        cursor: pointer; 
        font-weight: bold;
        width: 100%;
    }
    .btn-update { background: #ffc107; color: #333; }
    .btn-save { background: #28a745; color: white; }
    
    /* Table Styles */
    .history-table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    .history-table th, .history-table td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; }
    .history-table th { background-color: #f8f9fa; color: #333; font-weight: bold; }
</style>

<div class="maint-container">
    <a href="AssetServlet" class="back-btn">← Quay lại Danh sách Tài sản</a>
    
    <div class="maint-header">
        <h2 class="maint-title">Nhật ký Bảo trì: <span style="color:#dc3545">${asset.name} (${asset.code})</span></h2>
        <p class="maint-subtitle">
            <strong>📍 Vị trí:</strong> ${asset.location} &nbsp;|&nbsp; 
            <strong>⚡ Trạng thái hiện tại:</strong> 
            <c:choose>
                <c:when test="${asset.status == 'ACTIVE'}"><span class="badge bg-success">Tốt</span></c:when>
                <c:when test="${asset.status == 'MAINTENANCE'}"><span class="badge bg-warning text-dark">Bảo trì</span></c:when>
                <c:when test="${asset.status == 'BROKEN'}"><span class="badge bg-danger">Hỏng</span></c:when>
                <c:otherwise><span class="badge bg-secondary">${asset.status}</span></c:otherwise>
            </c:choose>
        </p>
    </div>
    <hr style="border-top: 1px solid #ddd; margin-bottom: 25px;"/>

    <div class="content-layout">
        
        <div class="left-col">
            <div class="form-box status-box">
                <h4 style="margin-top:0;">⚠ Cập nhật Trạng thái</h4>
                <form action="maintenance" method="POST">
                    <input type="hidden" name="action" value="update_status">
                    <input type="hidden" name="asset_id" value="${asset.id}">
                    
                    <label>Chuyển trạng thái sang:</label>
                    <select name="new_status">
                        <option value="ACTIVE" ${asset.status == 'ACTIVE' ? 'selected' : ''}>✅ Đang sử dụng (Tốt)</option>
                        <option value="MAINTENANCE" ${asset.status == 'MAINTENANCE' ? 'selected' : ''}>🛠 Đang bảo trì</option>
                        <option value="BROKEN" ${asset.status == 'BROKEN' ? 'selected' : ''}>❌ Đã hỏng</option>
                        <option value="LIQUIDATED" ${asset.status == 'LIQUIDATED' ? 'selected' : ''}>💰 Đã thanh lý</option>
                    </select>
                    
                    <button type="submit" class="btn-update">Cập nhật ngay</button>
                </form>
            </div>

            <div class="form-box">
                <h4 style="margin-top:0;">➕ Ghi nhận bảo trì mới</h4>
                <form action="maintenance" method="POST">
                    <input type="hidden" name="asset_id" value="${asset.id}">
                    
                    <label>Ngày thực hiện:</label>
                    <input type="date" name="maintenance_date" required>
                    
                    <label>Chi phí (VNĐ):</label>
                    <input type="number" name="cost" required min="0">
                    
                    <label>Người/Đơn vị thực hiện:</label>
                    <input type="text" name="performer" placeholder="VD: Gara Ô tô An Phát" required>
                    
                    <label>Mô tả chi tiết / Lý do hỏng:</label>
                    <textarea name="description" rows="4" placeholder="VD: Thay dầu nhớt, hàn trục bánh xe..."></textarea>
                    
                    <button type="submit" class="btn-save">Lưu phiếu bảo trì</button>
                </form>
            </div>
        </div>

        <div class="right-col">
            <h4 style="margin-top:0; margin-bottom: 15px;">📜 Lịch sử sửa chữa & Bảo dưỡng</h4>
            <div class="form-box" style="padding:0; overflow:hidden;"> <table class="history-table">
                    <thead>
                        <tr>
                            <th>Ngày tháng</th>
                            <th>Chi phí</th>
                            <th>Người thực hiện</th>
                            <th>Nội dung</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${history}" var="h">
                            <tr>
                                <td><fmt:formatDate value="${h.maintenanceDate}" pattern="dd/MM/yyyy"/></td>
                                <td style="color: #dc3545; font-weight: bold;">
                                    <fmt:formatNumber value="${h.cost}" type="currency" currencySymbol="₫"/>
                                </td>
                                <td>${h.performer}</td>
                                <td>${h.description}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty history}">
                            <tr>
                                <td colspan="4" style="text-align: center; color: gray; padding: 30px;">
                                    Chưa có dữ liệu bảo trì nào.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        
    </div>
</div>

</div> </body>
</html>