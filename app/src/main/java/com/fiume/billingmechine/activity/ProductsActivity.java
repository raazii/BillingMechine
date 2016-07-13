package com.fiume.billingmechine.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.adapter.ProductsListAdapter;
import com.fiume.billingmechine.db.DatabaseHelper;

/**
 * Created by Razi on 12/31/2015.
 */
public class ProductsActivity extends AppCompatActivity {

    private FloatingActionButton btnAddNew;
    private ListView lvItems;
    private ProductsListAdapter itemsAdapter;
    private DatabaseHelper dbHelper;
    private static Activity activity;
    private Cursor cursorItemData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.products_activity);
        activity = this;
        dbHelper = new DatabaseHelper(this);
        cursorItemData = dbHelper.getItemsData();
        itemsAdapter = new ProductsListAdapter(getApplicationContext(), cursorItemData);

        lvItems = (ListView) findViewById(R.id.lv_items);
        lvItems.setAdapter(itemsAdapter);
        btnAddNew = (FloatingActionButton) findViewById(R.id.fab);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        ProductsActivity.this, AddItemDialogActivity.class);
                intent.putExtra("flag", "new");
                startActivity(intent);
            }
        });


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView tvId = (TextView) view.findViewById(R.id.tv_Id);
                TextView tvICode = (TextView) view.findViewById(R.id.tv_icode);
                TextView tvIname = (TextView) view.findViewById(R.id.tv_iname);
               // TextView tvAraname = (TextView) view.findViewById(R.id.tv_arabic_name);
                TextView tvIdName = (TextView) view.findViewById(R.id.tv_idname);
                TextView tvIprice = (TextView) view.findViewById(R.id.tv_iprice);
                // TextView tvIcre = (TextView) view.findViewById(R.id.tv_Id);
                TextView tvIcolor = (TextView) view.findViewById(R.id.tv_icolor);

                Intent intent = new Intent(
                        ProductsActivity.this, AddItemDialogActivity.class);
                intent.putExtra("flag", "edit");
                intent.putExtra("id", tvId.getText().toString().trim());
                intent.putExtra("code", tvICode.getText().toString());
                intent.putExtra("name", tvIname.getText().toString());
                intent.putExtra("disname", tvIdName.getText().toString());
                intent.putExtra("price", tvIprice.getText().toString());
                intent.putExtra("color", tvIcolor.getText().toString());
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursorItemData = dbHelper.getItemsData();
        itemsAdapter = new ProductsListAdapter(getApplicationContext(), cursorItemData);
        // itemsAdapter.notifyDataSetChanged();
        lvItems.setAdapter(itemsAdapter);
        lvItems.smoothScrollToPosition(lvItems.getCount());

    }

    public void deletItem(final String id,final String title) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        AlertDialog dialog =  builder.setMessage("Do you want to delete " + title).
                setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteItemsData(id);
                        cursorItemData = dbHelper.getItemsData();
                        itemsAdapter = new ProductsListAdapter(getApplicationContext(), cursorItemData);
                        lvItems.setAdapter(itemsAdapter);
                    }
                }).setNegativeButton(" NO ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();

        //dialog.getWindow().setLayout(400, 200);
        dialog.show();
    }

}
