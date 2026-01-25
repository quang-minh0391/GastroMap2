package model;

import java.math.BigDecimal;

public class AssetCategory {

    private int id;
    private String name;
    private BigDecimal depreciationRate; // Tỉ lệ khấu hao (decimal)
    private String description;

    public AssetCategory() {
    }

    public AssetCategory(int id, String name, BigDecimal depreciationRate, String description) {
        this.id = id;
        this.name = name;
        this.depreciationRate = depreciationRate;
        this.description = description;
    }

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

    public BigDecimal getDepreciationRate() {
        return depreciationRate;
    }

    public void setDepreciationRate(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
