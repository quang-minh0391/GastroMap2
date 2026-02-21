package model;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PaymentVoucher {
    private int id;
    private String voucherCode;
    private String voucherType; // 'RECEIPT' (Thu) or 'PAYMENT' (Chi)
    private Integer memberId;   // Nullable
    private Integer partnerId;  // Nullable
    private BigDecimal amount;
    private String paymentMethod;
    private String description;
    private Timestamp createdDate;

    public PaymentVoucher() {}

    public PaymentVoucher(int id, String voucherCode, String voucherType, Integer memberId, Integer partnerId, BigDecimal amount, String paymentMethod, String description, Timestamp createdDate) {
        this.id = id;
        this.voucherCode = voucherCode;
        this.voucherType = voucherType;
        this.memberId = memberId;
        this.partnerId = partnerId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.createdDate = createdDate;
    }
    
    // Getter & Setter... (Bạn tự generate nhé)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }
    public String getVoucherType() { return voucherType; }
    public void setVoucherType(String voucherType) { this.voucherType = voucherType; }
    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }
    public Integer getPartnerId() { return partnerId; }
    public void setPartnerId(Integer partnerId) { this.partnerId = partnerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}