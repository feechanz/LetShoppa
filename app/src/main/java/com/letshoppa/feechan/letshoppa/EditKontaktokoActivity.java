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
import com.letshoppa.feechan.letshoppa.Class.Kontaktoko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class EditKontaktokoActivity extends AppCompatActivity {

    Kontaktoko currentKontaktoko;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kontaktoko);
        setTitle(getString(R.string.edit_shops_contact));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentKontaktoko = (Kontaktoko) i.getSerializableExtra(Kontaktoko.TAG_KONTAKTOKO);
        if(currentKontaktoko != null)
        {
            contactTypeEditText = (EditText) findViewById(R.id.contactTypeEditText);
            contactEditText = (EditText) findViewById(R.id.contactEditText);

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

            contactTypeEditText.setText(currentKontaktoko.getJeniskontak());
            contactEditText.setText(currentKontaktoko.getIsikontak());
        }
    }

    EditText contactTypeEditText;
    EditText contactEditText;

    private void saveData()
    {
        int kontaktokoid = currentKontaktoko.getKontaktokoid();
        String jeniskontak = contactTypeEditText.getText().toString();
        String isikontak = contactEditText.getText().toString();
        String url = AppHelper.domainURL +"/AndroidConnect/PutKontaktoko.php";
        Boolean cancel = false;
        View focusView = null;
        if(TextUtils.isEmpty(jeniskontak))
        {
            cancel = true;
            focusView = contactTypeEditText;
            contactTypeEditText.setError(getString(R.string.error_field_required));
        }
        if(TextUtils.isEmpty(isikontak))
        {
            cancel = true;
            focusView = contactEditText;
            contactEditText.setError(getString(R.string.error_field_required));
        }

        if(!cancel)
        {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_KONTAKTOKOID, String.valueOf(kontaktokoid)));
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_JENISKONTAK, jeniskontak));
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_ISIKONTAK, isikontak));
            UpdateDataTask task = new UpdateDataTask(url, parameter, EditKontaktokoActivity.this, new EndActivity());
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
