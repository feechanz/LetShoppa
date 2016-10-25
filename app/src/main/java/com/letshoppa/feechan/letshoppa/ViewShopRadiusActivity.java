package com.letshoppa.feechan.letshoppa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.letshoppa.feechan.letshoppa.AdapterList.ShopRadiusItemAdapter;
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

public class ViewShopRadiusActivity extends AppCompatActivity {

    Jenistoko currentJenis;
    TextView titleTextView;
    Button setLocationButton;
    EditText locationEditText;
    TextView locationTextView;
    NumberPicker kmNumberPicker;
    Button searchButton;
    Button viewLocationButton;
    ListView shopListView;

    double latitude;
    double longitude;
    boolean bisa;

    ArrayAdapter mAdapter;
    List shops;

    GpsService gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_radius);

        titleTextView = (TextView) findViewById(R.id.activityTitleShop);
        setLocationButton = (Button) findViewById(R.id.set_location_button);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        kmNumberPicker = (NumberPicker) findViewById(R.id.kmNumberPicker);
        searchButton = (Button) findViewById(R.id.searchButton);
        viewLocationButton = (Button) findViewById(R.id.viewLocationButton);
        shopListView = (ListView) findViewById(R.id.ShopListView);

        kmNumberPicker.setMinValue(1);
        kmNumberPicker.setMaxValue(100);
        kmNumberPicker.setValue(5);
        kmNumberPicker.setWrapSelectorWheel(true);

        bisa = false;
        shops = new ArrayList();
        latitude = 0;
        longitude = 0;

        mAdapter = new ShopRadiusItemAdapter(this,shops);
        shopListView.setAdapter(mAdapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetailShop((Toko)parent.getItemAtPosition(position));
            }
        });
        if(AppHelper.tipeShopItem != null) {
            titleTextView.setText(AppHelper.tipeShopItem.getNamajenis());
            setTitle(AppHelper.tipeShopItem.getNamajenis());
            currentJenis = AppHelper.tipeShopItem;
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchShopAsync(0);
                }
            });
            setLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setLocation();
                }
            });
            viewLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewLocation();
                }
            });
        }

    }
    private void viewLocation()
    {
        AppHelper.shops = shops;
        Intent intentView = new Intent(this,ViewShopsMapsActivity.class);
        intentView.putExtra(AppHelper.TAG_LATITUDE,this.latitude);
        intentView.putExtra(AppHelper.TAG_LONGITUDE,this.longitude);
        intentView.putExtra(AppHelper.TAG_TITLE,getString(R.string.current_location));
        startActivity(intentView);
    }
    private void openDetailShop(Toko toko)
    {
        if(AppHelper.currentAccount != null) {
            if(AppHelper.currentAccount.getAccountid().equals(String.valueOf(toko.getAccountid()) ))
            {
                Intent openShopIntent = new Intent(this, MyShopActivity.class);
                openShopIntent.putExtra(Toko.TAG_TOKO, toko);
                startActivity(openShopIntent);
            }
            else
            {
                Intent openShopIntent = new Intent(this, ShopActivity.class);
                openShopIntent.putExtra(Toko.TAG_TOKO, toko);
                startActivity(openShopIntent);
            }
        }
        else {
            Intent openShopIntent = new Intent(this, ShopActivity.class);
            openShopIntent.putExtra(Toko.TAG_TOKO, toko);
            startActivity(openShopIntent);
        }
    }

    public void fetchShopAsync(int page)
    {
        if(bisa)
        {
            String url = AppHelper.domainURL +"/AndroidConnect/GetAllTokosByJenistokoidAndJarak.php";
            ShopRadiusRefreshLoadTask task = new ShopRadiusRefreshLoadTask(url,currentJenis.getJenistokoid());
            task.execute();
        }
    }

    private void setBisa()
    {
        bisa = true;
        searchButton.setEnabled(true);
    }

    public class ShopRadiusRefreshLoadTask extends AsyncTask<Void, Void, Boolean>
    {
        private String url;
        private int jenistokoid;

        int jarak;
        String messagejson;
        int successjson;

        public ShopRadiusRefreshLoadTask(String url, int jenistokoid) {
            this.url = url;
            this.jenistokoid = jenistokoid;
            successjson=-1;
            messagejson="";
            shops.clear();
            this.jarak = kmNumberPicker.getValue();

        }
        JSONArray listshops = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair(Toko.TAG_JENISTOKOID, String.valueOf(jenistokoid)));
            parameter.add(new BasicNameValuePair(Toko.TAG_KEYWORD, ""));
            parameter.add(new BasicNameValuePair(Toko.TAG_LATITUDE, String.valueOf(latitude)));
            parameter.add(new BasicNameValuePair(Toko.TAG_LONGITUDE, String.valueOf(longitude)));
            parameter.add(new BasicNameValuePair(Toko.TAG_JARAK, String.valueOf(jarak)));

            JSONObject json = AppHelper.GetJsonObject(url,"POST",parameter);
            if(json != null)
            {
                try {
                    successjson = json.getInt(AppHelper.TAG_SUCCESS);
                    messagejson = json.getString(AppHelper.TAG_MESSAGE);
                    if (successjson == 1)
                    {
                        listshops = json.getJSONArray(Toko.TAG_TOKO);

                        for (int i=0;i<listshops.length();i++)
                        {
                            JSONObject tokoobject = listshops.getJSONObject(i);
                            Toko newtoko = Toko.GetTokoFromJson(tokoobject);
                            shops.add(newtoko);
                        }
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
                    if(AppHelper.Message != "") {
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
                messagejson = AppHelper.NoConnection;
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAdapter.notifyDataSetChanged();
            if (success)
            {

                if(bisa)
                {
                    viewLocationButton.setEnabled(true);
                }
                else
                {
                    viewLocationButton.setEnabled(false);
                }
            }
            else
            {
                viewLocationButton.setEnabled(false);
                Toast.makeText(ViewShopRadiusActivity.this, messagejson, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setLocation()
    {
        if(gps== null) {
            gps = new GpsService(ViewShopRadiusActivity.this);
        }
        gps.getLocation();
        if(gps.isCanUseGps())
        {
            if(gps.isCanGetLocation())
            {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                LocationAddresTask task = new LocationAddresTask(latitude,longitude);
                //set enable
                task.execute((Void) null);
            }
            else
            {
                Toast.makeText(ViewShopRadiusActivity.this,getString(R.string.waiting_for_location),Toast.LENGTH_SHORT).show();
            }
            gps.stopUsingGPS();
        }
        else
        {
            Toast.makeText(ViewShopRadiusActivity.this,getString(R.string.error_cant_get_location),Toast.LENGTH_SHORT).show();
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

            dialog = ProgressDialog.show(ViewShopRadiusActivity.this, "", getString(R.string.please_wait), true);
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
                Toast.makeText(ViewShopRadiusActivity.this, getString(R.string.selected) + " : " + result, Toast.LENGTH_LONG).show();
                locationTextView.setText(resultLangLong);
                locationEditText.setText(result);
                setBisa();
            }
            else
            {
                Toast.makeText(ViewShopRadiusActivity.this, getString(R.string.error_cant_get_location), Toast.LENGTH_LONG).show();
            }
        }
        @Override
        protected void onCancelled() {
        }
    }



}
