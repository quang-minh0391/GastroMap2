package model;

import java.sql.Timestamp;

/**
 * Entity class for batch_qr_codes table
 * Represents QR codes generated for production batches
 */
public class BatchQRCode {
    private Integer id;
    private Integer batchId;
    private String qrValue;
    private String status;
    private Timestamp createdAt;

    public BatchQRCode() {
    }

    public BatchQRCode(Integer id, Integer batchId, String qrValue,
                       String status, Timestamp createdAt) {
        this.id = id;
        this.batchId = batchId;
        this.qrValue = qrValue;
        this.status = status;
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

    public String getQrValue() {
        return qrValue;
    }

    public void setQrValue(String qrValue) {
        this.qrValue = qrValue;
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

