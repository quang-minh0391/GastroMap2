package model;

import java.sql.Date;

public class Contract {
    private int id;
    private String contractCode;
    private int memberId;
    private String contractType;
    private Date signingDate;
    private Date expiryDate;
    private double totalValue;
    private String status;
    private String documentPath; // Trường mới thêm vào

    public Contract() {}

    // Getter và Setter cho documentPath
    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    // Các Getter và Setter khác giữ nguyên như cũ
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getContractCode() { return contractCode; }
    public void setContractCode(String contractCode) { this.contractCode = contractCode; }
    public String getContractType() { return contractType; }
    public void setContractType(String contractType) { this.contractType = contractType; }
    public Date getSigningDate() { return signingDate; }
    public void setSigningDate(Date signingDate) { this.signingDate = signingDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}