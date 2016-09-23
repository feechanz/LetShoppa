package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 9/12/2016.
 */
public class MyShopItemAdapter extends ArrayAdapter
{
    private Context context;

    public MyShopItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Toko item = (Toko) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.fragment_my_shop_item,parent,false);

        TextView namaKategoriTextView = (TextView) viewToUse.findViewById(R.id.namaKategoriTextView);
        TextView namaTokoTextView = (TextView) viewToUse.findViewById(R.id.namaTokoTextView);

        namaKategoriTextView.setText("Kategori");
        namaTokoTextView.setText(item.getNamatoko());

        return viewToUse;
    }
}
