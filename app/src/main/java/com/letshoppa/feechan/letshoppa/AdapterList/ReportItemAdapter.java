package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Report;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/24/2016.
 */

public class ReportItemAdapter extends ArrayAdapter
{
    private Context context;

    public ReportItemAdapter(Context context, List items)
    {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Report item = (Report) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = mInflater.inflate(R.layout.listitem_report_item,parent,false);
        TextView categoryNameTextView = (TextView) view.findViewById(R.id.categoryNameTextView);
        TextView statusCartTextView = (TextView) view.findViewById(R.id.statusCartTextView);
        TextView statusWantToBuyTextView = (TextView) view.findViewById(R.id.statusWantBuyTextView);
        TextView statusPurchasedTextView = (TextView) view.findViewById(R.id.statusPurchasedTextView);
        TextView statusCancelledTextView = (TextView) view.findViewById(R.id.statusCancelTextView);
        TextView statusTotalTextView = (TextView) view.findViewById(R.id.statusTotalTextView);

        categoryNameTextView.setText(item.getNamajenis());
        statusCartTextView.setText(String.valueOf(item.getStatusincart()));
        statusWantToBuyTextView.setText(String.valueOf(item.getStatuswanttobuy()));
        statusPurchasedTextView.setText(String.valueOf(item.getStatusaccepted()));
        statusCancelledTextView.setText(String.valueOf(item.getStatuscancelled()));
        statusTotalTextView.setText(String.valueOf(item.getStatustotal()));
        return view;
    }
}