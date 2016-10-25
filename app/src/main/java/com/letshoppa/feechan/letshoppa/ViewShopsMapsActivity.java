package com.letshoppa.feechan.letshoppa;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.letshoppa.feechan.letshoppa.Class.AppHelper;
import com.letshoppa.feechan.letshoppa.Class.Toko;

public class ViewShopsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    double latitude;
    double longitude;
    private String title;
    private float zoomLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shops_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latitude = getIntent().getDoubleExtra(AppHelper.TAG_LATITUDE,0);
        longitude = getIntent().getDoubleExtra(AppHelper.TAG_LONGITUDE,0);
        title = getIntent().getStringExtra(AppHelper.TAG_TITLE);
        zoomLevel = 17.0f;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if(AppHelper.shops != null)
        {
            for(Object item : AppHelper.shops)
            {
                Toko shop = (Toko) item;
                LatLng pos = new LatLng(shop.getLatitude(), shop.getLongitude());
                MarkerOptions m =  new MarkerOptions().position(pos).title(shop.getNamatoko());
                m.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(m);
            }
        }

        LatLng currentPosition = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(currentPosition).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
    }
}
