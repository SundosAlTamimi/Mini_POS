package com.falconssoft.minipos;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.minipos.Modle.Settings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SalesReport extends AppCompatActivity {

    private LinearLayout back, alpha;
    private EditText from, to;
    private Button preview;
    DatabaseHandler DHandler;
    private Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sale_report);

        init();

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

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
                if (flag == 0)
                    from.setText(sdf.format(myCalendar.getTime()));
                else
                    to.setText(sdf.format(myCalendar.getTime()));
            }

        };
        return date;
    }

    void setThemeNo(int themeNo) {

        switch (themeNo) {
            case 2:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
                alpha.setBackgroundColor(getResources().getColor(R.color.rosy4));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                break;

            case 3:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
                alpha.setBackgroundColor(getResources().getColor(R.color.green3));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                break;

            case 4:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
                alpha.setBackgroundColor(getResources().getColor(R.color.gray3));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                break;

            case 5:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                alpha.setBackgroundColor(getResources().getColor(R.color.red3));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                break;

            case 6:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
                alpha.setBackgroundColor(getResources().getColor(R.color.pronz3));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                break;

            case 7:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
                alpha.setBackgroundColor(getResources().getColor(R.color.sky3));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                break;

            case 8:
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
                alpha.setBackgroundColor(getResources().getColor(R.color.blue4));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                break;

            case 9:
                back.setBackgroundColor(getResources().getColor(R.color.cream));
                alpha.setBackgroundColor(getResources().getColor(R.color.beetle_green));
                preview.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                break;

        }
    }

    void init() {
        back = findViewById(R.id.back);
        alpha = findViewById(R.id.alpha_linear);
        preview = findViewById(R.id.preview);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
    }
}
