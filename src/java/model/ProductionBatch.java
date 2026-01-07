package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entity class for production_batches table
 * Represents a production batch of agricultural products
 */
public class ProductionBatch {
    private Integer id;
    private String batchCode;
    private Integer productId;
    private Integer memberId;
    private Date harvestDate;
    private Date expiryDate;
    private Double totalQuantity;
    private String unit;
    private String status;
    private Timestamp createdAt;

    public ProductionBatch() {
    }

    public ProductionBatch(Integer id, String batchCode, Integer productId, Integer memberId,
                           Date harvestDate, Date expiryDate, Double totalQuantity,
                           String unit, String status, Timestamp createdAt) {
        this.id = id;
        this.batchCode = batchCode;
        this.productId = productId;
        this.memberId = memberId;
        this.harvestDate = harvestDate;
        this.expiryDate = expiryDate;
        this.totalQuantity = totalQuantity;
        this.unit = unit;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getHarvestDate() {
        return harvestDate;
    }

    public void setHarvestDate(Date harvestDate) {
        this.harvestDate = harvestDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

