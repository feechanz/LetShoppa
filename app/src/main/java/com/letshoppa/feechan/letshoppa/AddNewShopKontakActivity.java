package com.letshoppa.feechan.letshoppa;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.ContacttokoItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Kontaktoko;
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

public class AddNewShopKontakActivity extends AppCompatActivity {

    Toko currentShop;

    private SwipeRefreshLayout swipeContainer;
    private List listMyContacts;
    ArrayAdapter mAdapter;

    EditText contactEditText;
    EditText contactTypeEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_shop_kontak);

        Intent i = this.getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        if(currentShop != null) {
            this.setTitle(currentShop.getNamatoko());
            initializeView();
        }

    }

    private void initializeView()
    {
        Button add_kontak_button = (Button) findViewById(R.id.add_kontak_button);
        Button doneButton = (Button) findViewById(R.id.done_button);
        contactEditText = (EditText) findViewById(R.id.contactEditText);
        contactTypeEditText = (EditText) findViewById(R.id.contactTypeEditText);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_kontak_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });

        ListView listView = (ListView) findViewById(R.id.contactListView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        listMyContacts = new ArrayList();
        mAdapter = new ContacttokoItemAdapter(AddNewShopKontakActivity.this,listMyContacts);

        listView.setAdapter(mAdapter);

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

    private void addContact()
    {
        if(currentShop != null) {
            String jeniskontak = contactTypeEditText.getText().toString();
            String isikontak = contactEditText.getText().toString();
            int tokoid = currentShop.getTokoid();
            String url = AppHelper.domainURL +"/AndroidConnect/AddKontaktoko.php";

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_TOKOID, String.valueOf(tokoid)));
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_ISIKONTAK, isikontak));
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_JENISKONTAK, jeniskontak));
            UpdateDataTask task = new UpdateDataTask(url,parameter,AddNewShopKontakActivity.this, new RefreshMethod());
            task.execute();
        }
    }

    public class RefreshMethod implements IRefreshMethod
    {
        @Override
        public void refresh() {
            fetchShopAsync(0);
            contactEditText.setText("");
            contactTypeEditText.setText("");
        }
    }

    private void fetchShopAsync(int page)
    {
        if(currentShop != null)
        {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllKontaktokoByTokoId.php";
            ContactRefreshLoadTask loadtask = new ContactRefreshLoadTask(url,currentShop.getTokoid(),swipeContainer,mAdapter,AddNewShopKontakActivity.this);
            loadtask.execute();
            //ProductRefreshLoadTask loadTask = new ProductRefreshLoadTask(url, Integer.valueOf(currentShop.getTokoid()), swipeContainer, mAdapter, AddNewShopProductsActivity.this);
            //loadTask.execute((Void) null);
        }
        else
        {
            if(swipeContainer != null)
            {
                swipeContainer.setRefreshing(false);
            }
        }
    }

    class ContactRefreshLoadTask extends AsyncTask<Void, Void, Boolean> {
        SwipeRefreshLayout swipeContainer;
        private String url;
        private int tokoid;
        ArrayAdapter mAdapter;

        String messagejson;
        int successjson;

        Activity activity;

        public ContactRefreshLoadTask(String url, int tokoid, SwipeRefreshLayout
        swipeContainer, ArrayAdapter mAdapter, Activity activity){
            this.mAdapter = mAdapter;
            this.url = url;
            this.tokoid = tokoid;
            this.swipeContainer = swipeContainer;
            this.activity = activity;
            successjson = -1;
            messagejson = "";
            contacts = new ArrayList();

        }

        JSONArray mycontacts = null;
        List contacts;

        @Override
        protected Boolean doInBackground (Void...params){
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            BasicNameValuePair n = new BasicNameValuePair(Kontaktoko.TAG_TOKOID, String.valueOf(tokoid));
            parameter.add(n);

            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        mycontacts = json.getJSONArray(Kontaktoko.TAG_KONTAKTOKO);

                        for (int i = 0; i < mycontacts.length(); i++) {
                            JSONObject jsonobject = mycontacts.getJSONObject(i);
                            Kontaktoko newobject = Kontaktoko.GetKontaktokoFromJson(jsonobject);
                            contacts.add(newobject);
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
                mAdapter.addAll(contacts);
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
