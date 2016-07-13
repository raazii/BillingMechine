package com.fiume.billingmechine.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.adapter.BillReportAdapter;
import com.fiume.billingmechine.db.DatabaseHelper;

/**
 * Created by Razi on 12/31/2015.
 */
public class BillReportActivity extends AppCompatActivity {

    private static Activity activity;
    private ListView lvItems;
    private BillReportAdapter billAdapter;
    private DatabaseHelper dbHelper;
    private Cursor cursorItemData;
    private TextView tvTotalAmnt;
    private TextView tvTotalQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.bill_report_activity);

        activity = this;
        dbHelper = new DatabaseHelper(this);
        cursorItemData = dbHelper.getBillHeaderData();
        billAdapter = new BillReportAdapter(getApplicationContext(), cursorItemData);

        lvItems = (ListView) findViewById(R.id.lv_items);
        lvItems.setAdapter(billAdapter);

        tvTotalAmnt = (TextView) findViewById(R.id.tv_total);
        tvTotalAmnt.setText(String.valueOf(dbHelper.getTotalBIllAmnt()));

        tvTotalQty = (TextView) findViewById(R.id.tv_tot_qty);
        tvTotalQty.setText(String.valueOf(dbHelper.getTotalBIllQty()));

    }

    public void deletItem(final String id) {

        String Id;
        if (id.length() == 1) Id = "00" + id;
        else if (id.length() == 2) Id = "0" + id;
        else Id = id;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        AlertDialog dialog = builder.setMessage("Do you want to delete Bill - " + Id).
                setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteBillHeaderData(id);
                        dbHelper.deleteBillDetailData(id);
                        cursorItemData = dbHelper.getBillHeaderData();
                        billAdapter = new BillReportAdapter(getApplicationContext(), cursorItemData);
                        lvItems.setAdapter(billAdapter);
                    }
                }).setNegativeButton(" NO ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();

        dialog.getWindow().setLayout(400, 150);
        dialog.show();
    }

}
