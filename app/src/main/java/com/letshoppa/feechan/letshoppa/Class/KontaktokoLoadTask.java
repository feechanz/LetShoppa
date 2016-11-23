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
 * Created by Feechan on 11/22/2016.
 */

public class KontaktokoLoadTask extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private int tokoid;

    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;
    JSONArray mykontakJSON = null;
    List kontaks;

    ProgressDialog dialog;
    public KontaktokoLoadTask(String url,int tokoid, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = url;
        this.tokoid = tokoid;
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
        parameter.add(new BasicNameValuePair(Kontaktoko.TAG_TOKOID, String.valueOf(tokoid)));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    mykontakJSON = json.getJSONArray(Kontaktoko.TAG_KONTAKTOKO);

                    for (int i = 0; i < mykontakJSON.length(); i++) {
                        JSONObject jsonobject = mykontakJSON.getJSONObject(i);
                        Kontaktoko newkontak = Kontaktoko.GetKontaktokoFromJson(jsonobject);
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
