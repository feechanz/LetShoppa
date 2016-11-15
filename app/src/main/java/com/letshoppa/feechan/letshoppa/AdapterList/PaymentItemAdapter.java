package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Pembayaran;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 11/14/2016.
 */

public class PaymentItemAdapter extends ArrayAdapter
{
    private Context context;

    public PaymentItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Pembayaran item = (Pembayaran) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_payment_item,parent,false);

        TextView dateTextView = (TextView) viewToUse.findViewById(R.id.dateTextView);
        TextView paymentTextView = (TextView) viewToUse.findViewById(R.id.paymentTextView);

        dateTextView.setText(item.getTanggalpembayaran().toString());
        paymentTextView.setText(String.valueOf(item.getTotalpembayaran()));

        return viewToUse;
    }
}
