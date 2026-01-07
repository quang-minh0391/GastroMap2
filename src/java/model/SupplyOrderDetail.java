package model;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */

public class SupplyOrderDetail {
    private int id;
    private int orderId;       // FK: Liên kết với bảng supply_orders
    private int materialId;    // FK: Liên kết với bảng materials
    private double quantity;
    private double priceAtTime; // Giá tại thời điểm đặt hàng (tránh ảnh hưởng khi giá gốc thay đổi)
    private double subtotal;    // Thành tiền (quantity * priceAtTime)

    // 1. Constructor mặc định
    public SupplyOrderDetail() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT)
    public SupplyOrderDetail(int id, int orderId, int materialId, double quantity, double priceAtTime, double subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
        this.subtotal = subtotal;
    }

    // 3. Constructor không ID (Dùng khi INSERT)
    // Lưu ý: subtotal có thể được tính toán tự động trong code Java trước khi truyền vào đây
    public SupplyOrderDetail(int orderId, int materialId, double quantity, double priceAtTime, double subtotal) {
        this.orderId = orderId;
        this.materialId = materialId;
        this.quantity = quantity;
        this.priceAtTime = priceAtTime;
        this.subtotal = subtotal;
    }

    // --- Getters và Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtTime() {
        return priceAtTime;
    }

    public void setPriceAtTime(double priceAtTime) {
        this.priceAtTime = priceAtTime;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    // Phương thức tiện ích để tự tính thành tiền (nếu cần dùng trong logic Java)
    public void calculateSubtotal() {
        this.subtotal = this.quantity * this.priceAtTime;
    }
}
