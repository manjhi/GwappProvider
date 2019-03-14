package com.omninos.gwappprovider.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.omninos.gwappprovider.Utils.App;

public class MyLocationServices extends Service {


    LocationManager locationManager;
    private GoogleApiClient client;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GetLocation();
        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //get current location
    private void GetLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                    latitude = String.valueOf(location.getLatitude());
//                    longitude = String.valueOf(location.getLongitude());
                    App.getSinltonPojo().setLatitude(String.valueOf(location.getLatitude()));
                    App.getSinltonPojo().setLonitude(String.valueOf(location.getLongitude()));
                    Log.d("onLocationChanged: ", String.valueOf(location.getLatitude()));
                    Log.d("onLocationChanged: ", String.valueOf(location.getLongitude()));
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
            });
        } else if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                    latitude = String.valueOf(location.getLatitude());
//                    longitude = String.valueOf(location.getLongitude());
                    App.getSinltonPojo().setLatitude(String.valueOf(location.getLatitude()));
                    App.getSinltonPojo().setLonitude(String.valueOf(location.getLongitude()));
                    Log.d("onLocationChanged: ", String.valueOf(location.getLatitude()));
                    Log.d("onLocationChanged: ", String.valueOf(location.getLongitude()));
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
            });
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

}
