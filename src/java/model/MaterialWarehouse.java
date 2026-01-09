/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class MaterialWarehouse {
    private int id;
    private String name;
    private String location;
    private String description;
    private Date createdDate;

    // Constructor
    public MaterialWarehouse() {}

    public MaterialWarehouse(int id, String name, String location, String description, Date createdDate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.createdDate = createdDate;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
}

