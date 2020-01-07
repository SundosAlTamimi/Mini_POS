package com.falconssoft.minipos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemCard extends AppCompatActivity {

    private List<String> taxList = new ArrayList<>();
    private List<String> catList = new ArrayList<>();
    private List<String> subCatList = new ArrayList<>();

    private ArrayAdapter<String> taxAdapter, catAdapter, subCatAdapter;
    private Spinner taxSpinner;
    private Button catSpinner, subCatSpinner;
    private ImageView save, search, back;
    private EditText itemNo, barcode;
    private ImageView addCat, addSubCat, itemPic, catPic;
    private LinearLayout linear1, linear2, catLinear, subCatLinear;
    private RelativeLayout itemCardBack, alpha, relative;
    Button buttonShowDropDown;
    PopupWindow popupWindowDogs;
    DatabaseHandler DHandler;
    int picFlag;
    static Bitmap itemBitmapPic, categoryPic;

    ScaleAnimation scale;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_card);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemCard.this, MainActivity.class);
                startActivity(intent);
            }
        });

        catLinear.setVisibility(View.INVISIBLE);
        subCatLinear.setVisibility(View.INVISIBLE);

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings().getIpAddress() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

        scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .5f, ScaleAnimation.RELATIVE_TO_SELF, .8f);

        barcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    if (barcode.getText().toString().equals(""))
                        barcode.setText(itemNo.getText().toString());
            }
        });

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
//        catAdapter =
//        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        catSpinner.setAdapter(catAdapter);

        subCatList.add("المجموعة 1");
        subCatList.add("المجموعة 2");
        subCatList.add("المجموعة 3");
//        subCatAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCatList);
//        subCatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        subCatSpinner.setAdapter(subCatAdapter);
//        buttonShowDropDown=catSpinner;

        if (catList.size() != 0) {
            catSpinner.setText("" + catList.get(0));
        }
        catSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonShowDropDown = catSpinner;
                popupWindowDogs = popupWindowDogs(catList, buttonShowDropDown, "حذف المجموعه");
                popupWindowDogs.showAsDropDown(v, 0, 0);
            }
        });


        if (subCatList.size() != 0) {
            subCatSpinner.setText("" + subCatList.get(0));
        }
        subCatSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonShowDropDown = subCatSpinner;
                popupWindowDogs = popupWindowDogs(subCatList, buttonShowDropDown, "حذف المجموعه الفرعيه ");
                popupWindowDogs.showAsDropDown(v, 0, 0);
            }
        });

        addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
                    relative.setBackground(getResources().getDrawable(R.drawable.focused));
                    catLinear.setVisibility(View.INVISIBLE);
                    subCatLinear.setVisibility((View.INVISIBLE));
                    flag = 0;
                } else {
                    relative.setBackground(getResources().getDrawable(R.drawable.shape2));
                    subCatLinear.setVisibility((View.INVISIBLE));
                    catLinear.setVisibility(View.VISIBLE);
                    flag = 1;
                }

            }
        });

        addSubCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 1) {
//                    slideRight(catLinear);
                    relative.setBackground(getResources().getDrawable(R.drawable.focused));
                    subCatLinear.setVisibility((View.INVISIBLE));
                    catLinear.setVisibility(View.INVISIBLE);
                    flag = 0;
                } else {
                    relative.setBackground(getResources().getDrawable(R.drawable.shape2));
                    catLinear.setVisibility(View.INVISIBLE);
                    subCatLinear.setVisibility((View.VISIBLE));
                    flag = 1;
                }
            }
        });

        itemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
                picFlag = 0;
            }
        });

        catPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
                picFlag = 1;
            }
        });

        startAnimation();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image

                if (picFlag == 0) {
                    itemPic.setBackgroundDrawable(null);
                    itemBitmapPic = extras.getParcelable("data");
                    itemPic.setImageDrawable(new BitmapDrawable(getResources(), itemBitmapPic));

                } else {
                    catPic.setBackgroundDrawable(null);
                    categoryPic = extras.getParcelable("data");
                    catPic.setImageDrawable(new BitmapDrawable(getResources(), categoryPic));
                }
            }
        }
    }

    void setThemeNo(int themeNo) {

        switch (themeNo) {
            case 2:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
                alpha.setBackgroundColor(getResources().getColor(R.color.rosy4));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                break;

            case 3:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
                alpha.setBackgroundColor(getResources().getColor(R.color.green3));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                break;

            case 4:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
                alpha.setBackgroundColor(getResources().getColor(R.color.gray3));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                break;

            case 5:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                alpha.setBackgroundColor(getResources().getColor(R.color.red3));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                break;

            case 6:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
                alpha.setBackgroundColor(getResources().getColor(R.color.pronz3));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                break;

            case 7:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
                alpha.setBackgroundColor(getResources().getColor(R.color.sky3));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                break;

            case 8:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                itemCardBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
                alpha.setBackgroundColor(getResources().getColor(R.color.blue4));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                break;

            case 9:
                catLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
                subCatLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
                itemCardBack.setBackgroundColor(getResources().getColor(R.color.cream));
                alpha.setBackgroundColor(getResources().getColor(R.color.beetle_green));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.cream_dot));
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

    public PopupWindow popupWindowDogs(final List<String> list, final Button buttonShowDropDown, final String title) {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(dogsAdapter(list));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Context mContext = v.getContext();
                ItemCard mainActivity = ((ItemCard) mContext);

                // add some animation when a list item was clicked
                Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                fadeInAnimation.setDuration(10);
                v.startAnimation(fadeInAnimation);

                // dismiss the pop up
                mainActivity.popupWindowDogs.dismiss();

                // get the text and set it as the button text
                String selectedItemText = ((TextView) v).getText().toString();
                buttonShowDropDown.setText(selectedItemText);


            }
        });

        listViewDogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemCard.this);
                builder.setMessage(getResources().getString(R.string.delete_category_message));
                builder.setTitle("" + title);
                builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        if (list.size() != 0) {
                            ItemCard.this.buttonShowDropDown.setText("" + list.get(0));
                        } else {
                            ItemCard.this.buttonShowDropDown.setText("");
                        }
                        dogsAdapter(list).notifyDataSetChanged();
                    }
                });
                builder.show();
                return false;
            }
        });

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(220);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    /*
     * adapter where the list values will be set
     */
    private ArrayAdapter<String> dogsAdapter(final List<String> dogsArray) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.list_content, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
//                String item = getItem(position);
//                String[] itemArr = item.split("::");
//                String text = itemArr[0];
//                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(ItemCard.this);
                listItem.setBackground(getResources().getDrawable(R.drawable.shape6));
                listItem.setGravity(Gravity.CENTER);
                listItem.setText("" + dogsArray.get(position));
                listItem.setTag("" + dogsArray.get(position));
                listItem.setTextSize(18);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(getResources().getColor(R.color.black));

                return listItem;
            }
        };

        return adapter;
    }

    void init(){
        itemCardBack = findViewById(R.id.item_card_back);
        relative = findViewById(R.id.relative);
        alpha = findViewById(R.id.alpha);
        taxSpinner = findViewById(R.id.type_card_tax_percent);
        catSpinner = findViewById(R.id.type_card_group);
        subCatSpinner = findViewById(R.id.type_card_branch_group);
        addCat = findViewById(R.id.add_cat);
        catPic = findViewById(R.id.cat_image);
        addSubCat = findViewById(R.id.add_sub_cat);
        catLinear = findViewById(R.id.catLinear);
        subCatLinear = findViewById(R.id.subCatLinear);
        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        itemPic = findViewById(R.id.item_image);
        itemNo = findViewById(R.id.type_card_material_no);
        barcode = findViewById(R.id.type_card_barcode);
        save = findViewById(R.id.save);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back);
    }

}
