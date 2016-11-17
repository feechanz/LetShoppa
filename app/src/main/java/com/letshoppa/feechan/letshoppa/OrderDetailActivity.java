package com.letshoppa.feechan.letshoppa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.ContactItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ChangeOrderTask;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Kontak;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent i = getIntent();
        int orderid = i.getIntExtra(Order.TAG_ORDERID,0);
        buy = i.getStringExtra(Order.TAG_BUY);
        responded = i.getIntExtra(Order.TAG_RESPONDED,0);
        if (orderid != 0) {
            initialize(orderid);
        }
    }

    int responded;
    String buy;
    Order currentOrder;
    Account currentAccount;

    ImageView mProductImageView;
    TextView mNamaProductTextView;
    TextView mShopNameTextView;
    TextView mPriceTextView;
    TextView mQtyTextView;
    TextView mTotalTextView;
    TextView mStatusTextView;
    TextView mDateTextView;
    Button mFirstButton;
    Button mSecondButton;

    TextView mNameTextView;
    TextView mEmailTextView;

    ListView mContactListView;

    ArrayAdapter mAdapter;
    private List listKontaks;

    ProgressDialog dialog;

    private void initialize(int orderid) {
        mProductImageView = (ImageView) findViewById(R.id.id_icon_product);
        mNamaProductTextView = (TextView) findViewById(R.id.namaProductTextView);
        mShopNameTextView = (TextView) findViewById(R.id.shopNameTextView);
        mPriceTextView = (TextView) findViewById(R.id.priceTextView);
        mQtyTextView = (TextView) findViewById(R.id.qtyTextView);
        mTotalTextView = (TextView) findViewById(R.id.totalTextView);
        mStatusTextView = (TextView) findViewById(R.id.statusTextView);
        mDateTextView = (TextView) findViewById(R.id.dateTextView);
        mFirstButton = (Button) findViewById(R.id.firstButton);
        mSecondButton = (Button) findViewById(R.id.secondButton);

        mNameTextView = (TextView) findViewById(R.id.nameTextView);
        mEmailTextView = (TextView) findViewById(R.id.emailTextView);

        mContactListView = (ListView) findViewById(R.id.contactListView);



        listKontaks = new ArrayList();
        mAdapter = new ContactItemAdapter(OrderDetailActivity.this,listKontaks);
        mContactListView.setAdapter(mAdapter);

        LoadDetailOrder task = new LoadDetailOrder(orderid);
        task.execute();
    }

    private void buyOrder()
    {
        updateOrderDialog(currentOrder,2,R.string.prompt_buy_product);
    }

    private void acceptOrder()
    {
        updateOrderDialog(currentOrder,3,R.string.prompt_accept_order);
    }

    public  class RefreshFinish implements IRefreshMethod
    {
        @Override
        public void refresh() {
            finish();
        }
    }
    private void updateOrderDialog(final Order pesanan, final int newstatus, int promptid)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status
                        ChangeOrderTask task = new ChangeOrderTask(pesanan.getOrderid(),newstatus,OrderDetailActivity.this,new RefreshFinish());
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setMessage(getString(promptid)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }

    private void initializeOrderDetail()
    {
        if(currentOrder != null)
        {
            if(responded == 1)
            {

                if(currentOrder.getAccountid()==Integer.valueOf(AppHelper.currentAccount.getAccountid())) {
                    //pelanggan
                    if(currentOrder.getStatusorder() == 3)
                    {
                        mFirstButton.setText(getString(R.string.view_shipping));
                        mSecondButton.setText(getString(R.string.payment));

                        mFirstButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewShipping();
                            }
                        });
                        mSecondButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                payment();
                            }
                        });
                    }
                    else
                    {
                        mFirstButton.setVisibility(View.GONE);
                        mSecondButton.setVisibility(View.GONE);
                    }
                }
                else
                {
                    if(currentOrder.getStatusorder() == 3) {
                        mFirstButton.setText(getString(R.string.shipping));
                        mSecondButton.setText(getString(R.string.payment));

                        mFirstButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shipping();
                            }
                        });
                        mSecondButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewPayment();
                            }
                        });
                    }
                    else
                    {
                        mFirstButton.setVisibility(View.GONE);
                        mSecondButton.setVisibility(View.GONE);
                    }
                }

            }
            else
            {
                mSecondButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                if (Integer.valueOf(buy) == 1) {
                    //pelanggan
                    mFirstButton.setText(getString(R.string.buy));
                    mFirstButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buyOrder();
                        }
                    });
                } else {
                    mFirstButton.setText(getString(R.string.accept));
                    mFirstButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            acceptOrder();
                        }
                    });
                    mNameTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPerson(currentOrder);
                        }
                    });
                }
            }
            mNamaProductTextView.setText(currentOrder.getNamaproduk());
            mShopNameTextView.setText(currentOrder.getNamatoko());
            mPriceTextView.setText(AppHelper.decimalFormat(currentOrder.getHargaproduk()));
            mQtyTextView.setText(String.valueOf(currentOrder.getJumlahproduk()));
            double total = currentOrder.getJumlahproduk() * currentOrder.getHargaproduk();
            mTotalTextView.setText(AppHelper.decimalFormat(total));
            ImageLoadTask task =new ImageLoadTask(currentOrder.getGambarproduk(),mProductImageView);
            task.execute();
            if(currentOrder.getStatusorder() == 0)
            {
                //cancelled
                mStatusTextView.setText(getString(R.string.cancelled));
            }
            else if(currentOrder.getStatusorder() == 1)
            {
                mStatusTextView.setText(getString(R.string.incart));
            }
            else if(currentOrder.getStatusorder() == 2)
            {
                //want to buy
                if(Integer.valueOf(buy) == 1)
                {
                    //pelanggan
                    mStatusTextView.setText(getString(R.string.wanttobuy));
                }
                else
                {
                    mStatusTextView.setText(getString(R.string.pending));
                }
            }
            else if(currentOrder.getStatusorder() == 3)
            {
                //purchased
                if(Integer.valueOf(buy) == 1)
                {
                    //pelanggan
                    mStatusTextView.setText(getString(R.string.accepted));
                }
                else
                {
                    mStatusTextView.setText(getString(R.string.purchased));
                }
            }
        }
    }

    private void viewShipping()
    {
        Intent intent = new Intent(OrderDetailActivity.this,ViewPengirimanActivity.class);
        intent.putExtra(Order.TAG_ORDER, currentOrder);
        startActivity(intent);
    }
    private void shipping()
    {
        Intent intent = new Intent(OrderDetailActivity.this,PengirimanActivity.class);
        intent.putExtra(Order.TAG_ORDER, currentOrder);
        startActivity(intent);
    }
    private void viewPayment()
    {
        Intent intent = new Intent(OrderDetailActivity.this,ViewPaymentActivity.class);
        intent.putExtra(Order.TAG_ORDER, currentOrder);
        startActivity(intent);
    }
    private void payment()
    {
        Intent intent = new Intent(OrderDetailActivity.this,PaymentActivity.class);
        intent.putExtra(Order.TAG_ORDER, currentOrder);
        startActivity(intent);
    }


    public void openPerson(Order order)
    {
        Intent openPersonIntent = new Intent(OrderDetailActivity.this,PersonActivity.class);
        openPersonIntent.putExtra(Account.TAG_ACCOUNTID, String.valueOf(order.getAccountid()));
        startActivity(openPersonIntent);
    }


    private void initializeAccountDetail()
    {
        if(currentAccount != null)
        {
            mNameTextView.setText(currentAccount.getNama());
            mEmailTextView.setText(currentAccount.getEmail());
        }
    }

    public class LoadDetailOrder extends AsyncTask<Void, Void, Boolean>
    {
        String url;
        int orderid;
        String messagejson;
        int successjson;

        public LoadDetailOrder(int orderid)
        {
            this.orderid = orderid;
            url = AppHelper.domainURL + "/AndroidConnect/GetOrderByOrderId.php";
            successjson=-1;
            messagejson="";
            dialog = ProgressDialog.show(OrderDetailActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Order.TAG_ORDERID, String.valueOf(orderid)));
            // Getting JSON String from URL
            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if (json != null)
            {
                try
                {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if(successjson == 1)
                    {
                        JSONArray orderArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject orderObj = orderArray.getJSONObject(0);
                        Order order = Order.GetOrderFromJson(orderObj);
                        if (order != null)
                        {
                           currentOrder = order;
                            return true;
                        }
                        else
                        {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if(AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    }
                    else
                    {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else
            {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                LoadOneAccount task = new LoadOneAccount(currentOrder.getAccountid());
                initializeOrderDetail();
                task.execute();
            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, messagejson, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }

    public class LoadOneAccount extends AsyncTask<Void, Void, Boolean>
    {
        String url;
        int accountid;
        String messagejson;
        int successjson;

        public LoadOneAccount(int accountid)
        {
            this.accountid = accountid;
            url = AppHelper.domainURL + "/AndroidConnect/GetAccountByAccountId.php";
            successjson=-1;
            messagejson="";
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, String.valueOf(accountid)));
            // Getting JSON String from URL
            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if (json != null)
            {
                try
                {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if(successjson == 1)
                    {
                        JSONArray accountArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject accountObj = accountArray.getJSONObject(0);
                        Account account = Account.GetAccountFromJson(accountObj);
                        if (account != null)
                        {
                            currentAccount = account;
                            return true;
                        }
                        else
                        {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if(AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    }
                    else
                    {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else
            {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                LoadAllKontakAccount task = new LoadAllKontakAccount(Integer.valueOf(currentAccount.getAccountid()));
                initializeAccountDetail();
                task.execute();
            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, messagejson, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }
    }

    public class LoadAllKontakAccount extends AsyncTask<Void, Void, Boolean>
    {
        String url;
        int accountid;
        String messagejson;
        int successjson;

        JSONArray mykontakJSON = null;
        List kontaks;

        public LoadAllKontakAccount(int accountid) {
            this.accountid = accountid;
            url = AppHelper.domainURL + "/AndroidConnect/GetAllKontakByAccountId.php";
            successjson=-1;
            messagejson="";
            kontaks = new ArrayList();
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Kontak.TAG_ACCOUNTID, String.valueOf(accountid)));

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null)
            {
                try
                {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        mykontakJSON = json.getJSONArray(Kontak.TAG_KONTAK);

                        for (int i = 0; i < mykontakJSON.length(); i++) {
                            JSONObject orderobject = mykontakJSON.getJSONObject(i);
                            Kontak newkontak = Kontak.GetKontakFromJson(orderobject);
                            kontaks.add(newkontak);
                        }
                        return true;
                    }
                    else if(successjson == 0)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (JSONException e)
                {
                    successjson = 3;
                    if (AppHelper.Message != "") {
                        messagejson = AppHelper.Message;
                    } else {
                        messagejson = e.getMessage();
                    }
                    return false;
                }
            }
            else
            {
                successjson = 3;
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                mAdapter.clear();
                mAdapter.addAll(kontaks);
            }
            else
            {
                Toast.makeText(OrderDetailActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}