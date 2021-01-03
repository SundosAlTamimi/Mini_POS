package com.falconssoft.minipos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.falconssoft.minipos.Modle.Items;
import com.falconssoft.minipos.Modle.Settings;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.falconssoft.minipos.ItemCard.isShow;


public class GlobelFunction {
    Context context;
    DatabaseHandler databaseHandler;
    public  static String ipAddress;
    public  static String CoNo;
    public  static String CoYear;
    public  static int QtyControl;
    public  static int PriceControl;


    public static Items itemRefresh=new Items();
    public static List<Items>searchItemList=new ArrayList<>();

    public GlobelFunction() {


    }

    public String GlobelFunctionSetting(DatabaseHandler databaseHandler) {

        this.databaseHandler=databaseHandler;
        Settings settingModle=databaseHandler.getSettings();
        ipAddress=settingModle.getIpAddress();


        if(!TextUtils.isEmpty(ipAddress)){
            Log.e("ipAddress","globlFunction"+ipAddress);
            ipAddress=settingModle.getIpAddress();
            CoNo="1";
            CoYear ="2020";
            PriceControl=settingModle.getControlPrice();
            QtyControl=settingModle.getControlQty();
            return  ipAddress;

        }else{
            Log.e("ipAddress","noSetting"+ipAddress);
            ipAddress="noSetting";
            CoNo="1";
            CoYear ="2020";
            PriceControl=0;
            QtyControl=0;
            return "noSetting";

        }

    }


    @SuppressLint("WrongConstant")
    void  fillSearchCustomerPhoneNo(RecyclerView spinnerPhoneNo , String itemNo  ,Context context){
        List<Items> itemListTemp = new ArrayList<>();
        itemListTemp.clear();

        for(int i=0;i<searchItemList.size();i++){
            if(searchItemList.get(i).getItemNo().contains(itemNo)||searchItemList.get(i).getItemName().contains(itemNo)
                    ||searchItemList.get(i).getItemNameE().contains(itemNo)){

                Items phoneNo=searchItemList.get(i);
                itemListTemp.add(phoneNo);

            }

        }

        Log.e("SearchPoint",""+itemListTemp.size());

        if(itemListTemp.size()!=0) {
            LinearLayoutManager llm = new LinearLayoutManager(context);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            searchCustomerAdapter searchCustomerAdapt = new searchCustomerAdapter((ItemCard) context, itemListTemp);
            spinnerPhoneNo.setLayoutManager(llm);
            spinnerPhoneNo.setAdapter(searchCustomerAdapt);
            isShow=0;
        }else {
            spinnerPhoneNo.setVisibility(View.GONE);
        }
    }



    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    public  String DateInToday(){

        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String today = df.format(currentTimeAndDate);
        return convertToEnglish(today);
    }


    public  String DecimalFormat(String value){
        String returnVale="";
        double valueD=Double.parseDouble(value);
        DecimalFormat threeDForm;
        threeDForm = new DecimalFormat("#.###");
        returnVale=convertToEnglish(threeDForm.format(valueD));

        return  returnVale;
    }




}
