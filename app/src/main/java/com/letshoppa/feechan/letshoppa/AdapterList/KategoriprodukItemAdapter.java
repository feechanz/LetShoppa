package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Kategoriproduk;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/10/2016.
 */

public class KategoriprodukItemAdapter extends ArrayAdapter
{
    private Context context;
    //private Boolean useList = true;

    public KategoriprodukItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Kategoriproduk item = (Kategoriproduk) getItem(position);
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(item.getNamakategori());
        return label;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        Kategoriproduk item = (Kategoriproduk) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View viewToUse = mInflater.inflate(R.layout.listitem_kategoriproduk_item,parent,false);
        TextView textNamaJenis = (TextView) viewToUse.findViewById(R.id.namaTextView);
        textNamaJenis.setText( item.getNamakategori() );
        return viewToUse;
    }

}