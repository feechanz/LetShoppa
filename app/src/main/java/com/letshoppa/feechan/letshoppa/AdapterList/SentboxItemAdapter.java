package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Pesan;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/23/2016.
 */

public class SentboxItemAdapter extends ArrayAdapter
{
    private Context context;

    public SentboxItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Pesan item = (Pesan) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_pesan_item,parent,false);

        TextView fromTextView = (TextView) viewToUse.findViewById(R.id.fromTextView);
        TextView nameTextView = (TextView) viewToUse.findViewById(R.id.nameTextView);
        TextView dateTextView = (TextView) viewToUse.findViewById(R.id.dateTextView);


        fromTextView.setText(context.getString(R.string.to__));

        if(item != null) {
            nameTextView.setText(item.getNamapenerima());
            dateTextView.setText(item.getTanggalpesan().toString());
        }
        return viewToUse;
    }
}
