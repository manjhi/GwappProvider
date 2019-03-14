package com.omninos.gwappprovider.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.ViewModels.NotificationViewModel;
import com.omninos.gwappprovider.services.MyLocationServices;

import java.util.Map;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView centerTitle, requestUserName, requestUserService, requestUserPaymentMethod, requestUserProblemDetail, userAddress;
    private ImageView backButton, loader, userImage1;
    private Button acceptButton, bidCounter, rejectButton;
    private RequestActivity activity;
    private TextView simpleChronometer;
    private Animation rotation;
    private String message, paymentType, userImage, lAtitude, longitude, userName, userServices, problemDetail, userAddressString, Id, phoneString;
    private NotificationViewModel viewModel;
    private LinearLayout seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        activity = RequestActivity.this;


        viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);

        if (App.getSinltonPojo().getLonitude() != null && App.getSinltonPojo().getLonitude() != null) {

        } else {
            stopService(new Intent(activity, MyLocationServices.class));
//            startService(new Intent(activity,MyLocationServices.class));
            Intent intent = new Intent(RequestActivity.this, MyLocationServices.class);
            if (!isMyServiceRunning(intent.getClass())) {
                startService(intent);
            }
        }

        initView();
        SetUps();
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
        acceptButton = findViewById(R.id.acceptButton);
        simpleChronometer = findViewById(R.id.simpleChronometer);
        loader = findViewById(R.id.loader);
        requestUserName = findViewById(R.id.requestUserName);
        requestUserService = findViewById(R.id.requestUserService);
        requestUserPaymentMethod = findViewById(R.id.requestUserPaymentMethod);
        requestUserProblemDetail = findViewById(R.id.requestUserProblemDetail);
        bidCounter = findViewById(R.id.bidCounter);
        rejectButton = findViewById(R.id.rejectButton);
        userAddress = findViewById(R.id.userAddress);
        seconds = findViewById(R.id.seconds);
        userImage1 = findViewById(R.id.userImage);


        rotation = AnimationUtils.loadAnimation(RequestActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        loader.startAnimation(rotation);


    }

    private void SetUps() {
        backButton.setVisibility(View.GONE);
        centerTitle.setText("Request");
        acceptButton.setOnClickListener(this);
        bidCounter.setOnClickListener(this);
        rejectButton.setOnClickListener(this);

        if (getIntent().getStringExtra("Status").equalsIgnoreCase("Notification")) {

            loader.setVisibility(View.VISIBLE);
            seconds.setVisibility(View.VISIBLE);
            userImage1.setVisibility(View.GONE);

            message = getIntent().getStringExtra("message");
            paymentType = getIntent().getStringExtra("paymentType");
            userImage = getIntent().getStringExtra("userImage");
            lAtitude = getIntent().getStringExtra("lAtitude");
            longitude = getIntent().getStringExtra("longitude");
            userName = getIntent().getStringExtra("userName");
            userServices = getIntent().getStringExtra("userServices");
            problemDetail = getIntent().getStringExtra("problemDetail");
            userAddressString = getIntent().getStringExtra("address");
            Id = getIntent().getStringExtra("id");
            phoneString = getIntent().getStringExtra("phone");


            requestUserName.setText(userName);
            requestUserService.setText(userServices);
            requestUserPaymentMethod.setText(paymentType);
            requestUserProblemDetail.setText(problemDetail);
            userAddress.setText(userAddressString);

            new CountDownTimer(25000, 1000) {
                public void onTick(long millisUntilFinished) {
                    simpleChronometer.setText(String.valueOf(millisUntilFinished / 1000));
                }

                public void onFinish() {
                    simpleChronometer.setText("00");
                }

            }.start();

        } else if (getIntent().getStringExtra("Status").equalsIgnoreCase("Work")) {

            loader.setVisibility(View.GONE);
            seconds.setVisibility(View.GONE);
            userImage1.setVisibility(View.VISIBLE);
            paymentType = getIntent().getStringExtra("paymentType");
            userImage = getIntent().getStringExtra("userImage");
            lAtitude = getIntent().getStringExtra("lAtitude");
            longitude = getIntent().getStringExtra("longitude");
            userName = getIntent().getStringExtra("userName");
            userServices = getIntent().getStringExtra("userServices");
            problemDetail = getIntent().getStringExtra("problemDetail");
            userAddressString = getIntent().getStringExtra("address");
            Id = getIntent().getStringExtra("id");
            requestUserName.setText(userName);
            requestUserService.setText(userServices);
            requestUserPaymentMethod.setText(paymentType);
            requestUserProblemDetail.setText(problemDetail);
            userAddress.setText(userAddressString);
            Glide.with(activity).load(userImage).into(userImage1);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.backButton:
//                onBackPressed();
//                break;
            case R.id.acceptButton:
                rotation.cancel();
                Intent intent = new Intent(activity, ProviderRouteActivity.class);
                intent.putExtra("lAtitude", lAtitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("userName", userName);
                intent.putExtra("userImage", userImage);
                startActivity(intent);
                break;

            case R.id.bidCounter:
                startActivity(new Intent(activity, BidCounterActivity.class));
                break;

            case R.id.rejectButton:
                OpenDialogBox();
                break;
        }
    }

    private void OpenDialogBox() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.reason_popup, null);


        Button submit = view.findViewById(R.id.submitReason);
        final EditText edit_reason = view.findViewById(R.id.edit_reason);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                String reason = edit_reason.getText().toString();
                SendNotification(reason);

            }
        });

        alertDialog.show();

    }

    private void SendNotification(String reason) {
        viewModel.sendData(activity, "reject", reason, Id).observe(this, new Observer<Map>() {
            @Override
            public void onChanged(@Nullable Map map) {
                startActivity(new Intent(activity, HomeActivity.class));
                finishAffinity();
            }
        });
    }
}
