/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
import java.sql.Timestamp;

public class MaterialInventory {
    private int id;
    private Material material;          // Vật tư
    private MaterialWarehouse warehouse; // Kho
    private double quantity;           // Số lượng tồn
    private String unit;               // Đơn vị
    private Timestamp updatedAt;       // Thời gian cập nhật

    // Constructor
    public MaterialInventory() {}

    public MaterialInventory(int id, Material material, MaterialWarehouse warehouse, double quantity, String unit, Timestamp updatedAt) {
        this.id = id;
        this.material = material;
        this.warehouse = warehouse;
        this.quantity = quantity;
        this.unit = unit;
        this.updatedAt = updatedAt;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }

    public MaterialWarehouse getWarehouse() { return warehouse; }
    public void setWarehouse(MaterialWarehouse warehouse) { this.warehouse = warehouse; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
