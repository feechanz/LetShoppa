package com.letshoppa.feechan.letshoppa.Class;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by Feechan on 9/13/2016.
 */
public class GpsService extends Service implements LocationListener {
    private final Context context;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    boolean canGetLocation = false;
    private Location location;
    private double latitude = 0;
    private double longitude = 0;
    protected LocationManager locationManager;

    // GPS akan update ketika jarak sudah berubah lebih dari 5 meter
    private static final long MIN_JARAK_GPS_UPDATE = 0;//5;     // meter

    // GPS akan update pada waktu interval (1 menit)
    private static final long MIN_WAKTU_GPS_UPDATE = 0;//1000 * 60 * 1;

    public GpsService(Context context)
    {
        this.context = context;
        getLocation();
    }

    public Location getLocation()
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (locationManager != null)
        {
            if (isCanUseGps())
            {
                //koneksi dgn internet
                if (isNetworkEnable)
                {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    {
                        /*
                        if(isNetworkEnable())
                        {
                            //locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_WAKTU_GPS_UPDATE, MIN_JARAK_GPS_UPDATE, this);
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        if (isGPSEnable() && location == null) {
                            //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_WAKTU_GPS_UPDATE, MIN_JARAK_GPS_UPDATE, this);
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }*/
                        List<String> providers= locationManager.getProviders(true);
                        for(String provider : providers)
                        {
                            Location l = locationManager.getLastKnownLocation(provider);
                            if(l!=null)
                            {
                                if(location == null )
                                {
                                    location = l;
                                }
                                else if( l.getAccuracy() < location.getAccuracy())
                                {
                                    location = l;
                                }
                            }
                        }

                        if (location != null) {
                            canGetLocation = true;
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                else
                {
                    //tidak ada koneksi GPS dan Jaringan
                }
            }
        }
        //this.stopUsingGPS();
        return location;
    }


    public boolean isCanUseGps() {
        return isGPSEnable() || isNetworkEnable();
    }

    public boolean isGPSEnable() {
        return isGPSEnable;
    }

    public boolean isNetworkEnable() {
        return isNetworkEnable;
    }

    public boolean isCanGetLocation() {
        return canGetLocation;
    }

    public double getLatitude() {
        if (location != null)
            latitude = location.getLatitude();
        return latitude;
    }

    public void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // title Alertnya
        alertDialog.setTitle("GPS Setting");
        // pesan alert
        alertDialog.setMessage("GPS tidak aktif. Mau masuk ke setting Menu ?");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void stopUsingGPS() {
        if (locationManager != null)
        {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(GpsService.this);
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        if (location != null)
            longitude = location.getLongitude();
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (isNetworkEnable) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        else if (isGPSEnable) {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        canGetLocation = true;
        if(location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
