package model;

import java.math.BigDecimal;

public class FundCategory {

    private int id;
    private String fundName;
    private String description;
    private BigDecimal currentBalance;

    public FundCategory() {
    }

    public FundCategory(int id, String fundName, String description, BigDecimal currentBalance) {
        this.id = id;
        this.fundName = fundName;
        this.description = description;
        this.currentBalance = currentBalance;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }
}
