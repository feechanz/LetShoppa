package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

/**
 * Created by Feechan on 10/17/2016.
 */

public class PeopleItemAdapter extends ArrayAdapter
{
    private Context context;

    public PeopleItemAdapter(Context context, List items)
    {
        super(context, -1, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        final Account item = (Account) getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View view = mInflater.inflate(R.layout.child_people_item,parent,false);
        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        ImageView peopleImageView = (ImageView) view.findViewById(R.id.peopleImageView);
        emailTextView.setText(item.getEmail().trim());
        nameTextView.setText(item.getNama().trim());
        ImageLoadTask task = new ImageLoadTask(item.getLinkgambaraccount(),peopleImageView);
        task.execute();

        return view;
    }
}
