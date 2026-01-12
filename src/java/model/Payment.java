package model;

public class Payment {
    private int memberId;
    private int planId;
    private long amount;    // ĐÃ ĐỔI SANG LONG
    private long orderCode; 
    private String status;

    public Payment(int memberId, int planId, long amount, long orderCode, String status) {
        this.memberId = memberId;
        this.planId = planId;
        this.amount = amount;
        this.orderCode = orderCode;
        this.status = status;
    }

    // Getters và Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public long getAmount() { return amount; } // Trả về long
    public void setAmount(long amount) { this.amount = amount; }

    public long getOrderCode() { return orderCode; }
    public void setOrderCode(long orderCode) { this.orderCode = orderCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}