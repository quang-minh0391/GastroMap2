package model;

public class ProduceReceiptDetail {
    private int id;
    private int receiptId;
    private int productId;
    private int warehouseId;
    private double quantity;
    private double unitPrice;
    private double subtotal;

    // Constructor, Getter, Setter
    public ProduceReceiptDetail() {}

    public ProduceReceiptDetail(int id, int receiptId, int productId, int warehouseId, double quantity, double unitPrice, double subtotal) {
        this.id = id;
        this.receiptId = receiptId;
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
    
    
}