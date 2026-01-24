package model;

/**
 * Entity class for farm_products table
 * Represents agricultural product categories
 */
public class FarmProduct {
    private Integer id;
    private String name;
    private String unit;
    private String description;
    private String status;
    private Integer createdBy;
    private String createdAt;

    public FarmProduct() {
    }

    public FarmProduct(Integer id, String name, String unit, String description, String status, Integer createdBy, String createdAt) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

