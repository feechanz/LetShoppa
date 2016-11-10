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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText mOldPassword;
    EditText mNewPassword;
    EditText mReNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mOldPassword = (EditText) findViewById(R.id.password);
        mNewPassword = (EditText) findViewById(R.id.newpassword);
        mReNewPassword = (EditText) findViewById(R.id.renewpassword);
        Button firstButton = (Button) findViewById(R.id.changeButton);
        Button secondButton = (Button) findViewById(R.id.cancelButton);

        secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword();
            }
        });
    }

    private void editPassword()
    {
        String oldpassword =  mOldPassword.getText().toString();
        String newpassword = mNewPassword.getText().toString();
        String renewpassword = mReNewPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(oldpassword))
        {
            mOldPassword.setError(getString(R.string.error_field_required));
            focusView = mOldPassword;
            cancel = true;
        }

        if(TextUtils.isEmpty(newpassword))
        {
            mNewPassword.setError(getString(R.string.error_field_required));
            focusView = mNewPassword;
            cancel = true;
        }

        if(!TextUtils.equals(newpassword,renewpassword))
        {
            mReNewPassword.setError(getString(R.string.error_repassword_same_password));
            focusView = mReNewPassword;
            cancel = true;
        }

        if(cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            changePassTask task = new changePassTask(oldpassword,newpassword);
            task.execute();
        }
    }

    public class changePassTask extends AsyncTask<Void, Void, Boolean> {

        private String url;
        private final String OldPassword;
        private final String NewPassword;

        String messagejson;
        int successjson;

        ProgressDialog dialog;

        changePassTask(String oldPassword, String newPassword) {
            this.OldPassword = oldPassword;
            this.NewPassword = newPassword;
            url = AppHelper.domainURL + "/AndroidConnect/PutAccountPassword.php";

            dialog = ProgressDialog.show(ChangePasswordActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            // TODO: attempt authentication against a network service.

            // Building Parameters
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, AppHelper.currentAccount.getAccountid()));
            parameter.add(new BasicNameValuePair("oldpassword", OldPassword));
            parameter.add(new BasicNameValuePair(Account.TAG_PASSWORD, NewPassword));

            // Getting JSON String from URL

            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if(json!=null)
            {
                try
                {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if(successjson == 1) {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch(JSONException e)
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
            // TODO: register the new account here.
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(ChangePasswordActivity.this, messagejson, Toast.LENGTH_SHORT).show();
                finish();
            } else
            {
                switch (successjson)
                {
                    case 2:
                        mOldPassword.setError(getString(R.string.old_password_not_same));
                        mOldPassword.requestFocus();
                        break;
                    default:
                        Toast.makeText(ChangePasswordActivity.this, messagejson, Toast.LENGTH_SHORT).show();
                }
            }
            dialog.dismiss();
        }
    }
}
