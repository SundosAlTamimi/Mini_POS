package com.falconssoft.minipos;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.minipos.Modle.Categories;
import com.falconssoft.minipos.Modle.Items;

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
import java.util.Calendar;
import java.util.Locale;

import static com.falconssoft.minipos.GlobelFunction.ipAddress;

public class CashReport extends AppCompatActivity {

    private LinearLayout linear1, linear2, alpha;
    private EditText dateField, salesNoTax, tax, salesAfterTax;
    private String salsNoTx , tx , salsWthTx;
    String dDate;

    DatabaseHandler DHandler;
    private Calendar myCalendar;
    TextView back,preview;
GlobelFunction globelFunction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_report_new);

        init();
        globelFunction=new GlobelFunction();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dDate = dateField.getText().toString();
                new JSONTask().execute();
            }
        });

        DHandler = new DatabaseHandler(this);
//        if (DHandler.getSettings().getIpAddress() != null)
//            setThemeNo(DHandler.getSettings().getThemeNo());

        dateField.setText(globelFunction.DateInToday());
        startAnimation();

        myCalendar = Calendar.getInstance();
        globelFunction.GlobelFunctionSetting(DHandler);

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CashReport.this, openDatePickerDialog(), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        dateField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dDate = s.toString();
                new JSONTask().execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public DatePickerDialog.OnDateSetListener openDatePickerDialog() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                dateField.setText(sdf.format(myCalendar.getTime()));
            }

        };
        return date;
    }

    void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.down_from_top);
        animation.setFillAfter(true);
        linear1.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);
        animation.setFillAfter(true);
        linear2.startAnimation(animation2);

    }

//    void setThemeNo(int themeNo) {
//
//        switch (themeNo) {
//            case 2:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
//                alpha.setBackgroundColor(getResources().getColor(R.color.rosy4));
//                break;
//
//            case 3:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
//                alpha.setBackgroundColor(getResources().getColor(R.color.green3));
//                break;
//
//            case 4:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
//                alpha.setBackgroundColor(getResources().getColor(R.color.gray3));
//                break;
//
//            case 5:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
//                alpha.setBackgroundColor(getResources().getColor(R.color.red3));
//                break;
//
//            case 6:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
//                alpha.setBackgroundColor(getResources().getColor(R.color.pronz3));
//                break;
//
//            case 7:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
//                alpha.setBackgroundColor(getResources().getColor(R.color.sky3));
//                break;
//
//            case 8:
//                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
//                alpha.setBackgroundColor(getResources().getColor(R.color.blue4));
//                break;
//
//            case 9:
//                back.setBackgroundColor(getResources().getColor(R.color.cream));
//                alpha.setBackgroundColor(getResources().getColor(R.color.beetle_green));
//                break;
//
//        }
//    }

    void init() {
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        back = findViewById(R.id.back);
        alpha = findViewById(R.id.alpha_linear);
        dateField = findViewById(R.id.date);
        salesNoTax = findViewById(R.id.sales_no_tax);
        tax = findViewById(R.id.tax);
        salesAfterTax = findViewById(R.id.sales_after_tax);
        preview=findViewById(R.id.preview);
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
                URL url = new URL("http://"+ipAddress+"/miniPOS/import.php?FLAG=3&D_DATE='" + dDate + "'");

                Log.e("dDate", "********"+ dDate);

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

                salsNoTx= "";
                tx= "";
                salsWthTx = "";

                try {
                    JSONArray parentArrayCash = parentObject.getJSONArray("CASH_REPORT");

                    JSONObject innerObject = parentArrayCash.getJSONObject(0);

                    salsNoTx= ""+innerObject.getDouble("NET_TOTAL");
                    tx= ""+innerObject.getDouble("NET_TAX");
                    salsWthTx = ""+innerObject.getDouble("NET_TOTAL_WITH_TAX");

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

                salesNoTax.setText(salsNoTx);
                tax.setText(tx);
                salesAfterTax.setText(salsWthTx);

            } else {
                Toast.makeText(CashReport.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
