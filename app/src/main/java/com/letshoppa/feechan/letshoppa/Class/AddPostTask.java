package com.letshoppa.feechan.letshoppa.Class;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/22/2016.
 */

public class AddPostTask extends AsyncTask<Void, Void, Boolean> {
    private String url;
    private int successjson;
    private String messagejson;
    private Context context;
    private IRefreshMethod refreshMethod;

    ProgressDialog dialog;
    int accountid;
    String tulisan;

    public AddPostTask(String url, int accountid, String tulisan, Context context, IRefreshMethod refreshMethod) {
        messagejson = "";
        successjson = -1;
        this.url = url;
        this.accountid = accountid;
        this.tulisan = tulisan;
        this.context = context;
        this.refreshMethod = refreshMethod;
        dialog = ProgressDialog.show(context, "", context.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Post.TAG_ACCOUNTID, String.valueOf(accountid)));
        parameter.add(new BasicNameValuePair(Post.TAG_TULISAN, tulisan));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
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
        if (success) {
            refreshMethod.refresh();
        }
        dialog.dismiss();
        Toast.makeText(context, messagejson, Toast.LENGTH_SHORT).show();
        //set refresh off
    }
}