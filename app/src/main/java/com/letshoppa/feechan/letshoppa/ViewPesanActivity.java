package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Pesan;

public class ViewPesanActivity extends AppCompatActivity {

    Pesan currentPesan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pesan);

        setTitle(getString(R.string.message));

        Intent i = getIntent();
        currentPesan = (Pesan) i.getSerializableExtra(Pesan.TAG_PESAN);
        if(currentPesan != null)
        {
            TextView fromTextView = (TextView) findViewById(R.id.fromTextView);
            TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
            TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

            EditText pesanEditText = (EditText) findViewById(R.id.pesanEditText);
            Button closeButton = (Button) findViewById(R.id.closeButton);
            Button replyButton = (Button) findViewById(R.id.replyButton);

            dateTextView.setText(currentPesan.getTanggalpesan().toString());
            pesanEditText.setText(currentPesan.getIsipesan());

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            if(AppHelper.currentAccount != null)
            {
                if(currentPesan.getPengirimaccountid() == Integer.valueOf(AppHelper.currentAccount.getAccountid()))
                {
                    //sentbox
                    fromTextView.setText(getString(R.string.to__));
                    replyButton.setVisibility(View.GONE);
                    nameTextView.setText(currentPesan.getNamapenerima());
                }
                else
                {
                    //inbox
                    nameTextView.setText(currentPesan.getNamapengirim());
                    replyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            replyMessage();
                        }
                    });
                }

            }
        }
    }

    private  void replyMessage()
    {
        if(currentPesan != null) {
            int penerimaaccountid = currentPesan.getPengirimaccountid();
            String namapenerima = currentPesan.getNamapengirim();
            Intent sendPesanAct = new Intent(ViewPesanActivity.this, SendMessageActivity.class);
            sendPesanAct.putExtra(Pesan.TAG_PENERIMAACCOUNTID, penerimaaccountid);
            sendPesanAct.putExtra(Pesan.TAG_NAMAPENERIMA, namapenerima);
            startActivity(sendPesanAct);
        }
    }
}
