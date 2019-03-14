package com.omninos.gwappprovider.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.Utils.ConstantData;
import com.omninos.gwappprovider.ViewModels.RegisterLoginViewModel;
import com.omninos.gwappprovider.modelClasses.ResendOtp;

import java.util.Map;

public class OtpVerifyActivity extends AppCompatActivity implements View.OnClickListener {


    private OtpVerifyActivity activity;
    private EditText first, second, third, fourth;
    private TextView resend;
    private Button varify;
    private String otp_1, otp_2, otp_3, otp_4, compeleteOtp, OTP;
    private RegisterLoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        viewModel = ViewModelProviders.of(this).get(RegisterLoginViewModel.class);


        activity = OtpVerifyActivity.this;
        initView();


        //set TextChangeListener
        first.addTextChangedListener(generalTextWatcher);
        second.addTextChangedListener(generalTextWatcher);
        third.addTextChangedListener(generalTextWatcher);
        fourth.addTextChangedListener(generalTextWatcher);
    }


    //initiate all View ID's
    private void initView() {
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        varify = findViewById(R.id.submit);
        resend = findViewById(R.id.resend);


        varify.setOnClickListener(this);
        resend.setOnClickListener(this);
    }


    //Set TextWatcher
    TextWatcher generalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (first.getText().hashCode() == editable.hashCode()) {
                otp_1 = first.getText().toString().trim();
                if (!otp_1.equalsIgnoreCase("")) {
                    second.requestFocus();
                }

            } else if (second.getText().hashCode() == editable.hashCode()) {
                otp_2 = second.getText().toString().trim();
                if (!otp_2.equalsIgnoreCase("")) {
                    third.requestFocus();
                } else {
                    first.requestFocus();
                }

            } else if (third.getText().hashCode() == editable.hashCode()) {
                otp_3 = third.getText().toString().trim();
                if (!otp_3.equalsIgnoreCase("")) {
                    fourth.requestFocus();
                } else {
                    second.requestFocus();
                }
            } else if (fourth.getText().hashCode() == editable.hashCode()) {
                otp_4 = fourth.getText().toString().trim();
                if (!otp_4.equalsIgnoreCase("")) {
                    hideKeyboard(activity);
                } else {
                    third.requestFocus();
                }

            }

        }
    };


    //Hide keyboard Method
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                CheckOtp();
                break;
            case R.id.resend:
                ResendData();
                break;
        }
    }

    //Resend Otp Method
    private void ResendData() {
        viewModel.resendOtpLiveData(activity,  App.getAppPreference().GetString(activity,ConstantData.USERID)).observe(this, new Observer<ResendOtp>() {
            @Override
            public void onChanged(@Nullable ResendOtp resendOtp) {
                if (resendOtp.getSuccess().equalsIgnoreCase("1")){
                    Toast.makeText(activity, String.valueOf(resendOtp.getDetails().getOtp()), Toast.LENGTH_SHORT).show();
                    compeleteOtp= String.valueOf(resendOtp.getDetails().getOtp());
                }else {

                }
            }
        });
    }


    //Check OTP
    private void CheckOtp() {
        otp_1 = first.getText().toString().trim();
        otp_2 = second.getText().toString().trim();
        otp_3 = third.getText().toString().trim();
        otp_4 = fourth.getText().toString().trim();
        compeleteOtp = otp_1 + otp_2 + otp_3 + otp_4;
        if (compeleteOtp.length() != 4) {
            Toast.makeText(this, "enter values", Toast.LENGTH_SHORT).show();
        } else {
            //match OTP
            CheckOtpvalidation();
        }
    }

    private void CheckOtpvalidation() {

        viewModel.matchOtp(activity,   App.getAppPreference().GetString(activity, ConstantData.USERID), compeleteOtp).observe(this, new Observer<Map>() {
            @Override
            public void onChanged(@Nullable Map map) {
                if (map != null) {
                    OpendialogBox();
                } else {
                }
            }
        });

    }

    //popup dialogBox
    private void OpendialogBox() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.verify_popup, null);

        Button verify = view.findViewById(R.id.done);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                //Save Login Token
                App.getAppPreference().SaveString(activity, ConstantData.TOKEN, "1");
                startActivity(new Intent(activity, HomeActivity.class));
                finishAffinity();

            }
        });
        alertDialog.show();
    }
}
