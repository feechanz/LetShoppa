package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Jenistoko;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 9/13/2016.
 */
public class JenistokoAdapter extends ArrayAdapter {

    private Context context;

    public JenistokoAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Jenistoko item = (Jenistoko) getItem(position);
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(item.getNamajenis());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        Jenistoko item = (Jenistoko) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View viewToUse = mInflater.inflate(R.layout.fragment_tipe_shopping_item,parent,false);
        TextView textIdJenis = (TextView) viewToUse.findViewById(R.id.id_jenis_shop);
        TextView textNamaJenis = (TextView) viewToUse.findViewById(R.id.nama_jenis_toko);

        textIdJenis.setText(item.getJenistokoid() +"");
        textNamaJenis.setText( item.getNamajenis() );

        return viewToUse;
    }
}
