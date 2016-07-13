package com.fiume.billingmechine.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.fiume.billingmechine.R;

/**
 * Created by Razi on 12/15/2015.
 */
public class MainGridAdapter extends CursorAdapter {
    private Context context;

    @SuppressWarnings("deprecation")
    public MainGridAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {

        Holder holder = new Holder();

        holder.tvTitle = (TextView) convertView
                .findViewById(R.id.btn_item);
        holder.tvId = (TextView) convertView.findViewById(R.id.tv_id);
        holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
        holder.tvArName = (TextView) convertView.findViewById(R.id.tv_arabic_name);


      /*  holder.tvIColor.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(3))));*/
        if (cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#dd4f49")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c1);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#491996")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c2);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#544e4b")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c3);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#ed1846")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c4);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#40993a")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c5);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#3d70a3")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c6);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#d3236d")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c7);
        }else if(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6))).equals("#006a62")){
            holder.tvTitle.setBackgroundResource(R.drawable.item_bgrd_c8);
        }

        holder.tvTitle.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(3))).toUpperCase());
        holder.tvId.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(0))));
        holder.tvPrice.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(5))));
        holder.tvName.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(2))));
        holder.tvArName.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(4))));



    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
        context = arg0;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.main_grid_adapter_btn_layout, parent,
                false);

        return retView;
    }

    static class Holder {

        Button btnItem;
        TextView tvTitle,tvId,tvPrice,tvName,tvArName;

    }
}
