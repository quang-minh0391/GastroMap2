/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class ProduceType {
    private int id;
    private String name;
    private String defaultUnit; // Ánh xạ từ cột default_unit

    // 1. Constructor mặc định (Bắt buộc)
    public ProduceType() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT)
    public ProduceType(int id, String name, String defaultUnit) {
        this.id = id;
        this.name = name;
        this.defaultUnit = defaultUnit;
    }

    // 3. Constructor không có ID (Dùng khi INSERT)
    public ProduceType(String name, String defaultUnit) {
        this.name = name;
        this.defaultUnit = defaultUnit;
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

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }
    
    // Tùy chọn: Ghi đè phương thức toString để debug dễ hơn
    @Override
    public String toString() {
        return "ProduceType [id=" + id + ", name=" + name + ", defaultUnit=" + defaultUnit + "]";
    }
    
}
