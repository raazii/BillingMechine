package com.fiume.billingmechine.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.model.BillModel;

import java.util.List;

/**
 * Created by Razi on 12/15/2015.
 */
public class BillListAdapter extends BaseAdapter {
    private static List<BillModel> billItems;
    Context mContext;
    private final boolean firstClick;
    int lastPosition;
    private final Activity activity;
    private LayoutInflater inflater;
    private OnDeleteListener mDeleteListener;


    public BillListAdapter(Activity activity,
                           List<BillModel> salesItems, boolean firstClick) {
        this.activity = activity;
        billItems = salesItems;
        this.firstClick = firstClick;
    }

    @Override
    public int getCount() {
        return billItems.size();
    }

    @Override
    public Object getItem(int location) {
        return billItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemDeleteListener(OnDeleteListener mListener) {
        this.mDeleteListener = mListener;
    }




    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.bill_adapter, null);

        holder.tvItemId = (TextView) convertView
                .findViewById(R.id.tv_id);
        holder.etPrice = (TextView) convertView.findViewById(R.id.tv_price);
        holder.etQty = (TextView) convertView.findViewById(R.id.et_qty);
        holder.tvItemName = (TextView) convertView.findViewById(R.id.tv_name);
        holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
        holder.etUnitPrice = (TextView) convertView.findViewById(R.id.tv_unit_price);

        holder.etQty.setInputType(InputType.TYPE_NULL);
        holder.etQty.setTag(position);
        holder.ibDelete.setTag(position);

        BillModel item = billItems.get(position);

        holder.etPrice.setText(String.valueOf(item.getPrice()));
        holder.etQty.setText(String.valueOf(item.getQty()));
        holder.tvItemName.setText(item.getName());
        holder.tvItemId.setText(String.valueOf(position + 1));
        holder.etUnitPrice.setText(String.valueOf(item.getUnitPrice()));


        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // int pos = (Integer) v.getTag();
                mDeleteListener.onDeleteListener(position);
                notifyDataSetChanged();
            }
        });

        convertView.setTag(holder);

        return convertView;
    }

    public interface OnDeleteListener {
        public void onDeleteListener(int position);
    }



    public class Holder {
        TextView tvItemId, tvItemName, etUnitPrice, etPrice,etQty;
        ImageButton ibDelete;

    }

}