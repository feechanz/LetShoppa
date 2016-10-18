package com.letshoppa.feechan.letshoppa.AdapterList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.HeaderInfo;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.R;

import java.util.ArrayList;

/**
 * Created by Feechan on 10/9/2016.
 */

public class PeopleExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<HeaderInfo> deptList;

    public PeopleExpandableListAdapter(Context context, ArrayList<HeaderInfo> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Account> productList = deptList.get(groupPosition).getProductList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        Account detailInfo = (Account) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.child_people_item, null);
        }

        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView emailTextView = (TextView) view.findViewById(R.id.emailTextView);
        ImageView peopleImageView = (ImageView) view.findViewById(R.id.peopleImageView);
        emailTextView.setText(detailInfo.getEmail().trim());
        nameTextView.setText(detailInfo.getNama().trim());

        ImageLoadTask task = new ImageLoadTask(detailInfo.getLinkgambaraccount(),peopleImageView);
        task.execute();
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<Account> productList = deptList.get(groupPosition).getProductList();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        HeaderInfo headerInfo = (HeaderInfo) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.group_item, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.group_title);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

