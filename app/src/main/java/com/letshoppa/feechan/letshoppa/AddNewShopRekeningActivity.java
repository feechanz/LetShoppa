package com.letshoppa.feechan.letshoppa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.RekeningItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Rekening;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddNewShopRekeningActivity extends AppCompatActivity {

    Toko currentShop;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllRekeningsByTokoId.php";
    private SwipeRefreshLayout swipeContainer;
    private List listRekenings;
    ArrayAdapter mAdapter;

    EditText bankNameEditText;
    EditText bankAccountEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop_rekening);

        Intent i = this.getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop != null) {
            this.setTitle(currentShop.getNamatoko());
            initializeView();
        }
    }

    private void initializeView()
    {
        Button add_rekening_button = (Button) findViewById(R.id.add_rekening_button);
        Button doneButton = (Button) findViewById(R.id.done_button);
        bankNameEditText = (EditText) findViewById(R.id.bankNameEditText);
        bankAccountEditText = (EditText) findViewById(R.id.bankAccountEditText);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShopKontak();
            }
        });
        add_rekening_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRekening();
            }
        });

        ListView listView = (ListView) findViewById(R.id.rekeningListView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listRekenings = new ArrayList();
        mAdapter = new RekeningItemAdapter(AddNewShopRekeningActivity.this,listRekenings);

        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Rekening item = (Rekening) parent.getItemAtPosition(position);
                detailRekening(item);
                return true;
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchShopAsync(0);
            }
        });
        fetchShopAsync(0);
    }
    private void detailRekening(final Rekening rekening)
    {
        final CharSequence[] items = { getString(R.string.edit), getString(R.string.delete),getString(R.string.cancel) };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddNewShopRekeningActivity.this);

        builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                switch (item)
                {
                    case 0:
                        editRekening(rekening);
                        break;
                    case 1:
                        deleteRekening(rekening);
                        break;
                    default:

                }
            }

        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    private void editRekening(Rekening rekening)
    {
        Intent editRekAct = new Intent(AddNewShopRekeningActivity.this,EditRekeningActivity.class);
        editRekAct.putExtra(Rekening.TAG_REKENING,rekening);
        startActivity(editRekAct);
    }

    private void deleteRekening(final Rekening rekening)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status
                        String url= AppHelper.domainURL + "/AndroidConnect/DeleteRekening.php";
                        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                        parameter.add(new BasicNameValuePair(Rekening.TAG_REKENINGID,String.valueOf(rekening.getRekeningid())));
                        UpdateDataTask task = new UpdateDataTask(url,parameter,AddNewShopRekeningActivity.this,new RefreshMethod());
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AddNewShopRekeningActivity.this);
        builder.setMessage(getString(R.string.prompt_delete_bank_account_information)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }
    private void openShopKontak()
    {
        Intent intentView = new Intent(this,AddNewShopKontakActivity.class);
        intentView.putExtra(Toko.TAG_TOKO, currentShop);
        finish();
        startActivity(intentView);
    }
    private void addRekening()
    {
        if(currentShop != null) {
            String namabank = bankNameEditText.getText().toString();
            String rekeningbank = bankAccountEditText.getText().toString();
            int tokoid = currentShop.getTokoid();
            String url = AppHelper.domainURL +"/AndroidConnect/PostRekening.php";

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Rekening.TAG_TOKOID, String.valueOf(tokoid)));
            parameter.add(new BasicNameValuePair(Rekening.TAG_NAMABANK, namabank));
            parameter.add(new BasicNameValuePair(Rekening.TAG_NOMORREKENING, rekeningbank));
            UpdateDataTask task = new UpdateDataTask(url,parameter,AddNewShopRekeningActivity.this, new RefreshMethod());
            task.execute();
        }
    }

    public class RefreshMethod implements IRefreshMethod
    {
        @Override
        public void refresh() {
            fetchShopAsync(0);
            bankNameEditText.setText("");
            bankAccountEditText.setText("");
        }
    }

    private void fetchShopAsync(int page)
    {
        if(currentShop != null)
        {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllRekeningsByTokoId.php";
            RekeningRefreshLoadTask loadtask = new RekeningRefreshLoadTask(url,currentShop.getTokoid(),swipeContainer,mAdapter,AddNewShopRekeningActivity.this);
            loadtask.execute();
        }
        else
        {
            if(swipeContainer != null)
            {
                swipeContainer.setRefreshing(false);
            }
        }
    }

    class RekeningRefreshLoadTask extends AsyncTask<Void, Void, Boolean> {
        SwipeRefreshLayout swipeContainer;
        private String url;
        private int tokoid;
        ArrayAdapter mAdapter;

        String messagejson;
        int successjson;

        Activity activity;

        public RekeningRefreshLoadTask(String url, int tokoid, SwipeRefreshLayout
                swipeContainer, ArrayAdapter mAdapter, Activity activity){
            this.mAdapter = mAdapter;
            this.url = url;
            this.tokoid = tokoid;
            this.swipeContainer = swipeContainer;
            this.activity = activity;
            successjson = -1;
            messagejson = "";
            items = new ArrayList();

        }

        JSONArray mycontacts = null;
        List items;

        @Override
        protected Boolean doInBackground (Void...params){
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            BasicNameValuePair n = new BasicNameValuePair(Rekening.TAG_TOKOID, String.valueOf(tokoid));
            parameter.add(n);

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        mycontacts = json.getJSONArray(Rekening.TAG_REKENING);

                        for (int i = 0; i < mycontacts.length(); i++) {
                            JSONObject jsonobject = mycontacts.getJSONObject(i);
                            Rekening newobject = Rekening.GetRekeningFromJson(jsonobject);
                            items.add(newobject);
                        }
                        return true;
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
                messagejson = AppHelper.ConnectionFailed;
                return false;
            }
        }
        @Override
        protected void onPostExecute ( final Boolean success){
            if (success) {
                mAdapter.clear();
                mAdapter.addAll(items);
            } else {
                Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
            }
            //set refresh off
            if (swipeContainer != null) {
                swipeContainer.setRefreshing(false);
            }
        }
    }
}
