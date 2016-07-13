package com.fiume.billingmechine.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.activity.AddItemDialogActivity;
import com.fiume.billingmechine.adapter.ProductsListAdapter;
import com.fiume.billingmechine.db.DatabaseHelper;

/**
 * Created by Razi on 1/22/2016.
 */
public class ProductFragment extends Fragment implements ProductsListAdapter.OnDeleteListener {

    private static Activity activity;
    private FloatingActionButton btnAddNew;
    private ListView lvItems;
    private ProductsListAdapter itemsAdapter;
    private DatabaseHelper dbHelper;
    private Cursor cursorItemData;


    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        dbHelper = new DatabaseHelper(getContext());
        cursorItemData = dbHelper.getItemsData();
        itemsAdapter = new ProductsListAdapter(getContext(), cursorItemData);
        itemsAdapter.setOnItemDeleteListener(this);
        lvItems.setAdapter(itemsAdapter);
    }

 /*   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.products_activity, container, false);

        lvItems = (ListView) rootView.findViewById(R.id.lv_items);
        btnAddNew = (FloatingActionButton) rootView.findViewById(R.id.fab);

        activity = getActivity();
        dbHelper = new DatabaseHelper(getContext());
        cursorItemData = dbHelper.getItemsData();
        itemsAdapter = new ProductsListAdapter(getContext(), cursorItemData);
        itemsAdapter.setOnItemDeleteListener(this);


        lvItems.setAdapter(itemsAdapter);


        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        getActivity(), AddItemDialogActivity.class);
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
                TextView tvAraname = (TextView) view.findViewById(R.id.tv_arabic_name);
                TextView tvIdName = (TextView) view.findViewById(R.id.tv_idname);
                TextView tvIprice = (TextView) view.findViewById(R.id.tv_iprice);
                // TextView tvIcre = (TextView) view.findViewById(R.id.tv_Id);
                TextView tvIcolor = (TextView) view.findViewById(R.id.tv_icolor);

                Intent intent = new Intent(
                        getActivity(), AddItemDialogActivity.class);
                intent.putExtra("flag", "edit");
                intent.putExtra("id", tvId.getText().toString().trim());
                intent.putExtra("code", tvICode.getText().toString());
                intent.putExtra("name", tvIname.getText().toString());
                intent.putExtra("disname", tvIdName.getText().toString());
                intent.putExtra("price", tvIprice.getText().toString());
                intent.putExtra("color", tvIcolor.getText().toString());
                intent.putExtra("arName", tvAraname.getText().toString());
                startActivity(intent);

            }
        });

        return rootView;
    }


    @Override
    public void onDeleteListener(String id, String title) {
        deletItem(id,title);
    }

    private void deletItem(final String id, final String title) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        AlertDialog dialog =  builder.setMessage("Do you want to delete " + title).
                setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteItemsData(id);
                        cursorItemData = dbHelper.getItemsData();
                        itemsAdapter = new ProductsListAdapter(getActivity().getApplicationContext(), cursorItemData);
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
