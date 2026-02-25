<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>GastroMap Admin - Hệ thống Quản trị</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <% 
            String currentUri = request.getRequestURI(); 
            String fullName = (String) session.getAttribute("full_name");
            String displayLetter = (fullName != null && !fullName.isEmpty()) ? fullName.substring(0, 1).toUpperCase() : "U";
            Integer memberType = (Integer) session.getAttribute("member_type");
            // memberType: 1 = Nông dân, 2 = HTX, 3 = Quản lý HTX
            boolean isFarmer = (memberType != null && memberType == 1);
            boolean isHTX = (memberType != null && memberType == 2);
            boolean isManager = (memberType != null && memberType == 3);
        %>

        <div class="sidebar shadow">
            <div class="brand-section py-3 border-bottom border-secondary mb-1">
                <div class="text-center">
                    <div class="d-flex align-items-center justify-content-center gap-3 mb-3">
                        <a href="${pageContext.request.contextPath}/index.jsp" class="text-decoration-none">
                            <h5 class="text-white m-0">🍀 Gastromap</h5>
                        </a>

                        <div class="notification-wrapper">
                            <a href="${pageContext.request.contextPath}/meetingManager?service=list" class="text-decoration-none d-inline-block position-relative">
                                <i class="bi bi-bell-fill fs-6" id="bell-icon" style="color: #adb5bd;"></i>
                                <span id="noti-badge" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.55rem;">
                                    0
                                </span>
                            </a>
                        </div>
                    </div>

                    <div class="user-profile-top mt-2">
                        <a href="${pageContext.request.contextPath}/profile" class="text-decoration-none d-flex flex-column align-items-center">
                            <div class="avatar-circle mb-1" style="width: 38px; height: 38px;">
                                <span class="text-white fw-bold" style="font-size: 0.9rem;"><%= displayLetter %></span>
                            </div>
                            <span class="text-white fw-bold" style="font-size: 0.85rem;"><%= (fullName != null) ? fullName : "Người dùng" %></span>
                            <small class="text-success" style="font-size: 0.7rem;">● Đang hoạt động</small>
                        </a>
                    </div>
                </div>
            </div>

            <nav class="sidebar-nav-container overflow-y-auto" style="max-height: calc(100vh - 250px);">

                <%-- ========== MENU CHO HTX VÀ QUẢN LÝ HTX (Type 2, 3) ========== --%>
                <% if (isHTX || isManager) { %>
                <div class="nav-item-group w-100 d-block">
                    <div class="text-uppercase px-3 py-2 text-secondary" style="font-size: 0.65rem; letter-spacing: 1px; opacity: 0.8;">
                        Hệ thống
                    </div>
                    <a class="nav-link w-100 <%= (currentUri.contains("memberManager") || currentUri.contains("Member.jsp")) ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/memberManager?service=list">
                        <i class="bi bi-people-fill me-2"></i> Quản lý Thành viên
                    </a>
                    <a class="nav-link w-100 <%= currentUri.contains("meeting") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/meetingManager?service=list">
                        <i class="bi bi-calendar-check me-2"></i> Quản lý Cuộc họp
                    </a>
                </div>

                <div class="nav-item-group w-100 d-block mt-1">
                    <div class="text-uppercase px-3 py-2 text-secondary" style="font-size: 0.65rem; letter-spacing: 1px; opacity: 0.8;">
                        Sản xuất
                    </div>
                    <a class="nav-link w-100 <%= currentUri.contains("farm-products") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/farmProduct?service=list">
                        <i class="bi bi-box-seam me-2"></i> Nông sản
                    </a>
                    <a class="nav-link w-100 <%= currentUri.contains("production-batches") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/productionBatch?service=list">
                        <i class="bi bi-layers-half me-2"></i> Lô sản xuất
                    </a>
                    <a class="nav-link w-100 <%= currentUri.contains("traceability") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/traceability?service=history">
                        <i class="bi bi-qr-code-scan me-2"></i> Truy xuất nguồn gốc
                    </a>
                </div>

                <div class="nav-item-group w-100 d-block mt-1">
                    <div class="text-uppercase px-3 py-2 text-secondary" style="font-size: 0.65rem; letter-spacing: 1px; opacity: 0.8;">
                        Kho & Tài chính
                    </div>

                    <a class="nav-link w-100 <%= currentUri.contains("AssetServlet") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/AssetServlet">
                        <i class="bi bi-tools me-2"></i> Quản lý Tài sản
                    </a>

                    <a class="nav-link w-100 <%= currentUri.contains("/finance") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/finance">
                        <i class="bi bi-graph-up-arrow me-2"></i> Sổ cái Tài chính
                    </a>
                    
                    <% Integer mType = (Integer) session.getAttribute("member_type"); 
                       if (mType != null && mType == 1) { %>
                    <a class="nav-link w-100 <%= currentUri.contains("FarmerDebtHistoryServlet") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/FarmerDebtHistoryServlet">
                        <i class="bi bi-clock-history me-2"></i> Lịch sử nợ của tôi
                    </a>
                    <% } %>

                    <a class="nav-link w-100 <%= currentUri.contains("admin/finance.jsp") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/admin/finance.jsp">
                        <i class="bi bi-cash-stack me-2"></i> Tài chính & Báo cáo
                    </a>

                    <a class="nav-link w-100 <%= currentUri.contains("SearchMaterialServlet") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/SearchMaterialServlet">
                        <i class="bi bi-box-fill me-2"></i> Quản lí vật tư
                    </a>
                    
                    <a class="nav-link w-100 <%= currentUri.contains("DebtManagementServlet") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/DebtManagementServlet">
                        <i class="bi bi-credit-card-2-back-fill me-2"></i> Quản lí nợ
                    </a>
                     
                    <a class="nav-link w-100 <%= currentUri.contains("purchase_receipt.jsp") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/purchase/purchase_receipt.jsp">
                        <i class="bi bi-cart-fill me-2"></i> Thu mua sản phẩm
                    </a>
                    <a class="nav-link w-100 <%= (currentUri.contains("contractManager")) ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/contractManager?service=list">
                        <i class="bi bi-file-earmark-text-fill me-2"></i> Quản lý Hợp đồng
                    </a>
                </div>
                <% } %>

                <%-- ========== MENU CHO NÔNG DÂN (Type 1) ========== --%>
                <% if (isFarmer) { %>
                <div class="nav-item-group w-100 d-block">
                    <div class="text-uppercase px-3 py-2 text-secondary" style="font-size: 0.65rem; letter-spacing: 1px; opacity: 0.8;">
                        Chức năng
                    </div>
                    <a class="nav-link w-100 <%= currentUri.contains("meeting") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/meetingManager?service=list">
                        <i class="bi bi-calendar-check me-2"></i> Quản lý Cuộc họp
                    </a>
                    <a class="nav-link w-100 <%= (currentUri.contains("finance.jsp") || currentUri.contains("capital.jsp")) ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/admin/finance.jsp">
                        <i class="bi bi-cash-stack me-2"></i> Tài chính & Báo cáo
                    </a>
                    <a class="nav-link w-100 <%= currentUri.contains("FarmerDebtHistoryServlet") ? "active" : "" %>" 
                       href="${pageContext.request.contextPath}/FarmerDebtHistoryServlet">
                        <i class="bi bi-clock-history me-2"></i> Lịch sử nợ của tôi
                    </a>
                </div>
                <% } %>

            </nav>

            <div class="logout-section mt-auto p-2">
                <form action="${pageContext.request.contextPath}/loginURL" method="POST" class="m-0">
                    <input type="hidden" name="service" value="logoutUser">
                    <button type="submit" class="btn btn-outline-danger btn-sm w-100 py-1">
                        <i class="bi bi-box-arrow-right me-1"></i> Đăng xuất
                    </button>
                </form>
            </div>
        </div>

        <div class="main-area">

        <script>
            var socketPath = "ws://" + window.location.host + "${pageContext.request.contextPath}/notificationServer";
            var socket = new WebSocket(socketPath);

            socket.onmessage = function (event) {
                var message = event.data;
                var badge = document.getElementById("noti-badge");
                var count = parseInt(badge.innerText) || 0;
                badge.innerText = count + 1;

                var bell = document.getElementById("bell-icon");
                bell.classList.add("bell-ring");
                setTimeout(() => { bell.classList.remove("bell-ring"); }, 1000);

                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 4000,
                    timerProgressBar: true
                });

                Toast.fire({
                    icon: 'info',
                    title: 'Thông báo mới',
                    text: message
                });
            };

            socket.onopen = function () { console.log("Hệ thống thông báo: Online"); };
            socket.onclose = function () { console.log("Hệ thống thông báo: Offline"); };
        </script>