package model;

public class CapitalContribution {
    private int id;
    private int memberId;
    private String contribution;     // Nội dung: Góp tiền, góp đất...
    private String contributionDate; // Kiểu String
    private String receiptNumber;    // Lưu đường dẫn ảnh
    private String note;

    public CapitalContribution() {}

    public CapitalContribution(int id, int memberId, String contribution, String contributionDate, String receiptNumber, String note) {
        this.id = id;
        this.memberId = memberId;
        this.contribution = contribution;
        this.contributionDate = contributionDate;
        this.receiptNumber = receiptNumber;
        this.note = note;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public String getContribution() { return contribution; }
    public void setContribution(String contribution) { this.contribution = contribution; }
    public String getContributionDate() { return contributionDate; }
    public void setContributionDate(String contributionDate) { this.contributionDate = contributionDate; }
    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}