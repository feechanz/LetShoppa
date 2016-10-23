package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/22/2016.
 */

public class ReviewShopLoadTask extends AsyncTask<Void, Void, Boolean> {
    SwipeRefreshLayout swipeContainer;
    private String url;
    private int tokoid;
    ArrayAdapter mAdapter;

    String messagejson;
    int successjson;

    Activity activity;

    public ReviewShopLoadTask(String url, int tokoid, SwipeRefreshLayout swipeContainer, ArrayAdapter mAdapter, Activity activity) {
        this.mAdapter = mAdapter;
        this.url = url;
        this.tokoid = tokoid;
        this.swipeContainer = swipeContainer;
        this.activity = activity;
        successjson = -1;
        messagejson = "";
        reviews = new ArrayList();

    }

    JSONArray myreviews = null;
    List reviews;

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Review.TAG_TOKOID, String.valueOf(tokoid)));
        if(AppHelper.currentAccount != null) {
            parameter.add(new BasicNameValuePair(Review.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
        }
        else {
            parameter.add(new BasicNameValuePair(Review.TAG_ACCOUNTID, "0"));
        }
        JSONObject json = AppHelper.GetJsonObject(url, "POST", parameter);
        if (json != null) {
            try {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    myreviews = json.getJSONArray(Review.TAG_REVIEW);

                    for (int i = 0; i < myreviews.length(); i++) {
                        JSONObject reviewobj = myreviews.getJSONObject(i);
                        Review newreview = Review.GetReviewFromJson(reviewobj);
                        reviews.add(newreview);
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
            mAdapter.addAll(reviews);
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
