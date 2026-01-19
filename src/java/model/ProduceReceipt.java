package model;
import java.sql.Date;
import java.sql.Timestamp;

public class ProduceReceipt {
    private int id;
    private String receiptCode;
    private int memberId;
    private Date purchaseDate;
    private double totalAmount;
    private double amountPaid;
    private String note;
    private Timestamp createdAt;

    // Constructor, Getter, Setter (Bạn tự generate nhé - Alt+Insert trong Netbeans)
    public ProduceReceipt() {}

    public ProduceReceipt(int id, String receiptCode, int memberId, Date purchaseDate, double totalAmount, double amountPaid, String note, Timestamp createdAt) {
        this.id = id;
        this.receiptCode = receiptCode;
        this.memberId = memberId;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
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