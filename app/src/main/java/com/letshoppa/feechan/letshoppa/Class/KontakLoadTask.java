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
 * Created by Feechan on 10/23/2016.
 */

public class KontakLoadTask extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private int accountid;

    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;
    JSONArray mykontakJSON = null;
    List kontaks;

    ProgressDialog dialog;
    public KontakLoadTask(String url,int accountid, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = url;
        this.accountid = accountid;
        this.activity = activity;
        successjson = -1;
        messagejson = "";
        kontaks = new ArrayList();

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Kontak.TAG_ACCOUNTID, String.valueOf(accountid)));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
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
        if (!success)
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAdapter.clear();
            mAdapter.addAll(kontaks);
        }
        //set refresh off
        dialog.dismiss();
    }
}