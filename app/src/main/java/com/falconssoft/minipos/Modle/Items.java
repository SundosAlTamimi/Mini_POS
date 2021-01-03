package com.falconssoft.minipos.Modle;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Items {
    private String itemNo;
    private String barcode;
    private String itemName;
    private String itemNameE;
    private double price;
    private String category;
    private String subCategory;
    private String taxValue;
    private String description;
    private int pic;
    private double qty;
    private double net;
    private double netBeforeTax;
    private double netWithTax;
    private String companyYear;
    private String companyNo;
    private String voucherNo;
    private int voucherKind;
    private String voucherDate;
    private String posNo;
    private  String voucherStatus;
    private String isActive;
    private String serial;


    public Items(String itemNo, String itemName, double price, String category, double qty, double net, double netWithTax, String taxValue, String voucherNo, int voucherKind, String voucherDate, String companyNo, String posNo) { // orders
        this.itemNo = itemNo;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.qty = qty;
        this.net = net;
        this.taxValue = taxValue;
        this.voucherNo = voucherNo;
        this.voucherKind = voucherKind;
        this.voucherDate = voucherDate;
        this.companyNo = companyNo;
        this.posNo = posNo;
        this.netWithTax = netWithTax;
    }

    public Items(String itemNo, String barcode,  String itemName, String itemNameE, double price, String category, String subCategory, String taxValue, String description, int pic, String companyNo,String isActive) { // item card
        this.itemNo = itemNo;
        this.barcode = barcode;
        this.itemName = itemName;
        this.itemNameE = itemNameE;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.taxValue = taxValue;
        this.description = description;
        this.pic = pic;
        this.companyNo = companyNo;
        this.isActive =isActive;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemNameE() {
        return itemNameE;
    }

    public void setItemNameE(String itemNameE) {
        this.itemNameE = itemNameE;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getTaxValue() {
        return taxValue;
    }

    public void setTaxValue(String taxValue) {
        this.taxValue = taxValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyYear() {
        return companyYear;
    }

    public void setCompanyYear(String companyYear) {
        this.companyYear = companyYear;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public double getNetWithTax() {
        return netWithTax;
    }

    public void setNetWithTax(double netWithTax) {
        this.netWithTax = netWithTax;
    }

    public String getVoucherStatus() {
        return voucherStatus;
    }

    public void setVoucherStatus(String voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public int getVoucherKind() {
        return voucherKind;
    }

    public void setVoucherKind(int voucherKind) {
        this.voucherKind = voucherKind;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPosNo() {
        return posNo;
    }

    public void setPosNo(String posNo) {
        this.posNo = posNo;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public double getNetBeforeTax() {
        return netBeforeTax;
    }

    public void setNetBeforeTax(double netBeforeTax) {
        this.netBeforeTax = netBeforeTax;
    }

    public JSONObject getJSONObject() { // item card
        JSONObject obj = new JSONObject();

        try {
            obj.put("CONO","'"+ companyNo + "'");
            obj.put("COYEAR","2020" );
            obj.put("ITEMNO","'"+ itemNo + "'");
            obj.put("ITEM_BARCODE","'"+ barcode + "'");
            obj.put("ITEM_NAME_A","'"+ itemName + "'");
            obj.put("ITEM_NAME_E","'"+ itemNameE + "'");
            obj.put("CATEGORY_ID","'"+ category + "'");
            obj.put("SUB_CATEGORY_ID","'"+ subCategory + "'");
            obj.put("TAX_PERC",taxValue );
            obj.put("SALE_PRICE",price );
            obj.put("ITEMDESC","'"+ description + "'");
            obj.put("ITEM_PIC",pic );
            obj.put("INDATE","'"+ "1/1/2020" + "'");
            obj.put("IS_ACTIVE","'"+ isActive + "'");
            obj.put("SERIAL","'"+ serial + "'");

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject2() { // master
        JSONObject obj = new JSONObject();

        try {
            obj.put("CONO","'"+ companyNo + "'" );
            obj.put("COYEAR","2020" );
            obj.put("USERNO","'"+ "1000" + "'");
            obj.put("POSNO","'"+ posNo + "'");
            obj.put("VHF_NO","'"+ voucherNo + "'");
            obj.put("VHF_DATE","'"+ voucherDate + "'");
            obj.put("VHF_KIND","'"+ voucherKind + "'");
            obj.put("INDATE","'"+ voucherDate + "'");
            obj.put("VOUCHER_STATUS","'"+ voucherStatus + "'");

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject3() { // details
        JSONObject obj = new JSONObject();

        try {
            obj.put("CONO","'"+ companyNo + "'");
            obj.put("COYEAR","2020" );
            obj.put("POSNO","'"+ companyNo + "'");
            obj.put("VHF_NO","'"+ voucherNo + "'");
            obj.put("VHF_DATE","'"+ voucherDate + "'");
            obj.put("VHF_KIND","'"+ voucherKind + "'");
            obj.put("ITEMNO","'"+ itemNo + "'");
            obj.put("QTY","'"+ qty + "'");
            obj.put("PRICE","'"+ price + "'");
            obj.put("TOTAL","'"+ net + "'");
            obj.put("TOTAL_WITH_TAX","'"+ netWithTax + "'");
            obj.put("TAX",2);
            obj.put("INDATE","'"+ voucherDate + "'");
            obj.put("VOUCHER_STATUS","'"+ voucherStatus + "'");
        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

    public JSONObject getJSONObject4() { // master
        JSONObject obj = new JSONObject();

        try {
            obj.put("CONO","'"+ companyNo + "'" );
            obj.put("COYEAR","'"+ companyYear + "'"  );
            obj.put("ITEMNO","'"+ itemNo + "'" );
            obj.put("SERIAL","'"+ serial + "'" );

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }

}
