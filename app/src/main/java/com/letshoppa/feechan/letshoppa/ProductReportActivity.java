package com.letshoppa.feechan.letshoppa;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.ProductReportItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Report;
import com.letshoppa.feechan.letshoppa.Class.ShopReportLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_report);
        setTitle(getString(R.string.products_report));

        detail = false;
        detailImageButton = (ImageButton) findViewById(R.id.detailImageButton);
        controlLinearLayout = (LinearLayout) findViewById(R.id.controlLinearLayout);
        detailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShowControl();
            }
        });

        Button beginDateButton = (Button) findViewById(R.id.begin_date_button);
        Button endDateButton = (Button) findViewById(R.id.end_date_button);
        Button viewReportButton = (Button) findViewById(R.id.viewReportButton);
        Button sendReportButton = (Button) findViewById(R.id.sendReportButton);
        selectedBeginDateText = (TextView) findViewById(R.id.beginDateTextView);
        selectedEndDateText = (TextView) findViewById(R.id.endDateTextView);

        //set list report
        List listReports = new ArrayList();
        ListView listView = (ListView) findViewById(R.id.reportListView);
        mAdapter = new ProductReportItemAdapter(ProductReportActivity.this,listReports);
        listView.setAdapter(mAdapter);
        changeBeginDate("01/01/2016");
        changeEndDate("01/01/2016");
        beginDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeBeginDate();
            }
        });
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEndDate();
            }
        });
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

        Intent i = getIntent();
        currentToko=(Toko) i.getSerializableExtra(Toko.TAG_TOKO);
    }

    Toko currentToko;

    boolean detail;
    ImageButton detailImageButton;
    LinearLayout controlLinearLayout;


    private DatePickerDialog datePickerDialog;
    private TextView selectedBeginDateText;
    private TextView selectedEndDateText;
    private Date beginDate;
    private Date endDate;

    ArrayAdapter mAdapter;

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
        String url = AppHelper.domainURL + "/Downloads/ShopProductGenerator.php";
        if(AppHelper.currentAccount != null && currentToko != null)
        {
            String email = AppHelper.currentAccount.getEmail();

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_EMAIL, email));
            parameter.add(new BasicNameValuePair(Report.TAG_BEGINDATE, beginDate.toString()));
            parameter.add(new BasicNameValuePair(Report.TAG_ENDDATE, endDate.toString()));
            parameter.add(new BasicNameValuePair(Toko.TAG_NAMATOKO, currentToko.getNamatoko()));
            parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentToko.getTokoid())));
            UpdateDataTask task = new UpdateDataTask(url,parameter,ProductReportActivity.this);
            task.execute();
        }
    }

    private void refreshReportView()
    {
        if(currentToko != null) {
            String url = AppHelper.domainURL + "/AndroidConnect/GetShopprodukreports.php";
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID, String.valueOf(currentToko.getTokoid())));
            parameter.add(new BasicNameValuePair(Report.TAG_BEGINDATE, beginDate.toString()));
            parameter.add(new BasicNameValuePair(Report.TAG_ENDDATE, endDate.toString()));

            ShopReportLoadTask task = new ShopReportLoadTask(url, parameter, mAdapter, ProductReportActivity.this);
            task.execute();
        }
    }


    private void changeEndDate()
    {

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {
                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);
                        CharSequence strDate = DateFormat.format("E, dd-MMMM-yyyy", dtDob);
                        CharSequence stringDate = DateFormat.format("dd/MM/yyyy", dtDob);

                        Toast.makeText(ProductReportActivity.this,
                                "Date picked: " + strDate, Toast.LENGTH_SHORT).show();
                        //selectedDateText.setText(strDate);
                        changeEndDate(stringDate.toString());
                    }}, 2016,0, 1);
        datePickerDialog.show();

        return;
    }

    private void changeEndDate(String strdate)
    {
        endDate = getDate(strdate);
        SimpleDateFormat df = new SimpleDateFormat("E, dd-MMMM-yyyy");
        String date = df.format(endDate);
        selectedEndDateText.setText(date);
    }
    private void changeBeginDate(String strdate)
    {
        beginDate = getDate(strdate);
        SimpleDateFormat df = new SimpleDateFormat("E, dd-MMMM-yyyy");
        String date = df.format(beginDate);
        selectedBeginDateText.setText(date);
    }

    private void changeBeginDate()
    {

        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth)
                    {

                        Time chosenDate = new Time();
                        chosenDate.set(dayOfMonth, monthOfYear, year);
                        long dtDob = chosenDate.toMillis(true);
                        CharSequence strDate = DateFormat.format("E, dd-MMMM-yyyy", dtDob);
                        CharSequence stringDate = DateFormat.format("dd/MM/yyyy", dtDob);

                        Toast.makeText(ProductReportActivity.this,
                                "Date picked: " + strDate, Toast.LENGTH_SHORT).show();
                        //selectedDateText.setText(strDate);
                        changeBeginDate(stringDate.toString());
                    }}, 2016,0, 1);
        datePickerDialog.show();

        return;
    }

    private Date getDate(String dd_MM_YYYY)
    {
        Date result=null;
        SimpleDateFormat df = new SimpleDateFormat();
        df.applyPattern("dd/MM/yyyy");
        try {
            result = new Date((df.parse(dd_MM_YYYY)).getTime());
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
