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
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/18/2016.
 */

public class OrderItemAdapter extends ArrayAdapter
{
    private Context context;

    public OrderItemAdapter(Context context, List items) {
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
        final Order item = (Order) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_order_item,parent,false);

        TextView nameProductTextView = (TextView) viewToUse.findViewById(R.id.namaProductTextView);
        TextView priceTextView = (TextView) viewToUse.findViewById(R.id.priceTextView);
        TextView qtyTextView = (TextView) viewToUse.findViewById(R.id.qtyTextView);
        TextView totalTextView = (TextView) viewToUse.findViewById(R.id.totalTextView);
        ImageView iconProductImageView = (ImageView) viewToUse.findViewById(R.id.id_icon_product);

        if(item != null) {
            nameProductTextView.setText(item.getNamaproduk());
            priceTextView.setText(String.valueOf(item.getHargaproduk()));
            qtyTextView.setText(String.valueOf(item.getJumlahproduk()));
            double total = item.getJumlahproduk() * item.getHargaproduk();
            totalTextView.setText(String.valueOf(total));
            ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambarproduk(), iconProductImageView);
            loadShopTask.execute();
        }
        /*viewToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShop(item);
            }
        });*/
        //load shop image
//        ImageLoadTask loadShopTask = new ImageLoadTask(item.getGambartoko(),iconShopImageView);
//        loadShopTask.execute();
//
//        namaKategoriTextView.setText(item.getNamajenis());
//        namaTokoTextView.setText(item.getNamatoko());
//        locationTextView.setText(context.getString(R.string.location)+" : "+ item.getLokasitoko());

        return viewToUse;
    }
}
