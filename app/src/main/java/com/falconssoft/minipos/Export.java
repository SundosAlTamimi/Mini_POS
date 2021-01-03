package com.falconssoft.minipos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.falconssoft.minipos.Modle.Items;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.falconssoft.minipos.GlobelFunction.CoNo;
import static com.falconssoft.minipos.GlobelFunction.CoYear;
import static com.falconssoft.minipos.GlobelFunction.ipAddress;

public class Export {

    private JSONObject jsonObject;
    private String flag;
    Context context;
    GlobelFunction globelFunction;
    String ip;

    public Export(JSONObject jsonObject, String flag,Context context) {
        this.jsonObject = jsonObject;
        this.flag = flag;
        globelFunction =new GlobelFunction();

        this.context=context;

if(!ipAddress.equals("noSetting")) {
    if (flag.equals("Add_Item"))
        new JSONTaskAddItem().execute();

    if (flag.equals("Add_Category"))
        new JSONTaskAddCategory().execute();

    if (flag.equals("Close_Day"))
        new JSONTaskCloseDay().execute();

    if (flag.equals("Sales_Master"))
        new JSONTaskSalesMaster().execute();

    if (flag.equals("Sales_Details"))
        new JSONTaskSalesDetails().execute();

    if (flag.equals("Delete_Item"))
        new JSONTaskDeleteItem().execute();
    if (flag.equals("Update_Item"))
        new JSONTaskUpdateItem().execute();

}else {

}
    }

    private class JSONTaskAddItem extends AsyncTask<String, String, String> {

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
                nameValuePairs.add(new BasicNameValuePair("ADD_ITEM", "1"));
                nameValuePairs.add(new BasicNameValuePair("ITEM", jsonObject.toString()));

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

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("ADD ITEM SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("tag", "****Success");
                    Toast.makeText(context, "Save Item Card Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Save Item Card Fail", Toast.LENGTH_SHORT).show();

                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

    private class JSONTaskAddCategory extends AsyncTask<String, String, String> {

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
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("ADD_CATEGORY", "1"));
                nameValuePairs.add(new BasicNameValuePair("CATEGORY", jsonObject.toString()));

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
                Log.e("tagF", "" + JsonResponse);

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("ADD CATEGORY SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("tag", "****Success");
                    Toast.makeText(context, "Save Category Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Save Category Fail", Toast.LENGTH_SHORT).show();

                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Toast.makeText(context, "Save Category Fail B", Toast.LENGTH_SHORT).show();

                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

    private class JSONTaskCloseDay extends AsyncTask<String, String, String> {

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
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                nameValuePairs.add(new BasicNameValuePair("ADD_CATEGORY", "1"));
                nameValuePairs.add(new BasicNameValuePair("CLOSE_DAY", jsonObject.toString()));

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

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("CLOSE DAY SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("tag", "****Success");
                    Toast.makeText(context, "Close Day Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Close Day Fail", Toast.LENGTH_SHORT).show();

                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

    private class JSONTaskSalesMaster extends AsyncTask<String, String, String> {

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
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));

                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("SALES_MASTER", jsonObject.toString()));

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

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("SALES MASTER SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("tag", "****Success");
                    Toast.makeText(context, "Save Success", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Save Fail", Toast.LENGTH_SHORT).show();

                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

    private class JSONTaskSalesDetails extends AsyncTask<String, String, String> {

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
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));

                JSONArray jsonArray = new JSONArray();
                jsonArray = jsonObject.getJSONArray("Sales_Details");

                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("SALES_DETAILS", jsonArray.toString()));

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

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("SALES DETAILS SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("tag", "****Success");
                } else {
                    Log.e("tag", "****Failed to export data");
                }
            } else {
                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }
    private class JSONTaskDeleteItem extends AsyncTask<String, String, String> {

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
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));


                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("DELETE_ITEM", jsonObject.toString()));

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

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("Delete_SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Log.e("tag", "****Delete_SUCCESS");
                    ItemCard itemCard=new ItemCard();
                    itemCard.clearEditText();
                } else if (s.contains("Delete_FAIL")) {
                    Log.e("tag", "****Delete_FAIL");
                } else if (s.contains("Item_Can_Not_Delete")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.Item_Can_Not_Delete));
                    builder.setTitle(context.getResources().getString(R.string.done));
                    builder.setPositiveButton(context.getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    Log.e("tag", "****Item_Can_Not_Delete");
                }
            } else {

                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

    private class JSONTaskUpdateItem extends AsyncTask<String, String, String> {

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
                request.setURI(new URI("http://"+ipAddress+"/miniPOS/export.php"));


                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("UPDATE_ITEM", jsonObject.toString()));

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

                return JsonResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.contains("UPDATE_SUCCESS")) {
//                    databaseHandler.deleteBundle(bundleNumber);
                    Toast.makeText(context, "UPDATE SUCCESS", Toast.LENGTH_SHORT).show();
                    Log.e("tag", "****Delete_SUCCESS");
                    ItemCard itemCard=new ItemCard();
                    itemCard.clearEditText();
                } else if (s.contains("UPDATE_FAIL")) {
                    Log.e("tag", "****Delete_FAIL");
                    Toast.makeText(context, "Delete FAIL", Toast.LENGTH_SHORT).show();

                } else if (s.contains("Item_Can_Not_Delete")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage(context.getResources().getString(R.string.Item_Can_Not_Update));
                    builder.setTitle(context.getResources().getString(R.string.done));
                    builder.setPositiveButton(context.getResources().getString(R.string.done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    Log.e("tag", "****Item_Can_Not_Update");
                }
            } else {

                Log.e("tag", "****Failed to export data Please check internet connection");
            }
        }
    }

}
