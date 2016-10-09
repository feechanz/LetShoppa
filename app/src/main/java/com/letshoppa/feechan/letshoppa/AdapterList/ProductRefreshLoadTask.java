package com.letshoppa.feechan.letshoppa.AdapterList;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Produk;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/7/2016.
 */

public class ProductRefreshLoadTask extends AsyncTask<Void, Void, Boolean> {
    SwipeRefreshLayout swipeContainer;
    private String url;
    private int tokoid;
    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;

    public ProductRefreshLoadTask(String url, int tokoid, SwipeRefreshLayout swipeContainer, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = url;
        this.tokoid = tokoid;
        this.swipeContainer = swipeContainer;
        this.activity = activity;
        successjson = -1;
        messagejson = "";
        products = new ArrayList();

    }

    JSONArray myproducts = null;
    List products;

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        BasicNameValuePair n = new BasicNameValuePair(Produk.TAG_TOKOID, String.valueOf(tokoid));
        parameter.add(n);

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    myproducts = json.getJSONArray(Produk.TAG_PRODUK);

                    for (int i = 0; i < myproducts.length(); i++) {
                        JSONObject productobject = myproducts.getJSONObject(i);
                        Produk newproduk = Produk.GetProdukFromJson(productobject);
                        products.add(newproduk);
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
    protected void onPostExecute(final Boolean success) {
        if (success)
        {
            mAdapter.clear();
            mAdapter.addAll(products);
        }
        else
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
        //set refresh off
        if(swipeContainer != null)
        {
            swipeContainer.setRefreshing(false);
        }
    }
}
