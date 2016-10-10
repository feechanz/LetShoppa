package com.letshoppa.feechan.letshoppa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.JenistokoAdapter;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.GpsService;
import com.letshoppa.feechan.letshoppa.Class.Jenistoko;

import org.apache.http.NameValuePair;
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
    Button viewSelectedLocationButton;
    private List listJenisToko;
    private JenistokoAdapter mAdapter;
    GpsService gps;
    TextView locationTextView;
    EditText locationEditText;
    private void setInitializeView()
    {
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        viewSelectedLocationButton = (Button) findViewById(R.id.viewSelectedLocationButton);
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
        LocationAddresTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
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
            // mAuthTask = null;
            //showProgress(false);
        }
    }
}
