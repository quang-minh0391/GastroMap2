/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */

import java.sql.Timestamp;

public class MemberTransactionLedger {
    private int id;
    private int memberId;           // FK: Liên kết với thành viên
    private Timestamp transactionDate; // DATETIME
    private String transactionType; // Ví dụ: "DONG_PHI", "MUA_HANG"
    private Integer referenceId;    // ID tham chiếu (ví dụ ID đơn hàng), có thể null
    private double amount;          // Số tiền
    private String entryType;       // ENUM: 'DEBIT' (Nợ/Tăng) hoặc 'CREDIT' (Có/Giảm)
    private Double balanceAfter;    // Số dư sau giao dịch (có thể null nếu chưa tính)
    private String note;

    // 1. Constructor mặc định
    public MemberTransactionLedger() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT)
    public MemberTransactionLedger(int id, int memberId, Timestamp transactionDate, String transactionType, 
                                   Integer referenceId, double amount, String entryType, Double balanceAfter, String note) {
        this.id = id;
        this.memberId = memberId;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.referenceId = referenceId;
        this.amount = amount;
        this.entryType = entryType;
        this.balanceAfter = balanceAfter;
        this.note = note;
    }

    // 3. Constructor không ID (Dùng khi INSERT)
    // Lưu ý: transactionDate có thể để null nếu DB tự set CURRENT_TIMESTAMP, 
    // nhưng tốt nhất nên truyền từ Java để đồng bộ.
    public MemberTransactionLedger(int memberId, Timestamp transactionDate, String transactionType, 
                                   Integer referenceId, double amount, String entryType, Double balanceAfter, String note) {
        this.memberId = memberId;
        this.transactionDate = transactionDate;
        this.transactionType = transactionType;
        this.referenceId = referenceId;
        this.amount = amount;
        this.entryType = entryType;
        this.balanceAfter = balanceAfter;
        this.note = note;
    }

    // --- Getters và Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}