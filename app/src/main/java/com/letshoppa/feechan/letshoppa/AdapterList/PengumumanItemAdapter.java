package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.PengumumanItem;
import com.letshoppa.feechan.letshoppa.R;

import java.util.List;

public class PengumumanItemAdapter extends ArrayAdapter
{
    private Context context;
    //private Boolean useList = true;



    public PengumumanItemAdapter(Context context, List items) {
        super(context, -1, items);
        this.context = context;
    }

    /*private class ViewHolder
    {
        TextView isiPengumuman;
    }*/

    public View getView(int position, View convertView, ViewGroup parent)
    {
        //ViewHolder holder = null;
        PengumumanItem item = (PengumumanItem) getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View viewToUse = mInflater.inflate(R.layout.fragment_home_item,parent,false);

        TextView textViewPenulis = (TextView) viewToUse.findViewById(R.id.pembuatPengumumanTextView);
        TextView textView = (TextView) viewToUse.findViewById(R.id.isiPengumumanTextView);

        textViewPenulis.setText(item.getPenulisPengumuman());
        textView.setText( item.getIsiPengumuman() );
        //pokoknya dicopy

        /*if (convertView == null) {
            if(useList){
                viewToUse = mInflater.inflate(R.layout.fragment_home, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.fragment_home, null);
            }

            holder = new ViewHolder();
            holder.isiPengumuman = (TextView)viewToUse.findViewById(R.id.titleTextView);
            viewToUse.setTag(holder);
        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }*/


        //
        //holder.isiPengumuman.setText(item.getIsiPengumuman());
        return viewToUse;
    }
}
