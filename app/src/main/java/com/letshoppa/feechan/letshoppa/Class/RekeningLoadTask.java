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
 * Created by Feechan on 11/21/2016.
 */

public class RekeningLoadTask extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private int tokoid;

    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;
    JSONArray myrekeningJson = null;
    List rekenings;

    ProgressDialog dialog;
    public RekeningLoadTask(String url,int tokoid, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = url;
        this.tokoid = tokoid;
        this.activity = activity;
        successjson = -1;
        messagejson = "";
        rekenings = new ArrayList();

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Rekening.TAG_TOKOID, String.valueOf(tokoid)));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    myrekeningJson = json.getJSONArray(Rekening.TAG_REKENING);

                    for (int i = 0; i < myrekeningJson.length(); i++) {
                        JSONObject orderobject = myrekeningJson.getJSONObject(i);
                        Rekening newkontak = Rekening.GetRekeningFromJson(orderobject);
                        rekenings.add(newkontak);
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
            mAdapter.addAll(rekenings);
        }
        //set refresh off
        dialog.dismiss();
    }
}
