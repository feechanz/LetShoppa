package com.letshoppa.feechan.letshoppa;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {

    EditText voucherEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        voucherEditText = (EditText) findViewById(R.id.voucherEditText);
        Button userButton = (Button) findViewById(R.id.useButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useVoucher();
            }
        });
    }

    public final String TAG_VOUCHERCODE="vouchercode";
    private void useVoucher()
    {
        String vouchercode = voucherEditText.getText().toString();
        boolean cancel = false;
        if(TextUtils.isEmpty(vouchercode))
        {
            voucherEditText.setError(getString(R.string.error_field_required));
            voucherEditText.requestFocus();
            cancel = true;
        }
        if(!cancel)
        {
            String url = AppHelper.domainURL + "/AndroidConnect/PutPremiumService.php";
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
            param.add(new BasicNameValuePair(TAG_VOUCHERCODE, vouchercode));

            voucherTask task = new voucherTask(url,param);
            task.execute();
        }
    }
    ProgressDialog dialog;
    private class voucherTask extends AsyncTask<Void, Void, Boolean>
    {

        private int successjson;
        private String messagejson;
        private String url;
        List<NameValuePair> parameter;


        public voucherTask(String url, List<NameValuePair> parameter) {
            this.parameter = parameter;
            this.url = url;
            messagejson="";
            successjson=-1;

            dialog = ProgressDialog.show(VoucherActivity.this, "", VoucherActivity.this.getString(R.string.please_wait), true);
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
            if(success)
            {
                UserRefreshTask task = new UserRefreshTask();
                task.execute();
            }
            else {
                dialog.dismiss();
                Toast.makeText(VoucherActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            //set refresh off
        }
    }

    public class UserRefreshTask extends AsyncTask<Void, Void, Boolean> {

        private final String url_refresh_account = AppHelper.domainURL+"/AndroidConnect/GetAccountByAccountId.php";
        private final String mAccountid;

        String messagejson;
        int successjson;

        UserRefreshTask() {
            mAccountid = AppHelper.currentAccount.getAccountid();
            messagejson = "";
            successjson = -1;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, mAccountid));
            JSONObject json = AppHelper.GetJsonObject(url_refresh_account, "POST", parameter);
            if (json != null) {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1) {
                        JSONArray accountArray = json.getJSONArray(Account.TAG_ACCOUNT);
                        JSONObject accountObj = accountArray.getJSONObject(0);
                        Account account = Account.GetAccountFromJson(accountObj);
                        if (account != null)
                        {
                            AppHelper.currentAccount = account;
                            return true;
                        }
                        else
                        {
                            successjson = 3;
                            messagejson = AppHelper.Message;
                            return false;
                        }
                    } else {
                        return false;
                    }
                } catch (JSONException e)
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
        protected void onPostExecute(final Boolean success) {
            dialog.dismiss();
            if (success)
            {
                AppHelper.createLoginSession();
                AppHelper.getAccountFromSession();
                finish();
            }
            else
            {
                Toast.makeText(VoucherActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
