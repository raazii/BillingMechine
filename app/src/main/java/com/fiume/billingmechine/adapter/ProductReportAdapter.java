package com.fiume.billingmechine.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.model.ProductReportModel;

import java.util.List;

/**
 * Created by Razi on 13-Jun-16.
 */
public class ProductReportAdapter extends BaseAdapter {
    private final Activity activity;
    private final List<ProductReportModel> prdctList;
    private LayoutInflater inflater;

    public ProductReportAdapter(Activity activity,
                                List<ProductReportModel> prdctList) {
        this.activity = activity;
        this.prdctList = prdctList;
    }


    @Override
    public int getCount() {
        return prdctList.size();
    }

    @Override
    public Object getItem(int i) {
        return prdctList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        Holder holder = new Holder();

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.product_report_adapter,
                    null);//null

        holder.tvPName = (TextView) convertView
                .findViewById(R.id.tv_prdct_name);
        holder.tvPPrice = (TextView) convertView
                .findViewById(R.id.tv_prdct_price);
        holder.tvPTotQty = (TextView) convertView.findViewById(R.id.tv_prdct_qty);
        holder.tvPTotAmnt = (TextView) convertView
                .findViewById(R.id.tv_total_amnt);

        ProductReportModel reportModel = prdctList.get(position);

        holder.tvPName.setText(reportModel.getpName());
        holder.tvPPrice.setText(reportModel.getpPrice());
        holder.tvPTotQty.setText(reportModel.getpSaledQty());
        if (reportModel.getPtotalSaledPrice().contains("."))
            holder.tvPTotAmnt.setText(reportModel.getPtotalSaledPrice() + "0");
        else holder.tvPTotAmnt.setText(reportModel.getPtotalSaledPrice() + ".00");


        return convertView;
    }

    class Holder {
        TextView tvPName, tvPPrice, tvPTotQty, tvPTotAmnt;
    }
}