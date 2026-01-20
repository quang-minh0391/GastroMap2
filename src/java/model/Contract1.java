package model;

public class Contract1 {
    private int id;
    private String contractCode;
    private int memberId;
    private String contractType;
    private String signingDate; // Kiểu String theo mẫu của bạn
    private String expiryDate;  // Kiểu String theo mẫu của bạn
    private double totalValue;
    private String status;
    private String documentPath; // Lưu đường dẫn ảnh

    public Contract1() {}

    public Contract1(int id, String contractCode, int memberId, String contractType, 
                     String signingDate, String expiryDate, double totalValue, 
                     String status, String documentPath) {
        this.id = id;
        this.contractCode = contractCode;
        this.memberId = memberId;
        this.contractType = contractType;
        this.signingDate = signingDate;
        this.expiryDate = expiryDate;
        this.totalValue = totalValue;
        this.status = status;
        this.documentPath = documentPath;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }

    public String getSigningDate() { return signingDate; }
    public void setSigningDate(String signingDate) { this.signingDate = signingDate; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }
}