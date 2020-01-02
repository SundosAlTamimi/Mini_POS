package com.falconssoft.minipos.Modle;

public class Items {
    private String itemNo;
    private String itemName;
    private double price;
    private String category;
    private int pic;
    private double qty;
    private double net;

    public Items(String itemNo, String itemName, double price, int pic, String category) {
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.pic = pic;
    }

    public Items(String itemNo, String itemName, double price, String category, double qty, double net) {
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.qty = qty;
        this.net = net;
    }

    public Items() {

    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getNet() {
        return net;
    }

    public void setNet(double net) {
        this.net = net;
    }
}
