package com.falconssoft.minipos;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemCard extends AppCompatActivity {

    private List<String> taxList = new ArrayList<>();
    private List<String> catList = new ArrayList<>();
    private List<String> subCatList = new ArrayList<>();

    private ArrayAdapter<String> taxAdapter, catAdapter, subCatAdapter;
    private Spinner taxSpinner, catSpinner, subCatSpinner;
    private ImageView addCat, addSubCat;
    private LinearLayout linear1, linear2, catLinear, subCatLinear;
    private RelativeLayout itemCardBack, alpha;

    DatabaseHandler DHandler;

    ScaleAnimation scale;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_card);

        itemCardBack = findViewById(R.id.item_card_back);
        alpha = findViewById(R.id.alpha);
        taxSpinner = findViewById(R.id.type_card_tax_percent);
        catSpinner = findViewById(R.id.type_card_group);
        subCatSpinner = findViewById(R.id.type_card_branch_group);
        addCat = findViewById(R.id.add_cat);
        addSubCat = findViewById(R.id.add_sub_cat);
        catLinear = findViewById(R.id.catLinear);
        subCatLinear = findViewById(R.id.subCatLinear);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);


        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings().getIpAddress() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

        scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .5f, ScaleAnimation.RELATIVE_TO_SELF, .8f);

        taxList.add("0");
        taxList.add("4");
        taxList.add("8");
        taxList.add("10");
        taxList.add("16");
        taxAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, taxList);
        taxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taxSpinner.setAdapter(taxAdapter);

        catList.add("المجموعة 1");
        catList.add("المجموعة 2");
        catList.add("المجموعة 3");
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catList);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catSpinner.setAdapter(catAdapter);

        subCatList.add("المجموعة 1");
        subCatList.add("المجموعة 2");
        subCatList.add("المجموعة 3");
        subCatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCatList);
        subCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCatSpinner.setAdapter(subCatAdapter);


        addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
                    slideRight(subCatLinear);
                }
                subCatLinear.setVisibility((View.INVISIBLE));
                catLinear.setVisibility(View.VISIBLE);
                slideLeft(catLinear);
                flag = 1;

            }
        });

        addSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
                    slideRight(catLinear);
                }
                catLinear.setVisibility(View.INVISIBLE);
                subCatLinear.setVisibility((View.VISIBLE));
                slideLeft(subCatLinear);
                flag = 1;
            }
        });

        startAnimation();
    }

    void setThemeNo(int themeNo) {

        switch (themeNo) {
            case 2:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
                alpha.setBackgroundColor(getResources().getColor(R.color.rosy4));
                break;

            case 3:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
                alpha.setBackgroundColor(getResources().getColor(R.color.green3));
                break;

            case 4:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
                alpha.setBackgroundColor(getResources().getColor(R.color.gray3));
                break;

            case 5:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                alpha.setBackgroundColor(getResources().getColor(R.color.red3));
                break;

            case 6:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
                alpha.setBackgroundColor(getResources().getColor(R.color.pronz3));
                break;

            case 7:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
                alpha.setBackgroundColor(getResources().getColor(R.color.sky3));
                break;

            case 8:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
                alpha.setBackgroundColor(getResources().getColor(R.color.blue4));
                break;

            case 9:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
                itemCardBack.setBackgroundColor(getResources().getColor(R.color.cream));
                alpha.setBackgroundColor(getResources().getColor(R.color.beetle_green));
                break;


        }
    }

    void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.down_from_top);
        animation.setFillAfter(true);
        linear1.startAnimation(animation);

        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.up_from_bottom);
        animation.setFillAfter(true);
        linear2.startAnimation(animation2);


        scale.setStartOffset(500);
        scale.setDuration(700);
        scale.setInterpolator(new OvershootInterpolator());
        addCat.startAnimation(scale);
        addSubCat.startAnimation(scale);

    }

    public void slideRight(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                view.getWidth(),                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(900);
        animate.setStartTime(100);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                770,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(900);
        animate.setStartTime(100);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
}
