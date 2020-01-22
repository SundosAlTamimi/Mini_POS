package com.falconssoft.minipos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.minipos.Modle.Categories;
import com.falconssoft.minipos.Modle.Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ItemCard extends AppCompatActivity {

    private List<String> taxList = new ArrayList<>();
    private List<String> catList = new ArrayList<>();
    private List<String> subCatList = new ArrayList<>();

    private ArrayAdapter<String> taxAdapter, catAdapter, subCatAdapter;
    private Spinner taxSpinner;
    private Button catSpinner, subCatSpinner;
    private ImageView save, search, back;
    private ImageView addCat, addSubCat, itemPic, catPic;
    private LinearLayout linear1, linear2, catLinear, subCatLinear;
    private RelativeLayout itemCardBack, alpha, relative;
    private EditText itemNo, barcode, itemNameArabic, itemNameEnglish, salePrice, itemDescription, catName, subCatName;
    private TextView catNo, subCatNo;
    private Button saveCat, saveSubCat;
    Button buttonShowDropDown;
    PopupWindow popupWindowDogs;
    DatabaseHandler DHandler;
    int picFlag;
    static Bitmap itemBitmapPic, categoryPic;
    String posNo , companyNo;

    ArabicEncode arabicEncode;
    ScaleAnimation scale;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_card);

        arabicEncode = new ArabicEncode();
        init();

        catLinear.setVisibility(View.INVISIBLE);
        subCatLinear.setVisibility(View.INVISIBLE);

        DHandler = new DatabaseHandler(this);
        if (DHandler.getSettings().getIpAddress() != null)
            setThemeNo(DHandler.getSettings().getThemeNo());

        posNo = DHandler.getSettings().getPosNo();
        companyNo = DHandler.getSettings().getCompanyID();

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

        new JSONTask().execute();

//        catList.add("المجموعة 1");
//        catList.add("المجموعة 2");
//        catList.add("المجموعة 3");
//
//        subCatList.add("المجموعة 1");
//        subCatList.add("المجموعة 2");
//        subCatList.add("المجموعة 3");

//        if (catList.size() != 0)
//            catSpinner.setText("" + catList.get(0));
//
//        if (subCatList.size() != 0)
//            subCatSpinner.setText("" + subCatList.get(0));


        catLinear.setOnClickListener(null);
        subCatLinear.setOnClickListener(null);

        back.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        catSpinner.setOnClickListener(onClickListener);
        subCatSpinner.setOnClickListener(onClickListener);
        relative.setOnClickListener(onClickListener);
        addCat.setOnClickListener(onClickListener);
        addSubCat.setOnClickListener(onClickListener);
        saveCat.setOnClickListener(onClickListener);
        saveSubCat.setOnClickListener(onClickListener);
        itemPic.setOnClickListener(onClickListener);
        catPic.setOnClickListener(onClickListener);

        startAnimation();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.relative:
                    if (flag == 1) {
                        relative.setVisibility(View.GONE);
                        catLinear.setVisibility(View.INVISIBLE);
                        subCatLinear.setVisibility((View.INVISIBLE));
                        flag = 0;
                    }
                    break;
                case R.id.save:
                    if (!TextUtils.isEmpty(itemNo.getText().toString())) {
                        if (!TextUtils.isEmpty(barcode.getText().toString())) {
                            if (!TextUtils.isEmpty(itemNameArabic.getText().toString())) {
                                if (!TextUtils.isEmpty(itemNameEnglish.getText().toString())) {
                                    if (!TextUtils.isEmpty(salePrice.getText().toString())) {
                                        if (!TextUtils.isEmpty(catSpinner.getText().toString())) {
                                            if (!TextUtils.isEmpty(subCatSpinner.getText().toString())) {

                                                Items item = new Items(
                                                        itemNo.getText().toString(),
                                                        barcode.getText().toString(),
                                                        itemNameArabic.getText().toString(),
                                                        itemNameEnglish.getText().toString(),
                                                        Double.parseDouble(salePrice.getText().toString()),
                                                        catSpinner.getText().toString(),
                                                        subCatSpinner.getText().toString(),
                                                        taxSpinner.getSelectedItem().toString(),
                                                        itemDescription.getText().toString(),
                                                        1,
                                                        companyNo);

                                                clear();
                                                itemNo.setText("" + (Integer.parseInt(item.getItemNo())+1));

                                                new Export(item.getJSONObject(), "Add_Item");

                                            } else
                                                subCatSpinner.setError("Required!");
                                        } else
                                            catSpinner.setError("Required!");
                                    } else
                                        salePrice.setError("Required!");
                                } else
                                    itemNameEnglish.setError("Required!");
                            } else
                                itemNameArabic.setError("Required!");
                        } else
                            barcode.setError("Required!");
                    } else
                        itemNo.setError("Required!");

                    break;
                case R.id.back:
                    Intent intnt = new Intent(ItemCard.this, MainActivity.class);
                    startActivity(intnt);
                    break;
                case R.id.search:

                    break;
                case R.id.type_card_group:
                    buttonShowDropDown = catSpinner;
                    popupWindowDogs = popupWindowDogs(catList, "حذف المجموعه", 0);
                    popupWindowDogs.showAsDropDown(v, 0, 0);
                    break;
                case R.id.type_card_branch_group:
                    buttonShowDropDown = subCatSpinner;
                    popupWindowDogs = popupWindowDogs(subCatList, "حذف المجموعه الفرعيه ", 1);
                    popupWindowDogs.showAsDropDown(v, 0, 0);
                    break;
                case R.id.item_pic:

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

                    break;
                case R.id.cat_image:

                    Intent intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent2.setType("image/*");
                    intent2.putExtra("crop", "true");
                    intent2.putExtra("scale", true);
                    intent2.putExtra("outputX", 256);
                    intent2.putExtra("outputY", 256);
                    intent2.putExtra("aspectX", 1);
                    intent2.putExtra("aspectY", 1);
                    intent2.putExtra("return-data", true);
                    startActivityForResult(intent2, 1);
                    picFlag = 1;

                    break;
                case R.id.add_cat:
                    if (flag == 1) {
                        relative.setVisibility(View.GONE);
                        catLinear.setVisibility(View.INVISIBLE);
                        subCatLinear.setVisibility((View.INVISIBLE));
                        flag = 0;
                    } else {
                        relative.setVisibility(View.VISIBLE);
                        subCatLinear.setVisibility((View.INVISIBLE));
                        catLinear.setVisibility(View.VISIBLE);
                        flag = 1;
                    }
                    break;
                case R.id.add_sub_cat:
                    if (flag == 1) {
                        relative.setVisibility(View.GONE);
                        subCatLinear.setVisibility((View.INVISIBLE));
                        catLinear.setVisibility(View.INVISIBLE);
                        flag = 0;
                    } else {
                        relative.setVisibility(View.VISIBLE);
                        catLinear.setVisibility(View.INVISIBLE);
                        subCatLinear.setVisibility((View.VISIBLE));
                        flag = 1;
                    }
                    break;
                case R.id.add_cat_button:
                    if (!TextUtils.isEmpty(catName.getText().toString())) {

                        Categories category = new Categories(catNo.getText().toString(),
                                catName.getText().toString(),
                                1,
                                0,
                                posNo,
                                companyNo);

                        new Export(category.getJSONObject(), "Add_Category");
                        catList.add(catName.getText().toString());

                        catNo.setText(""+(Integer.parseInt(catNo.getText().toString())+1));
                        catName.setText("");
                        catPic.setBackgroundDrawable(null);

                    } else
                        catName.setError("Required!");
                    break;
                case R.id.add_subcat_button:
                    if (!TextUtils.isEmpty(subCatName.getText().toString())) {

                        Categories category = new Categories(subCatNo.getText().toString(),
                                subCatName.getText().toString(),
                                1,
                                1,
                                posNo,
                                companyNo);

                        new Export(category.getJSONObject(), "Add_Category");
                        subCatList.add(subCatName.getText().toString());

                        subCatNo.setText(""+(Integer.parseInt(subCatNo.getText().toString())+1));
                        subCatName.setText("");

                    } else
                        subCatName.setError("Required!");
                    break;
            }

        }
    };

    void clear() {

        taxSpinner.setSelection(0);
        catName.setText("");
        subCatName.setText("");
        catNo.setText("");
        subCatNo.setText("");
        itemNo.setText("");
        barcode.setText("");
        itemNameArabic.setText("");
        itemNameEnglish.setText("");
        salePrice.setText("");
        itemDescription.setText("");

        itemPic.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2));

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

    public String bitMapToString(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arr = baos.toByteArray();
            String result = Base64.encodeToString(arr, Base64.DEFAULT);
            return result;
        }

        return "";
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

    public PopupWindow popupWindowDogs(final List<String> list, final String title, final int flag) {

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

                if (flag == 0)
                    catSpinner.setText(selectedItemText);
                else
                    subCatSpinner.setText(selectedItemText);


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

    private ArrayAdapter<String> dogsAdapter(final List<String> dogsArray) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.list_content, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {


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
                URL url = new URL("http://10.0.0.214/miniPOS/import.php?FLAG=0");

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

                catList.clear();
                subCatList.clear();

                try {
                    JSONArray parentArrayCats = parentObject.getJSONArray("CATEGORIES");

                    for (int i = 0; i < parentArrayCats.length(); i++) {
                        JSONObject innerObject = parentArrayCats.getJSONObject(i);

                        if (innerObject.getInt("DESC_TYPE") == 0)
                            catList.add(ReturnArabic(innerObject.getString("DESC_NAME")));
                        else
                            subCatList.add(ReturnArabic(innerObject.getString("DESC_NAME")));

                    }
                } catch (JSONException e) {
                    Log.e("Import CATEGORIES", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayMaxes = parentObject.getJSONArray("MAX_SERIAL_GROUP");

                    for (int i = 0; i < parentArrayMaxes.length(); i++) {
                        JSONObject innerObject = parentArrayMaxes.getJSONObject(i);

                        if (innerObject.getInt("DESC_TYPE") == 0)
                            catNo.setText("" + (innerObject.getInt("MAX_SERIAL") +1));
                        else
                            subCatNo.setText("" + (innerObject.getInt("MAX_SERIAL") +1));

                    }


                } catch (JSONException e) {
                    Log.e("Import CATEGORIES", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayMaxes = parentObject.getJSONArray("MAX_SERIAL_ITEM");

                        JSONObject innerObject = parentArrayMaxes.getJSONObject(0);

                        if (!innerObject.getString("MAX_SERIAL").equals("null"))
                            itemNo.setText("" + (innerObject.getInt("MAX_SERIAL") +1));
                        else
                            itemNo.setText("1");



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

                if (catList.size() != 0)
                    catSpinner.setText("" + catList.get(0));

                if (subCatList.size() != 0)
                    subCatSpinner.setText("" + subCatList.get(0));

            } else {
                Toast.makeText(ItemCard.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String ReturnArabic(String PAR)
    {
        String sresult = "";
        char x;
        int[] t = new int[PAR.length()];
        for (int i = 0; i < PAR.length(); i++) {
            x = PAR.charAt(i);
            int z = (int) x;
            t[i] = z;

            if ((x >= 'a' && x <= 'z') || (x >= 'A' && x <= 'Z')) {
                sresult += x;
            } else {
                switch (t[i]) {
                    case 32: {
                        sresult += " ";
                        break;
                    }
                    case 33: {
                        sresult += "!";
                        break;
                    }
                    case 34: {
                        sresult += "\"";
                        break;
                    }
                    case 35: {
                        sresult += "#";
                        break;
                    }
                    case 36: {
                        sresult += "$";
                        break;
                    }
                    case 37: {
                        sresult += "%";
                        break;
                    }
                    case 38: {
                        sresult += "&";
                        break;
                    }
                    case 39: {
                        sresult += "'";
                        break;
                    }
                    case 40: {
                        sresult += "(";
                        break;
                    }
                    case 41: {
                        sresult += ")";
                        break;
                    }
                    case 42: {
                        sresult += "*";
                        break;
                    }
                    case 43: {
                        sresult += "+";
                        break;
                    }
                    case 44: {
                        sresult += ",";
                        break;
                    }
                    case 45: {
                        sresult += "-";
                        break;
                    }
                    case 46: {
                        sresult += ".";
                        break;
                    }
                    case 58: {
                        sresult += ":";
                        break;
                    }
                    case 59: {
                        sresult += ";";
                        break;
                    }
                    case 60: {
                        sresult += "<";
                        break;
                    }
                    case 61: {
                        sresult += "=";
                        break;
                    }
                    case 62: {
                        sresult += ">";
                        break;
                    }
                    case 63: {
                        Log.e("lll" , sresult);
                        sresult += "?";
                        break;
                    }
                    case 64: {
                        sresult += "@";
                        break;
                    }
                    case 91: {
                        sresult += "[";
                        break;
                    }
                    case 92: {
                        sresult += "\\";
                        break;
                    }
                    case 93: {
                        sresult += "]";
                        break;
                    }
                    case 94: {
                        sresult += "^";
                        break;
                    }
                    case 95: {
                        sresult += "_";
                        break;
                    }
                    case 96: {
                        sresult += "`";
                        break;
                    }
                    case 215: {
                        sresult += "×";
                        break;
                    }
                    case 247: {
                        sresult += "÷";
                        break;
                    }
                    case 236: {
                        sresult += "ى";
                        break;
                    }
                    case 240: {
                        sresult += "ً";
                        break;
                    }
                    case 241: {
                        sresult += "ٌ";
                        break;
                    }
                    case 242: {
                        sresult += "ٍ";
                        break;
                    }
                    case 243: {
                        sresult += "َ";
                        break;
                    }
                    case 245: {
                        sresult += "ُ";
                        break;
                    }
                    case 246: {
                        sresult += "ِ";
                        break;
                    }
                    case 248: {
                        sresult += "ّ";
                        break;
                    }
                    case 250: {
                        sresult += "ْ";
                        break;
                    }


                    //Number
                    case 48: {
                        sresult += "0";
                        break;
                    }
                    case 49: {
                        sresult += "1";
                        break;
                    }
                    case 50: {
                        sresult += "2";
                        break;
                    }
                    case 51: {
                        sresult += "3";
                        break;
                    }
                    case 52: {
                        sresult += "4";
                        break;
                    }
                    case 53: {
                        sresult += "5";
                        break;
                    }
                    case 54: {
                        sresult += "6";
                        break;
                    }
                    case 55: {
                        sresult += "7";
                        break;
                    }
                    case 56: {
                        sresult += "8";
                        break;
                    }
                    case 57: {
                        sresult += "9";
                        break;
                    }

                    // Arabic
                    case 199: {
                        sresult += "ا";
                        break;
                    }
                    case 200: {
                        sresult += "ب";
                        break;
                    }
                    case 201: {
                        sresult += "ة";
                        break;
                    }
                    case 202: {
                        sresult += "ت";
                        break;
                    }
                    case 203: {
                        sresult += "ث";
                        break;
                    }
                    case 204: {
                        sresult += "ج";
                        break;
                    }
                    case 205: {
                        sresult += "ح";
                        break;
                    }
                    case 206: {
                        sresult += "خ";
                        break;
                    }
                    case 207: {
                        sresult += "د";
                        break;
                    }
                    case 208: {
                        sresult += "ذ";
                        break;
                    }
                    case 209: {
                        sresult += "ر";
                        break;
                    }
                    case 210: {
                        sresult += "ز";
                        break;
                    }
                    case 211: {
                        sresult += "س";
                        break;
                    }
                    case 212: {
                        sresult += "ش";
                        break;
                    }
                    case 213: {
                        sresult += "ص";
                        break;
                    }
                    case 214: {
                        sresult += "ض";
                        break;
                    }
                    case 216: {
                        sresult += "ط";
                        break;
                    }
                    case 217: {
                        sresult += "ظ";
                        break;
                    }
                    case 218: {
                        sresult += "ع";
                        break;
                    }
                    case 219: {
                        sresult += "غ";
                        break;
                    }
                    case 221: {
                        sresult += "ف";
                        break;
                    }
                    case 222: {
                        sresult += "ق";
                        break;
                    }
                    case 223: {
                        sresult += "ك";
                        break;
                    }
                    case 225: {
                        sresult += "ل";
                        break;
                    }
                    case 227: {
                        sresult += "م";
                        break;
                    }
                    case 228: {
                        sresult += "ن";
                        break;
                    }
                    case 229: {
                        sresult += "ه";
                        break;
                    }
                    case 230: {
                        sresult += "و";
                        break;
                    }
                    case 237: {
                        sresult += "ي";
                        break;
                    }
                    case 193: {
                        sresult += "ء";
                        break;
                    }
                    case 194: {
                        sresult += "آ";
                        break;
                    }
                    case 195: {
                        sresult += "أ";
                        break;
                    }
                    case 196: {
                        sresult += "ؤ";
                        break;
                    }
                    case 197: {
                        sresult += "إ";
                        break;
                    }
                    case 198: {
                        sresult += "ئ";
                        break;
                    }
                }
            }
        }
        return sresult;
    }

    void init() {
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
        catName = findViewById(R.id.cat_name);
        subCatName = findViewById(R.id.sub_cat_name);
        saveCat = findViewById(R.id.add_cat_button);
        saveSubCat = findViewById(R.id.add_subcat_button);
        catNo = findViewById(R.id.cat_no);
        subCatNo = findViewById(R.id.subcat_no);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);

        itemPic = findViewById(R.id.item_image);

        itemNo = findViewById(R.id.type_card_material_no);
        barcode = findViewById(R.id.type_card_barcode);
        itemNameArabic = findViewById(R.id.type_card_arabic_name);
        itemNameEnglish = findViewById(R.id.type_card_english_name);
        salePrice = findViewById(R.id.type_card_sale_price);
        itemDescription = findViewById(R.id.description);

        save = findViewById(R.id.save);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back);
    }

}
