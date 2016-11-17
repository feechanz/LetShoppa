package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.letshoppa.feechan.letshoppa.AdapterList.OrderReportItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.OrderReportLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Shoporderreport;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderActivity extends AppCompatActivity {

    Toko currentToko;
    NumberPicker yearNumberPicker;
    boolean detail;
    ImageButton detailImageButton;
    LinearLayout controlLinearLayout;
    ArrayAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);
        setTitle(getString(R.string.shops_orders_report));

        yearNumberPicker = (NumberPicker) findViewById(R.id.yearNumberPicker);

        yearNumberPicker.setMinValue(2016);
        yearNumberPicker.setMaxValue(2026);
        yearNumberPicker.setValue(2016);
        yearNumberPicker.setWrapSelectorWheel(true);

        Intent i = getIntent();
        currentToko = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);


        detail = false;
        detailImageButton = (ImageButton) findViewById(R.id.detailImageButton);
        controlLinearLayout = (LinearLayout) findViewById(R.id.controlLinearLayout);
        detailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShowControl();
            }
        });

        List listReports = new ArrayList();
        ListView listView = (ListView) findViewById(R.id.reportListView);
        mAdapter = new OrderReportItemAdapter(ShopOrderActivity.this,listReports);
        listView.setAdapter(mAdapter);

        Button viewReportButton = (Button) findViewById(R.id.viewReportButton);
        Button sendReportButton = (Button) findViewById(R.id.sendReportButton);
        viewReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshReportView();
            }
        });
        sendReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReportToMail();
            }
        });

    }

    private void hideShowControl()
    {
        detail = !detail;
        if(detail)
        {
            controlLinearLayout.setVisibility(View.GONE);
            detailImageButton.setImageResource(R.drawable.ic_details_black_24dp);
        }
        else
        {
            controlLinearLayout.setVisibility(View.VISIBLE);
            detailImageButton.setImageResource(R.drawable.ic_change_history_black_24dp);
        }
    }

    private void sendReportToMail()
    {
        String url = AppHelper.domainURL + "/Downloads/ShopOrderGenerator.php";
        if(AppHelper.currentAccount != null && currentToko != null)
        {
            String email = AppHelper.currentAccount.getEmail();
            String year = String.valueOf(yearNumberPicker.getValue());

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_EMAIL, email));
            parameter.add(new BasicNameValuePair(Shoporderreport.TAG_YEAR, year));
            parameter.add(new BasicNameValuePair(Toko.TAG_NAMATOKO, currentToko.getNamatoko()));
            parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentToko.getTokoid())));
            UpdateDataTask task = new UpdateDataTask(url,parameter,ShopOrderActivity.this);
            task.execute();
        }
    }

    private void refreshReportView()
    {
        if(currentToko != null) {
            String url = AppHelper.domainURL + "/AndroidConnect/GetShoporderreports.php";
            int year = yearNumberPicker.getValue();

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentToko.getTokoid())));
            parameter.add(new BasicNameValuePair(Shoporderreport.TAG_YEAR, String.valueOf(year)));
            //parameter.add(new BasicNameValuePair(Report.TAG_BEGINDATE, beginDate.toString()));
            //parameter.add(new BasicNameValuePair(Report.TAG_ENDDATE, endDate.toString()));

            OrderReportLoadTask task = new OrderReportLoadTask(url, parameter, mAdapter, ShopOrderActivity.this);
            task.execute();
        }
    }
}
