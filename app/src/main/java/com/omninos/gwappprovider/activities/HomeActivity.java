package com.omninos.gwappprovider.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.ConstantData;
import com.omninos.gwappprovider.services.MyLocationServices;

import java.io.IOException;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {


    private GoogleMap mMap;
    private HomeActivity activity;
    private Spinner spinner;

    private ImageView menuButton, logo, navi_Image;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager locationManager;
    private GoogleApiClient client;
    private TextView navi_work_list, logout, navi_home, navi_done_work, navi_Support, navi_profile, navi_earning, navi_wallet, navi_chat, navi_seeBid;
    private DrawerLayout drawer;

    private GoogleSignInClient mgoogleSignInClient;


    private static final String[] Types = {"All", "Construction", "Maintenance", "Food"};

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            finish();
            finishAffinity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        activity = HomeActivity.this;

        initView();
        SetUps();
        if (App.getSinltonPojo().getLonitude() != null && App.getSinltonPojo().getLonitude() != null) {
            SetMap();
        } else {
            stopService(new Intent(activity, MyLocationServices.class));
//            startService(new Intent(activity,MyLocationServices.class));
            Intent intent = new Intent(HomeActivity.this, MyLocationServices.class);
            if (!isMyServiceRunning(intent.getClass())) {
                startService(intent);
                SetMap();
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

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


    //Setup actions
    private void SetUps() {
        menuButton.setOnClickListener(this);
        navi_work_list.setOnClickListener(this);
        navi_done_work.setOnClickListener(this);
        navi_Support.setOnClickListener(this);
        navi_profile.setOnClickListener(this);
        navi_earning.setOnClickListener(this);
        navi_wallet.setOnClickListener(this);
        logo.setOnClickListener(this);
        navi_chat.setOnClickListener(this);
        navi_seeBid.setOnClickListener(this);
        logout.setOnClickListener(this);
        navi_home.setOnClickListener(this);
        if (App.getAppPreference().getLoginDetail() != null) {
            Glide.with(this).load(App.getAppPreference().getLoginDetail().getDetails().getImage()).into(navi_Image);
        } else if (App.getAppPreference().getSocialLoginData() != null) {
            Glide.with(this).load(App.getAppPreference().getSocialLoginData().getDetails().getImage()).into(navi_Image);
        }
        //Set Type Spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this,
                android.R.layout.simple_spinner_item, Types);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //initiate All Id's
    private void initView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuButton = findViewById(R.id.menuButton);
        navi_work_list = findViewById(R.id.navi_work_list);
        navi_done_work = findViewById(R.id.navi_done_work);
        navi_Support = findViewById(R.id.navi_Support);
        navi_profile = findViewById(R.id.navi_profile);
        navi_earning = findViewById(R.id.navi_earning);
        navi_wallet = findViewById(R.id.navi_wallet);
        logo = findViewById(R.id.logo);
        navi_chat = findViewById(R.id.navi_chat);
        spinner = findViewById(R.id.spinner);
        navi_seeBid = findViewById(R.id.navi_seeBid);
        logout = findViewById(R.id.logout);
        navi_home = findViewById(R.id.navi_home);
        navi_Image = findViewById(R.id.navi_Image);
    }

    //Set Map on Activity
    private void SetMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        if (App.getSinltonPojo().getLatitude()!=null) {
//            LatLng latLng = new LatLng(Double.valueOf(App.getSinltonPojo().getLatitude()), Double.valueOf(App.getSinltonPojo().getLonitude()));
//            mMap.addMarker(new MarkerOptions().position(latLng));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
//        }

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            return;
//
//        }
//
//        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, new android.location.LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//
//                    double latitude = location.getLatitude();
//
//                    double longitude = location.getLongitude();
//
//                    LatLng latLng = new LatLng(latitude, longitude);
//
//                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        if (addressList.size() > 0 && addressList != null) {
//                            String str = addressList.get(0).getLocality() + ",";
//                            str += addressList.get(0).getCountryName();
////                            mMap.addMarker(new MarkerOptions().position(latLng).title(str));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//
//                }
//            });
//        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, new android.location.LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//
//                    double latitude = location.getLatitude();
//
//                    double longitude = location.getLongitude();
//
//                    LatLng latLng = new LatLng(latitude, longitude);
//
//                    Geocoder geocoder = new Geocoder(getApplicationContext());
//                    try {
//                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
//                        if (addressList.size() > 0 && addressList != null) {
//                            String str = addressList.get(0).getLocality() + ",";
//                            str += addressList.get(0).getCountryName();
////                            mMap.addMarker(new MarkerOptions().position(latLng).title(str));
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                }
//
//                @Override
//                public void onProviderEnabled(String provider) {
//
//                }
//
//                @Override
//                public void onProviderDisabled(String provider) {
//
//                }
//            });
//        }


    }

    //initiate map Ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocationIfPermitted();
    }

    //auto set location when update
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navi_home:
                drawer.closeDrawer(GravityCompat.START);
                break;

            //open Drawer
            case R.id.menuButton:
                DrawerMenuBar();
                break;

            //move to work list activi
            case R.id.navi_work_list:
                startActivity(new Intent(activity, WorkListActivity.class));
                break;

            case R.id.navi_done_work:
                startActivity(new Intent(activity, WorkLoadActivity.class));
                break;

            case R.id.navi_Support:
                startActivity(new Intent(activity, SupportActivity.class));
                break;
            case R.id.navi_profile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;

            case R.id.navi_earning:
                startActivity(new Intent(activity, MoneyTransferActivity.class));
                break;

            case R.id.navi_wallet:
                startActivity(new Intent(activity, MyWallet.class));
                break;

            case R.id.logo:
//                startActivity(new Intent(activity, RequestActivity.class));
                break;

            case R.id.navi_chat:
                startActivity(new Intent(activity, ChatActivity.class));
                break;
            case R.id.navi_seeBid:
                startActivity(new Intent(activity, SeeUsersPostActivity.class));
                break;

            case R.id.logout:
                if (App.getAppPreference().GetString(activity, ConstantData.LOGIN_TYPE).equalsIgnoreCase("Google")) {
                    mgoogleSignInClient.signOut();
                    App.getAppPreference().Logout(activity);
                    startActivity(new Intent(activity, LoginActivity.class));
                    finishAffinity();
                } else if (App.getAppPreference().GetString(activity, ConstantData.LOGIN_TYPE).equalsIgnoreCase("Facebook")) {
                    LoginManager.getInstance().logOut();
                    App.getAppPreference().Logout(activity);
                    startActivity(new Intent(activity, LoginActivity.class));
                    finishAffinity();
                } else {
                    App.getAppPreference().Logout(activity);
                    startActivity(new Intent(activity, LoginActivity.class));
                    finishAffinity();
                }
                break;
        }
    }

    private void DrawerMenuBar() {
        drawer.openDrawer(Gravity.START);
    }
}
