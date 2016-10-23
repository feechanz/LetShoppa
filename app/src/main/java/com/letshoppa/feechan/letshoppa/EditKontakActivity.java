package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Kontak;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class EditKontakActivity extends AppCompatActivity {
    Kontak currentContact;
    EditText contactTypeEditText;
    EditText contactEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kontak);
        setTitle(getString(R.string.edit_contact));

        Intent i = getIntent();
        currentContact = (Kontak) i.getSerializableExtra(Kontak.TAG_KONTAK);
        if(currentContact != null)
        {
            contactTypeEditText = (EditText) findViewById(R.id.contactTypeEditText);
            contactEditText = (EditText) findViewById(R.id.contactEditText);
            contactTypeEditText.setText(currentContact.getJeniskontak());
            contactEditText.setText(currentContact.getIsikontak());

            Button saveBtn = (Button) findViewById(R.id.saveButton);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveKontak();
                }
            });
        }
        Button cancelBtn = (Button) findViewById(R.id.cancelButton);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveKontak()
    {
        int kontakid = currentContact.getKontakid();
        String jeniskontak = contactTypeEditText.getText().toString();
        String isikontak = contactEditText.getText().toString();
        String url = AppHelper.domainURL +"/AndroidConnect/PutKontak.php";

        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
        parameter.add(new BasicNameValuePair(Kontak.TAG_KONTAKID, String.valueOf(kontakid)));
        parameter.add(new BasicNameValuePair(Kontak.TAG_ISIKONTAK, isikontak));
        parameter.add(new BasicNameValuePair(Kontak.TAG_JENISKONTAK, jeniskontak));
        UpdateDataTask task = new UpdateDataTask(url,parameter,EditKontakActivity.this, new EndActivity());
        task.execute();
    }

    public class EndActivity implements IRefreshMethod
    {

        @Override
        public void refresh() {
            finish();;
        }
    }
}
