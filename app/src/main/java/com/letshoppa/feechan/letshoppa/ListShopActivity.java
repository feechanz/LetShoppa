package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.ShopItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListShopActivity extends AppCompatActivity {


    public ListShopActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_shop);
        TextView textView = (TextView) findViewById(R.id.activityTitleShop);
        SearchView searchView = (SearchView) findViewById(R.id.searchViewShop);

        if(AppHelper.tipeShopItem != null) {
            textView.setText(AppHelper.tipeShopItem.getNamajenis());
            searchView.setQueryHint(getString(R.string.SearchText) + " " + AppHelper.tipeShopItem.getNamajenis());
            setTitle(AppHelper.tipeShopItem.getNamajenis());
        }

        //initialize back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //

        initializeListView();
    }

    String url = "http://letshoppa.itmaranatha.org/AndroidConnect/GetAllTokoByJenistokoId.php";;
    private SwipeRefreshLayout swipeContainer;
    private List listShops;
    ArrayAdapter mAdapter;

    private void initializeListView()
    {
        ListView listView = (ListView) findViewById(R.id.ShopListView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listShops = new ArrayList();
        mAdapter = new ShopItemAdapter(this,listShops);

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetailShop((Toko)parent.getItemAtPosition(position));
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchShopAsync(0);
            }
        });
        ShopRefreshLoadTask shopTask = new ShopRefreshLoadTask(url,AppHelper.tipeShopItem.getJenistokoid(),swipeContainer);
        shopTask.execute((Void) null);
    }
    private void openDetailShop(Toko toko)
    {
        Intent openShopIntent = new Intent(this,ShopActivity.class);
        openShopIntent.putExtra(Toko.TAG_TOKO,toko);
        startActivity(openShopIntent);
    }
    private void fetchShopAsync(int page)
    {
        if(AppHelper.tipeShopItem != null)
        {
            ShopRefreshLoadTask shopTask = new ShopRefreshLoadTask(url,AppHelper.tipeShopItem.getJenistokoid(),swipeContainer);
            shopTask.execute((Void) null);
        }
    }


    public class ShopRefreshLoadTask extends AsyncTask<Void, Void, Boolean>
    {
        SwipeRefreshLayout swipeContainer;
        private String url;
        private int jenistokoid;

        String messagejson;
        int successjson;

        public ShopRefreshLoadTask(String url, int jenistokoid, SwipeRefreshLayout swipeContainer) {
            this.url = url;
            this.jenistokoid = jenistokoid;
            this.swipeContainer = swipeContainer;
            successjson=-1;
            messagejson="";
            shops = new ArrayList();

        }
        JSONArray listshops = null;
        List shops;

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Toko.TAG_JENISTOKOID, String.valueOf(jenistokoid)));

            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if(json != null)
            {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        listshops = json.getJSONArray(Toko.TAG_TOKO);

                        for (int i=0;i<listshops.length();i++)
                        {
                            JSONObject tokoobject = listshops.getJSONObject(i);
                            Toko newtoko = Toko.GetTokoFromJson(tokoobject);
                            shops.add(newtoko);
                        }
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if(AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    }
                    else
                    {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else
            {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
            {
                mAdapter.clear();
                mAdapter.addAll(shops);
            }
            else
            {
                Toast.makeText(ListShopActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            //set refresh off
            if(swipeContainer != null)
            {
                swipeContainer.setRefreshing(false);
            }
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
