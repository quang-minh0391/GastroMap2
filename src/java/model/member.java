package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entity class for members table (READ ONLY for Person 1) Represents
 * cooperative members
 */
public class member {

    private int id;                  // ID of the user
    private String username;         // Username
    private String password;
    private String email;// Password (hashed);
    private String full_name;         // Full name
    private String phone;            // Phone number
    private String address;          // Address
    private int member_type;          // Member type (1: Farmer, 2: Cooperative;3: quản lí htx)
    private Integer coop_id;          // Cooperative ID (nullable)
    private String status;           // Account status (Active, Inactive, Expired)
    private String expiry_date;       // Account expiry date (formatted as String)
    private String plan_type;         // Plan type (MONTHLY, YEARLY, NONE)
    private String joined_date;       // Joined date (formatted as String)
    private String created_at;

    private double current_debt;

    public double getCurrent_debt() {
        return current_debt;
    }

    public void setCurrent_debt(double current_debt) {
        this.current_debt = current_debt;
    }

    public member(int id, String full_name, String phone, String address, double current_debt) {
        this.id = id;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
        this.current_debt = current_debt;
    }
    
    

    public member() {
    }

    public member(int id, String username, String password, String email, String full_name, String phone, String address, int member_type, Integer coop_id, String status, String expiry_date, String plan_type, String joined_date, String created_at) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
        this.member_type = member_type;
        this.coop_id = coop_id;
        this.status = status;
        this.expiry_date = expiry_date;
        this.plan_type = plan_type;
        this.joined_date = joined_date;
        this.created_at = created_at;
    }

    public member(String username, String password, String email, String full_name, String phone, String address, int member_type, Integer coop_id, String status, String expiry_date, String plan_type, String joined_date, String created_at) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.full_name = full_name;
        this.phone = phone;
        this.address = address;
        this.member_type = member_type;
        this.coop_id = coop_id;
        this.status = status;
        this.expiry_date = expiry_date;
        this.plan_type = plan_type;
        this.joined_date = joined_date;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMember_type() {
        return member_type;
    }

    public void setMember_type(int member_type) {
        this.member_type = member_type;
    }

    public Integer getCoop_id() {
        return coop_id;
    }

    public void setCoop_id(Integer coop_id) {
        this.coop_id = coop_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getPlan_type() {
        return plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
