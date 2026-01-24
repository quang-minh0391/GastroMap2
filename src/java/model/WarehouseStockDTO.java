package model;

public class WarehouseStockDTO {
    private int id;
    private String name;
    private double current_stock;

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getCurrent_stock() { return current_stock; }
    public void setCurrent_stock(double current_stock) { this.current_stock = current_stock; }
}