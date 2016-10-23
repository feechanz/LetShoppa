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
 * Created by Feechan on 10/23/2016.
 */

public class ShopRadiusItemAdapter extends ArrayAdapter
{
    private Context context;

    public ShopRadiusItemAdapter(Context context, List items) {
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

        View viewToUse = mInflater.inflate(R.layout.listitem_shop_radius_item,parent,false);

        TextView namaKategoriTextView = (TextView) viewToUse.findViewById(R.id.namaKategoriTextView);
        TextView namaTokoTextView = (TextView) viewToUse.findViewById(R.id.namaTokoTextView);
        TextView locationTextView = (TextView) viewToUse.findViewById(R.id.locationTokoTextView);
        TextView distanceTextView = (TextView) viewToUse.findViewById(R.id.distanceTextView);

        ImageView iconShopImageView = (ImageView) viewToUse.findViewById(R.id.id_icon_shop);

        ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambartoko(),iconShopImageView);
        loadShopTask.execute();

        namaKategoriTextView.setText(item.getNamajenis());
        namaTokoTextView.setText(item.getNamatoko());
        locationTextView.setText(context.getString(R.string.location)+" : "+ item.getLokasitoko());
        distanceTextView.setText(String.valueOf(item.getJarak()));

        return viewToUse;
    }
}
