package com.fiume.billingmechine.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fiume.billingmechine.model.BillModel;
import com.fiume.billingmechine.model.ProductReportModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Razi on 12/15/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3; //old version = 2
    private static final String DATABASE_NAME = "BilllingMechineDB";
    private List<BillModel> billList;
    private List<ProductReportModel> productReportlList;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String itemTableQry = "create table items_table ( _id integer primary key AUTOINCREMENT, i_code text NOT NULL, i_name text NOT NULL, i_d_name text, arabic_name text, i_price text not null, i_color text, i_created_at DATETIME DEFAULT CURRENT_TIMESTAMP );";
        String billHeaderQry = "create table bill_header_table ( _id integer primary key AUTOINCREMENT, b_created_at DATETIME DEFAULT CURRENT_TIMESTAMP, b_total_amnt text, b_total_item text );";
        String billDetailQry = "create table bill_detail_table ( _id integer primary key AUTOINCREMENT, b_h_id integer NOT NULL, b_i_id integer NOT NULL, i_name text NOT NULL, arabic_name text, i_price text not null, b_total_price text, b_qty text );";
        db.execSQL(billHeaderQry);
        db.execSQL(itemTableQry);
        db.execSQL(billDetailQry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // If you need to add a column
        if (newVersion > oldVersion) {
//            db.execSQL("ALTER TABLE bill_header_table ADD COLUMN filter_date text NOT NULL DEFAULT 0"); //v2.0
            db.execSQL("ALTER TABLE bill_detail_table ADD COLUMN filter_date text NOT NULL DEFAULT 0");   //v3.0
        }

    }

    public void insertItemData(String iCode, String iName, String iDispName, String arabicName, String iPrice, String iColor, String reminderTime) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("i_code", iCode);
        values.put("i_name", iName);
        values.put("i_d_name", iDispName);
        values.put("arabic_name", arabicName);
        values.put("i_price", iPrice);
        values.put("i_color", iColor);
        values.put("i_created_at", reminderTime);
        db.insert("items_table", null, values);

        db.close();
    }

    public void updateItemData(String id, String iCode, String iName, String iDispName, String arabicName, String iPrice, String iColor, String reminderTime) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("i_code", iCode);
        values.put("i_name", iName);
        values.put("i_d_name", iDispName);
        values.put("arabic_name", arabicName);
        values.put("i_price", iPrice);
        values.put("i_color", iColor);
        values.put("i_created_at", reminderTime);
        db.update("items_table", values, "_id = " + id, null);

        db.close();
    }

    public void insertBillDetailData(String bHeaderID, String bItemId, String iName, String arName, String iPrice, String bTotalPrice, String bTotalQty, String filterDate) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("b_h_id", bHeaderID);
        values.put("b_i_id", bItemId);
        values.put("i_name", iName);
        values.put("arabic_name", arName);
        values.put("i_price", iPrice);
        values.put("b_total_price", bTotalPrice);
        values.put("b_qty", bTotalQty);
        values.put("filter_date", filterDate);

        db.insert("bill_detail_table", null, values);

        db.close();
    }

    public void insertBillHeaderData(String createdAt, String totalAmnt, String totalItems, String filterDate) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("b_created_at", createdAt);
        values.put("b_total_amnt", totalAmnt);
        values.put("b_total_item", totalItems);
        values.put("filter_date", filterDate);

        db.insert("bill_header_table", null, values);

        db.close();
    }

    //GET PRODUCTS
    public Cursor getItemsData() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from items_table;";

        return db.rawQuery(query, null);
    }

    // GET BILL BY DATE
    public Cursor getDateFilterBill(String date) {
        SQLiteDatabase db = getReadableDatabase();
        // String query = "select * from bill_header_table where filter_date ='" + date + "';";
        String query = "select * from bill_header_table where b_created_at like '%" + date + "%'";

        return db.rawQuery(query, null);
    }

    // GET BILL HEADER
    public Cursor getBillHeaderData() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from bill_header_table order by _id desc;";
        return db.rawQuery(query, null);
    }

    // DELETE PRODUCTS
    public void deleteItemsData(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "delete from items_table where _id ='" + id + "';";
        db.execSQL(query);
    }

    // DELETE BILL HEADER
    public void deleteBillHeaderData(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "delete from bill_header_table where _id ='" + id + "';"; // order by _id desc
        db.execSQL(query);
    }

    // DELETE BILL DETAILS
    public void deleteBillDetailData(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "delete from bill_detail_table where b_h_id ='" + id + "';";
        db.execSQL(query);
    }

    /// GET PRODUCTS ID ONLY
    private ArrayList<String> getProductIds() {
        // SQLiteDatabase db = getReadableDatabase();
        String query = "select i_name from items_table;";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        cursor.moveToFirst();
        ArrayList<String> names = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("i_name")));
            cursor.moveToNext();
        }
        cursor.close();

        names.add("half rial");
        names.add("one rial");
        names.add("one and half rial");
        names.add("two rial");
        names.add("three rial");
        names.add("five rial");
        names.add("ten rial");

        return names;
    }

    public List<ProductReportModel> getProductsReportByDate(String date) {
        SQLiteDatabase db = getReadableDatabase();
        productReportlList = new ArrayList<>();
        for (String item : getProductIds()) {
            String query = "select i_name, i_price, sum (b_qty) as qty, sum (b_total_price) as total from bill_detail_table where i_name ='" + item + "' and filter_date ='" + date + "';";
            Cursor cQry = db.rawQuery(query, null);
            if (cQry != null && cQry.getCount() > 0) {
                while (cQry.moveToNext()) {
                    ProductReportModel itemModel = new ProductReportModel();
                    itemModel.setpName(item);
                    if (String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(1)))).equals("null"))
                        itemModel.setpPrice("0.00");
                    else
                        itemModel.setpPrice(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(1)))));
                    if (String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(2)))).equals("null"))
                        itemModel.setpSaledQty("0");
                    else
                        itemModel.setpSaledQty(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(2)))));
                    if (String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(3)))).equals("null"))
                        itemModel.setPtotalSaledPrice("0.0");
                    else
                        itemModel.setPtotalSaledPrice(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(3)))));

                    productReportlList.add(itemModel);
                }
                cQry.close();
            }
        }

        return productReportlList;
    }

    // GET PRODUCTS REPORT BY ID
    public List<ProductReportModel> getProductsReport() {
        SQLiteDatabase db = getReadableDatabase();
        productReportlList = new ArrayList<>();

        for (String item : getProductIds()) {
            String query = "select i_name, i_price, sum (b_qty) as qty, sum (b_total_price) as total from bill_detail_table where i_name ='" + item + "';";
            Cursor cQry = db.rawQuery(query, null);
            if (cQry != null && cQry.getCount() > 0) {
                while (cQry.moveToNext()) {
                    ProductReportModel itemModel = new ProductReportModel();
                    itemModel.setpName(cQry.getString(cQry.getColumnIndex(cQry
                            .getColumnName(0))));
                    itemModel.setpPrice(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(1)))));
                    itemModel.setpSaledQty(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(2)))));
                    itemModel.setPtotalSaledPrice(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(3)))));

                    productReportlList.add(itemModel);
                }
                cQry.close();
            }
        }
        return productReportlList;

    }

    // GET BILL DETAILS
    public List<BillModel> getBillDetailData(String id) {
        billList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String qry = "select * from bill_detail_table where b_h_id = '" + id + "';";
        Cursor cQry = db.rawQuery(qry, null);
        if (cQry != null && cQry.getCount() > 0) {
            while (cQry.moveToNext()) {
                BillModel itemModel = new BillModel();
                itemModel.setId(cQry.getString(cQry.getColumnIndex(cQry
                        .getColumnName(0))));
                itemModel.setName(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(3)))));
                itemModel.setArName(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(4)))));
                itemModel.setUnitPrice(String.valueOf(cQry.getString(cQry.getColumnIndex(cQry.getColumnName(5)))));

                itemModel.setPrice(cQry.getString(cQry.getColumnIndex(cQry
                        .getColumnName(6))));
                itemModel.setQty(cQry.getString(cQry.getColumnIndex(cQry
                        .getColumnName(7))));

                billList.add(itemModel);
            }
            cQry.close();
        }
        return billList;
    }

    public String getTotalAmntofBill(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select b_total_amnt from bill_header_table where _id = '" + id + "';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String total = String.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
        c.close();
        return total;
    }

    public String getTotalQtyofBill(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select b_total_item from bill_header_table where _id = '" + id + "';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String total = String.valueOf(c.getString(c.getColumnIndex(c.getColumnName(0))));
        c.close();
        return total;
    }

    public int getLastBillId() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select _id from bill_header_table order by  _id desc limit 1;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int id = Integer.parseInt(c.getString(c.getColumnIndex(c.getColumnName(0))));
        c.close();
        return id;
    }

    public Double getTotalBIllAmnt() {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(b_total_amnt) FROM bill_header_table;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String total = c.getString(c.getColumnIndex(c.getColumnName(0)));
        c.close();
        if (total.length() > 0)
            return Double.parseDouble(total + "0");
        else return 0.00;
    }

    public Double getTotalBIllAmntByDate(String date) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(b_total_amnt) FROM bill_header_table where b_created_at like '%" + date + "%'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String total = c.getString(c.getColumnIndex(c.getColumnName(0)));
        c.close();
        if (total.length() > 0)
            return Double.parseDouble(total + "0");
        else return 0.00;
    }

    public Double getTotalBIllQty() {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(b_total_item) FROM bill_header_table;";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getString(c.getColumnIndex(c.getColumnName(0))).length() > 0)
            return Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(0))));
        else return 0.0;
    }

    public Double getTotalBIllQtyByDate(String date) {

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(b_total_item) FROM bill_header_table where b_created_at like '%" + date + "%'";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getString(c.getColumnIndex(c.getColumnName(0))).length() > 0)
            return Double.parseDouble(c.getString(c.getColumnIndex(c.getColumnName(0))));
        else return 0.0;
    }

}
