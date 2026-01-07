package model;

import java.sql.Timestamp;

/**
 * Entity class for batch_inventory table
 * Represents inventory of production batches in warehouses
 */
public class BatchInventory {
    private Integer id;
    private Integer warehouseId;
    private Integer batchId;
    private Double remainingQuantity;
    private String unit;
    private Timestamp updatedAt;

    public BatchInventory() {
    }

    public BatchInventory(Integer id, Integer warehouseId, Integer batchId,
                          Double remainingQuantity, String unit, Timestamp updatedAt) {
        this.id = id;
        this.warehouseId = warehouseId;
        this.batchId = batchId;
        this.remainingQuantity = remainingQuantity;
        this.unit = unit;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Double getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(Double remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

