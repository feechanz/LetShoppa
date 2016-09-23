package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.R;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by Feechan on 9/23/2016.
 */

public class UploadLoadTask extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private List<NameValuePair> parameter;
    private String pathFile;
    private Activity activity;
    private int successjson;
    private String messagejson;
    //private ProgressBar progressBar;
    ProgressDialog dialog;

    public UploadLoadTask(String url, String pathFile, List<NameValuePair> parameter, Activity activity) {
        this.url = url;
        this.pathFile = pathFile;
        this.parameter = parameter;
        this.activity = activity;
        //this.progressBar = progressBar;
        this.successjson = 0;
        this.messagejson = "";
        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        File file = new File(pathFile);
        if(file != null)
        {
            JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter, file);
            if (json != null)
            {
                try {
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
                } catch (JSONException e) {
                    successjson = 3;
                    messagejson = e.getMessage();
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
        else
        {
            successjson = 3;
            messagejson = AppHelper.NoFile;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        super.onPostExecute(success);
        //progressBar.setVisibility(View.GONE);
        dialog.dismiss();
        if(success)
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
    }

}