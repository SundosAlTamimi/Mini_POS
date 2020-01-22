package com.falconssoft.minipos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.falconssoft.minipos.Modle.Items;
import com.falconssoft.minipos.Port.AlertView;

import com.sewoo.port.android.BluetoothPort;
import com.sewoo.request.android.RequestHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import static com.falconssoft.minipos.MainActivity.itemDetail;
import static com.falconssoft.minipos.MainActivity.orderedItems;
import static com.falconssoft.minipos.MainActivity.orderedListB;


// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)

public class BluetoothConnectMenu extends Activity {
    private static final String TAG = "BluetoothConnectMenu";
    private static final int REQUEST_ENABLE_BT = 2;
    ArrayAdapter<String> adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Vector<BluetoothDevice> remoteDevices;
    private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver disconnectReceiver;
    private Thread hThread;
    private Context context;
    private EditText btAddrBox;
    private Button connectButton;
    private Button searchButton;
    ArrayList<Items> orderedItemsB;
    ArrayList <String> itemDetailB;

    LinearLayout item;
    private ListView list;
    private BluetoothPort bluetoothPort;
    private CheckBox chkDisconnect;
    private static final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "//temp";
    private static final String fileName;
    private String lastConnAddr;
    static String idname;

    String getData;
    String today;


    DecimalFormat decimalFormat;


    static {
        fileName = dir + "//BTPrinter";
    }

    public BluetoothConnectMenu() {

    }

    private void bluetoothSetup() {
        this.clearBtDevData();
        this.bluetoothPort = BluetoothPort.getInstance();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter != null) {
            if (!this.mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.startActivityForResult(enableBtIntent, 2);
            }

        }
    }

    private void loadSettingFile() {
//        int rin = false;
        char[] buf = new char[128];

        try {
            FileReader fReader = new FileReader(fileName);
            int rin = fReader.read(buf);
            if (rin > 0) {
                this.lastConnAddr = new String(buf, 0, rin);
                this.btAddrBox.setText(this.lastConnAddr);
            }

            fReader.close();
        } catch (FileNotFoundException var4) {
            Log.i("BluetoothConnectMenu", "Connection history not exists.");
        } catch (IOException var5) {
            Log.e("BluetoothConnectMenu", var5.getMessage(), var5);
        }

    }

    private void saveSettingFile() {
        try {
            File tempDir = new File(dir);
            if (!tempDir.exists()) {
                tempDir.mkdir();
            }

            FileWriter fWriter = new FileWriter(fileName);
            if (this.lastConnAddr != null) {
                fWriter.write(this.lastConnAddr);
            }

            fWriter.close();
        } catch (FileNotFoundException var3) {
            Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
        } catch (IOException var4) {
            Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
        }

    }

    private void clearBtDevData() {
        this.remoteDevices = new Vector();
    }

    private void addPairedDevices() {
        Iterator iter = this.mBluetoothAdapter.getBondedDevices().iterator();

        while (iter.hasNext()) {
            BluetoothDevice pairedDevice = (BluetoothDevice) iter.next();
            if (this.bluetoothPort.isValidAddress(pairedDevice.getAddress())) {//note
                this.remoteDevices.add(pairedDevice);
                this.adapter.add(pairedDevice.getName() + "\n[" + pairedDevice.getAddress() + "] [Paired]");
            }
        }

    }

    double size_subList = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.bluetooth_menu);
        this.btAddrBox = (EditText) this.findViewById(R.id.EditTextAddressBT);
        this.connectButton = (Button) this.findViewById(R.id.ButtonConnectBT);
        BluetoothConnectMenu.this.connectButton.setEnabled(true);
        this.searchButton = (Button) this.findViewById(R.id.ButtonSearchBT);
        this.list = (ListView) this.findViewById(R.id.BtAddrListView);
        this.chkDisconnect = (CheckBox) this.findViewById(R.id.check_disconnect);
        this.chkDisconnect.setChecked(true);
        this.context = this;
        item=this.findViewById(R.id.item);
//        obj = new DatabaseHandler(BluetoothConnectMenu.this);

        decimalFormat = new DecimalFormat("##.000");
        orderedItemsB=new ArrayList<>();
        itemDetailB = new ArrayList<>();
        Date currentTimeAndDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        today = df.format(currentTimeAndDate);

//
        getData = getIntent().getStringExtra("printKey");
//        Bundle bundle = getIntent().getExtras();
//         allStudents = (List<Item>) bundle.get("ExtraData");
//
//         Log.e("all",allStudents.get(0).getBarcode());

        Log.e("printKey", "" + getData);
        this.loadSettingFile();
        this.bluetoothSetup();

        this.connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BluetoothConnectMenu.this.bluetoothPort.isConnected()) {
                    try {
                        BluetoothConnectMenu.this.btConn(BluetoothConnectMenu.this.mBluetoothAdapter.getRemoteDevice(remoteDevices.get(0).getAddress()));
                    } catch (IllegalArgumentException var3) {
                        Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
                        AlertView.showAlert(var3.getMessage(), BluetoothConnectMenu.this.context);
                        return;
                    } catch (IOException var4) {
                        Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                        AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu.this.context);
                        return;
                    }
                } else {
                    BluetoothConnectMenu.this.btDisconn();
                }

            }
        });
        this.searchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (!BluetoothConnectMenu.this.mBluetoothAdapter.isDiscovering()) {
                    BluetoothConnectMenu.this.clearBtDevData();
                    BluetoothConnectMenu.this.adapter.clear();
                    BluetoothConnectMenu.this.mBluetoothAdapter.startDiscovery();
                } else {
                    BluetoothConnectMenu.this.mBluetoothAdapter.cancelDiscovery();
                }

            }
        });
        this.adapter = new ArrayAdapter(BluetoothConnectMenu.this, R.layout.cci);

        this.list.setAdapter(this.adapter);
        this.addPairedDevices();
        this.list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                BluetoothDevice btDev = (BluetoothDevice) BluetoothConnectMenu.this.remoteDevices.elementAt(arg2);

                try {
                    if (BluetoothConnectMenu.this.mBluetoothAdapter.isDiscovering()) {
                        BluetoothConnectMenu.this.mBluetoothAdapter.cancelDiscovery();
                    }

                    BluetoothConnectMenu.this.btAddrBox.setText(btDev.getAddress());
                    BluetoothConnectMenu.this.btConn(btDev);
                } catch (IOException var8) {
                    AlertView.showAlert(var8.getMessage(), BluetoothConnectMenu.this.context);
                }
            }
        });
        this.discoveryResult = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothDevice remoteDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (remoteDevice != null) {
                    String key;
                    if (remoteDevice.getBondState() != 12) {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "]";
                    } else {
                        key = remoteDevice.getName() + "\n[" + remoteDevice.getAddress() + "] [Paired]";
                    }

                    if (BluetoothConnectMenu.this.bluetoothPort.isValidAddress(remoteDevice.getAddress())) {
                        BluetoothConnectMenu.this.remoteDevices.add(remoteDevice);
                        BluetoothConnectMenu.this.adapter.add(key);
                    }
                }

            }
        };
        this.registerReceiver(this.discoveryResult, new IntentFilter("android.bluetooth.device.action.FOUND"));
        this.searchStart = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothConnectMenu.this.connectButton.setEnabled(false);
                BluetoothConnectMenu.this.btAddrBox.setEnabled(false);
//                BluetoothConnectMenu.this.searchButton.setText(BluetoothConnectMenu.this.getResources().getString(2131034114));

                BluetoothConnectMenu.this.searchButton.setText("stop ");
            }
        };
        this.registerReceiver(this.searchStart, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED"));
        this.searchFinish = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                BluetoothConnectMenu.this.connectButton.setEnabled(true);
                BluetoothConnectMenu.this.btAddrBox.setEnabled(true);
//                BluetoothConnectMenu.this.searchButton.setText(BluetoothConnectMenu.this.getResources().getString(2131034113));
                BluetoothConnectMenu.this.searchButton.setText("search");

            }
        };
        this.registerReceiver(this.searchFinish, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        if (this.chkDisconnect.isChecked()) {
            this.disconnectReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                    if (!"android.bluetooth.device.action.ACL_CONNECTED".equals(action) && "android.bluetooth.device.action.ACL_DISCONNECTED".equals(action)) {
                        BluetoothConnectMenu.this.DialogReconnectionOption();
                    }

                }
            };
        }
        item.setVisibility(View.GONE);
        if(remoteDevices.size()!=0) {
            coon();
        }else{
            Toast.makeText(context, "Please Connect to Bluetooth ", Toast.LENGTH_SHORT).show();
        }

    }

    public void coon(){
        if (!BluetoothConnectMenu.this.bluetoothPort.isConnected()) {
            try {
                BluetoothConnectMenu.this.btConn(BluetoothConnectMenu.this.mBluetoothAdapter.getRemoteDevice(remoteDevices.get(0).getAddress()));
            } catch (IllegalArgumentException var3) {
                Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
                AlertView.showAlert(var3.getMessage(), BluetoothConnectMenu.this.context);
                return;
            } catch (IOException var4) {
                Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu.this.context);
                return;
            }
        } else {
            BluetoothConnectMenu.this.btDisconn();
        }
    }

    protected void onDestroy() {
        try {
            if (this.bluetoothPort.isConnected() && this.chkDisconnect.isChecked()) {
                this.unregisterReceiver(this.disconnectReceiver);
            }

            this.saveSettingFile();
            this.bluetoothPort.disconnect();
        } catch (IOException var2) {
            Log.e("BluetoothConnectMenu", var2.getMessage(), var2);
        } catch (InterruptedException var3) {
            Log.e("BluetoothConnectMenu", var3.getMessage(), var3);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
            this.hThread = null;
        }

        this.unregisterReceiver(this.searchFinish);
        this.unregisterReceiver(this.searchStart);
        this.unregisterReceiver(this.discoveryResult);
        super.onDestroy();
    }

    private void DialogReconnectionOption() {
        String[] items = new String[]{"Bluetooth printer"};
        Builder builder = new Builder(this);
        builder.setTitle("connection ...");
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).setPositiveButton("connect", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    BluetoothConnectMenu.this.btDisconn();
                    BluetoothConnectMenu.this.btConn(BluetoothConnectMenu.this.mBluetoothAdapter.getRemoteDevice(BluetoothConnectMenu.this.btAddrBox.getText().toString()));
                } catch (IllegalArgumentException var4) {
                    Log.e("BluetoothConnectMenu", var4.getMessage(), var4);
                    AlertView.showAlert(var4.getMessage(), BluetoothConnectMenu.this.context);
                } catch (IOException var5) {
                    Log.e("BluetoothConnectMenu", var5.getMessage(), var5);
                    AlertView.showAlert(var5.getMessage(), BluetoothConnectMenu.this.context);
                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                BluetoothConnectMenu.this.btDisconn();
            }
        });
        builder.show();
    }

    private void btConn(BluetoothDevice btDev) throws IOException {
        (new BluetoothConnectMenu.connTask()).execute(new BluetoothDevice[]{btDev});
    }

    private void btDisconn() {
        try {
            this.bluetoothPort.disconnect();
            if (this.chkDisconnect.isChecked()) {
                this.unregisterReceiver(this.disconnectReceiver);
            }
        } catch (Exception var2) {
            Log.e("BluetoothConnectMenu", var2.getMessage(), var2);
        }

        if (this.hThread != null && this.hThread.isAlive()) {
            this.hThread.interrupt();
        }

        this.connectButton.setText("Connect");
        this.list.setEnabled(true);
        this.btAddrBox.setEnabled(true);
        this.searchButton.setEnabled(true);
        Toast toast = Toast.makeText(this.context, "disconnect", Toast.LENGTH_SHORT);
        toast.show();
    }

    class connTask extends AsyncTask<BluetoothDevice, Void, Integer> {
        private final ProgressDialog dialog = new ProgressDialog(BluetoothConnectMenu.this, R.style.MyTheme);

        connTask() {
        }

        protected void onPreExecute() {

            this.dialog.setTitle(" Connect to printer ");
            this.dialog.setMessage("Please Wait ....");
            this.dialog.setCancelable(false);
            this.dialog.setCanceledOnTouchOutside(false);
            this.dialog.show();
            super.onPreExecute();
        }

        protected Integer doInBackground(BluetoothDevice... params) {
            Integer retVal = null;

            try {
                BluetoothConnectMenu.this.bluetoothPort.connect(params[0]);
                BluetoothConnectMenu.this.lastConnAddr = params[0].getAddress();

                retVal = 0;
            } catch (IOException var4) {
                Log.e("BluetoothConnectMenu", var4.getMessage());
                retVal = -1;
            }

            return retVal;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                RequestHandler rh = new RequestHandler();
                BluetoothConnectMenu.this.hThread = new Thread(rh);
                BluetoothConnectMenu.this.hThread.start();
                BluetoothConnectMenu.this.connectButton.setText("Connect");
                BluetoothConnectMenu.this.connectButton.setEnabled(false);
                BluetoothConnectMenu.this.list.setEnabled(false);
                BluetoothConnectMenu.this.btAddrBox.setEnabled(false);
                BluetoothConnectMenu.this.searchButton.setEnabled(false);
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }


                Toast toast = Toast.makeText(BluetoothConnectMenu.this.context, "Now Printing ", Toast.LENGTH_SHORT);
                toast.show();
                try {
//                int count = Integer.parseInt(getData);
                CPCLSample2 sample = new CPCLSample2(BluetoothConnectMenu.this);


                switch (Integer.parseInt(getData)){

                    case 0:
                        sample.selectContinuousPaper();
                        double totalQty=0.0;
                        orderedItemsB=orderedItems;
                        itemDetailB=itemDetail;
                        if(orderedItemsB.size()!=0) {
                            Bitmap bitmap1 = convertLayoutToImage_Report_titel(null, "-1");
                            sample.imageTestEnglishReport(1, bitmap1);

                            for (int i = 0; i < orderedItemsB.size(); i++) {
                                Bitmap bitmap2 = convertLayoutToImage_Report(orderedItemsB.get(i), i+"",-1,"");
                                sample.imageTestEnglishReport(1, bitmap2);

//                                totalQty+=orderedItems.get(i).getItemQty();
                            }

                            Bitmap bitmap5 = convertLayoutToImage_Report(null, "-4",-1,"");
                            sample.imageTestEnglishReport(1, bitmap5);
                            Items item=new Items();
//                            item.setItemQty((float) totalQty);
//                            Bitmap bitmap4 = convertLayoutToImage_Report(item, "-3",-1,"");
//                            sample.imageTestEnglishReport(1, bitmap4);

                            Bitmap bitmap2 = convertLayoutToImage_Report(null, "-5",0,"Total Before Tax : ");
                            sample.imageTestEnglishReport(1, bitmap2);


                            Bitmap bitmap6 = convertLayoutToImage_Report(null, "-5",1,"Tax Value : ");
                            sample.imageTestEnglishReport(1, bitmap6);

                            Bitmap bitmap7 = convertLayoutToImage_Report(null, "-5",2,"Total After Tax : ");
                            sample.imageTestEnglishReport(1, bitmap7);

                            Bitmap bitmap3 = convertLayoutToImage_Report(null, "-2",-1,"");
                            sample.imageTestEnglishReport(1, bitmap3);
                            Bitmap bitmap4 = convertLayoutToImage_Report(null, "-2",-1,"");
                            sample.imageTestEnglishReport(1, bitmap4);

                           orderedItemsB.clear();
                           itemDetailB.clear();
                          MainActivity mainActivity=new MainActivity();
                            mainActivity.clearFun();
                            mainActivity.reCalculate(BluetoothConnectMenu.this);

                        }else{
                            Toast.makeText(BluetoothConnectMenu.this, "No Item For Print ", Toast.LENGTH_SHORT).show();
                        }

                        break;

                }
                finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (BluetoothConnectMenu.this.chkDisconnect.isChecked()) {
                    BluetoothConnectMenu.this.registerReceiver(BluetoothConnectMenu.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_CONNECTED"));
                    BluetoothConnectMenu.this.registerReceiver(BluetoothConnectMenu.this.disconnectReceiver, new IntentFilter("android.bluetooth.device.action.ACL_DISCONNECTED"));
                }
            } else {
                if (this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }

                AlertView.showAlert("Disconnect Bluetoothُ", "Try Again ...", BluetoothConnectMenu.this.context);

            }

            super.onPostExecute(result);
        }

    }

    public void finishDialog(){
        finish();
    }

//    @SuppressLint("SetTextI18n")
//    private Bitmap convertLayoutToImage_shelfTag(ItemCard itemCard) {
//        LinearLayout linearView = null;
//        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
//        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog_Header.setCancelable(false);
//        dialog_Header.setContentView(R.layout.shlf_tag_dialog);
////        CompanyInfo companyInfo = obj.getAllCompanyInfo().get(0);
//
//        TextView itemName,price,BarcodeText,exp ;
//
//        LinearLayout ExpLiner,priceLiner;
//
//        ExpLiner= (LinearLayout) dialog_Header.findViewById(R.id.ExpLiner);
//        priceLiner= (LinearLayout) dialog_Header.findViewById(R.id.priceLiner);
//
//        itemName = (TextView) dialog_Header.findViewById(R.id.itemName);
//        price = (TextView) dialog_Header.findViewById(R.id.price);
//        BarcodeText=(TextView) dialog_Header.findViewById(R.id.BarcodeText);
//        exp=(TextView) dialog_Header.findViewById(R.id.exp);
//
//        ImageView barcode = (ImageView) dialog_Header.findViewById(R.id.barcodeShelf);
//
//        BarcodeText.setText(itemCard.getItemCode());
//        itemName.setText(itemCard.getItemName());
//        if(itemCard.getSalePrc().equals("**")){
//            priceLiner.setVisibility(View.INVISIBLE);
//        }else{
//            price.setText(itemCard.getSalePrc()+"JD");
//        }
//
//        if(itemCard.getDepartmentId().equals("**")){
//            ExpLiner.setVisibility(View.INVISIBLE);
//        }else{
//            exp.setText(itemCard.getDepartmentId());
//        }
//
//
//        try {
//            Bitmap bitmaps = encodeAsBitmap(itemCard.getItemCode(), BarcodeFormat.CODE_128, 300, 90);
//            barcode.setImageBitmap(bitmaps);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//
//        linearView = (LinearLayout) dialog_Header.findViewById(R.id.shelfTagLiner);
//
//        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearView.layout(1, 1, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
//
//        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());
//        dialog_Header.show();
//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();
//
//        return bit;// creates bitmap and returns the same
//
//
//    }
//
//    private Bitmap convertLayoutToImage_Barcode(ItemCard itemCard,String index) {
//        LinearLayout linearView = null;
//        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
//        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog_Header.setCancelable(false);
//        dialog_Header.setContentView(R.layout.barcode_print_dialog);
//
//
//        TextView itemName,price,BarcodeText,exp ;
//
//        LinearLayout ExpLiner,priceLiner;
//
////        ExpLiner= (LinearLayout) dialog_Header.findViewById(R.id.ExpLiner);
//        priceLiner= (LinearLayout) dialog_Header.findViewById(R.id.priceLiner);
//
//        itemName = (TextView) dialog_Header.findViewById(R.id.itemName);
//        price = (TextView) dialog_Header.findViewById(R.id.price);
//        BarcodeText=(TextView) dialog_Header.findViewById(R.id.BarcodeText);
////        exp=(TextView) dialog_Header.findViewById(R.id.exp);
//
//        ImageView barcode = (ImageView) dialog_Header.findViewById(R.id.barcodeShelf);
//
//        BarcodeText.setText(itemCard.getItemCode());
//        itemName.setText(itemCard.getItemName());
//        if(index.equals("**")){
//            priceLiner.setVisibility(View.INVISIBLE);
//        }else{
//            price.setText(itemCard.getSalePrc()+" JD");
//        }
//
////        if(itemCard.getDepartmentId().equals("**")){
////            ExpLiner.setVisibility(View.INVISIBLE);
////        }else{
////            exp.setText(itemCard.getDepartmentId());
////        }
//
//
//        try {
//            Bitmap bitmaps = encodeAsBitmap(itemCard.getItemCode(), BarcodeFormat.CODE_128, 50, 50);
//            barcode.setImageBitmap(bitmaps);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//
//
//        linearView = (LinearLayout) dialog_Header.findViewById(R.id.shelfTagLiner);
//
//        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearView.layout(1, 1, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
//
//        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());
//        dialog_Header.show();
//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        Bitmap bit =linearView.getDrawingCache();
//
//        return bit;// creates bitmap and returns the same
//
//
//    }

    private Bitmap convertLayoutToImage_Report(Items itemInfo, String index,int indexe ,String message) {
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Header.setCancelable(false);
        dialog_Header.setContentView(R.layout.row_for_print);
//        List list2 = ((List) ((ArrayList) long_listItems).clone());

        TextView itemName,price,ItemCode,Qty ;

        itemName = (TextView) dialog_Header.findViewById(R.id.itemName);
        price = (TextView) dialog_Header.findViewById(R.id.price);
        ItemCode=(TextView) dialog_Header.findViewById(R.id.itemCode);
        Qty=(TextView) dialog_Header.findViewById(R.id.qty);


        if(Integer.parseInt(index)==-1){
            ItemCode.setText("Item No");
            itemName.setText("Item Name");
            price.setText("Price");
            Qty.setText("Qty");
        }else if (Integer.parseInt(index)==-2){
                ItemCode.setText(" ");
                itemName.setText("   ");
                price.setText(" ");
                Qty.setText(" ");
        } else if (Integer.parseInt(index)==-3){
                ItemCode.setText(" ");
                itemName.setText("Total Qty =");
                price.setText(" ");
                Qty.setText(" ");

        }else if (Integer.parseInt(index)==-4) {
            ItemCode.setText("--------------------");
            itemName.setText("--------------------");
            price.setText("----------------");
            Qty.setText("----------------");
        }else if(Integer.parseInt(index)==-5){

                ItemCode.setText(message);
                itemName.setText(itemDetailB.get(indexe));
            price.setText(" ");
            Qty.setText(" ");

    }else{
            ItemCode.setText(itemInfo.getItemNo());
            itemName.setText(itemInfo.getItemName());
            price.setText(itemInfo.getPrice()+"");
            Qty.setText(itemInfo.getQty()+"");

        }




        linearView = (LinearLayout) dialog_Header.findViewById(R.id.LinerRaw);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(1, 1, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());
        dialog_Header.show();
        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

        return bit;// creates bitmap and returns the same


    }


    private Bitmap convertLayoutToImage_Report_titel(Items itemInfo, String index) {
        LinearLayout linearView = null;
        final Dialog dialog_Header = new Dialog(BluetoothConnectMenu.this);
        dialog_Header.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_Header.setCancelable(false);
        dialog_Header.setContentView(R.layout.title_for_print);


        TextView itemName,price,ItemCode,Qty,accu,date,company,taxNo,TelNo ;

        itemName = (TextView) dialog_Header.findViewById(R.id.itemName);
        price = (TextView) dialog_Header.findViewById(R.id.price);
        ItemCode=(TextView) dialog_Header.findViewById(R.id.itemCode);
        Qty=(TextView) dialog_Header.findViewById(R.id.qty);
        accu=(TextView) dialog_Header.findViewById(R.id.accu);
        date=(TextView) dialog_Header.findViewById(R.id.date);
        company=(TextView) dialog_Header.findViewById(R.id.companyName);
        taxNo=(TextView) dialog_Header.findViewById(R.id.taxNo);
        TelNo=(TextView) dialog_Header.findViewById(R.id.telNo);

        date.setText(today);

//        if(preparAc){
//            accu.setText("Not Accum");
//        }else{
//            accu.setText("Accum");
//        }


        if(Integer.parseInt(index)==-1){
            ItemCode.setText("Item No");
            itemName.setText("Item Name");
            price.setText("Price");
            Qty.setText("Qty");
        }




        linearView = (LinearLayout) dialog_Header.findViewById(R.id.LinerRaw);

        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        linearView.layout(1, 1, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());

        Log.e("size of img ", "width=" + linearView.getMeasuredWidth() + "      higth =" + linearView.getHeight());
        dialog_Header.show();
        linearView.setDrawingCacheEnabled(true);
        linearView.buildDrawingCache();
        Bitmap bit =linearView.getDrawingCache();

        return bit;// creates bitmap and returns the same


    }

//    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
//        String contentsToEncode = contents;
//        if (contentsToEncode == null) {
//            return null;
//        }
//        Map<EncodeHintType, Object> hints = null;
//        String encoding = guessAppropriateEncoding(contentsToEncode);
//        if (encoding != null) {
//            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
//            hints.put(EncodeHintType.CHARACTER_SET, encoding);
//        }
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix result;
//        try {
//            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
//        } catch (IllegalArgumentException iae) {
//            // Unsupported format
//            return null;
//        }
//        int width = result.getWidth();
//        int height = result.getHeight();
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            int offset = y * width;
//            for (int x = 0; x < width; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }

//    private static String guessAppropriateEncoding(CharSequence contents) {
//        // Very crude at the moment
//        for (int i = 0; i < contents.length(); i++) {
//            if (contents.charAt(i) > 0xFF) {
//                return "UTF-8";
//            }
//        }
//        return null;
//    }


}
