package com.letshoppa.feechan.letshoppa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.ImageLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Order;
import com.letshoppa.feechan.letshoppa.Class.Produk;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        initializeProduct();
    }
    Produk currentProduk;
    NumberPicker qtyNumberPicker;
    private void initializeProduct() {
        Intent i = this.getIntent();
        currentProduk = (Produk) i.getSerializableExtra(Produk.TAG_PRODUK);

        if(currentProduk != null) {
            TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
            ImageView produkImageView = (ImageView) findViewById(R.id.productImageView);
            TextView priceTextView = (TextView) findViewById(R.id.priceTextView);
            TextView descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);

            nameTextView.setText(currentProduk.getNamaproduk());
            priceTextView.setText("Rp. " + currentProduk.getHargaproduk());
            descriptionTextView.setText(currentProduk.getDeskripsiproduk());
            ImageLoadTask imageTask = new ImageLoadTask(currentProduk.getGambarproduk(), produkImageView);
            imageTask.execute();

            qtyNumberPicker = (NumberPicker) findViewById(R.id.qtyNumberPicker);
            qtyNumberPicker.setMinValue(1);
            qtyNumberPicker.setMaxValue(1000);
            qtyNumberPicker.setValue(1);
            qtyNumberPicker.setWrapSelectorWheel(true);
            setTitle(currentProduk.getNamaproduk());

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
    }
    private void addOrder()
    {
        int jumlah = qtyNumberPicker.getValue();
        if(AppHelper.currentAccount != null) {
            AddOrderTask task = new AddOrderTask(jumlah);
            task.execute();
        }
        else
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
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
                        addOrder();


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

    public class AddOrderTask extends AsyncTask<Void, Void, Boolean>
    {
        String url = AppHelper.domainURL+"/AndroidConnect/AddOrderToCart.php";
        ProgressDialog dialog;
        String messagejson;
        int successjson;

        int jumlah;
        public AddOrderTask(int jumlah)
        {
            this.jumlah = jumlah;
            dialog = ProgressDialog.show(ProductDetailActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
            messagejson="";
            successjson=-1;
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            if(AppHelper.currentAccount != null && currentProduk != null)
            {
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID,AppHelper.currentAccount.getAccountid()));
                parameter.add(new BasicNameValuePair(Order.TAG_NAMAPRODUK,currentProduk.getNamaproduk()));
                parameter.add(new BasicNameValuePair(Order.TAG_DESKRIPSIPRODUK,currentProduk.getDeskripsiproduk()));
                parameter.add(new BasicNameValuePair(Order.TAG_HARGAPRODUk,String.valueOf(currentProduk.getHargaproduk())));
                parameter.add(new BasicNameValuePair(Order.TAG_JUMLAHPRODUK,String.valueOf(jumlah)));
                parameter.add(new BasicNameValuePair(Order.TAG_PRODUKID,String.valueOf(currentProduk.getProdukid())));
                parameter.add(new BasicNameValuePair(Order.TAG_GAMBARPRODUK,currentProduk.getGambarproduk()));
                JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
                if(json != null)
                {
                    try
                    {
                        successjson = json.getInt(AppHelper.TAG_SUCCESS);
                        messagejson = json.getString(AppHelper.TAG_MESSAGE);
                        if(successjson == 1)
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
                        messagejson = e.getMessage();
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
            else
            {
                messagejson = getString(R.string.unknown_error);
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success)
        {
            if(success)
            {
                Toast.makeText(ProductDetailActivity.this, getString(R.string.success),Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                Toast.makeText(ProductDetailActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }
}