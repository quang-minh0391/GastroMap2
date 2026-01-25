package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class AssetMaintenance {

    private int id;
    private int assetId;
    private Date maintenanceDate;
    private BigDecimal cost;
    private String performer; // Người/Đơn vị thực hiện sửa chữa
    private String description;
    private Timestamp createdAt;

    public AssetMaintenance() {
    }

    public AssetMaintenance(int id, int assetId, Date maintenanceDate, BigDecimal cost, String performer, String description, Timestamp createdAt) {
        this.id = id;
        this.assetId = assetId;
        this.maintenanceDate = maintenanceDate;
        this.cost = cost;
        this.performer = performer;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

}
