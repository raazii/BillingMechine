package com.fiume.billingmechine.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ListView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.adapter.BillReportAdapter;
import com.fiume.billingmechine.adapter.ProductReportAdapter;
import com.fiume.billingmechine.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Razi on 3/25/2016.
 */
public class ReportsFragment extends Fragment {

    public static Activity activity;
    static Cursor cursorItemData;
    private static BillReportAdapter billAdapter;
    private static DatabaseHelper dbHelper;
    private static ListView productList;
    private static ProductReportAdapter listAdapter;
    private FloatingActionButton fabFilter;


    public ReportsFragment() {
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
        View rootView = inflater.inflate(R.layout.product_report_fragment, container, false);
        dbHelper = new DatabaseHelper(getContext());

        Calendar newCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        //String filterDate = df.format(newCalendar.getTime());

        listAdapter = new ProductReportAdapter(getActivity(), dbHelper.getProductsReport());

        productList = (ListView) rootView.findViewById(R.id.listView);
        productList.setAdapter(listAdapter);

        fabFilter = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment mDatePicker = new DatePickerFragment();
                mDatePicker.show(getFragmentManager(), "Select date");
            }
        });

        return rootView;
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
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

            listAdapter = new ProductReportAdapter(getActivity(), dbHelper.getProductsReportByDate(String.valueOf(day) + "-" + mnth + "-" + String.valueOf(year)));
            productList.setAdapter(listAdapter);

        }
    }
}
