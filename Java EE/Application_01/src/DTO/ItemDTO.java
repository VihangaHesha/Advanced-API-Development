package DTO;

public class ItemDTO {
    private String itemCode;
    private String name;
    private int qty;
    private double unitPrice;

    public ItemDTO(String itemCode, String name, int qty, double unitPrice) {
        this.itemCode = itemCode;
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "DTO.ItemDTO{" +
                "itemCode='" + itemCode + '\'' +
                ", name='" + name + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
