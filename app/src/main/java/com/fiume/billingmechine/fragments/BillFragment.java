package com.fiume.billingmechine.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.activity.BillDetailDialogActivity;
import com.fiume.billingmechine.adapter.BillReportAdapter;
import com.fiume.billingmechine.adapter.BillReportAdapter.OnDeleteListener;
import com.fiume.billingmechine.db.DatabaseHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Razi on 1/22/2016.
 */
public class BillFragment extends Fragment implements OnDeleteListener {

    public static Activity activity;
    public static String billId;
    private static ListView lvItems;
    private static BillReportAdapter billAdapter;
    private static DatabaseHelper dbHelper;
    private static Cursor cursorItemData;
    private static TextView tvTotalAmnt, tvTotalQty;
    private static Button btnDate;

    public BillFragment() {
        // Required empty public constructor
    }
/*

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bill_report_activity, container, false);
        dbHelper = new DatabaseHelper(getContext());
        cursorItemData = dbHelper.getBillHeaderData();
        billAdapter = new BillReportAdapter(getActivity().getApplicationContext(), cursorItemData);
        billAdapter.setOnItemDeleteListener(this);

        lvItems = (ListView) rootView.findViewById(R.id.lv_items);
        lvItems.setAdapter(billAdapter);

        btnDate = (Button) rootView.findViewById(R.id.btn_date);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
            }
        });

        tvTotalAmnt = (TextView) rootView.findViewById(R.id.tv_total);
        if (dbHelper.getBillHeaderData().getCount() > 0)
            tvTotalAmnt.setText(String.valueOf(dbHelper.getTotalBIllAmnt()));//%.2f
        else tvTotalAmnt.setText("0.00");

        tvTotalQty = (TextView) rootView.findViewById(R.id.tv_tot_qty);
        if (dbHelper.getBillHeaderData().getCount() > 0)
            tvTotalQty.setText(String.valueOf(dbHelper.getTotalBIllQty()));
        else tvTotalQty.setText("0.0");

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvId = (TextView) view.findViewById(R.id.tv_bill_no);
                billId = tvId.getText().toString().trim();
                Intent intent = new Intent(getActivity(), BillDetailDialogActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void deletItem(final String id) {

      /*  String Id;
        if (id.length() == 1) Id = "00" + id;
        else if (id.length() == 2) Id = "0" + id;
        else Id = id;*/

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        AlertDialog dialog = builder.setMessage("Do you want to delete Bill - " + id).
                setPositiveButton(" YES ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteBillHeaderData(id);
                        dbHelper.deleteBillDetailData(id);
                        cursorItemData = dbHelper.getBillHeaderData();
                        billAdapter = new BillReportAdapter(getActivity().getApplicationContext(), cursorItemData);
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

    @Override
    public void onDeleteListener(String id) {
        deletItem(id);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            //String mt = c.get(Calendar.)
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // displayCurrentTime.setText("Selected date: " + String.valueOf(year) + " - " + String.valueOf(month) + " - " + String.valueOf(day));
            String mnth;
            if (String.valueOf(month).length() == 1) mnth = "0" + String.valueOf(month + 1);
            else mnth = String.valueOf(month + 1);

            @SuppressLint("SimpleDateFormat") DateFormat inputFormatter1 = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = null;
            try {
                date1 = inputFormatter1.parse(String.valueOf(day) + "-" + mnth + "-" + String.valueOf(year));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            @SuppressLint("SimpleDateFormat") DateFormat outputFormatter1 = new SimpleDateFormat("MMM dd,yyyy");
            String filterDate = outputFormatter1.format(date1); //


            btnDate.setText(String.valueOf(day) + "-" + mnth + "-" + String.valueOf(year));
            cursorItemData = dbHelper.getDateFilterBill(filterDate);
            // cursorItemData = dbHelper.getDateFilterBill(String.valueOf(day)+ "-" + mnth + "-" +String.valueOf(year));  //dd-mm-yyyy
            // Log.d("cursorItemData", String.valueOf(cursorItemData.getCount()));
            billAdapter = new BillReportAdapter(getActivity().getApplicationContext(), cursorItemData);
            lvItems.setAdapter(billAdapter);
            tvTotalAmnt.setText(String.valueOf(dbHelper.getTotalBIllAmntByDate(filterDate)));
            tvTotalQty.setText(String.valueOf(dbHelper.getTotalBIllQtyByDate(filterDate)));
            // billAdapter.notifyDataSetChanged();
        }
    }
}
