package com.letshoppa.feechan.letshoppa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;

public class ListShopActivity extends AppCompatActivity {


    public ListShopActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shop);
        TextView textView = (TextView) findViewById(R.id.activityTitleShop);
        SearchView searchView = (SearchView) findViewById(R.id.searchViewShop);
        textView.setText(AppHelper.tipeShopItem.getNamajenis());
        searchView.setQueryHint(getString(R.string.SearchText) + " "+ AppHelper.tipeShopItem.getNamajenis());
    }
}
