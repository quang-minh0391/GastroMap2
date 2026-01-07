/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */

import java.sql.Date;

public class PurchaseReceipt {
    private int id;
    private String code;
    private int memberId;           // FK: Bắt buộc
    private Integer contractId;     // FK: Có thể null (nếu nhập ngoài hợp đồng)
    private Integer partnerId;      // FK: Có thể null
    private Date receiptDate;
    private double totalAmount;
    private String warehouseLocation; // Ánh xạ từ warehouse_location
    private String paymentStatus;

    // 1. Constructor mặc định
    public PurchaseReceipt() {
    }

    // 2. Constructor đầy đủ (Dùng khi SELECT)
    public PurchaseReceipt(int id, String code, int memberId, Integer contractId, Integer partnerId, 
                           Date receiptDate, double totalAmount, String warehouseLocation, String paymentStatus) {
        this.id = id;
        this.code = code;
        this.memberId = memberId;
        this.contractId = contractId;
        this.partnerId = partnerId;
        this.receiptDate = receiptDate;
        this.totalAmount = totalAmount;
        this.warehouseLocation = warehouseLocation;
        this.paymentStatus = paymentStatus;
    }

    // 3. Constructor không ID (Dùng khi INSERT)
    public PurchaseReceipt(String code, int memberId, Integer contractId, Integer partnerId, 
                           Date receiptDate, double totalAmount, String warehouseLocation, String paymentStatus) {
        this.code = code;
        this.memberId = memberId;
        this.contractId = contractId;
        this.partnerId = partnerId;
        this.receiptDate = receiptDate;
        this.totalAmount = totalAmount;
        this.warehouseLocation = warehouseLocation;
        this.paymentStatus = paymentStatus;
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

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}