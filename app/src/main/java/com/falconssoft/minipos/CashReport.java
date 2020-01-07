package com.falconssoft.minipos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CashReport extends AppCompatActivity {

    private LinearLayout linear1, linear2, back, alpha;
    private EditText dateField, salesNoTax, tax, salesAfterTax;

    DatabaseHandler DHandler;
    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_report);

        init();

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings().getIpAddress() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

        startAnimation();

        myCalendar = Calendar.getInstance();

        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CashReport.this, openDatePickerDialog(), myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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

    void setThemeNo(int themeNo) {

        switch (themeNo) {
            case 2:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
                alpha.setBackgroundColor(getResources().getColor(R.color.rosy4));
                break;

            case 3:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
                alpha.setBackgroundColor(getResources().getColor(R.color.green3));
                break;

            case 4:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
                alpha.setBackgroundColor(getResources().getColor(R.color.gray3));
                break;

            case 5:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                alpha.setBackgroundColor(getResources().getColor(R.color.red3));
                break;

            case 6:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
                alpha.setBackgroundColor(getResources().getColor(R.color.pronz3));
                break;

            case 7:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
                alpha.setBackgroundColor(getResources().getColor(R.color.sky3));
                break;

            case 8:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
                alpha.setBackgroundColor(getResources().getColor(R.color.blue4));
                break;

            case 9:
                back.setBackgroundColor(getResources().getColor(R.color.cream));
                alpha.setBackgroundColor(getResources().getColor(R.color.beetle_green));
                break;

        }
    }

    void init(){
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        back = findViewById(R.id.back);
        alpha = findViewById(R.id.alpha_linear);
        dateField = findViewById(R.id.date);
        salesNoTax = findViewById(R.id.sales_no_tax);
        tax = findViewById(R.id.tax);
        salesAfterTax = findViewById(R.id.sales_after_tax);
    }
}
