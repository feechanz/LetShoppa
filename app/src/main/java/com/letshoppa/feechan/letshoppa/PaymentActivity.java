package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.PaymentItemAdapter;
import com.letshoppa.feechan.letshoppa.AdapterList.RekeningItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Class.PaymentsLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Pembayaran;
import com.letshoppa.feechan.letshoppa.Class.RekeningLoadTask;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    ArrayAdapter mAdapterBank;
    private List listBank;
    ListView listViewBank;

    ArrayAdapter mAdapter;
    private List listPayments;
    ListView listView;
    Order currentOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        setTitle(getString(R.string.payment));

        Button mAddPaymentButton = (Button) findViewById(R.id.addPaymentButton);
        mAddPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPaymentActivity();
            }
        });

        listView = (ListView) findViewById(R.id.paymentListView);
        listPayments =  new ArrayList();
        mAdapter = new PaymentItemAdapter(PaymentActivity.this,listPayments);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pembayaran item = (Pembayaran)parent.getItemAtPosition(position);
                detailPayment(item);
            }
        });

        ///lv bank
        listViewBank = (ListView) findViewById(R.id.bankListView);
        listBank = new ArrayList();
        mAdapterBank = new RekeningItemAdapter(PaymentActivity.this,listBank);
        listViewBank.setAdapter(mAdapterBank);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentOrder = (Order) i.getSerializableExtra(Order.TAG_ORDER);
        if(currentOrder != null)
        {
            refreshPayments();
            refreshBank();
        }
    }
    private void refreshBank()
    {
        if(currentOrder != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllRekeningsByTokoId.php";
            int tokoid = currentOrder.getTokoid();
            RekeningLoadTask task = new RekeningLoadTask(url, tokoid, mAdapterBank, PaymentActivity.this);
            task.execute();
        }
    }

    private void detailPayment(Pembayaran pembayaran)
    {
        if(pembayaran != null) {
            Intent intent = new Intent(PaymentActivity.this, PaymentDetailActivity.class);
            intent.putExtra(Pembayaran.TAG_PEMBAYARAN, pembayaran);
            startActivity(intent);
        }
    }

    private void openAddPaymentActivity()
    {
        if(currentOrder != null) {
            Intent intent = new Intent(PaymentActivity.this, AddPaymentActivity.class);
            intent.putExtra(Order.TAG_ORDER, currentOrder);
            startActivity(intent);
        }
    }

    private void refreshPayments()
    {
        if(currentOrder != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetPembayaransByOrderId.php";
            int orderid = Integer.valueOf(currentOrder.getOrderid());
            PaymentsLoadTask task = new PaymentsLoadTask(url, orderid, mAdapter, PaymentActivity.this);
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
    @Override
    public void onResume()
    {
        super.onResume();
        refreshPayments();
    }
}
