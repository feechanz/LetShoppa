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
import com.letshoppa.feechan.letshoppa.Class.Produk;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/7/2016.
 */

public class ShopProductItemAdapter extends ArrayAdapter
{
    private Context context;

    public ShopProductItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Produk item = (Produk) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_shop_product_item,parent,false);

        TextView namaKategoriTextView = (TextView) viewToUse.findViewById(R.id.namaKategoriTextView);
        TextView namaProdukTextView = (TextView) viewToUse.findViewById(R.id.namaProductTextView);
        TextView hargaProdukTextView = (TextView) viewToUse.findViewById(R.id.hargaProdukTextView);
        ImageView iconProductImageView = (ImageView) viewToUse.findViewById(R.id.id_icon_product);

        //load produk image
        ImageLoadTask loadSProdukTask = new ImageLoadTask(item.getGambarproduk(),iconProductImageView);
        loadSProdukTask.execute();

        namaKategoriTextView.setText(item.getNamakategori());
        namaProdukTextView.setText(item.getNamaproduk());
        hargaProdukTextView.setText(String.valueOf(item.getHargaproduk()));

        return viewToUse;
    }
}
