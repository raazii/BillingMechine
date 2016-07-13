package com.fiume.billingmechine.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.db.DatabaseHelper;

/**
 * Created by Razi on 12/15/2015.
 */
public class ProductsListAdapter extends CursorAdapter {
    private Context context;
    DatabaseHelper databaseHelper;
    Cursor cursorDb;
    String[] srlNo;
    private OnDeleteListener mDeleteListener;

    @SuppressWarnings("deprecation")
    public ProductsListAdapter(Context context, Cursor c) {
        super(context, c);
    }

    public void setOnItemDeleteListener(OnDeleteListener mListener) {
        this.mDeleteListener = mListener;
    }


    @Override
    public void bindView(View convertView, final Context context,final Cursor cursor) {

        Holder holder = new Holder();

        holder.tvId = (TextView) convertView
                .findViewById(R.id.tv_Id);
        holder.tvItemName = (TextView) convertView
                .findViewById(R.id.tv_iname);
        holder.tvICode = (TextView) convertView.findViewById(R.id.tv_icode);
        holder.tvIdisName = (TextView) convertView
                .findViewById(R.id.tv_idname);
        holder.tvIPrice = (TextView) convertView
                .findViewById(R.id.tv_iprice);
        holder.tvICreatedAt = (TextView) convertView.findViewById(R.id.tv_icreated);
        holder.tvIColor = (TextView) convertView.findViewById(R.id.tv_icolor);
        holder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
        holder.tvArabicName = (TextView) convertView
                .findViewById(R.id.tv_arabic_name);


        holder.tvId.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(0))));
        holder.tvICode.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(1))));
        holder.tvItemName.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(2))));
        holder.tvArabicName.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(4))));
        holder.tvIdisName.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(3))));
        holder.tvIPrice.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(5))));
        holder.tvIColor.setBackgroundColor(Color.parseColor(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(6)))));
        holder.tvIColor.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(6))));
        holder.tvICreatedAt.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(7))));
      /*  holder.tvIColor.setText(cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(3))));*/

        final String iId = cursor.getString(cursor.getColumnIndex(cursor
                .getColumnName(0)));
        final String iName = cursor.getString(cursor.getColumnIndex("i_name"));

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   ((ProductsActivity) ProductsActivity.activity).deletItem(iId, iName);
                mDeleteListener.onDeleteListener(iId,iName);
               // notifyDataSetChanged();
            }
        });


    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
        context = arg0;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.item_adapter_settings, parent,
                false);

        return retView;
    }

    static class Holder {

        TextView tvId, tvItemName,tvArabicName, tvICode,tvIdisName,tvIPrice,tvICreatedAt,tvIColor;
        ImageButton ibDelete;

    }
    public interface OnDeleteListener {
        public void onDeleteListener(String id, String title);
    }



}
