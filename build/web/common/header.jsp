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