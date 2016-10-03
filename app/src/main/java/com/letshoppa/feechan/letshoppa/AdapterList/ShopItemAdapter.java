package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/2/2016.
 */

public class ShopItemAdapter extends ArrayAdapter
{
    private Context context;

    public ShopItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }
    /*public void openShop(Toko toko)
    {
        Intent openShopIntent = new Intent(context,MyShopActivity.class);
        openShopIntent.putExtra(Toko.TAG_TOKO,toko);
        context.startActivity(openShopIntent);
    }*/

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Toko item = (Toko) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.fragment_shop_item,parent,false);

        TextView namaKategoriTextView = (TextView) viewToUse.findViewById(R.id.namaKategoriTextView);
        TextView namaTokoTextView = (TextView) viewToUse.findViewById(R.id.namaTokoTextView);
        TextView locationTextView = (TextView) viewToUse.findViewById(R.id.locationTokoTextView);
        ImageView iconShopImageView = (ImageView) viewToUse.findViewById(R.id.id_icon_shop);

        /*viewToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShop(item);
            }
        });*/
        //load shop image
        ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambartoko(),iconShopImageView);
        loadShopTask.execute();

        namaKategoriTextView.setText(item.getNamajenis());
        namaTokoTextView.setText(item.getNamatoko());
        locationTextView.setText(context.getString(R.string.location)+" : "+ item.getLokasitoko());

        return viewToUse;
    }
}