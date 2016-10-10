package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.letshoppa.feechan.letshoppa.Class.Toko;

public class AddNewShopProductsActivity extends AppCompatActivity {

    Toko currentShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop_products);

        Intent i = this.getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop != null)
        {
            this.setTitle(currentShop.getNamatoko());
        }
    }
}
