package com.falconssoft.minipos;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CashReport extends AppCompatActivity {

    private LinearLayout linear1, linear2, back, alpha;

    DatabaseHandler DHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cash_report);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        back = findViewById(R.id.back);
        alpha = findViewById(R.id.alpha_linear);

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings().getIpAddress() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

        startAnimation();

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
}
