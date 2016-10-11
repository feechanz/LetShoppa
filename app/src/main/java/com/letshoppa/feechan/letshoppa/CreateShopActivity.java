package com.letshoppa.feechan.letshoppa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.JenistokoAdapter;
import com.letshoppa.feechan.letshoppa.Class.Account;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.GpsService;
import com.letshoppa.feechan.letshoppa.Class.Jenistoko;
import com.letshoppa.feechan.letshoppa.Class.Toko;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreateShopActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);
        setInitializeView();
        setInitializeSpinner();
        setGPS();
        this.setTitle(getString(R.string.create_shop));
    }
    Button createButton;
    Button viewSelectedLocationButton;
    private List listJenisToko;
    private JenistokoAdapter mAdapter;
    GpsService gps;
    TextView locationTextView;
    EditText locationEditText;
    EditText nameEditText;
    EditText deskripsiEditText;
    Spinner kategorishopspinner;
    private void setInitializeView()
    {
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        deskripsiEditText = (EditText) findViewById(R.id.deskripsiEditText);
        kategorishopspinner = (Spinner) findViewById(R.id.kategorishopspinner);

        locationEditText = (EditText) findViewById(R.id.locationEditText);
        locationTextView = (TextView) findViewById(R.id.locationTextView);

        viewSelectedLocationButton = (Button) findViewById(R.id.viewSelectedLocationButton);
        createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createShopDialog();
            }
        });
        viewSelectedLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocationOnMap();
            }
        });
    }
    private void setInitializeSpinner()
    {
        Spinner spinner = (Spinner) findViewById(R.id.kategorishopspinner);

        listJenisToko = new ArrayList();
        listJenisToko.add(new Jenistoko(1,"Toko Fashion & Aksesoris"));
        listJenisToko.add(new Jenistoko(2,"Toko Pakaian"));
        listJenisToko.add(new Jenistoko(3,"Toko Elektronik"));
        listJenisToko.add(new Jenistoko(4,"Toko Mainan & Hobi"));
        listJenisToko.add(new Jenistoko(5,"Toko DVD/CD Film, Musik, Game"));

        mAdapter = new JenistokoAdapter(CreateShopActivity.this, listJenisToko);
        spinner.setAdapter(mAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Jenistoko jenistoko = (Jenistoko)mAdapter.getItem(position);
                Toast.makeText(CreateShopActivity.this, getString(R.string.selected)+" : " + jenistoko.getNamajenis(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private Toko validationShop()
    {
        String namatoko = nameEditText.getText().toString();
        String deskripsitoko = deskripsiEditText.getText().toString();
        String lokasitoko = locationEditText.getText().toString();
        double latitude = latitudeShop;
        double longitude = longitudeShop;
        Jenistoko jenistoko = (Jenistoko) kategorishopspinner.getSelectedItem();
        View focusView = null;

        boolean cancel = false;

        if(TextUtils.isEmpty(namatoko))
        {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(deskripsitoko))
        {
            deskripsiEditText.setError(getString(R.string.error_field_required));
            focusView = deskripsiEditText;
            cancel = true;
        }

        if(TextUtils.isEmpty(lokasitoko))
        {
            locationEditText.setError(getString(R.string.error_field_required));
            focusView = locationEditText;
            cancel = true;
        }
        if(AppHelper.currentAccount == null)
        {
            cancel = true;
        }
        if(jenistoko == null)
        {
            cancel = true;
        }
        if(cancel)
        {
            if(focusView != null) {
                focusView.requestFocus();
            }
            else
            {
                Toast.makeText(CreateShopActivity.this,getString(R.string.unknown_error),Toast.LENGTH_SHORT).show();
            }
            return null;
        }
        else
        {
            Toko result = new Toko();
            result.setNamatoko(namatoko);
            result.setDeskripsitoko(deskripsitoko);
            result.setLokasitoko(lokasitoko);
            result.setLatitude(latitude);
            result.setLongitude(longitude);
            result.setAccountid(Integer.valueOf(AppHelper.currentAccount.getAccountid()));
            result.setJenistokoid(jenistoko.getJenistokoid());
            return result;
        }
    }

    private void createNewShop()
    {
        Toko shop = validationShop();
        if(shop != null)
        {
            CreateShopTask task = new CreateShopTask(shop);
            task.execute();
        }
    }
    private void createShopDialog()
    {
        DialogInterface.OnClickListener dialogClickListener =new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE:
                        createNewShop();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.prompt_create_shop)+"?")
                .setPositiveButton(getString(R.string.yes),dialogClickListener).
                setNegativeButton(getString(R.string.no),dialogClickListener).show();
    }
    private void viewLocationOnMap()
    {
        Intent intentView = new Intent(this,ViewOneMapsActivity.class);
        intentView.putExtra(AppHelper.TAG_LATITUDE,this.latitudeShop);
        intentView.putExtra(AppHelper.TAG_LONGITUDE,this.longitudeShop);
        startActivity(intentView);
    }
    double latitudeShop ;
    double longitudeShop ;
    private void setGPS()
    {

        Button mButtonSetLocation = (Button) findViewById(R.id.set_location_button);
        mButtonSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocation();
            }
        });
    }
    private void setLocation()
    {
        if(gps== null) {
            gps = new GpsService(CreateShopActivity.this);
        }
        gps.getLocation();
        if(gps.isCanUseGps())
        {

            latitudeShop = gps.getLatitude();
            longitudeShop = gps.getLongitude();
            if(gps.isCanGetLocation())
            {
                LocationAddresTask locationAdd = new LocationAddresTask(latitudeShop,longitudeShop);

                //set enable
                locationEditText.setEnabled(true);
                viewSelectedLocationButton.setEnabled(true);
                createButton.setEnabled(true);
                locationAdd.execute((Void) null);
            }
            else
            {
                Toast.makeText(CreateShopActivity.this,getString(R.string.waiting_for_location),Toast.LENGTH_SHORT).show();
            }
            gps.stopUsingGPS();
        }
        else
        {
            Toast.makeText(CreateShopActivity.this,getString(R.string.error_cant_get_location),Toast.LENGTH_SHORT).show();
            gps.showSettingAlert();
        }
    }
    private final String TAG_RESULTS = "results";
    private final String TAG_FORMATTED_ADDRESS = "formatted_address";
    public class LocationAddresTask extends AsyncTask<Void, Void, Boolean> {
        private final double latitude;
        private final double longitude;
        public String result;
        public String resultLangLong;
        ProgressDialog dialog;

        LocationAddresTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;

            dialog = ProgressDialog.show(CreateShopActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
        }
        @Override
        protected Boolean doInBackground(Void... params)
        {
            result = "";
            resultLangLong="";
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true";
            //String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=-6.886216599999999,107.5807599&sensor=true";
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            // Getting JSON String from URL

            JSONObject json = AppHelper.GetJsonObject(url, "GET", parameter);
            if (json != null) {
                try {
                    JSONArray resultArray = json.getJSONArray(TAG_RESULTS);
                    if (resultArray.length() > 0) {
                        JSONObject jsonObject = resultArray.getJSONObject(0);
                        result = jsonObject.getString(TAG_FORMATTED_ADDRESS) ;
                        resultLangLong = getString(R.string.Coordinate) + " : " + latitude + "," + longitude;

                    } else {
                        result = getString(R.string.error_cant_get_location);
                    }
                } catch (JSONException e) {
                    result = getString(R.string.error_json);
                }

            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {

            dialog.dismiss();
            if (success) {
                Toast.makeText(CreateShopActivity.this, getString(R.string.selected) + " : " + result, Toast.LENGTH_LONG).show();
                locationTextView.setText(resultLangLong);
                locationEditText.setText(result);
            } else {
                Toast.makeText(CreateShopActivity.this, getString(R.string.error_cant_get_location), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
        }
    }

    private void openAddProductActivity(Toko newtoko)
    {
        Intent intentView = new Intent(this,AddNewShopProductsActivity.class);
        intentView.putExtra(Toko.TAG_TOKO, newtoko);
        finish();
        startActivity(intentView);
    }

    public class CreateShopTask extends  AsyncTask<Void, Void, Boolean> {
        Toko toko;
        int tokoid;

        String messagejson;
        int successjson;
        String url=AppHelper.domainURL+"/AndroidConnect/AddShopByAccountId.php";

        ProgressDialog dialog;

        public CreateShopTask(Toko toko) {
            this.toko = toko;
            this.tokoid = 0;
            dialog = ProgressDialog.show(CreateShopActivity.this, "", getString(R.string.please_wait), true);
            dialog.setCancelable(false);
            successjson=0;
            messagejson="";
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(toko != null && AppHelper.currentAccount != null) {
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                parameter.add(new BasicNameValuePair(Toko.TAG_NAMATOKO, toko.getNamatoko()));
                parameter.add(new BasicNameValuePair(Toko.TAG_DESKRIPSITOKO, toko.getDeskripsitoko()));
                parameter.add(new BasicNameValuePair(Toko.TAG_LOKASITOKO, toko.getLokasitoko()));
                parameter.add(new BasicNameValuePair(Toko.TAG_LATITUDE, String.valueOf(toko.getLatitude())));
                parameter.add(new BasicNameValuePair(Toko.TAG_LONGITUDE, String.valueOf(toko.getLongitude())));
                parameter.add(new BasicNameValuePair(Toko.TAG_JENISTOKOID, String.valueOf(toko.getJenistokoid())));
                parameter.add(new BasicNameValuePair(Account.TAG_ACCOUNTID, String.valueOf(AppHelper.currentAccount.getAccountid())));

                JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
                if(json != null)
                {
                    try
                    {
                        successjson = json.getInt(AppHelper.TAG_SUCCESS);
                        messagejson = json.getString(AppHelper.TAG_MESSAGE);
                        if(successjson == 1)
                        {
                            tokoid = Integer.valueOf(json.getString(Toko.TAG_TOKOID));
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    catch (JSONException e) {
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
            else
            {
                messagejson = getString(R.string.unknown_error);
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if(success)
            {
                if (tokoid != 0 && toko != null)
                {
                    toko.setTokoid(tokoid);
                    openAddProductActivity(toko);
                }
                else
                {
                    Toast.makeText(CreateShopActivity.this, getString(R.string.id_error), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(CreateShopActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }

        @Override
        protected void onCancelled() {
        }
    }
}
