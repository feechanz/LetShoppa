package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
 * Created by Feechan on 11/14/2016.
 */

public class PaymentsLoadTask extends AsyncTask<Void, Void, Boolean> {
    private String url;
    private int orderid;
    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;
    JSONArray mypembayaranJSON = null;
    List pembayarans;

    ProgressDialog dialog;

    public PaymentsLoadTask(String url,int orderid, ArrayAdapter mAdapter, Activity activity)
    {
        this.mAdapter = mAdapter;
        this.url = url;
        this.orderid = orderid;
        this.activity = activity;
        successjson = -1;
        messagejson = "";
        pembayarans = new ArrayList();

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Order.TAG_ORDERID, String.valueOf(orderid)));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    mypembayaranJSON = json.getJSONArray(Pembayaran.TAG_PEMBAYARAN);

                    for (int i = 0; i < mypembayaranJSON.length(); i++)
                    {
                        JSONObject pembayaranobject = mypembayaranJSON.getJSONObject(i);
                        Pembayaran newpembayaran = Pembayaran.GetPembayaranFromJson(pembayaranobject);
                        pembayarans.add(newpembayaran);
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
        } else {
            successjson = 3;
            messagejson = AppHelper.ConnectionFailed;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (!success)
        {
            if(successjson != 0) {
                Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            mAdapter.clear();
            mAdapter.addAll(pembayarans);
        }
        dialog.dismiss();
    }
}
