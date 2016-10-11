package com.letshoppa.feechan.letshoppa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.KategoriprodukItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Kategoriproduk;
import com.letshoppa.feechan.letshoppa.Class.LoadKategoriTask;
import com.letshoppa.feechan.letshoppa.Class.Produk;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        setTitle(getString(R.string.AddProduct));
        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);
        if(currentShop != null)
        {
            setSpinner();
        }
        initializeView();
        //this.setFinishOnTouchOutside(false);
    }
    Toko currentShop;

    Spinner kategoriprodukspinner;
    ArrayAdapter mAdapter;

    EditText nameEditText;
    EditText deskripsiEditText;
    EditText priceEditText;
    Button addButton;
    private void setSpinner()
    {
        kategoriprodukspinner = (Spinner) findViewById(R.id.kategoriproductspinner);
        List listkategori = new ArrayList();
        mAdapter = new KategoriprodukItemAdapter(AddProductsActivity.this, listkategori);
        kategoriprodukspinner.setAdapter(mAdapter);
        LoadKategoriTask kategoriTask = new LoadKategoriTask(this,currentShop.getJenistokoid(),mAdapter);
        kategoriTask.execute();
    }

    private void initializeView()
    {
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        deskripsiEditText = (EditText) findViewById(R.id.deskripsiEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        addButton = (Button) findViewById(R.id.add_product_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductDialog();
            }
        });
    }
    private Produk validateProduct()
    {
        Kategoriproduk kategoriproduk = (Kategoriproduk) kategoriprodukspinner.getSelectedItem();
        String nameProduk = nameEditText.getText().toString();
        String deskripsiProduk = deskripsiEditText.getText().toString();
        String priceProduk = priceEditText.getText().toString();
        View focusView = null;
        boolean cancel = false;

        if(kategoriproduk == null)
        {
            cancel = true;
        }
        if(currentShop == null)
        {
            cancel = true;
        }

        if(TextUtils.isEmpty(nameProduk))
        {
            nameEditText.setError(getString(R.string.error_field_required));;
            focusView = nameEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(deskripsiProduk))
        {
            deskripsiEditText.setError(getString(R.string.error_field_required));
            focusView = deskripsiEditText;
            cancel = true;
        }
        if(TextUtils.isEmpty(priceProduk))
        {
            priceEditText.setError(getString(R.string.error_field_required));
            focusView = priceEditText;
            cancel = true;
        }

        if(cancel)
        {
            if(focusView != null)
            {
                focusView.requestFocus();
            }
            else
            {
                Toast.makeText(AddProductsActivity.this,getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        else
        {
            Produk result = new Produk();
            //
            result.setKategoriprodukid(kategoriproduk.getKategoriprodukid());
            result.setNamaproduk(nameProduk);
            result.setDeskripsiproduk(deskripsiProduk);
            result.setHargaproduk(Double.valueOf(priceProduk));
            result.setTokoid(currentShop.getTokoid());

            return result;
        }
    }
    private  void addProduct()
    {
        Produk produk = validateProduct();
        if(produk != null)
        {
            AddProdukTask addTask = new AddProdukTask(produk);
            addTask.execute();
        }
    }
    private void addProductDialog()
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        addProduct();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.prompt_create_product)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }

    public class AddProdukTask extends AsyncTask<Void, Void, Boolean> {
        Produk produk;

        String url= AppHelper.domainURL+"/AndroidConnect/AddProdukByTokoId.php";
        ProgressDialog dialog;
        String messagejson;
        int successjson;
        public AddProdukTask(Produk produk) {
            this.produk = produk;
            dialog = ProgressDialog.show(AddProductsActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
            messagejson="";
            successjson=0;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(produk != null && currentShop != null)
            {
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                parameter.add(new BasicNameValuePair(Toko.TAG_TOKOID,String.valueOf(produk.getTokoid())));
                parameter.add(new BasicNameValuePair(Produk.TAG_NAMAPRODUK, produk.getNamaproduk()));
                parameter.add(new BasicNameValuePair(Produk.TAG_DESKRIPSIPRODUK, produk.getDeskripsiproduk()));
                parameter.add(new BasicNameValuePair(Produk.TAG_HARGAPRODUK, String.valueOf(produk.getHargaproduk())));
                parameter.add(new BasicNameValuePair(Produk.TAG_KATEGORIPRODUKID, String.valueOf(produk.getKategoriprodukid())));

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
        protected void onPostExecute(final Boolean success) {
            if (success)
            {
                Toast.makeText(AddProductsActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(AddProductsActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

}
