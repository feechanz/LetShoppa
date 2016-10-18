package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/18/2016.
 */

public class OrderLoadTask extends AsyncTask<Void, Void, Boolean> {

    SwipeRefreshLayout swipeContainer;
    private String url;
    private int accountid;
    private int statusorder;
    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;
    JSONArray myorderJSON = null;
    List orders;

    ProgressDialog dialog;
    public OrderLoadTask(int accountid,int statusorder, SwipeRefreshLayout swipeContainer, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = AppHelper.domainURL+"/AndroidConnect/GetAllOrderByAccountIdAndStatusOrder.php";
        this.accountid = accountid;
        this.statusorder = statusorder;
        this.swipeContainer = swipeContainer;
        this.activity = activity;
        successjson = -1;
        messagejson = "";
        orders = new ArrayList();

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Order.TAG_ACCOUNTID, String.valueOf(accountid)));
        parameter.add(new BasicNameValuePair(Order.TAG_STATUSORDER, String.valueOf(statusorder)));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    myorderJSON = json.getJSONArray(Order.TAG_ORDER);

                    for (int i = 0; i < myorderJSON.length(); i++) {
                        JSONObject orderobject = myorderJSON.getJSONObject(i);
                        Order neworder = Order.GetOrderFromJson(orderobject);
                        orders.add(neworder);
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
            mAdapter.addAll(orders);
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
        dialog.dismiss();
    }
}
