package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class FixedAsset {

    private int id;
    private String code;
    private String name;
    private int categoryId;
    private int purchaseReceiptId; // Có thể null trong DB, nhưng int mặc định là 0
    private Date purchaseDate;
    private BigDecimal initialValue; // Giá trị ban đầu
    private BigDecimal currentValue; // Giá trị hiện tại (sau khấu hao)
    private String status;           // ACTIVE, BROKEN, LIQUIDATED...
    private String location;
    private Timestamp createdAt;

    public FixedAsset() {
    }

    public FixedAsset(int id, String code, String name, int categoryId, int purchaseReceiptId, Date purchaseDate, BigDecimal initialValue, BigDecimal currentValue, String status, String location, Timestamp createdAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.categoryId = categoryId;
        this.purchaseReceiptId = purchaseReceiptId;
        this.purchaseDate = purchaseDate;
        this.initialValue = initialValue;
        this.currentValue = currentValue;
        this.status = status;
        this.location = location;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPurchaseReceiptId() {
        return purchaseReceiptId;
    }

    public void setPurchaseReceiptId(int purchaseReceiptId) {
        this.purchaseReceiptId = purchaseReceiptId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(BigDecimal currentValue) {
        this.currentValue = currentValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
