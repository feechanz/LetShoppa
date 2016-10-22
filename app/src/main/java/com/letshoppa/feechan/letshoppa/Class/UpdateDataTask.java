package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;
import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Feechan on 10/20/2016.
 */

public class UpdateDataTask extends AsyncTask<Void, Void, Boolean> {

    private int successjson;
    private String messagejson;
    private String url;
    List<NameValuePair> parameter;
    ProgressDialog dialog;
    IRefreshMethod refreshMethod;
    private Activity activity;

    public UpdateDataTask(String url, List<NameValuePair> parameter, Activity activity) {
        this(url,parameter,activity,null);

    }

    public UpdateDataTask(String url, List<NameValuePair> parameter, Activity activity, IRefreshMethod refreshMethod) {
        this.parameter = parameter;
        this.activity = activity;
        this.url = url;
        this.refreshMethod = refreshMethod;
        messagejson="";
        successjson=-1;

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);

    }

    @Override
    protected Boolean doInBackground(Void... params)
    {
        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null)
        {
            try
            {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1)
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
                if (AppHelper.Message != "")
                {
                    messagejson = AppHelper.Message;
                }
                else
                {
                    messagejson = e.getMessage();
                }
                return false;
            }
        }
        else
        {
            successjson = 3;
            messagejson = AppHelper.ConnectionFailed;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success)
    {
        if (success)
        {
            //refreshMethod.refresh();
            if(refreshMethod != null)
            {
                refreshMethod.refresh();
            }
        }
        dialog.dismiss();
        Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        //set refresh off
    }

}
