package com.letshoppa.feechan.letshoppa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.letshoppa.feechan.letshoppa.AdapterList.ContacttokoItemAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Kontaktoko;
import com.letshoppa.feechan.letshoppa.Class.KontaktokoLoadTask;
import com.letshoppa.feechan.letshoppa.Class.Toko;
import com.letshoppa.feechan.letshoppa.Class.UpdateDataTask;
import com.letshoppa.feechan.letshoppa.Interface.IRefreshMethod;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ShopKontakActivity extends AppCompatActivity {

    Toko currentShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_kontak);
        setTitle(getString(R.string.shops_contact));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Intent i = getIntent();
        currentShop = (Toko) i.getSerializableExtra(Toko.TAG_TOKO);

        contactTypeEditText = (EditText) findViewById(R.id.contactTypeEditText);
        contactEditText = (EditText) findViewById(R.id.contactEditText);
        addContactButton = (Button) findViewById(R.id.add_contact_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactDialog();
            }
        });

        //set listview
        listView = (ListView) findViewById(R.id.MyContactListView);
        listKontaks = new ArrayList();
        mAdapter = new ContacttokoItemAdapter(ShopKontakActivity.this,listKontaks);
        listView.setAdapter(mAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Kontaktoko item = (Kontaktoko)parent.getItemAtPosition(position);
                detailKontak(item);
                return true;
            }
        });

        refreshKontak();
    }
    private void detailKontak(final Kontaktoko kontak)
    {
        final CharSequence[] items = { getString(R.string.edit), getString(R.string.delete),getString(R.string.cancel) };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShopKontakActivity.this);

        builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                switch (item)
                {
                    case 0:
                        editKontak(kontak);
                        break;
                    case 1:
                        deleteKontak(kontak);
                        break;
                    default:

                }
            }

        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
    private void editKontak(Kontaktoko kontak)
    {
        Intent editKontakAct = new Intent(ShopKontakActivity.this,EditKontaktokoActivity.class);
        editKontakAct.putExtra(Kontaktoko.TAG_KONTAKTOKO,kontak);
        startActivity(editKontakAct);
    }
    private void deleteKontak(final Kontaktoko kontak)
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        //change status
                        String url= AppHelper.domainURL + "/AndroidConnect/DeleteKontaktoko.php";
                        List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                        parameter.add(new BasicNameValuePair(Kontaktoko.TAG_KONTAKTOKOID,String.valueOf(kontak.getKontaktokoid())));
                        UpdateDataTask task = new UpdateDataTask(url,parameter,ShopKontakActivity.this,new RefreshMethod());
                        task.execute();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ShopKontakActivity.this);
        builder.setMessage(getString(R.string.prompt_delete_contact)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }

    EditText contactTypeEditText;
    EditText contactEditText;
    Button addContactButton;

    ArrayAdapter mAdapter;
    private List listKontaks;
    ListView listView;

    private void addContactDialog() {
        boolean cancel = false;
        String contactType = contactTypeEditText.getText().toString();
        String contact = contactEditText.getText().toString();

        View focusView = null;
        if (TextUtils.isEmpty(contact))
        {
            focusView = contactEditText;
            contactEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if(TextUtils.isEmpty(contactType))
        {
            focusView = contactEditText;
            contactTypeEditText.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if(!cancel) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            addContact();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(ShopKontakActivity.this);
            builder.setMessage(getString(R.string.add_contact) + "?")
                    .setPositiveButton(getString(R.string.yes), dialogClickListener).
                    setNegativeButton(getString(R.string.no), dialogClickListener).show();
        }
        else
        {
            focusView.requestFocus();
        }
    }
    private void addContact()
    {
        //add contact
        if(currentShop != null) {
            String jeniskontak = contactTypeEditText.getText().toString();
            String isikontak = contactEditText.getText().toString();
            int tokoid = currentShop.getTokoid();
            String url = AppHelper.domainURL +"/AndroidConnect/AddKontaktoko.php";

            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_TOKOID, String.valueOf(tokoid)));
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_ISIKONTAK, isikontak));
            parameter.add(new BasicNameValuePair(Kontaktoko.TAG_JENISKONTAK, jeniskontak));
            UpdateDataTask task = new UpdateDataTask(url,parameter,ShopKontakActivity.this, new RefreshMethod());
            task.execute();
        }
    }

    private void refreshKontak()
    {
        if(currentShop != null) {
            String url = AppHelper.domainURL+"/AndroidConnect/GetAllKontaktokoByTokoId.php";
            int tokoid = currentShop.getTokoid();
            KontaktokoLoadTask task = new KontaktokoLoadTask(url, tokoid, mAdapter, ShopKontakActivity.this);
            task.execute();
        }
    }

    public class RefreshMethod implements IRefreshMethod
    {
        @Override
        public void refresh() {
            refreshKontak();
            contactEditText.setText("");
            contactTypeEditText.setText("");
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        refreshKontak();
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
