package model;

public class DebtDTO {
    private int id;
    private String name;
    private String phone;
    private double amount;

    
    

    // Getter và Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}