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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/24/2016.
 */

public class ReportLoadTask extends AsyncTask<Void, Void, Boolean> {
    private String url;
    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;

    Date endDate;
    Date beginDate;
    ProgressDialog dialog;

    public ReportLoadTask(String url, Date endDate, Date beginDate, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = url;
        this.activity = activity;
        this.beginDate = beginDate;
        this.endDate = endDate;
        successjson = -1;
        messagejson = "";
        reports = new ArrayList();

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
    }

    JSONArray myreports = null;
    List reports;

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Report.TAG_BEGINDATE, beginDate.toString()));
        parameter.add(new BasicNameValuePair(Report.TAG_ENDDATE, endDate.toString()));

        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    myreports = json.getJSONArray(Report.TAG_REPORT);

                    for (int i = 0; i < myreports.length(); i++) {
                        JSONObject reportObj = myreports.getJSONObject(i);
                        Report newReport = Report.GetReportFromJson(reportObj);
                        reports.add(newReport);
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
            mAdapter.addAll(reports);
        }
        else
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
