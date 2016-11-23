package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.AdapterList.RekeningItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.RekeningLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import java.util.ArrayList;
import java.util.List;

public class ViewShopRekeningActivity extends AppCompatActivity {
    Toko currentShop;

    ArrayAdapter mAdapter;
    private List listRekenings;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_rekening);
        setTitle(getString(R.string.bank_account_information));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        listView = (ListView) findViewById(R.id.bankListView);
        listRekenings = new ArrayList();
        mAdapter = new RekeningItemAdapter(ViewShopRekeningActivity.this,listRekenings);
        listView.setAdapter(mAdapter);
        if(currentShop != null) {

            TextView shopNameTextView = (TextView) findViewById(R.id.shopNameTextView);
            shopNameTextView.setText(currentShop.getNamatoko());
            refreshRekening();
        }

    }
    private void refreshRekening()
    {
        if(currentShop != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllRekeningsByTokoId.php";
            int tokoid = Integer.valueOf(currentShop.getTokoid());
            RekeningLoadTask task = new RekeningLoadTask(url, tokoid, mAdapter, ViewShopRekeningActivity.this);
            task.execute();
        }
    }


    @Override
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
