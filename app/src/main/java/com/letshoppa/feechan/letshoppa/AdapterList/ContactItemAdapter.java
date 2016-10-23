package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Kontak;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/23/2016.
 */

public class ContactItemAdapter extends ArrayAdapter
{
    private Context context;

    public ContactItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Kontak item = (Kontak) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_kontak_item,parent,false);

        TextView contactTypeTextView = (TextView) viewToUse.findViewById(R.id.contactTypeTextView);
        TextView contactTextView = (TextView) viewToUse.findViewById(R.id.contactTextView);

        contactTypeTextView.setText(item.getJeniskontak().toString());
        contactTextView.setText(item.getIsikontak().toString());
        return viewToUse;
    }
}