package com.falconssoft.minipos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {

    Button logIn;
    LinearLayout back, logLinear;
    DatabaseHandler DHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        logIn = findViewById(R.id.log_in);
        back = findViewById(R.id.log_back);
        logLinear = findViewById(R.id.log_linear);

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this , MainActivity.class);
                startActivity(intent);
            }
        });

    }

    void setThemeNo(int themeNo) {

        switch (themeNo) {
            case 2:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log2));
                break;

            case 3:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log1));
                break;

            case 4:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log3));
                break;

            case 5:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log4));
                break;

            case 6:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log5));
                break;

            case 7:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log6));
                break;

            case 8:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log8));
                break;

            case 9:
                logIn.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.log7));
                break;



        }
    }
}
