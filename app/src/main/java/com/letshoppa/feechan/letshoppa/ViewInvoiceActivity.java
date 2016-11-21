package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Order;

public class ViewInvoiceActivity extends AppCompatActivity {

    Order currentOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_view_invoice);
        setTitle(getString(R.string.invoice));
        Intent i = getIntent();
        currentOrder = (Order) i.getSerializableExtra(Order.TAG_ORDER);
        initialize();
    }

    ImageView mProductImageView;
    TextView mNamaProductTextView;
    TextView mShopNameTextView;
    TextView mPriceTextView;
    TextView mQtyTextView;
    TextView mTotalTextView;
    TextView mStatusTextView;
    TextView mDateTextView;
    TextView mConfirmationTextView;

    private void initialize() {
        if(currentOrder != null) {
            mProductImageView = (ImageView) findViewById(R.id.id_icon_product);
            mNamaProductTextView = (TextView) findViewById(R.id.namaProductTextView);
            mShopNameTextView = (TextView) findViewById(R.id.shopNameTextView);
            mPriceTextView = (TextView) findViewById(R.id.priceTextView);
            mQtyTextView = (TextView) findViewById(R.id.qtyTextView);
            mTotalTextView = (TextView) findViewById(R.id.totalTextView);
            mStatusTextView = (TextView) findViewById(R.id.statusTextView);
            mDateTextView = (TextView) findViewById(R.id.dateTextView);
            mConfirmationTextView = (TextView) findViewById(R.id.confirmationTextView);

            mNamaProductTextView.setText(currentOrder.getNamaproduk());
            mShopNameTextView.setText(currentOrder.getNamatoko());
            mPriceTextView.setText(AppHelper.decimalFormat(currentOrder.getHargaproduk()));
            mQtyTextView.setText(String.valueOf(currentOrder.getJumlahproduk()));
            double total = currentOrder.getJumlahproduk() * currentOrder.getHargaproduk();
            mTotalTextView.setText(AppHelper.decimalFormat(total));
            if(currentOrder.getStatusorder() == 4)
            {
                mConfirmationTextView.setText(getString(R.string.confirmed));
            }

            ImageLoadTask task =new ImageLoadTask(currentOrder.getGambarproduk(),mProductImageView);
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
