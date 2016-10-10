package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.letshoppa.feechan.letshoppa.AdapterList.KategoriprodukItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.LoadKategoriTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import java.util.ArrayList;
import java.util.List;

public class AddProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        setTitle(getString(R.string.AddProduct));
        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);
        if(currentShop != null)
        {
            setSpinner();
        }
        //this.setFinishOnTouchOutside(false);
    }
    Toko currentShop;

    Spinner kategoriprodukspinner;
    ArrayAdapter mAdapter;
    private void setSpinner()
    {
        kategoriprodukspinner = (Spinner) findViewById(R.id.kategoriproductspinner);
        List listkategori = new ArrayList();
        mAdapter = new KategoriprodukItemAdapter(AddProductsActivity.this, listkategori);
        kategoriprodukspinner.setAdapter(mAdapter);
        LoadKategoriTask kategoriTask = new LoadKategoriTask(this,currentShop.getJenistokoid(),mAdapter);
        kategoriTask.execute();
    }


}
