package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.MyShopProductItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.ProductRefreshLoadTask;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import java.util.ArrayList;
import java.util.List;

public class AddNewShopProductsActivity extends AppCompatActivity {

    Toko currentShop;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllProductByTokoId.php";
    private SwipeRefreshLayout swipeContainer;
    private List listMyProducts;
    ArrayAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop_products);

        Intent i = this.getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop != null)
        {
            this.setTitle(currentShop.getNamatoko());
            initializeView();
        }
    }

    private void initializeView()
    {
        Button add_product_button = (Button) findViewById(R.id.add_product_button);
        Button doneButton = (Button) findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        ListView listView = (ListView) findViewById(R.id.MyProductsListView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listMyProducts = new ArrayList();
        mAdapter = new MyShopProductItemAdapter(AddNewShopProductsActivity.this,listMyProducts,currentShop.getJenistokoid());

        listView.setAdapter(mAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchShopAsync(0);
            }
        });
        ProductRefreshLoadTask loadTask = new ProductRefreshLoadTask(url, Integer.valueOf(currentShop.getTokoid()), swipeContainer, mAdapter, AddNewShopProductsActivity.this);
        loadTask.execute((Void) null);
    }

    public void fetchShopAsync(int page)
    {
        if(currentShop != null) {
            ProductRefreshLoadTask loadTask = new ProductRefreshLoadTask(url, Integer.valueOf(currentShop.getTokoid()), swipeContainer, mAdapter, AddNewShopProductsActivity.this);
            loadTask.execute((Void) null);
        }
        else
        {
            if(swipeContainer != null)
            {
                swipeContainer.setRefreshing(false);
            }
        }
    }

    private  void addProduct()
    {
        Intent addProductAct = new Intent(AddNewShopProductsActivity.this,AddProductsActivity.class);
        addProductAct.putExtra(Toko.TAG_TOKO,currentShop);
        startActivity(addProductAct);
    }

}
