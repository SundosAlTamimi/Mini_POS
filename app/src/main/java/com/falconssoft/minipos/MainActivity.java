package com.falconssoft.minipos;

import android.animation.Animator;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
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
import java.util.ArrayList;
import java.util.List;

import static com.falconssoft.minipos.GlobelFunction.ipAddress;
import static com.falconssoft.minipos.GlobelFunction.searchItemList;

public class MainActivity extends AppCompatActivity {

    private GridView listItemGrid;
    private ItemListAdapter itemListAdapter;
    private CategoryItemListAdapter categoryItemListAdapter;
    public static OrderedListAdapter orderedItemsAdapter;
    private HorizontalListView categoryListView;
    private ListView orderedList;
    public static ListView orderedListB;
    private Button saveSettings, savePay, priceOk, qtyOk, clear, cancelButton;
    private Button save, search;
    private static TextView sumNoTax, tax, sumAfterTax;
    private LinearLayout topLinear, rightLinear, back, settingsBack, reportsBack, itemsBack, saveBack, functionsBack;
    private com.github.clans.fab.FloatingActionMenu menuLabelsRight;
    private com.github.clans.fab.FloatingActionButton fabAddItem, fabFunctions, fabSettings;
    ItemListAdapter gridAdapter;
    TextView required;
    String posNo, companyNo;
    int lastPosition=0;

    TextView closeDay, newDay, totalCashText,priceBack, qtyBack;

    public static List<List<Items>> holdVoucherList;

    String totalCash;
    ArrayList<Items> subItems;
    ArrayList<Categories> categories;
    ArrayList<Items> gridItems;
    public static ArrayList<String> itemNo;
    public static ArrayList<String> itemDetail;
    String searchQuery;
    GridView itemsGrid;
    //    Button holdMButton ;
    String today, tomorrow;
    String voucherNo;
    Toolbar toolbar;
    TextView catName, showHold;
    EditText itemCatSearch;
    String searchQItem = "";
    GlobelFunction globelFunction;
    Button voidLastButton, holdButton;
    RecyclerView recyclerViews;

    int cPrice = 0, cQty = 0;
    static double sum = 0, sumWithTax = 0, taxValue = 0, totalTax = 0, due = 0;


    Dialog settingsDialog, reportsDialog, itemsDialog, saveDialog, priceDialog, functionsDialog;
    static DatabaseHandler DHandler;

    ArrayList<Items> items;
    public static ArrayList<Items> orderedItems;

    public static int theme = 9;
    private CarouselLayoutManager layoutManagerd;
    private List<String> picforbar;
    LinearLayout linerRec;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);


        DHandler = new DatabaseHandler(MainActivity.this);
        if (DHandler.getSettings() != null) {
            posNo = DHandler.getSettings().getPosNo();
            companyNo = DHandler.getSettings().getCompanyID();
        }


        required = new EditText(MainActivity.this);

        init();

        holdVoucherList = new ArrayList<>();
        picforbar = new ArrayList<>();

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        globelFunction = new GlobelFunction();

        globelFunction.GlobelFunctionSetting(DHandler);
        orderedListB = orderedList;
        items = new ArrayList<>();
        subItems = new ArrayList<>();
        categories = new ArrayList<>();
        orderedItems = new ArrayList<>();
        itemNo = new ArrayList<>();
        itemDetail = new ArrayList<>();

//        search.setOnClickListener(onClickListener);
        clear.setOnClickListener(onClickListener);
        save.setOnClickListener(onClickListener);
        showHold.setOnClickListener(onClickListener);
        voidLastButton.setOnClickListener(onClickListener);
        holdButton.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);
//        fabAddItem.setOnClickListener(onClickListener);
//        fabFunctions.setOnClickListener(onClickListener);
//        fabSettings.setOnClickListener(onClickListener);

        new JSONTask().execute();
        orderedItemsAdapter = new OrderedListAdapter(MainActivity.this, orderedItems);
        orderedList.setAdapter(orderedItemsAdapter);
        orderedList.setOnItemLongClickListener(onItemLongClickListener);

        itemCatSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterIf(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        showHold.setText("" + holdVoucherList.size());
        picforbar.add("تعديل فاتورة");
        picforbar.add("اضافة فاتورة");
        picforbar.add("طباعة فاتورة");

        picforbar.add(" تعديل سند قبض");
        picforbar.add("اضافة سند قبض");
        picforbar.add("طباعة سند قبض");

        picforbar.add("اعدادات");

        linerRec.setVisibility(View.GONE);

        startAnimation();

//        if (DHandler.getSettings() == null)//.getIpAddress()
//            DHandler.addSettings(new Settings("", "", 9, 0, 0, "", 0, ""));
//        else {
//            setThemeNo(DHandler.getSettings().getThemeNo());
//            theme = DHandler.getSettings().getThemeNo();
//        }

    }


    void filterIf(String s) {

        if (!s.equals("")) {
//                    filter(items, s.toString(), s.toString(), listItemGrid);
            searchQItem = s;
            itemCatSearchFilter(s);
        } else {
            searchQItem = "";
            fillSubListItem(lastPosition);
            itemListAdapter = new ItemListAdapter(MainActivity.this, subItems);

            listItemGrid.setAdapter(itemListAdapter);
        }

    }

    public void itemCatSearchFilter(String itemName) {

        ArrayList<Items> tempList = new ArrayList<>();
        for (int i = 0; i < subItems.size(); i++) {

            if (subItems.get(i).getItemName().contains(itemName)) {
                Items itemsM = new Items();
                itemsM = subItems.get(i);
                tempList.add(itemsM);
            }

        }
        subItems=tempList;
        itemListAdapter = new ItemListAdapter(MainActivity.this, subItems);

        listItemGrid.setAdapter(itemListAdapter);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.save:
                    if (orderedItems.size() != 0) {
                        saveDialog();
                    } else
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.no_items_message), Toast.LENGTH_LONG).show();
                    break;

                case R.id.search:
//                    gridAdapter = new ItemGridAdapter(MainActivity.this, items);
//                    itemsDialog();
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

                case R.id.voidLastButton:
                    voidLastItemFromOrder();
                    break;

                case R.id.showHold:

                    showAllDataAccount();

                    Toast.makeText(MainActivity.this, "hold", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.cancel_button:

                    linerRec.setVisibility(View.GONE);

                    break;
                case R.id.holdButton:
                    if (orderedItems.size() != 0) {
//                        holdMButton.setVisibility(View.VISIBLE);
//                        insertImage();
                        orderedItems.get(0).setNetWithTax(Double.parseDouble(sumAfterTax.getText().toString()));
                        orderedItems.get(0).setNetBeforeTax(Double.parseDouble(sumNoTax.getText().toString()));
                        orderedItems.get(0).setTaxValue(tax.getText().toString());
                        holdVoucherList.add(new ArrayList<Items>(orderedItems));
                        showHold.setText("" + holdVoucherList.size());
                        saveVoucher("1");
                        clearFun();
                    } else {
                        Toast.makeText(MainActivity.this, "no item", Toast.LENGTH_SHORT).show();
                    }
                    break;

//                case R.id.addNewItem:
//                    Intent intentAdd = new Intent(MainActivity.this, ItemCard.class);
//                    startActivity(intentAdd);
//                    break;
            }

        }
    };

    void voidLastItemFromOrder() {

        if (orderedItems.size() != 0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getResources().getString(R.string.delete_message_for_item));
            builder.setTitle(getResources().getString(R.string.delete_last_item));
            builder.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    itemNo.remove(itemNo.size() - 1);
                    orderedItems.remove((orderedItems.size() - 1));
                    orderedItemsAdapter.notifyDataSetInvalidated();
                    reCalculate(MainActivity.this);

                }
            });
            builder.show();

        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.no_items_message), Toast.LENGTH_LONG).show();
        }

    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            switch (parent.getId()) {

                case R.id.itemListView:
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
                            orderedItems.get(i).setNetWithTax(netWithTax + calculateTax(subItems.get(position1).getPrice(), subItems.get(position1).getTaxValue()));

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
                                    , calculateTax(subItems.get(position1).getPrice(), subItems.get(position1).getTaxValue())
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

                case R.id.categoriList:

                    Log.e("categoriList", "" + categories.get(position).getCatName());
//                    for (int i = 0; i < items.size(); i++) {
//                        if (categories.get(position).getCatName().equals(items.get(i).getCategory())) {
////                            if(items.get(i).getIsActive().equals("1")) {
//                                subItems.add(items.get(i));
////                            }
//                        }
//                    }
//                    itemListAdapter.notifyDataSetChanged();
                    lastPosition=position;
                    fillSubListItem(position);
                    catName.setText(categories.get(position).getCatName());
                    filterIf(searchQItem);
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
                            orderedItems.get(i).setNetWithTax(netWithTax + calculateTax(orderedItems.get(i).getPrice(), orderedItems.get(i).getTaxValue()));
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
                                    , calculateTax(gridItems.get(position2).getPrice(), gridItems.get(position2).getTaxValue())
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
                                try {
                                    Log.e("Control",""+globelFunction.PriceControl);
                                    if (globelFunction.PriceControl == 1) {
                                        priceDialog(position);
                                    } else
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.cant_edit_price_message), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cant_edit_price_message), Toast.LENGTH_LONG).show();
                                }

                            } else {
                                try {
                                    Log.e("Control",""+globelFunction.QtyControl);
                                    if (globelFunction.QtyControl == 1)
                                        qtyDialog(position);
                                    else
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.cant_edit_qty_message), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cant_edit_qty_message), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    builder.show();
                    break;
            }
            return false;
        }
    };

    void fillSubListItem(int position){
        subItems.clear();
        for (int i = 0; i < items.size(); i++) {
            if (categories.get(position).getCatName().equals(items.get(i).getCategory())) {
//                            if(items.get(i).getIsActive().equals("1")) {
                subItems.add(items.get(i));
//                            }
            }
        }

    }
    Double calculateTax(Double value, String taxValue) {

        Double endValue;
        Double taxValu = Double.parseDouble(taxValue);

        try {
            if (DHandler.getSettings().getTaxCalcKind() == 0) {
                endValue = value + (value * taxValu / 100);

            } else {
                endValue = value + ((value * taxValu / 100) / (1 + (taxValu / 100)));

            }
        } catch (Exception e) {
            endValue = value + (value * taxValu / 100);
        }

        return endValue;
    }

    void reCalculate(Context context) {

        sum = 0;
        sumWithTax = 0;
        totalTax = 0;

        GlobelFunction globelFunction = new GlobelFunction();

        for (int i = 0; i < orderedItems.size(); i++) {

            sum += orderedItems.get(i).getNet();

            double tax = Double.parseDouble(orderedItems.get(i).getTaxValue());
            try {
                if (DHandler.getSettings().getTaxCalcKind() == 0) {

                    totalTax += orderedItems.get(i).getNet() * tax / 100;
                    sumWithTax += orderedItems.get(i).getNet() + (orderedItems.get(i).getNet() * tax / 100);
                } else {
                    totalTax += (orderedItems.get(i).getNet() * tax / 100) / (1 + (tax / 100));
                    sumWithTax += orderedItems.get(i).getNet() + ((orderedItems.get(i).getNet() * tax / 100) / (1 + (tax / 100)));
                }
            } catch (Exception e) {
                totalTax += orderedItems.get(i).getNet() * tax / 100;
                sumWithTax += orderedItems.get(i).getNet() + (orderedItems.get(i).getNet() * tax / 100);
            }
        }
//        taxValue = 2;
//        due = sum + (sum * taxValue / 100);

        sumNoTax.setText("" + globelFunction.DecimalFormat("" + sum));
        tax.setText("" + globelFunction.DecimalFormat("" + totalTax));
        sumAfterTax.setText("" + globelFunction.DecimalFormat("" + sumWithTax));

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

//        setDialogTheme(theme, itemsBack, new Button(MainActivity.this));

        searchQuery = "";
        gridItems = items;

        gridAdapter = new ItemListAdapter(MainActivity.this, gridItems);
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

//                filter(items, searchQuery, cat.getSelectedItem().toString(), itemsGrid);
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
//                filter(items, searchQuery, cat.getSelectedItem().toString(), itemsGrid);

                return false;
            }
        });

        itemsGrid.setOnItemClickListener(onItemClickListener);

        itemsDialog.show();
    }

    public void priceDialog(final int position) {
        priceDialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        priceDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        priceDialog.setCancelable(false);
        priceDialog.setContentView(R.layout.price_dialog);
        priceDialog.setCanceledOnTouchOutside(true);

        priceBack = priceDialog.findViewById(R.id.price_back);
        final EditText price = priceDialog.findViewById(R.id.price);
        priceOk = priceDialog.findViewById(R.id.ok);

        priceDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        setDialogTheme(theme, priceBack, priceOk);

        price.setText("" + orderedItems.get(position).getPrice());

        priceOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!price.getText().toString().equals("")) {

                    orderedItems.get(position).setPrice(Double.parseDouble(price.getText().toString()));
                    orderedItems.get(position).setNet(orderedItems.get(position).getQty() * orderedItems.get(position).getPrice());
                    reCalculate(MainActivity.this);
                    orderedItemsAdapter.notifyDataSetChanged();
                    priceDialog.dismiss();
                }
            }
        });
        priceDialog.show();

    }

    void qtyDialog(final int position) {
        final Dialog qtyDialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        qtyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qtyDialog.setContentView(R.layout.quantity_dialog);

        qtyDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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


//        setDialogTheme(theme, qtyBack, qtyOk);

        qtyOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(qty.getText().toString())) {
                    double quantity = Double.parseDouble(qty.getText().toString());

                    if (quantity > 0) {
                        orderedItems.get(position).setQty(quantity);
                        orderedItems.get(position).setNet(quantity* orderedItems.get(position).getPrice());
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


//    public void filter(ArrayList<Items> items, String item, String category, GridView itemsGrid) {
//
//        ArrayList<Items> tempList = new ArrayList<>();
//        tempList.clear();
//        for (int k = 0; k < items.size(); k++) {
////            Log.e("******", items.get(k).getCategory() + "   " + category + " ---- " + items.get(k).getItemName() + "   " + item);
//            if (
//                    ((items.get(k).getItemName()).toUpperCase().contains(item.toUpperCase()) || item.equals("")));/* &&
//                            ((items.get(k).getCategory()).equals(category) || category.equals("")))*/ {
//                Items items1=new Items();
//                  items1=items.get(k);
//                tempList.add(items1);
////                Log.e("******2", items.get(k).getCategory() + "   " + category + " ---- " + items.get(k).getItemName() + "   " + item);
//            }
//        }
//
////        gridItems = tempList;
//        Log.e("******3", "   " + tempList.size());
//        gridAdapter = new ItemListAdapter(MainActivity.this, tempList);
//        itemsGrid.setAdapter(gridAdapter);
//    }

    public void functionsDialog() {
        functionsDialog = new Dialog(MainActivity.this);
        functionsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        functionsDialog.setCancelable(false);
        functionsDialog.setContentView(R.layout.functions);
        functionsDialog.setCanceledOnTouchOutside(true);

        functionsBack = functionsDialog.findViewById(R.id.functions_back);
        Button reports = functionsDialog.findViewById(R.id.reports);
        Button dayClose = functionsDialog.findViewById(R.id.day_close);

//        setDialogTheme(theme, functionsBack, reports);
//        setDialogTheme(theme, functionsBack, dayClose);

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

//        setDialogTheme(theme, reportsBack, new Button(MainActivity.this));

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
        final Dialog closeDialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        closeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        closeDialog.setCancelable(false);
        closeDialog.setContentView(R.layout.close_day_dialog);
        closeDialog.setCanceledOnTouchOutside(true);
        closeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        CardView closeDialogLiner = closeDialog.findViewById(R.id.closeDialogLiner);


        final Button close;
        close = closeDialog.findViewById(R.id.close);
        closeDay = closeDialog.findViewById(R.id.closeDay);
        newDay = closeDialog.findViewById(R.id.newDay);
        totalCashText = closeDialog.findViewById(R.id.totalCash);


        new JSONTask2().execute();

        newDay.setText(tomorrow);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CloseDay closeDay = new CloseDay(today, "1", Double.parseDouble(totalCash), companyNo);
                new Export(closeDay.getJSONObject(), "Close_Day",MainActivity.this);

                Intent intent = new Intent(MainActivity.this, LogIn.class);
                startActivity(intent);

                closeDialog.dismiss();
            }
        });

        closeDialog.show();
    }

    public void settingDialog() {
        settingsDialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        settingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setCancelable(false);
        settingsDialog.setContentView(R.layout.settings_dialog);
        settingsDialog.setCanceledOnTouchOutside(false);

        settingsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        settingsBack = settingsDialog.findViewById(R.id.settings_back);
        final EditText ip = settingsDialog.findViewById(R.id.ip);
        final EditText company = settingsDialog.findViewById(R.id.company);
        final EditText companyID = settingsDialog.findViewById(R.id.company_id);
        final EditText posNo = settingsDialog.findViewById(R.id.pos_no);
        final CheckBox price = settingsDialog.findViewById(R.id.price);
        final CheckBox qty = settingsDialog.findViewById(R.id.qty);
        final RadioGroup taxCalcKind = settingsDialog.findViewById(R.id.tax_type);
        final RadioButton exclude = settingsDialog.findViewById(R.id.exclude);
        RadioButton include = settingsDialog.findViewById(R.id.include);

        Button cancel = settingsDialog.findViewById(R.id.cancel);
        saveSettings = settingsDialog.findViewById(R.id.save);

//        setDialogTheme(theme, settingsBack, saveSettings);

        Settings settings = DHandler.getSettings();

        if (settings != null) {

            ip.setText(DHandler.getSettings().getIpAddress());
            company.setText(DHandler.getSettings().getCompanyName());
            companyID.setText(DHandler.getSettings().getCompanyID());
            posNo.setText(DHandler.getSettings().getPosNo());


            if (DHandler.getSettings().getControlPrice() == 1) {
                price.setChecked(true);
            }

            if (DHandler.getSettings().getControlQty() == 1) {
                qty.setChecked(true);
            }

            if (DHandler.getSettings().getTaxCalcKind() == 0)
                exclude.setChecked(true);
            else
                include.setChecked(true);
        }


        final int[] taxKind = {0};

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss();
            }
        });
        saveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DHandler.deleteSettings();
                if (price.isChecked()) {
                    cPrice = 1;
                } else {
                    cPrice = 0;
                }

                if (qty.isChecked()) {
                    cQty = 1;
                } else {
                    cQty = 0;
                }


                if (exclude.isChecked()) {
                    taxKind[0] = 0;
                } else {
                    taxKind[0] = 1;
                }

                DHandler.addSettings(new Settings(company.getText().toString(),ip.getText().toString(),
                        theme, cPrice, cQty, companyID.getText().toString(), taxKind[0], posNo.getText().toString()));
                settingsDialog.dismiss();
            }
        });

        settingsDialog.show();
    }

    public void saveDialog() {
        saveDialog = new Dialog(MainActivity.this, R.style.Theme_Dialog);
        saveDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        saveDialog.setCancelable(false);
        saveDialog.setContentView(R.layout.pay_dialog);
        saveDialog.setCanceledOnTouchOutside(true);
        saveDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        saveBack = saveDialog.findViewById(R.id.save_back);
        required = saveDialog.findViewById(R.id.required);
        final EditText payed = saveDialog.findViewById(R.id.payed);
        final TextView remaining = saveDialog.findViewById(R.id.remaining);
        savePay = saveDialog.findViewById(R.id.pay);

//        setDialogTheme(theme, saveBack, savePay);

        GlobelFunction globelFunction = new GlobelFunction();
        required.setText("" + globelFunction.DecimalFormat("" + sumWithTax));
        payed.setText("" + globelFunction.DecimalFormat("" + sumWithTax));

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
                saveVoucher("0");
//                new Export(orderedItems.get(0).getJSONObject2(), "Sales_Master");
//
//                try {
//                    JSONArray jsonArray = new JSONArray();
//                    for (int i = 0; i < orderedItems.size(); i++) {
//                        jsonArray.put(orderedItems.get(i).getJSONObject3());
//                    }
//
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("Sales_Details", jsonArray);
//                    new Export(jsonObject, "Sales_Details");
//
////                    new JSONTask2().execute();
//
//                    Intent intent=new Intent(MainActivity.this,BluetoothConnectMenu.class);
//                    intent.putExtra("printKey", "0");
//                    startActivity(intent);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                itemDetail.add(sumNoTax.getText().toString().substring(sumNoTax.getText().toString().indexOf(":")+1,sumNoTax.getText().toString().length()));
//                itemDetail.add(tax.getText().toString().substring(tax.getText().toString().indexOf(":")+1,tax.getText().toString().length()));
//                itemDetail.add(sumAfterTax.getText().toString().substring(sumAfterTax.getText().toString().indexOf(":")+1,sumAfterTax.getText().toString().length()));
////                orderedItems.clear();
//                orderedItemsAdapter.notifyDataSetChanged();
//                reCalculate(MainActivity.this);

                saveDialog.dismiss();
            }
        });

        saveDialog.show();

    }


    void saveVoucher(String vStatus) {

        orderedItems.get(0).setVoucherStatus(vStatus);
        orderedItems.get(0).setVoucherDate(globelFunction.DateInToday());

        new Export(orderedItems.get(0).getJSONObject2(), "Sales_Master",MainActivity.this);

        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < orderedItems.size(); i++) {
                orderedItems.get(i).setVoucherStatus(vStatus);
                orderedItems.get(i).setVoucherDate(globelFunction.DateInToday());
                jsonArray.put(orderedItems.get(i).getJSONObject3());
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Sales_Details", jsonArray);
            new Export(jsonObject, "Sales_Details",MainActivity.this);

//                    new JSONTask2().execute();

//            Intent intent=new Intent(MainActivity.this,BluetoothConnectMenu.class);
//            intent.putExtra("printKey", "0");
//            startActivity(intent);

            clearFun();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        itemDetail.add(sumNoTax.getText().toString());
        itemDetail.add(tax.getText().toString());
        itemDetail.add(sumAfterTax.getText().toString());
//                orderedItems.clear();
        orderedItemsAdapter.notifyDataSetChanged();
        reCalculate(MainActivity.this);


    }


    void clearFun() {
        orderedItems.clear();
        itemNo.clear();
        orderedItemsAdapter.notifyDataSetChanged();
        tax.setText("0.0");
        sumNoTax.setText("0.0");
        sumAfterTax.setText("0.0");

    }

    public String convertToEnglish(String value) {
        String newValue = (((((((((((value + "").replaceAll("١", "1")).replaceAll("٢", "2")).replaceAll("٣", "3")).replaceAll("٤", "4")).replaceAll("٥", "5")).replaceAll("٦", "6")).replaceAll("٧", "7")).replaceAll("٨", "8")).replaceAll("٩", "9")).replaceAll("٠", "0").replaceAll("٫", "."));
        return newValue;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if(linerRec.getVisibility()==View.VISIBLE){
//            linerRec.setVisibility(View.GONE);
//        }else {
        Intent intent = new Intent(MainActivity.this, LogIn.class);
        startActivity(intent);
//        }
    }

    void init() {

        save = findViewById(R.id.save);
//        search = findViewById(R.id.search);
        clear = findViewById(R.id.clear);
        itemCatSearch = findViewById(R.id.itemCatSearch);
        listItemGrid = findViewById(R.id.itemListView);
        categoryListView = findViewById(R.id.categoriList);
        orderedList = findViewById(R.id.list);
        topLinear = findViewById(R.id.top_linear);
        rightLinear = findViewById(R.id.right_linear);
        back = findViewById(R.id.back);

        sumNoTax = findViewById(R.id.sum_no_tax);
        tax = findViewById(R.id.tax);
        sumAfterTax = findViewById(R.id.sum_after_tax);

        menuLabelsRight = findViewById(R.id.menu_labels_right);
        catName = findViewById(R.id.catName);
        voidLastButton = findViewById(R.id.voidLastButton);
        holdButton = findViewById(R.id.holdButton);
        showHold = findViewById(R.id.showHold);
        recyclerViews = findViewById(R.id.recyclerViews);
        linerRec = findViewById(R.id.linerRec);
        cancelButton = findViewById(R.id.cancel_button);
//         holdMButton = findViewById(R.id.holdMButton);
//        holdMButton.setVisibility(View.GONE);
//        fabAddItem = findViewById(R.id.fab_add_item);
//        fabFunctions = findViewById(R.id.fab_function);
//        fabSettings = findViewById(R.id.fab_settings);
    }


    void showAllDataAccount() {
        showHold.setText("" + holdVoucherList.size());
        linerRec.setVisibility(View.VISIBLE);
        layoutManagerd = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true);

        recyclerViews.setLayoutManager(layoutManagerd);
        recyclerViews.setHasFixedSize(true);
        recyclerViews.addOnScrollListener(new CenterScrollListener());
        layoutManagerd.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        recyclerViews.setAdapter(new TestAdapterForbar(this, holdVoucherList));
        recyclerViews.requestFocus();
        recyclerViews.scrollToPosition(6);
        recyclerViews.requestFocus();


    }

    public void fillList(ListView holdList, List<Items> list) {

        OrderedListAdapterHold orderedItemsAdapter = new OrderedListAdapterHold(MainActivity.this, list);
        holdList.setAdapter(orderedItemsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menue_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.setting:
                settingDialog();
                // do your code
                return true;
            case R.id.add_item:
                // do your code
                Intent intent = new Intent(MainActivity.this, ItemCard.class);
                startActivity(intent);
                return true;
            case R.id.CashReport:
                Intent cashIntent = new Intent(MainActivity.this, CashReport.class);
                startActivity(cashIntent);
                // do your code
                return true;
            case R.id.SalesReport:
                Intent salesIntent = new Intent(MainActivity.this, SalesReport.class);
                startActivity(salesIntent);
                // do your code
                return true;
            case R.id.CloseDay:
                closeDayDialog();
                // do your code

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    static class CViewHolderForbar extends RecyclerView.ViewHolder {

        ListView holdList;
        TextView BTax, ATax, tax;
        Button clear, doneButton;

        public CViewHolderForbar(View itemView) {
            super(itemView);
//            ItemName = itemView.findViewById(R.id.textbar);
//            layBar = itemView.findViewById(R.id.layBar);
//            itemImage = itemView.findViewById(R.id.imgbar);

            holdList = itemView.findViewById(R.id.listHold);
            BTax = itemView.findViewById(R.id.sum_no_tax);
            ATax = itemView.findViewById(R.id.sum_after_tax);
            tax = itemView.findViewById(R.id.tax);
            clear = itemView.findViewById(R.id.clear);
            doneButton = itemView.findViewById(R.id.done_button);

        }
    }

    class TestAdapterForbar extends RecyclerView.Adapter<CViewHolderForbar> {
        Context context;
        List<List<Items>> list;
//DatabaseHandler db;

        public TestAdapterForbar(Context context, List<List<Items>> list) {
            this.context = context;
            this.list = list;
//        db=new DatabaseHandler(this.context);
        }


        @Override
        public CViewHolderForbar onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.voucher_in_recycle_list, viewGroup, false);
            return new CViewHolderForbar(view);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(final CViewHolderForbar cViewHolder, final int i) {

            fillList(cViewHolder.holdList, list.get(i));

            cViewHolder.BTax.setText(""+list.get(i).get(0).getNetBeforeTax());
            cViewHolder.ATax.setText(""+list.get(i).get(0).getNetWithTax());
            cViewHolder.tax.setText(""+list.get(i).get(0).getTaxValue());
            Log.e("holdTTT", "" + i);


            cViewHolder.doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (orderedItems.size() != 0) {
//                        holdVoucherList.add(new ArrayList<Items>(orderedItems));
//                        clearFun();
//                        orderedItems=(new ArrayList<>(holdVoucherList.get(i)));
//                        fillItemNo();
//                        orderedItemsAdapter = new OrderedListAdapter(MainActivity.this, orderedItems);
//                        orderedList.setAdapter(orderedItemsAdapter);
//                        holdVoucherList.remove(i);
//                        showHold.setText("" + holdVoucherList.size());
//                        reCalculate(context);
//                        linerRec.setVisibility(View.GONE);

                        returnVoucherHold(0, i, context);

                    } else {
                        returnVoucherHold(1, i, context);

                    }

                }
            });

            cViewHolder.clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("holdTTT", "" + i);

                    holdVoucherList.remove(i);

                    showAllDataAccount();
                }
            });
//            cViewHolder.ItemName.setText(list.get(i));
//            cViewHolder.layBar.setTag("" + i);

//            switch (i){
//
//                case 0:
//
////                    DrawableCompat.setTint(
////                            DrawableCompat.wrap(cViewHolder.itemImage.getDrawable()),
////                            ContextCompat.getColor(context, R.color.Orange)
////                    );
//
//                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.Orange));
//                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.Orange));
//
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_edit_black_24dp));
//
//                    break;
//                case 1:
//                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.Orange));
//                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.Orange));
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_black_24dp));
//                    break;
//                case 2:
//                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.Orange));
//                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.Orange));
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_print_black_24dp));
//                    break;
//                case 3:
////                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.dark_blue));
////                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.dark_blue));
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_edit_black_24dp));
//                    break;
//                case 4:
////                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.dark_blue));
////                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.dark_blue));
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_add_black_24dp));
//                    break;
//                case 5:
////                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.dark_blue));
////                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.dark_blue));
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_print_black_24dp));
//                    break;
//
//                case 6:
//                    cViewHolder.itemImage.setColorFilter(ContextCompat.getColor(context, R.color.blue_ice));
//                    cViewHolder.itemImage.setBorderColor(context.getResources().getColor(R.color.blue_ice));
//                    cViewHolder.itemImage.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_settings_black_24dp));
//                    break;
//
//            }


            final boolean[] longIsOpen = {false};
//            cViewHolder.layBar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    picforbar.add("تعديل فاتورة");
////                    picforbar.add("اضافة فاتورة");
////                    picforbar.add("طباعة فاتورة");
////
////                    picforbar.add(" تعديل سند قبض");
////                    picforbar.add("اضافة سند قبض");
////                    picforbar.add("طباعة سند قبض");
////
////                    picforbar.add("اعدادات");
//
//
////                    switch (i){
////                        case 0://تعديل فاتورة
////                            finish();
////                            Intent editIntent=new Intent(MainActivityOn.this,MakeVoucher.class);
////                            editIntent.putExtra("EDIT_VOUCHER","EDIT_VOUCHER");
////                            // ChequeInfo
//////                            editeIntent.putExtra("ChequeInfo",chequeInfo);
////                            startActivity(editIntent);
////                            break;
////                        case 1://اضافة فاتورة
////                            finish();
////                            Intent AddVocher= new Intent(MainActivityOn.this,MakeVoucher.class);
////                            startActivity(AddVocher);
////                            break;
////                        case 2://طباعة فاتورة
////                            finish();
////                            Intent PrintVoucherIntent= new Intent(MainActivityOn.this, PrintVoucher.class);
////                            startActivity(PrintVoucherIntent);
////                            break;
////                        case 3://تعديل سند قبض
////                            finish();
////                            Intent editRecIntent=new Intent(MainActivityOn.this,Receipt.class);
////                            editRecIntent.putExtra("EDIT_REC","EDIT_REC");
////                            // ChequeInfo
//////                            editeIntent.putExtra("ChequeInfo",chequeInfo);
////                            startActivity(editRecIntent);
////                            break;
////                        case 4://اضافة سند قبض
////                            finish();
////                            Intent receipt= new Intent(MainActivityOn.this, Receipt.class);
////                            startActivity(receipt);
////                            break;
////                        case 5://طباعة سند قبض
////                            finish();
////                            Intent PrintRecCashIntent= new Intent(MainActivityOn.this, PrintRecCash.class);
////                            startActivity(PrintRecCashIntent);
////                            break;
////
////                        case 6://اعدادات
////                            finish();
////                            Intent SettingIntent= new Intent(MainActivityOn.this, AppSetting.class);
////                            startActivity(SettingIntent);
////                            break;
////                    }
//
//                }
//            });

            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        }

        @Override
        public int getItemCount() {
            return list.size();
//            return Integer.MAX_VALUE;
        }
    }

    void fillItemNo() {
        for (int i = 0; i < orderedItems.size(); i++) {
            String itemN = orderedItems.get(i).getItemNo();
            itemNo.add(itemN);
        }

    }

    void returnVoucherHold(int flag, int i, Context context) {

        if (flag == 0) {
            holdVoucherList.add(new ArrayList<Items>(orderedItems));
        }
        clearFun();
        orderedItems = (new ArrayList<>(holdVoucherList.get(i)));
        fillItemNo();
        orderedItemsAdapter = new OrderedListAdapter(MainActivity.this, orderedItems);
        orderedList.setAdapter(orderedItemsAdapter);
        holdVoucherList.remove(i);
        showHold.setText("" + holdVoucherList.size());
        reCalculate(context);
        linerRec.setVisibility(View.GONE);

    }


//    public Button insertImage(){
////        imageView.setImageBitmap(StringToBitMap(picItem));
//        int p1[] = new int[2];
//        int p2[] = new int[2];
//        showHold.getLocationInWindow(p2);
//        holdMButton.getLocationInWindow(p1);
//        holdMButton.setVisibility(View.VISIBLE);
//        Log.e("location ",""+p1[0]+"    "+p1[1]);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
//        holdMButton.setLayoutParams(params);
//        holdMButton.setX(p1[0]);
//        holdMButton.setY(p1[1]);
//
////        imageMove.addView(imageView);
//        holdMButton.animate()
//                .x(p2[0])
//                .y(p2[1])
//                .setDuration(1000)
//                .setListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//
//                        holdMButton.setVisibility(View.GONE);
//                        showHold.setText("" + holdVoucherList.size());
//
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//
//
//                })
//                .start();
//
//        return holdMButton;
//    }

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
                URL url = new URL("http://"+ipAddress+"/miniPOS/import.php?FLAG=1");

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
                    searchItemList.clear();
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
                        item.setIsActive(innerObject.getString("IS_ACTIVE"));
                        if(item.getIsActive().equals("1")) {
                            subItems.add(item);
                            items.add(item);
                        }
                        searchItemList.add(item);
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

                itemListAdapter = new ItemListAdapter(MainActivity.this, subItems);
                listItemGrid.setAdapter(itemListAdapter);

                listItemGrid.setOnItemClickListener(onItemClickListener);

                categoryItemListAdapter = new CategoryItemListAdapter(MainActivity.this, categories);
                categoryListView.setAdapter(categoryItemListAdapter);

                categoryListView.setOnItemClickListener(onItemClickListener);

                Log.e("categories", "" + categories.size());


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
                URL url = new URL("http://"+ipAddress+"/miniPOS/import.php?FLAG=2");

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
