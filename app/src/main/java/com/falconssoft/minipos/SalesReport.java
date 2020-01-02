package com.falconssoft.minipos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.minipos.Modle.Settings;

public class SalesReport extends AppCompatActivity {

    private LinearLayout back, alpha;
    private Button preview;
    DatabaseHandler DHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sale_report);

        back = findViewById(R.id.back);
        alpha = findViewById(R.id.alpha_linear);
        preview = findViewById(R.id.preview);

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

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
}
