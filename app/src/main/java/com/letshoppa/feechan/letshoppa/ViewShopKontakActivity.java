package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.ContacttokoItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.KontaktokoLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import java.util.ArrayList;
import java.util.List;

public class ViewShopKontakActivity extends AppCompatActivity {

    Toko currentShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_kontak);
        setTitle(getString(R.string.shops_contact));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        //set listview
        listView = (ListView) findViewById(R.id.ContactListView);
        listKontaks = new ArrayList();
        mAdapter = new ContacttokoItemAdapter(ViewShopKontakActivity.this,listKontaks);
        listView.setAdapter(mAdapter);

        refreshKontak();
    }

    ArrayAdapter mAdapter;
    private List listKontaks;
    ListView listView;

    private void refreshKontak()
    {
        if(currentShop != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllKontaktokoByTokoId.php";
            int tokoid = currentShop.getTokoid();
            KontaktokoLoadTask task = new KontaktokoLoadTask(url, tokoid, mAdapter, ViewShopKontakActivity.this);
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
