/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */


import java.sql.Date;

public class SupplyOrder {
    private int id;
    private String code;
    private int memberId;         // FK: Liên kết với bảng Members (bắt buộc)
    private Integer contractId;   // FK: Liên kết với bảng Contracts (có thể null)
    private Date orderDate;
    private double totalAmount;   // DECIMAL(15,2)
    private String paymentStatus;
    private String note;

    // 1. Constructor mặc định
    public SupplyOrder() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT)
    public SupplyOrder(int id, String code, int memberId, Integer contractId, Date orderDate, double totalAmount, String paymentStatus, String note) {
        this.id = id;
        this.code = code;
        this.memberId = memberId;
        this.contractId = contractId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.note = note;
    }

    // 3. Constructor không ID (Dùng khi INSERT)
    public SupplyOrder(String code, int memberId, Integer contractId, Date orderDate, double totalAmount, String paymentStatus, String note) {
        this.code = code;
        this.memberId = memberId;
        this.contractId = contractId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
        this.note = note;
    }

    // --- Getters và Setters ---

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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
