package model;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class Material {
    private int id;
    private String name;
    private String unit;
    private double unitPrice;       // Ánh xạ từ cột unit_price (DECIMAL)
    private double stockQuantity;   // Ánh xạ từ cột stock_quantity (DECIMAL)
    private String description;

    // 1. Constructor mặc định (Bắt buộc)
    public Material() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT dữ liệu lên)
    public Material(int id, String name, String unit, double unitPrice, double stockQuantity, String description) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    // 3. Constructor không có ID (Dùng khi INSERT dữ liệu mới)
    public Material(String name, String unit, double unitPrice, double stockQuantity, String description) {
        this.name = name;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.description = description;
    }

    // --- Getters và Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(double stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
