package com.letshoppa.feechan.letshoppa.Class;

import android.app.Activity;
import android.app.Dialog;
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
 * Created by Feechan on 10/10/2016.
 */

public class LoadKategoriTask extends AsyncTask<Void, Void, Boolean> {
    Activity activity;
    int jenistokoid;
    ArrayAdapter mAdapter;
    String messagejson;
    int successjson;
    Dialog dialog;
    String url = AppHelper.domainURL+"/AndroidConnect/GetAllKategoriProdukByJenistokoid.php";

    JSONArray mykategoris = null;
    List kategoris;

    public LoadKategoriTask(Activity activity, int jenistokoid, ArrayAdapter mAdapter) {
        this.activity = activity;
        this.jenistokoid = jenistokoid;
        this.mAdapter = mAdapter;

        dialog = ProgressDialog.show(activity, "", activity.getString(R.string.please_wait), true);
        dialog.setCancelable(false);
        kategoris = new ArrayList();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Toko.TAG_JENISTOKOID, String.valueOf(jenistokoid)));
        JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
        if(json != null)
        {
            try
            {
                successjson = json.getInt(AppHelper.TAG_SUCCESS);
                messagejson = json.getString(AppHelper.TAG_MESSAGE);
                if (successjson == 1) {
                    mykategoris = json.getJSONArray(Kategoriproduk.TAG_KATEGORIPRODUK);

                    for (int i = 0; i < mykategoris.length(); i++) {
                        JSONObject kategoriobject = mykategoris.getJSONObject(i);
                        Kategoriproduk kategori = Kategoriproduk.GetKategoriprodukFromJson(kategoriobject);
                        kategoris.add(kategori);
                    }
                    return true;
                } else {
                    return false;
                }
            }
            catch (JSONException e)
            {
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
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success)
        {
            mAdapter.clear();
            mAdapter.addAll(kategoris);
        }
        else
        {
            Toast.makeText(activity, messagejson, Toast.LENGTH_SHORT).show();
        }
        dialog.dismiss();
    }
}
