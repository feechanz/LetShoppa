package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Shopprodukreport;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 11/17/2016.
 */

public class ProductReportItemAdapter extends ArrayAdapter { private Context context;

    public ProductReportItemAdapter(Context context, List items)
    {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Shopprodukreport item = (Shopprodukreport) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = mInflater.inflate(R.layout.listitem_product_report_item,parent,false);
        TextView productNameTextView = (TextView) view.findViewById(R.id.productNameTextView);
        TextView qtyTextView = (TextView) view.findViewById(R.id.qtyTextView);

        productNameTextView.setText(item.getNamaproduk());
        qtyTextView.setText(String.valueOf(item.getJumlahorder()));
        return view;
    }

}
