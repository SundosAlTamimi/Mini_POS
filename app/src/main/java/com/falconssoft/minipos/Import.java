package com.falconssoft.minipos;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.falconssoft.minipos.Modle.Categories;
import com.falconssoft.minipos.Modle.Items;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static com.falconssoft.minipos.GlobelFunction.CoNo;
import static com.falconssoft.minipos.GlobelFunction.CoYear;
import static com.falconssoft.minipos.GlobelFunction.ipAddress;
import static com.falconssoft.minipos.GlobelFunction.itemRefresh;
import static com.falconssoft.minipos.GlobelFunction.searchItemList;
import static com.falconssoft.minipos.MainActivity.holdVoucherList;

public class Import {

    private JSONObject jsonObject;
    private String flag;
    Context context;
    GlobelFunction globelFunction;
    String ip;

    public Import(JSONObject jsonObject, String flag, Context context) {
        this.jsonObject = jsonObject;
        this.flag = flag;
        globelFunction =new GlobelFunction();

        this.context=context;

if(!ipAddress.equals("noSetting")) {
    if (flag.equals("GetItemInfo"))
        new JSONGetItem().execute();

}else {

}
    }

    private class JSONGetItem extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String JsonResponse = null;
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost();
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));//import 192.168.2.17:8088

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                nameValuePairs.add(new BasicNameValuePair("flag", "6"));
                nameValuePairs.add(new BasicNameValuePair("ITEM_NO", jsonObject.toString()));

                request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse response = client.execute(request);

                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                JsonResponse = sb.toString();
                Log.e("tag", "" + JsonResponse);
                Log.e("importInfo", "****Success"+JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("importInfo", "****Success"+s.toString());
            if (s != null) {
                if (s.contains("ITEMNO")) {


                    try {
                        JSONObject parentObject = new JSONObject(s);

                        JSONArray jsonArray=parentObject.getJSONArray("ITEM_INFO");
//                        for(int i=0;i<jsonArray.length();i++) {
                            JSONObject innerObject = jsonArray.getJSONObject(0);

                            Items item = new Items();
                            item.setItemNo(innerObject.getString("ITEMNO"));
                            item.setBarcode(innerObject.getString("ITEM_BARCODE"));
                            item.setItemName(innerObject.getString("ITEM_NAME_A"));
                            item.setItemNameE(innerObject.getString("ITEM_NAME_E"));
                            item.setCategory(innerObject.getString("CATEGORY_ID"));
                            item.setSubCategory(innerObject.getString("SUB_CATEGORY_ID"));
                            item.setTaxValue(innerObject.getString("TAX_PERC"));
                            item.setPrice(innerObject.getDouble("SALE_PRICE"));
                            item.setDescription(innerObject.getString("ITEMDESC"));
                            item.setPic(innerObject.getInt("ITEM_PIC"));
                            item.setIsActive(innerObject.getString("IS_ACTIVE"));

                            itemRefresh=item;

                        ItemCard itemCard=new ItemCard();
                        itemCard.fillEditeText(itemRefresh);
//                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("importInfo", "****Success"+s.toString());
//                    Toast.makeText(context, "Save Item Card Success", Toast.LENGTH_SHORT).show();
                } else {
                    ItemCard itemCard=new ItemCard();
                    itemCard.clearEditText();
//                    Toast.makeText(context, "Save Item Card Fail", Toast.LENGTH_SHORT).show();

                    Log.e("tag", "****Failed to export data");
                }
            } else {
                ItemCard itemCard=new ItemCard();
                itemCard.clearEditText();
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }


    private class JSONHoldList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            URLConnection connection = null;
            BufferedReader reader = null;
            String finalJson = null;

            try {
                URL url = new URL("http://"+ipAddress+"/miniPOS/import.php?FLAG=6&CONO="+CoNo+"&COYEAR="+CoYear);

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                finalJson = sb.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                //                subCatList.clear();

                holdVoucherList.clear();
                List<Items>temp=new ArrayList<>();
                try {
                    JSONArray parentArrayCats = parentObject.getJSONArray("HOLD_LIST");

                    for (int i = 0; i < parentArrayCats.length(); i++) {
                        JSONObject innerObject = parentArrayCats.getJSONObject(i);

                        Items item = new Items();
                        item.setItemNo(innerObject.getString("ITEMNO"));
                        item.setBarcode(innerObject.getString("ITEM_BARCODE"));
                        item.setItemName(innerObject.getString("ITEM_NAME_A"));
                        item.setItemNameE(innerObject.getString("ITEM_NAME_E"));
                        item.setCategory(innerObject.getString("CATEGORY_ID"));
                        item.setSubCategory(innerObject.getString("SUB_CATEGORY_ID"));
                        item.setTaxValue(innerObject.getString("TAX_PERC"));
                        item.setPrice(innerObject.getDouble("SALE_PRICE"));
                        item.setDescription(innerObject.getString("ITEMDESC"));
                        item.setPic(innerObject.getInt("ITEM_PIC"));
                        item.setIsActive(innerObject.getString("IS_ACTIVE"));

                        temp.add(item);


                    }
                    holdVoucherList.add(temp);
                } catch (JSONException e) {
                    Log.e("Import CATEGORIES", e.getMessage().toString());
                }



            } catch (MalformedURLException e) {
                Log.e("CATEGORIES", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("CATEGORIES", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("CATEGORIES", "********ex3  " + e.toString());
                e.printStackTrace();
            } finally {
                Log.e("CATEGORIES", "********finally");
                if (connection != null) {
                    Log.e("CATEGORIES", "********ex4");
                    // connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return finalJson;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.e("result", "*****************" + result);




            } else {
                Toast.makeText(context, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
