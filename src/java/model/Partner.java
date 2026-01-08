package model;


import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class Partner {
    private int id;
    private String name;
    private String type;
    private String phone;
    private String address;
    private String taxCode;     // Mapping từ cột tax_code
    private Date createdDate;   // Mapping từ cột created_date (dùng java.sql.Date cho JDBC)

    // 1. Constructor mặc định (Bắt buộc để JSP/Servlet khởi tạo object)
    public Partner() {
    }

    // 2. Constructor đầy đủ tham số (Dùng khi đọc dữ liệu từ Database lên)
    public Partner(int id, String name, String type, String phone, String address, String taxCode, Date createdDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.address = address;
        this.taxCode = taxCode;
        this.createdDate = createdDate;
    }

    // 3. Constructor không có ID (Dùng khi tạo mới để insert vào DB)
    public Partner(String name, String type, String phone, String address, String taxCode, Date createdDate) {
        this.name = name;
        this.type = type;
        this.phone = phone;
        this.address = address;
        this.taxCode = taxCode;
        this.createdDate = createdDate;
    }

    // --- Getters và Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
