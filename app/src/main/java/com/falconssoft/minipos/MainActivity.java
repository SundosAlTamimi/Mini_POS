package com.falconssoft.minipos;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.minipos.Modle.Categories;
import com.falconssoft.minipos.Modle.CloseDay;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView cats;
    private CategoryListAdapter catAdapter;
    private ItemListAdapter itemsAdapter;
    public static OrderedListAdapter orderedItemsAdapter;
    private HorizontalListView listView;
    private ListView orderedList;
    public static ListView orderedListB;
    private Button saveSettings, savePay, priceOk, qtyOk;
    private ImageView save, search, clear;
    private static TextView sumNoTax, tax, sumAfterTax;
    private LinearLayout topLinear, rightLinear, back, settingsBack, reportsBack, itemsBack, saveBack, priceBack, qtyBack, functionsBack;
    private com.github.clans.fab.FloatingActionMenu menuLabelsRight;
    private com.github.clans.fab.FloatingActionButton fabAddItem, fabFunctions, fabSettings;
    ItemGridAdapter gridAdapter;
    TextView required;
    String posNo, companyNo;

    TextView closeDay, newDay, totalCashText;

    String totalCash;
    ArrayList<Items> subItems;
    ArrayList<Categories> categories;
    ArrayList<Items> gridItems;
    public static ArrayList<String> itemNo;
    public static ArrayList<String> itemDetail;
    String searchQuery;
    GridView itemsGrid;

    String today, tomorrow;
    String voucherNo;

    int cPrice = 0, cQty = 0;
    static double sum = 0, sumWithTax = 0, taxValue = 0, totalTax = 0, due = 0;


    Dialog settingsDialog, reportsDialog, itemsDialog, saveDialog, priceDialog, functionsDialog;
    static DatabaseHandler DHandler;

    ArrayList<Items> items;
    public static ArrayList<Items> orderedItems;

    public static int theme = 9;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DHandler = new DatabaseHandler(MainActivity.this);
        if(DHandler.getSettings()!=null) {
            posNo = DHandler.getSettings().getPosNo();
            companyNo = DHandler.getSettings().getCompanyID();
        }


        required = new EditText(MainActivity.this);

        init();
        orderedListB=orderedList;
        items = new ArrayList<>();
        subItems = new ArrayList<>();
        categories = new ArrayList<>();
        orderedItems = new ArrayList<>();
        itemNo = new ArrayList<>();
        itemDetail = new ArrayList<>();

        search.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        fabAddItem.setOnClickListener(onClickListener);
        fabFunctions.setOnClickListener(onClickListener);
        fabSettings.setOnClickListener(onClickListener);

        new JSONTask().execute();
//        categories.add(new Categories("1", "بطاطا", R.drawable.botato, 0));
//        categories.add(new Categories("2", "برجر", R.drawable.burgerr, 0));
//        categories.add(new Categories("3", "سمك", R.drawable.fish, 0));
//        categories.add(new Categories("5", "فواكه", R.drawable.watermelon, 0));
//        categories.add(new Categories("4", "وجبات", R.drawable.corden, 0));
//        categories.add(new Categories("6", "سلطات", R.drawable.salad, 0));
//        categories.add(new Categories("7", "ليمون", R.drawable.limon, 0));
//        categories.add(new Categories("8", "فواكه", R.drawable.fruit, 0));


        orderedItemsAdapter = new OrderedListAdapter(MainActivity.this, orderedItems);
        orderedList.setAdapter(orderedItemsAdapter);
        orderedList.setOnItemLongClickListener(onItemLongClickListener);

//        items.add(new Items("1", "بطاطا", 10, R.drawable.botato, "خضار", 1));
//        items.add(new Items("2", "برجر", 10, R.drawable.burgerr, "لحوم", 1));
//        items.add(new Items("3", "سمك", 10, R.drawable.fish, "اسماك", 1));
//        items.add(new Items("4", "بطيخ", 10, R.drawable.watermelon, "فواكه", 1));
//        items.add(new Items("5", "كوردن بلو", 10, R.drawable.corden, "لحوم", 1));
//        items.add(new Items("6", "سلطة", 10, R.drawable.salad, "خضار", 1));
//        items.add(new Items("7", "ليمون", 10, R.drawable.limon, "فواكه", 1));
//        items.add(new Items("8", "فراولة", 10, R.drawable.fruit, "فواكه", 1));
//        items.add(new Items("9", "بطيخ", 10, R.drawable.watermelon, "فواكه", 1));
//        items.add(new Items("10", "برجر", 10, R.drawable.burgerr, "لحوم", 1));
//        items.add(new Items("11", "فراولة", 10, R.drawable.fruit, "فواكه", 1));
//        items.add(new Items("12", "سمك", 10, R.drawable.fish, "اسماك", 1));
//        items.add(new Items("13", "كوردن بلو", 10, R.drawable.corden, "لحوم", 1));
//        items.add(new Items("14", "سلطة", 10, R.drawable.salad, "خضار", 1));
//        items.add(new Items("15", "ليمون", 10, R.drawable.limon, "فواكه", 1));
//        items.add(new Items("16", "فراولة", 10, R.drawable.fruit, "فواكه", 1));
//        items.add(new Items("17", "بطيخ", 10, R.drawable.watermelon, "فواكه", 1));


        startAnimation();

        if (DHandler.getSettings() == null)//.getIpAddress()
            DHandler.addSettings(new Settings("", "", 9, 0, 0, "", 0, ""));
        else {
            setThemeNo(DHandler.getSettings().getThemeNo());
            theme = DHandler.getSettings().getThemeNo();
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.save:
                    if (orderedItems.size() != 0)
                        saveDialog();
                    else
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.no_items_message), Toast.LENGTH_LONG).show();
                    break;

                case R.id.search:
                    gridAdapter = new ItemGridAdapter(MainActivity.this, items);
                    itemsDialog();
                    break;

                case R.id.clear:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getResources().getString(R.string.delete_message));
                    builder.setTitle(getResources().getString(R.string.delete_all));
                    builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            orderedItems.clear();
                            itemNo.clear();
                            orderedItemsAdapter.notifyDataSetChanged();
                            reCalculate(MainActivity.this);

                        }
                    });
                    builder.show();
                    break;

                case R.id.fab_add_item:
                    Intent intent = new Intent(MainActivity.this, ItemCard.class);
                    startActivity(intent);
                    break;

                case R.id.fab_function:
                    functionsDialog();
                    break;

                case R.id.fab_settings:
                    settingDialog();
                    break;
            }

        }
    };

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {

                case R.id.listview:
                    boolean found = false;
                    int position1 = position;
                    if (orderedItems.size() != 0) {//.indexOf
                        Log.e("fffff", "" + itemNo.indexOf(subItems.get(position1).getItemNo()));
                        int i = itemNo.indexOf(subItems.get(position1).getItemNo());
                        if (i != -1) {
                            found = true;
                            double qty = orderedItems.get(i).getQty(), net = orderedItems.get(i).getNet(), netWithTax = orderedItems.get(i).getNetWithTax();
                            orderedItems.get(i).setQty(++qty);
                            orderedItems.get(i).setNet(net + subItems.get(position1).getPrice());
                            orderedItems.get(i).setNetWithTax(netWithTax + calculateTax(subItems.get(position1).getPrice() , subItems.get(position1).getTaxValue()));

                        }
                    }

                    if (!found) {
                        if (!voucherNo.equals("-1")) {
                            itemNo.add(subItems.get(position1).getItemNo());
                            orderedItems.add(new Items(subItems.get(position1).getItemNo()
                                    , subItems.get(position1).getItemName()
                                    , subItems.get(position1).getPrice()
                                    , subItems.get(position1).getCategory()
                                    , 1
                                    , (subItems.get(position1).getPrice())
                                    , calculateTax(subItems.get(position1).getPrice() , subItems.get(position1).getTaxValue())
                                    , subItems.get(position1).getTaxValue()
                                    , voucherNo
                                    , 0
                                    , today
                                    , companyNo
                                    , posNo));
                        } else
                            Toast.makeText(MainActivity.this, "please check internet connection", Toast.LENGTH_LONG).show();
                    }

                    orderedItemsAdapter.notifyDataSetChanged();
                    reCalculate(MainActivity.this);

                    break;

                case R.id.categories:
                    subItems.clear();
                    for (int i = 0; i < items.size(); i++) {
                        if (categories.get(position).getCatName().equals(items.get(i).getCategory()))
                            subItems.add(items.get(i));
                    }
                    itemsAdapter.notifyDataSetChanged();
                    break;

                case R.id.items_grid:
                    boolean found2 = false;
                    int position2 = position;
                    if (orderedItems.size() != 0) {//.indexOf
                        int i = itemNo.indexOf(gridItems.get(position2).getItemNo());
                        if (i != -1) {
                            found2 = true;
                            double qty = orderedItems.get(i).getQty(), net = orderedItems.get(i).getNet(), netWithTax = orderedItems.get(i).getNetWithTax();
                            orderedItems.get(i).setQty(++qty);
                            orderedItems.get(i).setNet(net + orderedItems.get(i).getPrice());
                            orderedItems.get(i).setNetWithTax(netWithTax + calculateTax(orderedItems.get(i).getPrice() , orderedItems.get(i).getTaxValue()));
                        }
                    }

                    if (!found2) {
                        if (!voucherNo.equals("-1")) {
                            itemNo.add(gridItems.get(position2).getItemNo());
                            orderedItems.add(new Items(gridItems.get(position2).getItemNo()
                                    , gridItems.get(position2).getItemName()
                                    , gridItems.get(position2).getPrice()
                                    , gridItems.get(position2).getCategory()
                                    , 1
                                    , gridItems.get(position2).getPrice()
                                    , calculateTax(gridItems.get(position2).getPrice() , gridItems.get(position2).getTaxValue())
                                    , subItems.get(position2).getTaxValue()
                                    , voucherNo
                                    , 0
                                    , today
                                    , companyNo
                                    , posNo));
                        } else
                            Toast.makeText(MainActivity.this, "please check internet connection", Toast.LENGTH_LONG).show();
                    }
                    orderedItemsAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, getResources().getString(R.string.item_added_message), Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            switch (parent.getId()) {
                case R.id.list:
                    String[] options = {
                            getResources().getString(R.string.delete_item),
                            getResources().getString(R.string.edit_price),
                            getResources().getString(R.string.edit_qty)};

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Pick a color");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage(getResources().getString(R.string.delete_item_message));
                                builder.setTitle(getResources().getString(R.string.delete_item));
                                builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        orderedItems.remove(position);
                                        itemNo.remove(position);
                                        orderedItemsAdapter.notifyDataSetChanged();
                                        reCalculate(MainActivity.this);

                                    }
                                });
                                builder.setNeutralButton(getResources().getString(R.string.clos), null);
                                builder.show();

                            } else if (which == 1) {
                                if (DHandler.getSettings().getControlPrice() == 1)
                                    priceDialog(position);
                                else
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cant_edit_price_message), Toast.LENGTH_LONG).show();

                            } else {
                                if (DHandler.getSettings().getControlQty() == 1)
                                    qtyDialog(position);
                                else
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cant_edit_qty_message), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.show();
                    break;
            }
            return false;
        }
    };

    Double calculateTax(Double value , String taxValue){

        Double endValue ;
        Double taxValu = Double.parseDouble(taxValue);

        if (DHandler.getSettings().getTaxCalcKind() == 0) {
            endValue = value + (value * taxValu / 100) ;

        } else {
            endValue = value + ((value * taxValu / 100 ) / (1 + (taxValu / 100)));

        }

        return endValue;
    }

    void reCalculate(Context context) {

        sum = 0;
        sumWithTax = 0;
        totalTax = 0;

        for (int i = 0; i < orderedItems.size(); i++) {

            sum += orderedItems.get(i).getNet();

            double tax = Double.parseDouble(orderedItems.get(i).getTaxValue());
            if (DHandler.getSettings().getTaxCalcKind() == 0) {

                totalTax += orderedItems.get(i).getNet() * tax / 100 ;
                sumWithTax += orderedItems.get(i).getNet() + (orderedItems.get(i).getNet() * tax / 100);
            } else {
                totalTax += (orderedItems.get(i).getNet() * tax / 100 ) / (1 + (tax / 100));
                sumWithTax += orderedItems.get(i).getNet() + ((orderedItems.get(i).getNet() * tax / 100 ) / (1 + (tax / 100)));
            }
        }
//        taxValue = 2;
//        due = sum + (sum * taxValue / 100);

        sumNoTax.setText(context.getResources().getString(R.string.total_no_tax) + " : " + sum);
        tax.setText(context.getResources().getString(R.string.tax_value) + " : " + totalTax);
        sumAfterTax.setText(context.getResources().getString(R.string.net_sales) + " : " + sumWithTax);

    }

    void startAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.down_from_top);
        animation.setFillAfter(true);
        rightLinear.startAnimation(animation);

//        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.INFINITE, .5f, ScaleAnimation.RELATIVE_TO_SELF, .8f);
//        scale.setStartOffset(500);
//        scale.setDuration(700);
//        scale.setInterpolator(new OvershootInterpolator());
//        menuLabelsRight.startAnimation(scale);

        slideLeft(topLinear);
    }

    public void slideLeft(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                770,                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(900);
        animate.setStartTime(200);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void itemsDialog() {
        itemsDialog = new Dialog(MainActivity.this);
        itemsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        itemsDialog.setCancelable(false);
        itemsDialog.setContentView(R.layout.items_dialog);
        itemsDialog.setCanceledOnTouchOutside(true);

        final SearchView searchView = itemsDialog.findViewById(R.id.mSearchTh);
        final Spinner cat = itemsDialog.findViewById(R.id.category);
        itemsBack = itemsDialog.findViewById(R.id.items_back);
        itemsGrid = itemsDialog.findViewById(R.id.items_grid);

        setDialogTheme(theme, itemsBack, new Button(MainActivity.this));

        searchQuery = "";
        gridItems = items;

        gridAdapter = new ItemGridAdapter(MainActivity.this, gridItems);
        itemsGrid.setAdapter(gridAdapter);

        ArrayList<String> catList = new ArrayList<>();
        catList.add("");
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCatType() == 0)
                catList.add(categories.get(i).getCatName());
        }

        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catList);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat.setAdapter(catAdapter);
        cat.setSelection(0);

        cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                filter(items, searchQuery, cat.getSelectedItem().toString(), itemsGrid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                searchQuery = query;
                filter(items, searchQuery, cat.getSelectedItem().toString(), itemsGrid);

                return false;
            }
        });

        itemsGrid.setOnItemClickListener(onItemClickListener);

        itemsDialog.show();
    }

    public void priceDialog(final int position) {
        priceDialog = new Dialog(MainActivity.this);
        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(false);
        priceDialog.setContentView(R.layout.price_dialog);
        priceDialog.setCanceledOnTouchOutside(true);

        priceBack = priceDialog.findViewById(R.id.price_back);
        final EditText price = priceDialog.findViewById(R.id.price);
        priceOk = priceDialog.findViewById(R.id.ok);

        setDialogTheme(theme, priceBack, priceOk);

        price.setText("" + orderedItems.get(position).getPrice());

        priceOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!price.getText().toString().equals("")) {

                    orderedItems.get(position).setPrice(Double.parseDouble(price.getText().toString()));
                    orderedItems.get(position).setNet(orderedItems.get(position).getQty() * Double.parseDouble(price.getText().toString()));
                    orderedItemsAdapter.notifyDataSetChanged();

                    priceDialog.dismiss();
                }
            }
        });
        priceDialog.show();

    }

    void qtyDialog(final int position) {
        final Dialog qtyDialog = new Dialog(MainActivity.this);
        qtyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qtyDialog.setContentView(R.layout.quantity_dialog);

        qtyBack = qtyDialog.findViewById(R.id.qty_back);
        final EditText qty = qtyDialog.findViewById(R.id.quantity_dialog_qty);
        qtyOk = qtyDialog.findViewById(R.id.quantity_dialog_done);

        qty.setText("" + orderedItems.get(position).getQty());
        qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty.setText("");
            }
        });


        setDialogTheme(theme, qtyBack, qtyOk);

        qtyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(qty.getText().toString())) {
                    double quantity = Double.parseDouble(qty.getText().toString());

                    if (quantity > 0) {
                        orderedItems.get(position).setQty(quantity);
                        orderedItems.get(position).setNet(quantity * 10);
                        orderedItemsAdapter.notifyDataSetChanged();
                        reCalculate(MainActivity.this);
                        qtyDialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "الكمية اقل من 1!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    qty.setError("حقل فارغ!");
                }
            }
        });
        qtyDialog.show();
    }

    public void filter(ArrayList<Items> items, String item, String category, GridView itemsGrid) {

        ArrayList<Items> tempList = new ArrayList<>();
        for (int k = 0; k < items.size(); k++) {
//            Log.e("******", items.get(k).getCategory() + "   " + category + " ---- " + items.get(k).getItemName() + "   " + item);
            if (
                    ((items.get(k).getItemName()).toUpperCase().contains(item.toUpperCase()) || item.equals("")) &&
                            ((items.get(k).getCategory()).equals(category) || category.equals(""))) {
                tempList.add(items.get(k));
//                Log.e("******2", items.get(k).getCategory() + "   " + category + " ---- " + items.get(k).getItemName() + "   " + item);
            }
        }

        gridItems = tempList;
//        Log.e("******3", "   " + gridItems.size());
        gridAdapter = new ItemGridAdapter(MainActivity.this, gridItems);
        itemsGrid.setAdapter(gridAdapter);
    }

    public void functionsDialog() {
        functionsDialog = new Dialog(MainActivity.this);
        functionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        functionsDialog.setCancelable(false);
        functionsDialog.setContentView(R.layout.functions);
        functionsDialog.setCanceledOnTouchOutside(true);

        functionsBack = functionsDialog.findViewById(R.id.functions_back);
        Button reports = functionsDialog.findViewById(R.id.reports);
        Button dayClose = functionsDialog.findViewById(R.id.day_close);

        setDialogTheme(theme, functionsBack, reports);
        setDialogTheme(theme, functionsBack, dayClose);

        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportDialog();
                functionsDialog.dismiss();
            }
        });

        dayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDayDialog();
                functionsDialog.dismiss();
            }
        });
        functionsDialog.show();
    }

    public void reportDialog() {
        reportsDialog = new Dialog(MainActivity.this);
        reportsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        reportsDialog.setCancelable(false);
        reportsDialog.setContentView(R.layout.reports);
        reportsDialog.setCanceledOnTouchOutside(true);

        reportsBack = reportsDialog.findViewById(R.id.reports_back);
        LinearLayout cashR = reportsDialog.findViewById(R.id.cash_report);
        LinearLayout salesR = reportsDialog.findViewById(R.id.sales_report);

        setDialogTheme(theme, reportsBack, new Button(MainActivity.this));

        cashR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CashReport.class);
                startActivity(intent);
            }
        });

        salesR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalesReport.class);
                startActivity(intent);
            }
        });
        reportsDialog.show();
    }

    @SuppressLint("SetTextI18n")
    public void closeDayDialog() {
        final Dialog closeDialog = new Dialog(MainActivity.this);
        closeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        closeDialog.setCancelable(false);
        closeDialog.setContentView(R.layout.close_day_dialog);
        closeDialog.setCanceledOnTouchOutside(true);

        LinearLayout closeDialogLiner = closeDialog.findViewById(R.id.closeDialogLiner);


        final Button close;
        close = closeDialog.findViewById(R.id.close);
        closeDay = closeDialog.findViewById(R.id.closeDay);
        newDay = closeDialog.findViewById(R.id.newDay);
        totalCashText = closeDialog.findViewById(R.id.totalCash);

        setDialogTheme(theme, closeDialogLiner, close);

        new JSONTask2().execute();

        newDay.setText(tomorrow);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CloseDay closeDay = new CloseDay(today, "1", Double.parseDouble(totalCash), companyNo);
                new Export(closeDay.getJSONObject(), "Close_Day");

                Intent intent = new Intent(MainActivity.this , LogIn.class);
                startActivity(intent);

                closeDialog.dismiss();
            }
        });

        closeDialog.show();
    }

    public void settingDialog() {
        settingsDialog = new Dialog(MainActivity.this);
        settingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setCancelable(false);
        settingsDialog.setContentView(R.layout.settings_dialog);
        settingsDialog.setCanceledOnTouchOutside(true);

        settingsBack = settingsDialog.findViewById(R.id.settings_back);
        final EditText ip = settingsDialog.findViewById(R.id.ip);
        final EditText company = settingsDialog.findViewById(R.id.company);
        final EditText companyID = settingsDialog.findViewById(R.id.company_id);
        final EditText posNo = settingsDialog.findViewById(R.id.pos_no);
        CheckBox price = settingsDialog.findViewById(R.id.price);
        CheckBox qty = settingsDialog.findViewById(R.id.qty);
        RadioGroup taxCalcKind = settingsDialog.findViewById(R.id.tax_type);
        RadioButton exclude = settingsDialog.findViewById(R.id.exclude);
        RadioButton include = settingsDialog.findViewById(R.id.include);


        ImageView creamDot = settingsDialog.findViewById(R.id.cream_dot);
        ImageView rosyDot = settingsDialog.findViewById(R.id.rosy_dot);
        ImageView skyDot = settingsDialog.findViewById(R.id.sky_dot);
        ImageView pronzeDot = settingsDialog.findViewById(R.id.pronze_dot);
        ImageView grayDot = settingsDialog.findViewById(R.id.gray_dot);
        ImageView redDot = settingsDialog.findViewById(R.id.red_dot);
        ImageView greenDot = settingsDialog.findViewById(R.id.green_dot);
        ImageView blueDot = settingsDialog.findViewById(R.id.blue_dot);

        saveSettings = settingsDialog.findViewById(R.id.save);

        setDialogTheme(theme, settingsBack, saveSettings);

        if (DHandler.getSettings() != null) {

            ip.setText(DHandler.getSettings().getIpAddress());
            company.setText(DHandler.getSettings().getCompanyName());

            if (DHandler.getSettings().getControlPrice() == 1) {
                price.setChecked(true);
                cPrice = 1;
            }

            if (DHandler.getSettings().getControlQty() == 1) {
                qty.setChecked(true);
                cQty = 1;
            }

            if (DHandler.getSettings().getTaxCalcKind() == 0)
                exclude.setChecked(true);
            else
                include.setChecked(true);
        }

        price.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    cPrice = 1;
                else
                    cPrice = 0;
            }
        });

        qty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                    cQty = 1;
                else
                    cQty = 0;
            }
        });

        final int taxKind = taxCalcKind.getCheckedRadioButtonId() == R.id.exclude ? 0 : 1;


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) v;

                switch (imageView.getId()) {

                    case R.id.rosy_dot:
                        theme = 2;
                        break;
                    case R.id.green_dot:
                        theme = 3;
                        break;
                    case R.id.gray_dot:
                        theme = 4;
                        break;
                    case R.id.red_dot:
                        theme = 5;
                        break;
                    case R.id.pronze_dot:
                        theme = 6;
                        break;
                    case R.id.sky_dot:
                        theme = 7;
                        break;
                    case R.id.blue_dot:
                        theme = 8;
                        break;
                    case R.id.cream_dot:
                        theme = 9;
                        break;
                }

                setThemeNo(theme);
                setDialogTheme(theme, settingsBack, saveSettings);
                DHandler.updateTheme(theme);
            }
        };

        creamDot.setOnClickListener(onClickListener);
        rosyDot.setOnClickListener(onClickListener);
        skyDot.setOnClickListener(onClickListener);
        pronzeDot.setOnClickListener(onClickListener);
        grayDot.setOnClickListener(onClickListener);
        redDot.setOnClickListener(onClickListener);
        greenDot.setOnClickListener(onClickListener);
        blueDot.setOnClickListener(onClickListener);

        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DHandler.updateSettings(new Settings(ip.getText().toString(), company.getText().toString(), theme, cPrice, cQty, companyID.getText().toString(), taxKind, posNo.getText().toString()));
                settingsDialog.dismiss();
            }
        });

        settingsDialog.show();
    }

    public void saveDialog() {
        saveDialog = new Dialog(MainActivity.this);
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.setCancelable(false);
        saveDialog.setContentView(R.layout.pay_dialog);
        saveDialog.setCanceledOnTouchOutside(true);

        saveBack = saveDialog.findViewById(R.id.save_back);
        required = saveDialog.findViewById(R.id.required);
        final EditText payed = saveDialog.findViewById(R.id.payed);
        final TextView remaining = saveDialog.findViewById(R.id.remaining);
        savePay = saveDialog.findViewById(R.id.pay);

        setDialogTheme(theme, saveBack, savePay);

        required.setText("" + sumWithTax);
        payed.setText("" + sumWithTax);

        payed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payed.setText("");
            }
        });

        payed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("***", payed.getText().toString());
                if (payed.getText().toString().equals(""))
                    remaining.setText("0");
                else
                    remaining.setText(convertToEnglish(String.format("%.3f", (Double.parseDouble(required.getText().toString()) - Double.parseDouble(payed.getText().toString())))));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        savePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Export(orderedItems.get(0).getJSONObject2(), "Sales_Master");

                try {
                    JSONArray jsonArray = new JSONArray();
                    for (int i = 0; i < orderedItems.size(); i++) {
                        jsonArray.put(orderedItems.get(i).getJSONObject3());
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Sales_Details", jsonArray);
                    new Export(jsonObject, "Sales_Details");

//                    new JSONTask2().execute();

                    Intent intent=new Intent(MainActivity.this,BluetoothConnectMenu.class);
                    intent.putExtra("printKey", "0");
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                itemDetail.add(sumNoTax.getText().toString().substring(sumNoTax.getText().toString().indexOf(":")+1,sumNoTax.getText().toString().length()));
                itemDetail.add(tax.getText().toString().substring(tax.getText().toString().indexOf(":")+1,tax.getText().toString().length()));
                itemDetail.add(sumAfterTax.getText().toString().substring(sumAfterTax.getText().toString().indexOf(":")+1,sumAfterTax.getText().toString().length()));
//                orderedItems.clear();
                orderedItemsAdapter.notifyDataSetChanged();
                reCalculate(MainActivity.this);

                saveDialog.dismiss();
            }
        });

        saveDialog.show();

    }

    void setThemeNo(int themeNo) {

        switch (themeNo) {
            case 2:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.rosy1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_rosy));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.rosy_blue));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.rosy_blue));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.rosy_blue));

                fabAddItem.setColorNormal(getResources().getColor(R.color.rosy_blue));
                fabAddItem.setColorPressed(getResources().getColor(R.color.rosy_blue));

                fabFunctions.setColorNormal(getResources().getColor(R.color.rosy_blue));
                fabFunctions.setColorPressed(getResources().getColor(R.color.rosy_blue));

                fabSettings.setColorNormal(getResources().getColor(R.color.rosy_blue));
                fabSettings.setColorPressed(getResources().getColor(R.color.rosy_blue));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                break;

            case 3:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.green1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_green));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.iguana_green));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.iguana_green));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.iguana_green));

                fabAddItem.setColorNormal(getResources().getColor(R.color.iguana_green));
                fabAddItem.setColorPressed(getResources().getColor(R.color.iguana_green));

                fabFunctions.setColorNormal(getResources().getColor(R.color.iguana_green));
                fabFunctions.setColorPressed(getResources().getColor(R.color.iguana_green));

                fabSettings.setColorNormal(getResources().getColor(R.color.iguana_green));
                fabSettings.setColorPressed(getResources().getColor(R.color.iguana_green));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                break;

            case 4:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.gray1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_gray));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.gray_orange));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.gray_orange));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.gray_orange));

                fabAddItem.setColorNormal(getResources().getColor(R.color.gray_orange));
                fabAddItem.setColorPressed(getResources().getColor(R.color.gray_orange));

                fabFunctions.setColorNormal(getResources().getColor(R.color.gray_orange));
                fabFunctions.setColorPressed(getResources().getColor(R.color.gray_orange));

                fabSettings.setColorNormal(getResources().getColor(R.color.gray_orange));
                fabSettings.setColorPressed(getResources().getColor(R.color.gray_orange));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                break;

            case 5:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.red1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_red));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.red_black));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.red_black));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.red_black));

                fabAddItem.setColorNormal(getResources().getColor(R.color.red_black));
                fabAddItem.setColorPressed(getResources().getColor(R.color.red_black));

                fabFunctions.setColorNormal(getResources().getColor(R.color.red_black));
                fabFunctions.setColorPressed(getResources().getColor(R.color.red_black));

                fabSettings.setColorNormal(getResources().getColor(R.color.red_black));
                fabSettings.setColorPressed(getResources().getColor(R.color.red_black));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                break;

            case 6:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.pronz1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_pronz));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.red_black));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.red_black));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.red_black));

                fabAddItem.setColorNormal(getResources().getColor(R.color.red_black));
                fabAddItem.setColorPressed(getResources().getColor(R.color.red_black));

                fabFunctions.setColorNormal(getResources().getColor(R.color.red_black));
                fabFunctions.setColorPressed(getResources().getColor(R.color.red_black));

                fabSettings.setColorNormal(getResources().getColor(R.color.red_black));
                fabSettings.setColorPressed(getResources().getColor(R.color.red_black));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                break;

            case 7:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.sky1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_sky));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.sky_brown));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.sky_brown));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.sky_brown));

                fabAddItem.setColorNormal(getResources().getColor(R.color.sky_brown));
                fabAddItem.setColorPressed(getResources().getColor(R.color.sky_brown));

                fabFunctions.setColorNormal(getResources().getColor(R.color.sky_brown));
                fabFunctions.setColorPressed(getResources().getColor(R.color.sky_brown));

                fabSettings.setColorNormal(getResources().getColor(R.color.sky_brown));
                fabSettings.setColorPressed(getResources().getColor(R.color.sky_brown));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                break;

            case 8:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.blue1));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_blue));
                back.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.gray_blue));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.gray_blue));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.gray_blue));

                fabAddItem.setColorNormal(getResources().getColor(R.color.gray_blue));
                fabAddItem.setColorPressed(getResources().getColor(R.color.gray_blue));

                fabFunctions.setColorNormal(getResources().getColor(R.color.gray_blue));
                fabFunctions.setColorPressed(getResources().getColor(R.color.gray_blue));

                fabSettings.setColorNormal(getResources().getColor(R.color.gray_blue));
                fabSettings.setColorPressed(getResources().getColor(R.color.gray_blue));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                break;

            case 9:
                rightLinear.setBackgroundColor(getResources().getColor(R.color.cream_rosy));
                topLinear.setBackgroundDrawable(getResources().getDrawable(R.drawable.hor_shape_sky));
                back.setBackgroundColor(getResources().getColor(R.color.cream));

                menuLabelsRight.setMenuButtonColorNormal(getResources().getColor(R.color.gray));
                menuLabelsRight.setMenuButtonColorPressed(getResources().getColor(R.color.gray));
                menuLabelsRight.setMenuButtonColorRipple(getResources().getColor(R.color.gray));

                fabAddItem.setColorNormal(getResources().getColor(R.color.sky_brown));
                fabAddItem.setColorPressed(getResources().getColor(R.color.sky_brown));

                fabFunctions.setColorNormal(getResources().getColor(R.color.beetle_green));
                fabFunctions.setColorPressed(getResources().getColor(R.color.beetle_green));

                fabSettings.setColorNormal(getResources().getColor(R.color.cream_rosy));
                fabSettings.setColorPressed(getResources().getColor(R.color.cream_rosy));

                save.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                search.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                clear.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                break;


        }
    }

    void setDialogTheme(int themeNo, LinearLayout dialogBack, Button button) {

        switch (themeNo) {
            case 2:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_rose));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.rosy_dot));
                break;

            case 3:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_green));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_dot));
                break;

            case 4:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_gray));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_dot));
                break;

            case 5:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_dot));
                break;

            case 6:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_pronz));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.pronze_dot));
                break;

            case 7:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_sky));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.sky_dot));
                break;

            case 8:
                dialogBack.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_blue));
                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_dot));
                break;

            case 9:
                dialogBack.setBackgroundColor(getResources().getColor(R.color.cream));
                button.setBackground(getResources().getDrawable(R.drawable.rosy_dot));
                break;
        }

    }
    void clearFun(){
     orderedItems.clear();
      itemNo.clear();
       orderedItemsAdapter.notifyDataSetChanged();

    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
    }

    void init() {

        save = findViewById(R.id.save);
        search = findViewById(R.id.search);
        clear = findViewById(R.id.clear);

        cats = findViewById(R.id.categories);
        listView = findViewById(R.id.listview);
        orderedList = findViewById(R.id.list);
        topLinear = findViewById(R.id.top_linear);
        rightLinear = findViewById(R.id.right_linear);
        back = findViewById(R.id.back);

        sumNoTax = findViewById(R.id.sum_no_tax);
        tax = findViewById(R.id.tax);
        sumAfterTax = findViewById(R.id.sum_after_tax);

        menuLabelsRight = findViewById(R.id.menu_labels_right);
        fabAddItem = findViewById(R.id.fab_add_item);
        fabFunctions = findViewById(R.id.fab_function);
        fabSettings = findViewById(R.id.fab_settings);
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
                URL url = new URL("http://10.0.0.214/miniPOS/import.php?FLAG=1");

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

                categories.clear();
//                subCatList.clear();

                try {
                    JSONArray parentArrayCats = parentObject.getJSONArray("CATEGORIES");

                    for (int i = 0; i < parentArrayCats.length(); i++) {
                        JSONObject innerObject = parentArrayCats.getJSONObject(i);

                        Categories category = new Categories();
                        category.setCatType(innerObject.getInt("DESC_TYPE"));
                        category.setCatNo(innerObject.getString("DESC_CODE"));
                        category.setCatName(innerObject.getString("DESC_NAME"));
                        category.setPic(innerObject.getInt("DESC_PIC"));

                        categories.add(category);
                    }
                } catch (JSONException e) {
                    Log.e("Import CATEGORIES", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayMaxes = parentObject.getJSONArray("ITEMS");

                    for (int i = 0; i < parentArrayMaxes.length(); i++) {
                        JSONObject innerObject = parentArrayMaxes.getJSONObject(i);

                        Items item = new Items();
                        item.setItemNo(innerObject.getString("ITEMNO"));
                        item.setBarcode(innerObject.getString("ITEM_BARCODE"));
                        item.setItemName(innerObject.getString("ITEM_NAME_A"));
                        item.setItemNameE(innerObject.getString("ITEM_NAME_E"));
                        item.setCategory(innerObject.getString("CATEGORY_ID"));
                        item.setSubCategory(innerObject.getString("SUB_CATEGORY_ID"));
                        item.setTaxValue(innerObject.getString("TAX_PERC"));
                        item.setPrice(innerObject.getDouble("SALE_PRICE"));
                        item.setDescription(innerObject.getString("ITEMDESC"));
                        item.setPic(innerObject.getInt("ITEM_PIC"));

                        subItems.add(item);
                        items.add(item);
                    }

                } catch (JSONException e) {
                    Log.e("Import CATEGORIES", e.getMessage().toString());
                }

                try {
                    JSONArray parentArrayInfo = parentObject.getJSONArray("INFO");
                    JSONObject innerObject = parentArrayInfo.getJSONObject(0);
                    JSONArray parentArrayInfoAlter = parentObject.getJSONArray("INFO_ALTER");
                    JSONObject innerObject2 = parentArrayInfoAlter.getJSONObject(0);

                    if (!innerObject.getString("SYSTEM_DATE").equals("null")) {

                        today = innerObject.getString("SYSTEM_DATE");
                        tomorrow = innerObject.getString("SYSTEM_DATE_NEXT");
                    } else {

                        today = innerObject2.getString("SYSTEM_DATE");
                        tomorrow = innerObject2.getString("SYSTEM_DATE_NEXT");
                    }


                    voucherNo = "" + (Integer.parseInt(innerObject2.getString("MAX_VOUCHER")) + 1);


                } catch (JSONException e) {
                    Log.e("Import INFO", e.getMessage().toString());
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

                catAdapter = new CategoryListAdapter(MainActivity.this, categories);
                cats.setAdapter(catAdapter);

                cats.setOnItemClickListener(onItemClickListener);

                itemsAdapter = new ItemListAdapter(MainActivity.this, subItems);
                listView.setAdapter(itemsAdapter);

                listView.setOnItemClickListener(onItemClickListener);


            } else {
                Toast.makeText(MainActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class JSONTask2 extends AsyncTask<String, String, String> {

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
                URL url = new URL("http://10.0.0.214/miniPOS/import.php?FLAG=2");

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
                    JSONArray parentArrayCloseDate = parentObject.getJSONArray("CLOSE_DATE");
                    JSONObject innerObject = parentArrayCloseDate.getJSONObject(0);

                    if (!innerObject.getString("TODAY").equals("null")) {

                        today = innerObject.getString("TODAY");
                        tomorrow = innerObject.getString("TOMORROW");
                    }

                    JSONArray parentArrayTotalCash = parentObject.getJSONArray("TOTAL_CASH");
                    JSONArray parentArrayTotalCashAlter = parentObject.getJSONArray("TOTAL_CASH_ALTER");
                    JSONObject innerObject2 = parentArrayTotalCash.getJSONObject(0);
                    JSONObject innerObject3 = parentArrayTotalCashAlter.getJSONObject(0);

                    if (!innerObject2.getString("TOTAL_C").equals("null")) {
                        totalCash = innerObject2.getString("TOTAL_C");

                    } else {
                        if (!innerObject3.getString("TOTAL_C").equals("null")) {
                            totalCash = innerObject3.getString("TOTAL_C");
                        } else
                            totalCash = "0";
                    }

                } catch (JSONException e) {
                    Log.e("Import INFO", e.getMessage().toString());
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

                closeDay.setText(today);
                totalCashText.setText(totalCash);
                newDay.setText(tomorrow);


            } else {
                Toast.makeText(MainActivity.this, "Not able to fetch data from server, please check url.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
