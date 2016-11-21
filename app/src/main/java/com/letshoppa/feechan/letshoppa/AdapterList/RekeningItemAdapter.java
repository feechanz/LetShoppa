package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Rekening;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 11/21/2016.
 */

public class RekeningItemAdapter extends ArrayAdapter
{
    private Context context;

    public RekeningItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Rekening item = (Rekening) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_rekening_item,parent,false);

        TextView bankNameTextView = (TextView) viewToUse.findViewById(R.id.banknameTextView);
        TextView bankAccountTextView = (TextView) viewToUse.findViewById(R.id.bankAccountTextView);

        bankNameTextView.setText(item.getNamabank());
        bankAccountTextView.setText(item.getNomorrekening());

        return viewToUse;
    }
}
