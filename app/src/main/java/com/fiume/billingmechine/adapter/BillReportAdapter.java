package com.fiume.billingmechine.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.db.DatabaseHelper;

/**
 * Created by Razi on 12/31/2015.
 */
public class BillReportAdapter extends CursorAdapter {
    private Context context;
    DatabaseHelper databaseHelper;
    Cursor cursorDb;
    String[] srlNo;
    private OnDeleteListener mDeleteListener;

    @SuppressWarnings("deprecation")
    public BillReportAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public void setOnItemDeleteListener(OnDeleteListener mListener) {
        this.mDeleteListener = mListener;
    }


    @Override
    public void bindView(View convertView, Context context, final Cursor cursor) {

        Holder holder = new Holder();

        holder.tvBillNO = (TextView) convertView
                .findViewById(R.id.tv_bill_no);
        holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
        holder.tvTotalQty = (TextView) convertView.findViewById(R.id.tv_total_qty);
        holder.tvTotalAmnt = (TextView) convertView.findViewById(R.id.tv_total_amnt);

        holder.btnDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);

        /*if(cursor.getString(cursor.getColumnIndex("_id")).length() == 1){
            holder.tvBillNO.setText("00"+cursor.getString(cursor.getColumnIndex(cursor
                    .getColumnName(0))));
        }else if(cursor.getString(cursor.getColumnIndex("_id")).length() == 2){
            holder.tvBillNO.setText("0"+cursor.getString(cursor.getColumnIndex(cursor
                    .getColumnName(0))));
        }else{*/
        holder.tvBillNO.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(0))));
        // }
        holder.tvTime.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(1))));
        holder.tvTotalQty.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(3))) + " ");
        holder.tvTotalAmnt.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(2))));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String iId = cursor.getString(cursor.getColumnIndex("_id"));
                // ((BillReportActivity) BillReportActivity.activity).deletItem(iId);
                mDeleteListener.onDeleteListener(iId);
            }
        });


    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
        context = arg0;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.bill_head_report_adapter, parent,
                false);

        return retView;
    }

    public interface OnDeleteListener {
        public void onDeleteListener(String id);
    }

    static class Holder {

        ImageButton btnDelete;
        TextView tvBillNO, tvTime, tvTotalQty, tvTotalAmnt;

    }

}
