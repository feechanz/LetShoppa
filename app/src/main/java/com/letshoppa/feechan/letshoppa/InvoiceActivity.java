package com.letshoppa.feechan.letshoppa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ChangeOrderTask;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

public class InvoiceActivity extends AppCompatActivity {

    Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_invoice);
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
        if (currentOrder != null) {
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

            Button mConfirmButton = (Button) findViewById(R.id.confirmButton);
            if (currentOrder.getStatusorder() == 4) {
                mConfirmButton.setEnabled(false);
                mConfirmButton.setText(getString(R.string.confirmed));
                mConfirmationTextView.setText(getString(R.string.confirmed));
            } else {
                mConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmationDialog();
                    }
                });
            }

            ImageLoadTask task = new ImageLoadTask(currentOrder.getGambarproduk(), mProductImageView);
            task.execute();
        }
    }

    private void confirmationDialog() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        confirmOrder();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(InvoiceActivity.this);
        builder.setMessage(getString(R.string.confirm_order) + "?")
                .setPositiveButton(getString(R.string.yes), dialogClickListener).
                setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    private void confirmOrder() {
        ChangeOrderTask task = new ChangeOrderTask(currentOrder.getOrderid(),4,InvoiceActivity.this,new RefreshMethod());
        task.execute();
    }
    class RefreshMethod implements IRefreshMethod
    {
        @Override
        public void refresh() {
            currentOrder.setStatusorder(4);
            finish();
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
