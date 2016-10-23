package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.ContactItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.KontakLoadTask;

import java.util.ArrayList;
import java.util.List;

public class ViewContactActivity extends AppCompatActivity {

    ArrayAdapter mAdapter;
    private List listKontaks;
    ListView listView;

    int accountid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        //set listview
        Intent i = getIntent();
        accountid = i.getIntExtra(Account.TAG_ACCOUNTID,0);

        listView = (ListView) findViewById(R.id.ContactListView);
        listKontaks = new ArrayList();
        mAdapter = new ContactItemAdapter(ViewContactActivity.this,listKontaks);
        listView.setAdapter(mAdapter);
        refreshKontak();
    }

    private void refreshKontak()
    {
        if(accountid != 0) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllKontakByAccountId.php";
            KontakLoadTask task = new KontakLoadTask(url, accountid, mAdapter, ViewContactActivity.this);
            task.execute();
        }
    }
}
