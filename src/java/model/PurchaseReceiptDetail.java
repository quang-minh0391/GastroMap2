package model;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */

public class PurchaseReceiptDetail {
    private int id;
    private int receiptId;      // FK: Liên kết với purchase_receipts
    private int produceTypeId;  // FK: Liên kết với produce_types
    private double quantity;
    private String unit;
    private double finalUnitPrice; // Ánh xạ cột final_unit_price
    private double subtotal;       // Ánh xạ cột subtotal
    private String note;

    // 1. Constructor mặc định
    public PurchaseReceiptDetail() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT)
    public PurchaseReceiptDetail(int id, int receiptId, int produceTypeId, double quantity, String unit, double finalUnitPrice, double subtotal, String note) {
        this.id = id;
        this.receiptId = receiptId;
        this.produceTypeId = produceTypeId;
        this.quantity = quantity;
        this.unit = unit;
        this.finalUnitPrice = finalUnitPrice;
        this.subtotal = subtotal;
        this.note = note;
    }

    // 3. Constructor không ID (Dùng khi INSERT)
    public PurchaseReceiptDetail(int receiptId, int produceTypeId, double quantity, String unit, double finalUnitPrice, double subtotal, String note) {
        this.receiptId = receiptId;
        this.produceTypeId = produceTypeId;
        this.quantity = quantity;
        this.unit = unit;
        this.finalUnitPrice = finalUnitPrice;
        this.subtotal = subtotal;
        this.note = note;
    }

    // --- Getters và Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getProduceTypeId() {
        return produceTypeId;
    }

    public void setProduceTypeId(int produceTypeId) {
        this.produceTypeId = produceTypeId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getFinalUnitPrice() {
        return finalUnitPrice;
    }

    public void setFinalUnitPrice(double finalUnitPrice) {
        this.finalUnitPrice = finalUnitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // Phương thức tiện ích tính thành tiền
    public void calculateSubtotal() {
        this.subtotal = this.quantity * this.finalUnitPrice;
    }
}