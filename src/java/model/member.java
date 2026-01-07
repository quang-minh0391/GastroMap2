package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Entity class for members table (READ ONLY for Person 1)
 * Represents cooperative members
 */
public class Member {
    private Integer id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String address;
    private Integer memberType;
    private Integer coopId;
    private String status;
    private Date expiryDate;
    private String planType;
    private Date joinedDate;
    private Timestamp createdAt;

    public Member() {
    }

    public Member(Integer id, String username, String password, String fullName,
                  String phone, String address, Integer memberType, Integer coopId,
                  String status, Date expiryDate, String planType,
                  Date joinedDate, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.memberType = memberType;
        this.coopId = coopId;
        this.status = status;
        this.expiryDate = expiryDate;
        this.planType = planType;
        this.joinedDate = joinedDate;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    public Integer getCoopId() {
        return coopId;
    }

    public void setCoopId(Integer coopId) {
        this.coopId = coopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

