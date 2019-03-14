package com.omninos.gwappprovider.activities;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.ConstantData;
import com.omninos.gwappprovider.ViewModels.RegisterLoginViewModel;
import com.omninos.gwappprovider.adapter.SubServiceAdapter;
import com.omninos.gwappprovider.modelClasses.SocialLoginModel;
import com.omninos.gwappprovider.modelClasses.UserRegisterLoginModel;
import com.omninos.gwappprovider.services.MyLocationServices;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChooseSubServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button choose;
    private ChooseSubServicesActivity activity;
    private SubServiceAdapter adapter;
    List<String> ServiceIds = new ArrayList<>();
    private RegisterLoginViewModel viewModel;
    StringBuilder serviceId;
    private String StrServiceId, latitude = "0.0", longitude = "0.0", reg_id;

    LocationManager locationManager;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sub_services);
        activity = ChooseSubServicesActivity.this;

        //initiate firebase
        FirebaseApp.initializeApp(activity);
        //create instance of ViewModel
        viewModel = ViewModelProviders.of(this).get(RegisterLoginViewModel.class);

        initView();
        SetUps();
//        GetLocation();


        if (App.getSinltonPojo().getLonitude() != null && App.getSinltonPojo().getLonitude() != null) {

        } else {
            stopService(new Intent(activity, MyLocationServices.class));
//            startService(new Intent(activity,MyLocationServices.class));
            Intent intent = new Intent(ChooseSubServicesActivity.this, MyLocationServices.class);
            if (!isMyServiceRunning(intent.getClass())) {
                startService(intent);
            }
        }


        latitude = App.getSinltonPojo().getLatitude();
        longitude = App.getSinltonPojo().getLonitude();
    }


//
//    //Get Current Location
//    private void GetLocation() {
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//
//
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    //get current latitude or longitude
//                    latitude = String.valueOf(location.getLatitude());
//                    longitude = String.valueOf(location.getLongitude());
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
//        } else if (locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)) {
//            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
//                @Override
//                public void onLocationChanged(Location location) {
//                    //get Current latitude or longitude
//                    latitude = String.valueOf(location.getLatitude());
//                    longitude = String.valueOf(location.getLongitude());
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
//        //create GoogleApiClient instance
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//    }

    //Set Actions
    private void SetUps() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new SubServiceAdapter(activity, App.getSinltonPojo().getSubServicesData(), new SubServiceAdapter.Choose() {
            @Override
            public void ChooseSubService(int position) {
                if (!ServiceIds.contains(App.getSinltonPojo().getSubServicesData().get(position).getId())) {
                    ServiceIds.add(App.getSinltonPojo().getSubServicesData().get(position).getId());
                } else {
                    ServiceIds.remove(App.getSinltonPojo().getSubServicesData().get(position).getId());
                }
            }
        });
        recyclerView.setAdapter(adapter);

        choose.setOnClickListener(this);
    }

    //initiate All Id's
    private void initView() {
        choose = findViewById(R.id.choose);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose:
                UserRegister();
                break;
        }
    }

    //User Register
    private void UserRegister() {
        try {
            //Check Services ID's
            serviceId = new StringBuilder();
            for (String s : ServiceIds) {
                serviceId.append(s + ",");
            }
            int size = serviceId.length();
            serviceId.deleteCharAt(size - 1);
            System.out.println("Data Is: " + serviceId);
            //set ID's into String
            StrServiceId = serviceId.toString();
            System.out.println("Data Sys: " + StrServiceId);
            //move To Register
            if (App.getSinltonPojo().getSocialLoginStatus()!=null){
                UserSocialLoginApi();
            }else {
                NewData();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Choose Services", Toast.LENGTH_SHORT).show();
            //if Id's are empty
            serviceId = new StringBuilder();
        }
    }

    private void UserSocialLoginApi() {

        // MultipartBody.Part driverLicence, MultipartBody.Part insurance

        //create firebase registration ID
        reg_id = FirebaseInstanceId.getInstance().getToken();

        MultipartBody.Part licience = null, insurence = null;

        //Convert Data into Multipart
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderName());

        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderEmail());

        RequestBody imageBody=RequestBody.create(MediaType.parse("text/plain"),App.getSinltonPojo().getUserimagePath());

        RequestBody numberBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderPhone());

        RequestBody serviceTypeBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderServiceType());

        RequestBody qualificationBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderQualification());

        RequestBody SubserviceBody = RequestBody.create(MediaType.parse("text/plain"), StrServiceId);

        RequestBody deviceTypeBody = RequestBody.create(MediaType.parse("text/plain"), "Android");

        RequestBody reg_idBody = RequestBody.create(MediaType.parse("text/plain"), reg_id);

        RequestBody socialIdBody=RequestBody.create(MediaType.parse("text/plain"),App.getSinltonPojo().getSocialID());

        RequestBody mainServiceBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getMainServices());

        RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), latitude);

        RequestBody lngBody = RequestBody.create(MediaType.parse("text/plain"), longitude);

        RequestBody loginTypeBody=RequestBody.create(MediaType.parse("text/plain"),"Social Login");

        //make driving licence File Multipart
        if (App.getSinltonPojo().getDrivingLicence() != null) {
            File file = new File(App.getSinltonPojo().getDrivingLicence());
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            licience = MultipartBody.Part.createFormData("driverLicence", file.getName(), requestFile);
        }

        //make insurance File Multipart
        if (App.getSinltonPojo().getProviderInsurance() != null) {
            File file1 = new File(App.getSinltonPojo().getDrivingLicence());
            final RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            insurence = MultipartBody.Part.createFormData("insurance", file1.getName(), requestFile1);
        }

        viewModel.SocialLogin(activity,nameBody,emailBody,imageBody,numberBody,serviceTypeBody,qualificationBody,latBody,lngBody,mainServiceBody,SubserviceBody,socialIdBody,deviceTypeBody,reg_idBody,loginTypeBody,licience,insurence).observe(this, new Observer<SocialLoginModel>() {
            @Override
            public void onChanged(@Nullable SocialLoginModel socialLoginModel) {
                if (socialLoginModel.getSuccess().equalsIgnoreCase("1")){
                    App.getSinltonPojo().setSocialLoginStatus(null);
                    App.getAppPreference().SaveString(activity, ConstantData.USERID, socialLoginModel.getDetails().getId());
                    Toast.makeText(activity, String.valueOf(socialLoginModel.getDetails().getOtp()), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, OtpVerifyActivity.class));
                }else {
                    Toast.makeText(activity, socialLoginModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void NewData() {

        //create firebase registration ID
        reg_id = FirebaseInstanceId.getInstance().getToken();

        MultipartBody.Part licience = null, insurence = null, userImagePathBody = null;

        //Convert Data into Multipart
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderName());

        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderEmail());

        RequestBody numberBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderPhone());

        RequestBody passwordBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderPassword());

        RequestBody serviceTypeBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderServiceType());

        RequestBody qualificationBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getProviderQualification());

        RequestBody SubserviceBody = RequestBody.create(MediaType.parse("text/plain"), StrServiceId);

        RequestBody deviceTypeBody = RequestBody.create(MediaType.parse("text/plain"), "Android");

        RequestBody reg_idBody = RequestBody.create(MediaType.parse("text/plain"), reg_id);

        RequestBody mainServiceBody = RequestBody.create(MediaType.parse("text/plain"), App.getSinltonPojo().getMainServices());

        RequestBody latBody = RequestBody.create(MediaType.parse("text/plain"), latitude);

        RequestBody lngBody = RequestBody.create(MediaType.parse("text/plain"), longitude);

        RequestBody chargeBody=RequestBody.create(MediaType.parse("text/plain"),App.getSinltonPojo().getCharges());


        //make driving licence File Multipart
        if (App.getSinltonPojo().getDrivingLicence() != null) {
            File file = new File(App.getSinltonPojo().getDrivingLicence());
            final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            licience = MultipartBody.Part.createFormData("driverLicence", file.getName(), requestFile);
        }

        //make insurance File Multipart
        if (App.getSinltonPojo().getProviderInsurance() != null) {
            File file1 = new File(App.getSinltonPojo().getDrivingLicence());
            final RequestBody requestFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
            insurence = MultipartBody.Part.createFormData("insurance", file1.getName(), requestFile1);
        }

        //make userImage File Multipart
        if (App.getSinltonPojo().getUserimagePath() != null) {
            File file2 = new File(App.getSinltonPojo().getUserimagePath());
            final RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), file2);
            userImagePathBody = MultipartBody.Part.createFormData("image", file2.getName(), requestFile2);
        }

        //Set all values on ViewModel
        viewModel.userRegister(activity, nameBody, emailBody, numberBody, passwordBody, serviceTypeBody, qualificationBody, mainServiceBody, SubserviceBody, deviceTypeBody, reg_idBody, licience, insurence, userImagePathBody, latBody, lngBody,chargeBody).observe(this, new Observer<UserRegisterLoginModel>() {
            @Override
            public void onChanged(@Nullable UserRegisterLoginModel userRegisterLoginModel) {
                if (userRegisterLoginModel.getSuccess().equalsIgnoreCase("1")) {
                    //Save UserId
                    App.getAppPreference().SaveString(activity, ConstantData.USERID, userRegisterLoginModel.getDetails().getId());
                    Toast.makeText(activity, String.valueOf(userRegisterLoginModel.getDetails().getOtp()), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, OtpVerifyActivity.class));
                } else {
                    Toast.makeText(activity, userRegisterLoginModel.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}
