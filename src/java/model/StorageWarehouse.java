package model;

/**
 * Entity class for storage_warehouses table
 * Represents storage warehouse locations
 */
public class StorageWarehouse {
    private Integer id;
    private String name;
    private String location;
    private String description;
    private Integer coopId;

    public StorageWarehouse() {
    }

    public StorageWarehouse(Integer id, String name, String location, String description, Integer coopId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.description = description;
        this.coopId = coopId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCoopId() {
        return coopId;
    }

    public void setCoopId(Integer coopId) {
        this.coopId = coopId;
    }
}

