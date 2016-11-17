package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Shoporderreport;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 11/17/2016.
 */

public class OrderReportItemAdapter extends ArrayAdapter { private Context context;

    public OrderReportItemAdapter(Context context, List items)
    {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Shoporderreport item = (Shoporderreport) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = mInflater.inflate(R.layout.listitem_order_report_item,parent,false);
        TextView yearTextView = (TextView) view.findViewById(R.id.yearTextView);
        TextView monthTextView = (TextView) view.findViewById(R.id.monthTextView);
        TextView totalTextView = (TextView) view.findViewById(R.id.totalTextView);

        yearTextView.setText(String.valueOf(item.getYear()));

        switch (item.getMonth())
        {
            case 1:
                monthTextView.setText(context.getString(R.string.january));
                break;
            case 2:
                monthTextView.setText(context.getString(R.string.february));
                break;
            case 3:
                monthTextView.setText(context.getString(R.string.march));
                break;
            case 4:
                monthTextView.setText(context.getString(R.string.april));
                break;
            case 5:
                monthTextView.setText(context.getString(R.string.may));
                break;
            case 6:
                monthTextView.setText(context.getString(R.string.june));
                break;
            case 7:
                monthTextView.setText(context.getString(R.string.july));
                break;
            case 8:
                monthTextView.setText(context.getString(R.string.august));
                break;
            case 9:
                monthTextView.setText(context.getString(R.string.september));
                break;
            case 10:
                monthTextView.setText(context.getString(R.string.october));
                break;
            case 11:
                monthTextView.setText(context.getString(R.string.november));
                break;
            default:
                monthTextView.setText(context.getString(R.string.december));
        }
        totalTextView.setText(String.valueOf(item.getJumlahorder()));
        return view;
    }

}
