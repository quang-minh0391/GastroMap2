/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Viet Duc
 */
import java.math.BigDecimal;
import java.sql.Timestamp;

public class FinancialTransaction {

    private int id;
    private Timestamp transactionDate;
    private int categoryId;
    private BigDecimal amount;
    private String transactionType; // 'IN' (Thu) hoặc 'OUT' (Chi)
    private String sourceTable;     // Ví dụ: 'contracts', 'purchase_receipts' (để truy vết nếu cần)
    private int sourceId;           // ID của bảng nguồn
    private String description;

    private String categoryName;

    public FinancialTransaction() {
    }

    public FinancialTransaction(int id, Timestamp transactionDate, int categoryId, BigDecimal amount, String transactionType, String sourceTable, int sourceId, String description) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.categoryId = categoryId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.sourceTable = sourceTable;
        this.sourceId = sourceId;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
