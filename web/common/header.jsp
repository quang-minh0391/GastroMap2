<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>GastroMap Admin</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <% 
            String currentUri = request.getRequestURI(); 
            String fullName = (String) session.getAttribute("full_name");
            String displayLetter = (fullName != null && !fullName.isEmpty()) ? fullName.substring(0, 1).toUpperCase() : "U";
        %>

        <div class="sidebar shadow">
            <div class="brand-section py-4 border-bottom border-secondary mb-2 position-relative">
                <div class="position-absolute" style="top: 15px; right: 20px;">
                    <a href="${pageContext.request.contextPath}/admin/notifications.jsp" class="text-white text-decoration-none position-relative notification-bell">
                        <i class="bi bi-bell-fill fs-5"></i>
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem;">
                            3
                        </span>
                    </a>
                </div>

                <div class="text-center">
                    <h4 class="text-white m-0 mb-3">🍀 Gastromap</h4>

                    <div class="user-profile-top mt-3">
                        <a href="${pageContext.request.contextPath}/profile.jsp" class="text-decoration-none d-flex flex-column align-items-center">
                            <div class="avatar-circle mb-2">
                                <span class="text-white fw-bold"><%= displayLetter %></span>
                            </div>
                            <span class="text-white fw-bold"><%= (fullName != null) ? fullName : "Người dùng" %></span>
                            <small class="text-success" style="font-size: 0.75rem;">● Đang hoạt động</small>
                        </a>
                    </div>
                </div>
            </div>

            <nav class="nav flex-column">
                <a class="nav-link <%= currentUri.contains("product.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/admin/product.jsp">1. 📦 Quản lý Sản phẩm</a>
                <a class="nav-link <%= currentUri.contains("sales.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/admin/sales.jsp">2. 🛒 Quản lý Bán hàng</a>
                <a class="nav-link <%= currentUri.contains("finance.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/admin/finance.jsp">3. 💰 Tài chính & Báo cáo</a>
                <a class="nav-link <%= currentUri.contains("dashboard.jsp") || currentUri.contains("index.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/admin/dashboard.jsp">4. 🤝 Quản lý HTX Tích hợp</a>
                <a class="nav-link <%= currentUri.contains("qr_code.jsp") ? "active" : "" %>" href="${pageContext.request.contextPath}/admin/qr_code.jsp">5. 🔍 QR & Truy xuất</a>
                <a class="nav-link <%= currentUri.contains("list_materials.jsp") ? "active" : "" %>"  href="${pageContext.request.contextPath}/SearchMaterialServlet">6. 📦 Quản lý Vật Tư</a>
            </nav>

            <div class="logout-section mt-auto p-3">
                <form action="${pageContext.request.contextPath}/loginURL" method="POST" class="m-0">
                    <input type="hidden" name="service" value="logoutUser">
                    <button type="submit" class="btn btn-outline-danger btn-sm w-100 py-2">🚪 Đăng xuất</button>
                </form>
            </div>
        </div>

        <div class="main-area">