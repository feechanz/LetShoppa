package com.letshoppa.feechan.letshoppa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Produk;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initializeProduct();
    }
    Produk currentProduk;
    private void initializeProduct() {
        Intent i = this.getIntent();
        currentProduk = (Produk) i.getSerializableExtra(Produk.TAG_PRODUK);

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        ImageView produkImageView = (ImageView) findViewById(R.id.productImageView);
        TextView priceTextView = (TextView) findViewById(R.id.priceTextView);
        TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

        nameTextView.setText(currentProduk.getNamaproduk());
        priceTextView.setText("Rp. "+currentProduk.getHargaproduk());
        descriptionTextView.setText(currentProduk.getDeskripsiproduk());
        ImageLoadTask imageTask = new ImageLoadTask(currentProduk.getGambarproduk(),produkImageView);
        imageTask.execute();

        Button addBtn = (Button) findViewById(R.id.addButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProdukToCart();
            }
        });
        Button cancelBtn = (Button) findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void addProdukToCart()
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //run save location
                        Toast.makeText(ProductDetailActivity.this, getString(R.string.success),Toast.LENGTH_LONG).show();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.prompt_add_product)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }
}