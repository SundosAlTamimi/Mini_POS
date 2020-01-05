package com.falconssoft.minipos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.falconssoft.minipos.Modle.Settings;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "MiniBOSDatabase";
    static SQLiteDatabase db;

    //******************************************************************
    private static final String SETTINGS_TABLE = "SETTINGS";

    private static final String SETTINGS_COMPANY_NAME = "COMPANY_NAME";
    private static final String SETTINGS_IP_ADDRESS = "IP_ADDRESS";
    private static final String SETTINGS_THEME_NO = "THEME_NO";
    private static final String SETTINGS_CONTROL_PRICE = "CONTROL_PRICE";
    private static final String SETTINGS_CONTROL_QTY = "CONTROL_QTY";
    private static final String SETTINGS_COMPANY_ID = "COMPANY_ID";
    private static final String SETTINGS_TAX_CALC_KIND = "TAX_CALC_KIND";
    private static final String SETTINGS_POS_NO = "POS_NO";

    //******************************************************************

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_SETTINGS = "CREATE TABLE " + SETTINGS_TABLE + "("
                + SETTINGS_COMPANY_NAME + " TEXT,"
                + SETTINGS_IP_ADDRESS + " TEXT,"
                + SETTINGS_THEME_NO + " INTEGER,"
                + SETTINGS_CONTROL_PRICE + " TEXT,"
                + SETTINGS_CONTROL_QTY + " TEXT,"
                + SETTINGS_COMPANY_ID + " TEXT,"
                + SETTINGS_TAX_CALC_KIND + " TEXT,"
                + SETTINGS_POS_NO + " TEXT" + ")";
        db.execSQL(CREATE_TABLE_SETTINGS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            db.execSQL("ALTER TABLE SETTINGS ADD " + SETTINGS_POS_NO + " TEXT");
        }catch (Exception e){
            Log.e("upgrade", "SETTINGS");
        }

        try {
            db.execSQL("ALTER TABLE SETTINGS ADD " + SETTINGS_TAX_CALC_KIND + " TEXT");
        }catch (Exception e){
            Log.e("upgrade", "SETTINGS");
        }

    }

    // **************************************************** Adding ****************************************************

    public void addSettings(Settings settings) {
        db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SETTINGS_COMPANY_NAME, settings.getCompanyName());
        contentValues.put(SETTINGS_IP_ADDRESS, settings.getIpAddress());
        contentValues.put(SETTINGS_THEME_NO, settings.getThemeNo());
        contentValues.put(SETTINGS_CONTROL_PRICE, settings.getControlPrice());
        contentValues.put(SETTINGS_CONTROL_QTY, settings.getControlQty());
        contentValues.put(SETTINGS_COMPANY_ID, settings.getCompanyID());
        contentValues.put(SETTINGS_TAX_CALC_KIND, settings.getTaxCalcKind());
        contentValues.put(SETTINGS_POS_NO, settings.getPosNo());

        db.insert(SETTINGS_TABLE, null, contentValues);
        db.close();
    }


    // **************************************************** Getting ****************************************************

    public Settings getSettings() {
        Settings settings = null;
        String selectQuery = "SELECT * FROM " + SETTINGS_TABLE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                settings = new Settings();
                settings.setCompanyName(cursor.getString(0));
                settings.setIpAddress(cursor.getString(1));
                settings.setThemeNo(cursor.getInt(2));
                settings.setControlPrice(cursor.getInt(3));
                settings.setControlQty(cursor.getInt(4));
                settings.setCompanyID(cursor.getString(5));
                settings.setTaxCalcKind(cursor.getInt(6));
                settings.setPosNo(cursor.getString(7));

            } while (cursor.moveToNext());
        }
        return settings;
    }

    // **************************************************** Update ****************************************************

    public void updateSettings(Settings settings) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SETTINGS_IP_ADDRESS, settings.getIpAddress());
        values.put(SETTINGS_COMPANY_NAME, settings.getCompanyName());
        values.put(SETTINGS_CONTROL_PRICE, settings.getControlPrice());
        values.put(SETTINGS_CONTROL_QTY, settings.getControlQty());
        values.put(SETTINGS_COMPANY_ID, settings.getCompanyID());
        values.put(SETTINGS_TAX_CALC_KIND, settings.getTaxCalcKind());
        values.put(SETTINGS_POS_NO, settings.getPosNo());

        db.update(SETTINGS_TABLE, values, null, null);
    }

    public void updateTheme(int themeNo) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SETTINGS_THEME_NO, themeNo);
        db.update(SETTINGS_TABLE, values, null, null);
    }


    // **************************************************** Delete ****************************************************

    public void deleteSettings() {
        db = this.getWritableDatabase();
        db.delete(SETTINGS_TABLE, null, null);
        db.close();
    }

}
