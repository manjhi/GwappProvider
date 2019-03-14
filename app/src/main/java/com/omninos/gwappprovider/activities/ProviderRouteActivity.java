package com.omninos.gwappprovider.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.directionApi.DirectionPojo;
import com.omninos.gwappprovider.directionApi.Leg;
import com.omninos.gwappprovider.directionApi.Route;
import com.omninos.gwappprovider.directionApi.Step;
import com.omninos.gwappprovider.modelClasses.RouteViewModel;
import com.omninos.gwappprovider.services.MyLocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderRouteActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {


    private TextView centerTitle, name;
    private ImageView backButton;
    private ProviderRouteActivity activity;
    private CircleImageView image;
    private String UserName, UserImage, latitude, longitude;
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private String status, driverCurrentLatitude, driverCurrentLongitude, driverDestinationLatitude, driverDestinationLongitude, dAddress, driverAddTelephone;
    private GoogleMap map;
    LocationManager locationManager;
//    private RouteViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_route);

        activity = ProviderRouteActivity.this;

//        viewModel=ViewModelProviders.of(activity).get(RouteViewModel.class);
        initView();
        SetUps();

        if (App.getSinltonPojo().getLonitude() != null && App.getSinltonPojo().getLonitude() != null) {

        } else {
            stopService(new Intent(activity, MyLocationServices.class));
//            startService(new Intent(activity,MyLocationServices.class));
            Intent intent = new Intent(ProviderRouteActivity.this, MyLocationServices.class);
            if (!isMyServiceRunning(intent.getClass())) {
                startService(intent);
            }
        }

        driverCurrentLatitude = "30.7051648";
        driverCurrentLongitude = "76.6844778";

        driverDestinationLatitude = getIntent().getStringExtra("lAtitude");
        driverDestinationLongitude = getIntent().getStringExtra("longitude");
//        new CountDownTimer(5000, 1000) {
//            public void onTick(long millisUntilFinished) {
//            }
//
//            public void onFinish() {
//                startActivity(new Intent(activity, StartWorkActivity.class));
//            }
//        }.start();

        initDirectionAPI();
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


    private void initView() {
        centerTitle = findViewById(R.id.centerTitle);
        backButton = findViewById(R.id.backButton);
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);

    }

    private void SetUps() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;

        }

        backButton.setOnClickListener(this);
        centerTitle.setText("Request");
        UserName = getIntent().getStringExtra("userName");
        UserImage = getIntent().getStringExtra("userImage");
        latitude = getIntent().getStringExtra("lAtitude");
        longitude = getIntent().getStringExtra("longitude");
        Glide.with(this).load(UserImage).into(image);
        name.setText(UserName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }


    private void initDirectionAPI() {

        if (App.getSinltonPojo().getLatitude() != null && App.getSinltonPojo().getLonitude() != null) {

            Map<String, String> data = new HashMap<>();
            data.put("origin", App.getSinltonPojo().getLatitude() + "," + App.getSinltonPojo().getLonitude());
            data.put("destination", driverDestinationLatitude + "," + driverDestinationLongitude);
            data.put("key", ProviderRouteActivity.this.getResources().getString(R.string.key));
            // ApiClient.getClientRoute();

            if (CommonUtils.isNetworkConnected(activity)) {
                Api apiInterface = ApiClient.getClientRoute().create(Api.class);

                CommonUtils.showProgress(activity, "");
                apiInterface.getPolyLine(data).enqueue(new Callback<DirectionPojo>() {
                    @Override
                    public void onResponse(@NonNull Call<DirectionPojo> call, @NonNull Response<DirectionPojo> response) {
                        CommonUtils.dismissProgress();
                        List<Route> routeList = response.body().getRoutes();

                   /* if (polyline != null) {
                        polyline.remove();
                    }*/

                        polylineOptions = new PolylineOptions();
                        polylineOptions.width(5).color(Color.BLUE).geodesic(true);
                        //  polylineOptions.add(driverLatLng);

                        for (int i = 0; i < routeList.size(); i++) {
                            List<Leg> legList = routeList.get(i).getLegs();
                            for (int j = 0; j < legList.size(); j++) {
                                List<Step> stepList = legList.get(j).getSteps();
                                for (int k = 0; k < stepList.size(); k++) {
                                    String polyline = stepList.get(k).getPolyline().getPoints();
                                    List<LatLng> latlngList = decodePolyline(polyline);
                                    for (int z = 0; z < latlngList.size(); z++) {
                                        LatLng point = latlngList.get(z);
                                        polylineOptions.add(point);
                                    }
                                }
                            }
                        }
                        polyline = map.addPolyline(polylineOptions);
                    }

                    @Override
                    public void onFailure(@NonNull Call<DirectionPojo> call, @NonNull Throwable t) {
                        CommonUtils.dismissProgress();
                        Toast.makeText(ProviderRouteActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            } else {
                Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private List<LatLng> decodePolyline(String polyline) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = polyline.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = polyline.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (App.getSinltonPojo().getLatitude() != null && App.getSinltonPojo().getLonitude() != null) {
            LatLng latLng = new LatLng(Double.valueOf(App.getSinltonPojo().getLatitude()), Double.valueOf(App.getSinltonPojo().getLonitude()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
        }
        map.setMyLocationEnabled(true);
    }
}
