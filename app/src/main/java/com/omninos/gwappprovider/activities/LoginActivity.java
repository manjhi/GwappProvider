package com.omninos.gwappprovider.activities;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.omninos.gwappprovider.R;
import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.Utils.ConstantData;
import com.omninos.gwappprovider.ViewModels.RegisterLoginViewModel;
import com.omninos.gwappprovider.modelClasses.CheckSocialLoginModel;
import com.omninos.gwappprovider.modelClasses.ProviderLoginModel;
import com.omninos.gwappprovider.services.MyLocationServices;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivity activity;
    private CallbackManager callbackManager;
    private Button signin, fb_login, google_login;
    private TextView tv_signup;
    private EditText userEmail, userPass;
    private String S_userEmail, S_userPass, latitude = "0.0", longitude = "0.0", reg_id;
    private RegisterLoginViewModel viewModel;
    private String fbEmail, fbLastName, fbFirstName, fbId, userName, fbSocialUserserName, userStringEmail, socialId, loginType, userImage;
    private URL fbProfilePicture;
    private GoogleSignInClient mgoogleSignInClient;
    private int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = LoginActivity.this;

        //create viewmodel instance
        viewModel = ViewModelProviders.of(this).get(RegisterLoginViewModel.class);


        initView(); GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SetUps();


        //facebook login
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();


        if (App.getSinltonPojo().getLonitude() != null && App.getSinltonPojo().getLonitude() != null) {

        } else {
            stopService(new Intent(activity, MyLocationServices.class));
//            startService(new Intent(activity,MyLocationServices.class));
            Intent intent = new Intent(LoginActivity.this, MyLocationServices.class);
            if (!isMyServiceRunning(intent.getClass())) {
                startService(intent);
            }
        }

        latitude = App.getSinltonPojo().getLatitude();
        longitude = App.getSinltonPojo().getLonitude();

    }


    //initiate all ID's
    private void initView() {

        signin = findViewById(R.id.signin);
        tv_signup = findViewById(R.id.tv_signup);
        userPass = findViewById(R.id.userPass);
        fb_login = findViewById(R.id.fb_login);
        userEmail = findViewById(R.id.userEmail);
        google_login = findViewById(R.id.google_login);
    }

    //perform actions
    private void SetUps() {
        signin.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        fb_login.setOnClickListener(this);
        google_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                Validate();
//                startActivity(new Intent(activity,HomeActivity.class));
                break;

            case R.id.tv_signup:
                startActivity(new Intent(activity, RegisterActivity.class));
                break;

            case R.id.fb_login:
                FBLogin();
                break;

            case R.id.google_login:
                SignIn();
                break;
        }
    }

    private void SignIn() {
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void FBLogin() {
        if (CommonUtils.isNetworkConnected(activity)) {
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile"));
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("onSuccess: ", loginResult.getAccessToken().getToken());
                    getFacebookData(loginResult);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFacebookData(LoginResult loginResult) {
        CommonUtils.showProgress(activity, "");
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                CommonUtils.dismissProgress();
                try {

                    if (object.has("id")) {
                        fbId = object.getString("id");
                        Log.e("LoginActivity", "id" + fbId);

                    }
                    //check permission first userName
                    if (object.has("first_name")) {
                        fbFirstName = object.getString("first_name");
                        Log.e("LoginActivity", "first_name" + fbFirstName);

                    }
                    //check permisson last userName
                    if (object.has("last_name")) {
                        fbLastName = object.getString("last_name");
                        Log.e("LoginActivity", "last_name" + fbLastName);
                    }
                    //check permisson email
                    if (object.has("email")) {
                        fbEmail = object.getString("email");
                        Log.e("LoginActivity", "email" + fbEmail);
                    }

                    fbSocialUserserName = fbFirstName + " " + fbLastName;

                    JSONObject jsonObject = new JSONObject(object.getString("picture"));
                    if (jsonObject != null) {
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        Log.e("Loginactivity", "json oject get picture" + dataObject);
                        fbProfilePicture = new URL("https://graph.facebook.com/" + fbId + "/picture?width=500&height=500");
                        Log.e("LoginActivity", "json object=>" + object.toString());
                    }


                    if (fbEmail == null || fbEmail.equalsIgnoreCase("")) {
                        Toast.makeText(activity, "Email not found in your facebook account", Toast.LENGTH_SHORT).show();
                        facebooksignout();

                        //userName,fbSocialUserserName,userStringEmail,socialId,loginType,userImage
                    } else {
                        userName = fbSocialUserserName;
                        userStringEmail = fbEmail;
                        socialId = fbId;
                        loginType = "facebook";

                        if (fbProfilePicture != null) {
                            userImage = String.valueOf(fbProfilePicture);
                        } else {
                            userImage = "";
                        }

                        App.getAppPreference().SaveString(activity, ConstantData.LOGIN_TYPE, "Facebook");
                        CheckSocialId();
//
//                        initSocialApi(getString(R.string.facebook));

                    }

                } catch (Exception e) {

                }
            }
        });

        Bundle bundle = new Bundle();
        Log.e("LoginActivity", "bundle set");
        bundle.putString("fields", "id, first_name, last_name,email,picture,gender,location");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    private void CheckSocialId() {
        viewModel.CheckSocial(activity, socialId).observe(activity, new Observer<CheckSocialLoginModel>() {
            @Override
            public void onChanged(@Nullable CheckSocialLoginModel map) {
                if (map.getSuccess().equals("0")) {
                    App.getSinltonPojo().setSocialLoginStatus("SocialLogin");
                    App.getSinltonPojo().setSocialID(socialId);
                    Intent intent = new Intent(activity, RegisterActivity.class);
                    intent.putExtra("name", userName);
                    intent.putExtra("SocailId", socialId);
                    intent.putExtra("email", userStringEmail);
                    intent.putExtra("image", userImage);
                    startActivity(intent);
                } else {
                    App.getAppPreference().saveSocialLoginDetail(map);
                    App.getAppPreference().SaveString(activity, ConstantData.USERID, map.getDetails().getId());
                    //Save token for session management
                    App.getAppPreference().SaveString(activity, ConstantData.TOKEN, "1");

                    //save All data in sharedPreference
                    startActivity(new Intent(activity, HomeActivity.class));
                    finishAffinity();
                }
            }
        });
    }

    //facebook Logout
    private void facebooksignout() {
        LoginManager.getInstance().logOut();
    }


    //check validations
    private void Validate() {
        S_userEmail = userEmail.getText().toString().trim();
        S_userPass = userPass.getText().toString().trim();
        if (S_userEmail.isEmpty()) {
            userEmail.setError("enter email");
        } else if (S_userPass.isEmpty()) {
            userPass.setError("enter password");
        } else {
            Login();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult task = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            // Signed in successfolly, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d("Account: ", acct.getDisplayName());
            Log.d("Account: ", acct.getId());
            Log.d("Account: ", acct.getEmail());
            socialId = acct.getId();
            userName = acct.getDisplayName();
            userStringEmail = acct.getEmail();
            if (acct.getPhotoUrl() != null) {
                userImage = String.valueOf(acct.getPhotoUrl());
            } else {
                userImage = "";
            }
            App.getAppPreference().SaveString(activity, ConstantData.LOGIN_TYPE, "Google");
            CheckSocialId();
        } else {
        }
    }


    private void Login() {

        //firebase initialte
        FirebaseApp.initializeApp(this);

        //get registration id from firebase
        reg_id = FirebaseInstanceId.getInstance().getToken();

        //send data to viewmodel
        viewModel.loginModelLiveData(activity, S_userEmail, S_userPass, reg_id, "Android", latitude, longitude).observe(this, new Observer<ProviderLoginModel>() {
            @Override
            public void onChanged(@Nullable ProviderLoginModel providerLoginModel) {
                if (providerLoginModel.getSuccess().equalsIgnoreCase("1")) {
                    //save UserId
                    App.getAppPreference().SaveString(activity, ConstantData.USERID, providerLoginModel.getDetails().getId());
                    //Save token for session management
                    App.getAppPreference().SaveString(activity, ConstantData.TOKEN, "1");

                    //save All data in sharedPreference
                    App.getAppPreference().saveLoginDetail(providerLoginModel);
                    startActivity(new Intent(activity, HomeActivity.class));
                    finishAffinity();
                } else if (providerLoginModel.getSuccess().equalsIgnoreCase("2")) {
                    App.getAppPreference().SaveString(activity, ConstantData.USERID, providerLoginModel.getDetails().getId());
                    Toast.makeText(activity, String.valueOf(providerLoginModel.getDetails().getOtp()), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity, OtpVerifyActivity.class));
                } else {
                    Toast.makeText(activity, providerLoginModel.getMessage(), Toast.LENGTH_SHORT).show();
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
