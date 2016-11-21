package com.letshoppa.feechan.letshoppa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.AdapterList.RekeningItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Rekening;
import com.letshoppa.feechan.letshoppa.Class.RekeningLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ShopRekeningActivity extends AppCompatActivity {

    EditText mBankAccountEditText;
    EditText mBankNameEditText;

    Toko currentShop;

    ArrayAdapter mAdapter;
    private List listRekenings;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_rekening);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        mBankAccountEditText = (EditText) findViewById(R.id.bankAccountEditText);
        mBankNameEditText = (EditText) findViewById(R.id.bankNameEditText);

        listView = (ListView) findViewById(R.id.bankListView);
        listRekenings = new ArrayList();
        mAdapter = new RekeningItemAdapter(ShopRekeningActivity.this,listRekenings);
        listView.setAdapter(mAdapter);
        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Rekening item = (Kontak)parent.getItemAtPosition(position);
                detailKontak(item);
                return true;
            }
        });*/
        Button addBankButton = (Button) findViewById(R.id.add_bank_button);
        addBankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRekeningDialog();
            }
        });
        if(currentShop != null) {

            TextView shopNameTextView = (TextView) findViewById(R.id.shopNameTextView);
            shopNameTextView.setText(currentShop.getNamatoko());
            refreshRekening();
        }
    }

    private void refreshRekening()
    {
        if(currentShop != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllRekeningsByTokoId.php";
            int tokoid = Integer.valueOf(currentShop.getTokoid());
            RekeningLoadTask task = new RekeningLoadTask(url, tokoid, mAdapter, ShopRekeningActivity.this);
            task.execute();
        }
    }

    private void addRekeningDialog() {
        boolean cancel = false;
        String bankaccountnumber = mBankAccountEditText.getText().toString();
        String bankname = mBankNameEditText.getText().toString();

        View focusView = null;
        if (TextUtils.isEmpty(bankaccountnumber))
        {
            focusView = mBankAccountEditText;
            mBankAccountEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(TextUtils.isEmpty(bankname))
        {
            focusView = mBankNameEditText;
            mBankNameEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(!cancel) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            addBank();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(ShopRekeningActivity.this);
            builder.setMessage(getString(R.string.add_bank_account) + "?")
                    .setPositiveButton(getString(R.string.yes), dialogClickListener).
                    setNegativeButton(getString(R.string.no), dialogClickListener).show();
        }
        else
        {
            focusView.requestFocus();
        }
    }
    private void addBank()
    {
        //add bank
        if(currentShop != null) {
            String namabank = mBankNameEditText.getText().toString();
            String rekeningbank = mBankAccountEditText.getText().toString();
            int tokoid = currentShop.getTokoid();
            String url = AppHelper.domainURL +"/AndroidConnect/PostRekening.php";

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Rekening.TAG_TOKOID, String.valueOf(tokoid)));
            parameter.add(new BasicNameValuePair(Rekening.TAG_NAMABANK, namabank));
            parameter.add(new BasicNameValuePair(Rekening.TAG_NOMORREKENING, rekeningbank));
            UpdateDataTask task = new UpdateDataTask(url,parameter,ShopRekeningActivity.this,new RefreshMethod());
            task.execute();
        }
    }
    class RefreshMethod implements IRefreshMethod
    {

        @Override
        public void refresh() {
            refreshRekening();
            mBankNameEditText.setText("");
            mBankAccountEditText.setText("");
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
