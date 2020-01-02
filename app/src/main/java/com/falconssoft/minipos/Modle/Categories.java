package com.falconssoft.minipos.Modle;

public class Categories {
    private String catNo;
    private String catName;
    private int pic;

    public Categories(String catNo, String catName, int pic) {
        this.catNo = catNo;
        this.catName = catName;
        this.pic = pic;
    }

    public String getCatNo() {
        return catNo;
    }

    public void setCatNo(String catNo) {
        this.catNo = catNo;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }
}
