package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entity class for batch_stock_ins table
 * Represents stock-in records for production batches
 */
public class BatchStockIn {
    private Integer id;
    private Integer batchId;
    private Integer warehouseId;
    private Double quantity;
    private String unit;
    private Date receivedDate;
    private String receivedBy;
    private String note;
    private Timestamp createdAt;

    public BatchStockIn() {
    }

    public BatchStockIn(Integer id, Integer batchId, Integer warehouseId, Double quantity,
                        String unit, Date receivedDate, String receivedBy,
                        String note, Timestamp createdAt) {
        this.id = id;
        this.batchId = batchId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.unit = unit;
        this.receivedDate = receivedDate;
        this.receivedBy = receivedBy;
        this.note = note;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

