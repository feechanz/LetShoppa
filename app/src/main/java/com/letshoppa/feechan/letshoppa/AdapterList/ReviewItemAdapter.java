package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Review;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/22/2016.
 */

public class ReviewItemAdapter extends ArrayAdapter
{
    private Context context;

    public ReviewItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Review item = (Review) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.listitem_review_item,parent,false);

        TextView namaTextView = (TextView) viewToUse.findViewById(R.id.namaTextView);
        TextView dateTextView = (TextView) viewToUse.findViewById(R.id.dateTextView);
        TextView isiReviewTextView = (TextView) viewToUse.findViewById(R.id.isiReviewTextView);
        RatingBar ratingBar = (RatingBar) viewToUse.findViewById(R.id.ratingBar);

        namaTextView.setText(item.getNama());
        dateTextView.setText(item.getTanggalreview().toString());
        isiReviewTextView.setText(item.getIsireview());
        ratingBar.setRating((float) item.getPointreview());
        return viewToUse;
    }
}
