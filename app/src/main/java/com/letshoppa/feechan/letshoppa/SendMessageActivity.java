package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Pesan;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity {

    int penerimaacountid;
    String namapenerima;
    EditText pesanEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        setTitle(getString(R.string.send_message));

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        pesanEditText = (EditText) findViewById(R.id.pesanEditText);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        penerimaacountid = i.getIntExtra(Pesan.TAG_PENERIMAACCOUNTID,0);
        namapenerima = i.getStringExtra(Pesan.TAG_NAMAPENERIMA);

        if(penerimaacountid != 0)
        {
            TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMessage();
                }
            });
            nameTextView.setText(namapenerima);
        }
    }

    private void sendMessage()
    {
        String isipesan=pesanEditText.getText().toString();

        boolean cancel =false;
        if(TextUtils.isEmpty(isipesan))
        {
            cancel=true;
            pesanEditText.setError(getString(R.string.error_field_required));
            pesanEditText.requestFocus();
        }
        if(AppHelper.currentAccount == null)
        {
            cancel =true;
        }
        if(!cancel)
        {
            String url= AppHelper.domainURL+"/AndroidConnect/AddPesan.php";
            int pengirimaccountid = Integer.valueOf(AppHelper.currentAccount.getAccountid());

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Pesan.TAG_PENGIRIMACCOUNTID, String.valueOf(pengirimaccountid)));
            parameter.add(new BasicNameValuePair(Pesan.TAG_PENERIMAACCOUNTID, String.valueOf(penerimaacountid)));
            parameter.add(new BasicNameValuePair(Pesan.TAG_ISIPESAN, isipesan));
            UpdateDataTask task = new UpdateDataTask(url,parameter,SendMessageActivity.this, new FinishClass());
            task.execute();
        }
    }

    public class FinishClass implements IRefreshMethod
    {

        @Override
        public void refresh() {
            finish();
        }
    }
}
