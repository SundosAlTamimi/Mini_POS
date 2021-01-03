package com.falconssoft.minipos;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.minipos.Modle.Items;
import com.falconssoft.minipos.Modle.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.falconssoft.minipos.GlobelFunction.ipAddress;

public class SalesReport extends AppCompatActivity {

    private LinearLayout  alpha;
    private EditText from, to;
//    private Button preview;
    DatabaseHandler DHandler;
    private Calendar myCalendar;
    private TableLayout tableLayout;
    private String fromDate, toDate;

    ArrayList<Items> items;
    TextView back,preview;
GlobelFunction globelFunction;
EditText searchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sale_report_new);

        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        globelFunction=new GlobelFunction();

        items = new ArrayList<>();
        DHandler = new DatabaseHandler(this);
//        if (DHandler.getSettings() != null)
//            setThemeNo(DHandler.getSettings().getThemeNo());

        myCalendar = Calendar.getInstance();

        from.setText(globelFunction.DateInToday());
        to.setText(globelFunction.DateInToday());
        fromDate=globelFunction.DateInToday();
        toDate=globelFunction.DateInToday();
        globelFunction.GlobelFunctionSetting(DHandler);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SalesReport.this, openDatePickerDialog(0), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SalesReport.this, openDatePickerDialog(1), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                searchByNoNameItem();
                new JSONTask().execute();

            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchByNoNameItem();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog(final int flag) {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if (flag == 0) {
                    from.setText(sdf.format(myCalendar.getTime()));
//                    myCalendar.add(Calendar.DATE, -1);
//                    Calendar calendar = myCalendar;
//                    calendar.add(Calendar.DATE, -1);
                    fromDate = sdf.format(myCalendar.getTime());
                } else {
                    to.setText(sdf.format(myCalendar.getTime()));
//                    myCalendar.add(Calendar.DATE, 1);
//                    Calendar calendar2 = myCalendar;
//                    calendar2.add(Calendar.DATE, +1);
                    toDate = sdf.format(myCalendar.getTime());
                }

                myCalendar = Calendar.getInstance();
            }

        };
        return date;
    }

//    void setThemeNo(int themeNo) {
//
//        switch (themeNo) {
//            case 2:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
//                alpha.setBackgroundColor(getResources().getColor(R.color.rosy4));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
//                break;
//
//            case 3:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
//                alpha.setBackgroundColor(getResources().getColor(R.color.green3));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
//                break;
//
//            case 4:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
//                alpha.setBackgroundColor(getResources().getColor(R.color.gray3));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
//                break;
//
//            case 5:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
//                alpha.setBackgroundColor(getResources().getColor(R.color.red3));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
//                break;
//
//            case 6:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
//                alpha.setBackgroundColor(getResources().getColor(R.color.pronz3));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
//                break;
//
//            case 7:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
//                alpha.setBackgroundColor(getResources().getColor(R.color.sky3));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
//                break;
//
//            case 8:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
//                alpha.setBackgroundColor(getResources().getColor(R.color.blue4));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
//                break;
//
//            case 9:
//                back.setBackgroundColor(getResources().getColor(R.color.cream));
//                alpha.setBackgroundColor(getResources().getColor(R.color.beetle_green));
//                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
//                break;
//
//        }
//    }



    void searchByNoNameItem(){
        String itemNoName=searchEditText.getText().toString();

        List<Items>temp=new ArrayList<>();
        if(!TextUtils.isEmpty(itemNoName)) {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getItemNo().contains(itemNoName)/*||items.get(i).getItemName().contains(itemNoName)*/) {
                    Items item = new Items();
                    item = items.get(i);
                    temp.add(item);
                }
            }
            fillTable(temp);

        }else {
            fillTable(items);
        }


    }

    private class JSONTask extends AsyncTask<String, String, String> {

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
                URL url = new URL("http://"+ipAddress+"/miniPOS/import.php?FLAG=4&FROM_DATE='" + fromDate + "'&TO_DATE='" + toDate + "'");

//                Log.e("dDate", "********"+ dDate);

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


                try {
                    items.clear();
                    JSONArray parentArraySales = parentObject.getJSONArray("SALES_REPORT");

                    for (int i = 0; i < parentArraySales.length(); i++) {
                        JSONObject innerObject = parentArraySales.getJSONObject(i);

                        Items item = new Items();
                        item.setItemNo(innerObject.getString("ITEMNO"));
                        item.setQty(innerObject.getDouble("QTY"));
                        item.setNet(innerObject.getDouble("NET_TOTAL"));
                        item.setNetWithTax(innerObject.getDouble("NET_TOTAL_WITH_TAX"));

                        items.add(item);
                    }
                } catch (JSONException e) {
                    Log.e("Import SALES_REPORT", e.getMessage().toString());
                }


            } catch (MalformedURLException e) {
                Log.e("SALES_REPORT", "********ex1");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("SALES_REPORT", e.getMessage().toString());
                e.printStackTrace();

            } catch (JSONException e) {
                Log.e("SALES_REPORT", "********ex3  " + e.toString());
                e.printStackTrace();
            } finally {
                Log.e("SALES_REPORT", "********finally");
                if (connection != null) {
                    Log.e("SALES_REPORT", "********ex4");
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

                searchByNoNameItem();
            } else {
                searchByNoNameItem();
                Toast.makeText(SalesReport.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    void fillTable(List<Items>items) {

        tableLayout.removeAllViews();
        for (int k = 0; k < items.size(); k++) {

//                TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableRow tableRow = new TableRow(SalesReport.this);
//                tableRow.setLayoutParams(tableParams);

            for (int i = 0; i < 4; i++) {

                TextView textView = new TextView(SalesReport.this);
                TableRow.LayoutParams textViewParam = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                textViewParam.setMargins(0, 5, 0, 3);
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(SalesReport.this.getResources().getColor(R.color.black));
                textView.setLayoutParams(textViewParam);
                switch (i) {
                    case 0:
                        textView.setText(items.get(k).getItemNo());
                        break;
                    case 1:
                        textView.setText("" + items.get(k).getQty());
                        break;
                    case 2:
                        textView.setText("" + items.get(k).getNet());
                        break;
                    case 3:
                        textView.setText("" + items.get(k).getNetWithTax());
                        break;
                }

                if(k%2==0){
                    tableRow.setBackgroundColor(SalesReport.this.getResources().getColor(R.color.greenL));


                }

                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }

    }


    void init() {
        back = findViewById(R.id.back);
        alpha = findViewById(R.id.alpha_linear);
        preview = findViewById(R.id.preview);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        tableLayout = findViewById(R.id.table);
        searchEditText=findViewById(R.id.searchEditText);
    }
}
