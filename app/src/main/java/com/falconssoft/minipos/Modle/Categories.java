package com.falconssoft.minipos.Modle;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Categories {
    private String catNo;
    private String catName;
    private int catType;
    private int pic;
    private String posNo;
    private String companyNo;

    public Categories(String catNo, String catName, int pic, int catType, String posNo, String companyNo) {
        this.catNo = catNo;
        this.catName = catName;
        this.pic = pic;
        this.catType = catType;
        this.posNo = posNo;
        this.companyNo = companyNo;
    }

    public Categories() {

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

    public int getCatType() {
        return catType;
    }

    public void setCatType(int catType) {
        this.catType = catType;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("CONO","'"+ companyNo + "'" );
            obj.put("COYEAR","2020" );
            obj.put("DESC_TYPE",catType );
            obj.put("DESC_CODE",catNo );
            obj.put("DESC_NAME","'"+ catName + "'");
            obj.put("DESC_PIC",pic );
            obj.put("INDATE","'"+ "2020" + "'");

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
