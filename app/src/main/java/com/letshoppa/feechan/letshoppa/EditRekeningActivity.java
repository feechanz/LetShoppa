package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Rekening;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class EditRekeningActivity extends AppCompatActivity {

    Rekening currentRekening;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rekening);
        setTitle(getString(R.string.edit_shops_bank_account));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentRekening = (Rekening) i.getSerializableExtra(Rekening.TAG_REKENING);
        if(currentRekening != null)
        {
            bankNameEditText = (EditText) findViewById(R.id.bankNameEditText);
            bankAccountEditText = (EditText) findViewById(R.id.bankAccountEditText);

            Button cancelButton = (Button) findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            Button saveButton = (Button) findViewById(R.id.saveButton);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveData();
                }
            });

            bankNameEditText.setText(currentRekening.getNamabank());
            bankAccountEditText.setText(currentRekening.getNomorrekening());
        }
    }
    EditText bankNameEditText;
    EditText bankAccountEditText;

    private void saveData()
    {
        int rekeningid = currentRekening.getRekeningid();
        String bankname = bankNameEditText.getText().toString();
        String bankrekening = bankAccountEditText.getText().toString();
        String url = AppHelper.domainURL +"/AndroidConnect/PutRekening.php";
        Boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(bankname))
        {
            cancel = true;
            focusView = bankNameEditText;
            bankNameEditText.setError(getString(R.string.error_field_required));
        }
        if(TextUtils.isEmpty(bankrekening))
        {
            cancel = true;
            focusView = bankAccountEditText;
            bankAccountEditText.setError(getString(R.string.error_field_required));
        }

        if(!cancel)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Rekening.TAG_REKENINGID, String.valueOf(rekeningid)));
            parameter.add(new BasicNameValuePair(Rekening.TAG_NAMABANK, bankname));
            parameter.add(new BasicNameValuePair(Rekening.TAG_NOMORREKENING, bankrekening));
            UpdateDataTask task = new UpdateDataTask(url, parameter, EditRekeningActivity.this, new EndActivity());
            task.execute();
        }
        else
        {
            focusView.requestFocus();
        }
    }

    public class EndActivity implements IRefreshMethod
    {
        @Override
        public void refresh() {
            finish();;
        }
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
