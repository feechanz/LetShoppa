package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Pembayaran;

public class PaymentDetailActivity extends AppCompatActivity {

    Pembayaran currentPembayaran;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_detail);
        setTitle(getString(R.string.payment));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentPembayaran = (Pembayaran) i.getSerializableExtra(Pembayaran.TAG_PEMBAYARAN);
        if(currentPembayaran != null)
        {
            initialize();
        }
    }

    private void initialize()
    {
        ImageView pembayaranImageView = (ImageView) findViewById(R.id.pembayaranImageView);
        EditText informationEditText = (EditText) findViewById(R.id.informationEditText);
        EditText costEditText = (EditText) findViewById(R.id.costEditText);
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

        informationEditText.setText(currentPembayaran.getKeterangan());
        costEditText.setText(String.valueOf(currentPembayaran.getTotalpembayaran()));
        dateTextView.setText(currentPembayaran.getTanggalpembayaran().toString());
        ImageLoadTask task = new ImageLoadTask(currentPembayaran.getBuktipembayaran(),pembayaranImageView);
        task.execute();
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
