<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GastroMap Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%
        // Lấy đường dẫn trang hiện tại để xử lý màu xanh (active) trên menu
        String currentUri = request.getRequestURI();
    %>
    <div class="sidebar">
        <div class="text-center mb-4">
            <h4 class="text-white">🍀 Gastromap</h4>
        </div>
        <nav class="nav flex-column">
            <!-- Module Người 1: Quản lý Sản xuất & Truy xuất -->
            <div class="nav-section-title text-white-50 px-3 py-2 small fw-bold">SẢN XUẤT & TRUY XUẤT</div>
            
            <a class="nav-link <%= currentUri.contains("farm-products") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/farm-products">🌾 Danh mục Nông sản</a>
            
            <a class="nav-link <%= currentUri.contains("/batches") && !currentUri.contains("qr") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/batches">📦 Lô Sản xuất</a>
            
            <a class="nav-link <%= currentUri.contains("warehouses") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/warehouses">🏭 Quản lý Kho</a>
            
            <a class="nav-link <%= currentUri.contains("inventory") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/inventory">📊 Tồn Kho</a>
            
            <a class="nav-link <%= currentUri.contains("stock-ins") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/stock-ins">📥 Nhập Kho</a>
            
            <a class="nav-link <%= currentUri.contains("qr-codes") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/qr-codes">📱 Mã QR</a>
            
            <a class="nav-link <%= currentUri.contains("traceability") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/traceability">🔍 Truy xuất Nguồn gốc</a>
            
            <hr class="border-secondary my-2">
            
            <!-- Các module khác -->
            <div class="nav-section-title text-white-50 px-3 py-2 small fw-bold">QUẢN LÝ KHÁC</div>
            
            <a class="nav-link <%= currentUri.contains("product.jsp") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/admin/product.jsp">1. 📦 Quản lý Sản phẩm</a>
            
            <a class="nav-link <%= currentUri.contains("sales.jsp") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/admin/sales.jsp">2. 🛒 Quản lý Bán hàng</a>
            
            <a class="nav-link <%= currentUri.contains("finance.jsp") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/admin/finance.jsp">3. 💰 Tài chính & Báo cáo</a>
            
            <a class="nav-link <%= currentUri.contains("dashboard.jsp") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/admin/dashboard.jsp">4. 🤝 Quản lý HTX Tích hợp</a>
            
            <a class="nav-link <%= currentUri.contains("qr_code.jsp") ? "active" : "" %>" 
               href="${pageContext.request.contextPath}/admin/qr_code.jsp">5. 🔍 QR & Truy xuất</a>
        </nav>
    </div>

    <div class="main-area">
        <div class="top-search d-flex align-items-center mb-4">
            <span class="me-2">🔍</span>
            <input type="text" class="form-control border-0 bg-transparent" placeholder="Gastromap.com">
        </div>
