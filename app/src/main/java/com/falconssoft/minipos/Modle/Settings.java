package com.falconssoft.minipos.Modle;

public class Settings {
    private String companyName;
    private String ipAddress;
    private int themeNo;
    private int controlPrice = 0;
    private int controlQty = 0;

    public Settings(String companyName, String ipAddress, int themeNo, int controlPrice, int controlQty) {
        this.companyName = companyName;
        this.ipAddress = ipAddress;
        this.themeNo = themeNo;
        this.controlPrice = controlPrice;
        this.controlQty = controlQty;
    }

    public Settings() {

    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getThemeNo() {
        return themeNo;
    }

    public void setThemeNo(int themeNo) {
        this.themeNo = themeNo;
    }

    public int getControlPrice() {
        return controlPrice;
    }

    public void setControlPrice(int controlPrice) {
        this.controlPrice = controlPrice;
    }

    public int getControlQty() {
        return controlQty;
    }

    public void setControlQty(int controlQty) {
        this.controlQty = controlQty;
    }
}
