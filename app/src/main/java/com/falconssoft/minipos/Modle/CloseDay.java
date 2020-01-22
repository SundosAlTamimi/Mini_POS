package com.falconssoft.minipos.Modle;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class CloseDay {
    private String  date;
    private String userNo;
    private double totalCash;
    private String companyNo;

    public CloseDay(String date, String userNo, double totalCash, String companyNo) {
        this.date = date;
        this.userNo = userNo;
        this.totalCash = totalCash;
        this.companyNo = companyNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(double totalCash) {
        this.totalCash = totalCash;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("CONO","'"+ companyNo + "'" );
            obj.put("COYEAR","2020" );
            obj.put("VHF_DATE","'"+ date + "'");
            obj.put("USERNO","'"+ userNo + "'");
            obj.put("TOTAL_CASH",totalCash );

        } catch (JSONException e) {
            Log.e("Tag" , "JSONException");
        }
        return obj;
    }
}
