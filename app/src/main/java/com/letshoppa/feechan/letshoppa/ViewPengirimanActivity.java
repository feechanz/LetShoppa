package com.letshoppa.feechan.letshoppa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Class.Pengiriman;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewPengirimanActivity extends AppCompatActivity {

    Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pengiriman);
        setTitle(getString(R.string.shipping));

        Intent i = getIntent();
        currentOrder = (Order) i.getSerializableExtra(Order.TAG_ORDER);
        if(currentOrder != null) {
            initialize();
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    TextView mDateTextView;
    EditText mExpeditionEditText;
    EditText mReceiptEditText;
    EditText mInformationEditText;
    EditText mCostEditText;

    Pengiriman currentPengiriman;

    private void initialize()
    {
        TextView mNamaTextView = (TextView) findViewById(R.id.nameTextView);
        TextView mNamaProductTextView = (TextView) findViewById(R.id.namaProductTextView);
        mNamaTextView.setText(currentOrder.getNamapembeli());
        mNamaProductTextView.setText(currentOrder.getNamaproduk());

        mExpeditionEditText = (EditText) findViewById(R.id.expeditionEditText);
        mReceiptEditText = (EditText) findViewById(R.id.receiptEditText);
        mInformationEditText = (EditText) findViewById(R.id.informationEditText);
        mCostEditText = (EditText) findViewById(R.id.costEditText);
        mDateTextView = (TextView) findViewById(R.id.dateTextView);

        LoadPengirimanTask task = new LoadPengirimanTask();
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

    public class LoadPengirimanTask extends AsyncTask<Void, Void, Boolean>
    {
        String url;
        String messagejson;
        int successjson;
        ProgressDialog dialog;

        public LoadPengirimanTask() {
            this.url = AppHelper.domainURL+"/AndroidConnect/GetPengirimanByOrderId.php";
            messagejson="";
            successjson=-1;
            dialog = ProgressDialog.show(ViewPengirimanActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Pengiriman.TAG_ORDERID, String.valueOf(currentOrder.getOrderid())));

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        JSONArray pengirimanArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject pengirimanObj = pengirimanArray.getJSONObject(0);
                        Pengiriman pengiriman = Pengiriman.GetPengirimanFromJson(pengirimanObj);
                        if (pengiriman != null) {
                            currentPengiriman = pengiriman;
                            return true;
                        } else {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    } else {
                        return false;
                    }

                } catch (JSONException e) {
                    successjson = 3;
                    if (AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    } else {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            } else {
                successjson = 3;
                messagejson = AppHelper.NoConnection;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success)
            {
                if(successjson==1 && currentPengiriman != null)
                {
                    mDateTextView.setText(currentPengiriman.getTanggalpengiriman().toString());
                    mExpeditionEditText.setText(currentPengiriman.getJasaekspedisi());
                    mReceiptEditText.setText(currentPengiriman.getNoresi());
                    mInformationEditText.setText(currentPengiriman.getKeterangan());
                    mCostEditText.setText("Rp. "+AppHelper.decimalFormat(currentPengiriman.getBiayakirim()));
                }
            }
            else
            {
                Toast.makeText(ViewPengirimanActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}
