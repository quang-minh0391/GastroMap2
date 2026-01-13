package model;
public class SubscriptionPlan {
    private int id;
    private String planName;
    private double price;
    private int durationDays;
    public SubscriptionPlan(int id, String planName, double price, int durationDays) {
        this.id = id; this.planName = planName; this.price = price; this.durationDays = durationDays;
    }
    // Getters
    public int getId() { return id; }
    public String getPlanName() { return planName; }
    public double getPrice() { return price; }
    public int getDurationDays() { return durationDays; }
}