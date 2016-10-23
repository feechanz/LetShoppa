package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Feechan on 10/23/2016.
 */

public class ReviewDataLoadTask extends AsyncTask<Void, Void, Boolean> {

    Activity activity;
    TextView scoreTextView;
    TextView jumlahTextView;
    RatingBar scoreRatingBar;
    String url;
    String messagejson;
    int successjson;
    int tokoid;
    double score;
    int jumlah;

    public ReviewDataLoadTask(String url, TextView scoreTextView, TextView jumlahTextView, RatingBar scoreRatingBar, int tokoid, Activity activity) {
        this.url = url;
        this.scoreTextView = scoreTextView;
        this.jumlahTextView = jumlahTextView;
        this.scoreRatingBar = scoreRatingBar;
        this.tokoid = tokoid;
        this.activity = activity;

        this.messagejson="";
        this.successjson=-1;
        this.score=0;
        this.jumlah=0;
    }

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
                score = json.getDouble(Review.TAG_SCORE);
                jumlah = json.getInt(Review.TAG_JUMLAH);

                if (successjson == 1) {
                    return true;
                } else {
                    return false;
                }
            }
            catch (JSONException e)
            {
                successjson = 3;
                if (AppHelper.Message != "")
                {
                    messagejson = AppHelper.Message;
                } else
                {
                    messagejson = e.getMessage();
                }
                return false;
            }
        } else {
            successjson = 3;
            messagejson = AppHelper.NoConnection;
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(!success)
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
        scoreTextView.setText(String.valueOf(score));
        scoreRatingBar.setRating((float)score);
        jumlahTextView.setText(String.valueOf(jumlah));
    }
}
