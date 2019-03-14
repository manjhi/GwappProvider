package com.omninos.gwappprovider.ViewModels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;
import android.widget.Toast;

import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.App;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.modelClasses.CheckEmailPhoneModel;
import com.omninos.gwappprovider.modelClasses.CheckSocialLoginModel;
import com.omninos.gwappprovider.modelClasses.ProviderLoginModel;
import com.omninos.gwappprovider.modelClasses.ResendOtp;
import com.omninos.gwappprovider.modelClasses.SocialLoginModel;
import com.omninos.gwappprovider.modelClasses.UserRegisterLoginModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class RegisterLoginViewModel extends ViewModel {

    private MutableLiveData<UserRegisterLoginModel> userRegister;
    private MutableLiveData<Map> otpData;
    private MutableLiveData<ResendOtp> resendOtpMutableLiveData;
    private MutableLiveData<ProviderLoginModel> userLogin;
    private MutableLiveData<CheckEmailPhoneModel> checkEmail;
    private MutableLiveData<CheckEmailPhoneModel> checkPhone;
    private MutableLiveData<CheckSocialLoginModel> checkSocialID;
    private MutableLiveData<SocialLoginModel> SocialLogin;

    public LiveData<UserRegisterLoginModel> userRegister(final Activity activity, RequestBody username, RequestBody userEmail, RequestBody userPhone, RequestBody password, RequestBody ServiceType, RequestBody WorkQualification, RequestBody ServiceId, RequestBody SubServices, RequestBody Device_type, RequestBody regid, MultipartBody.Part driverLicence, MultipartBody.Part insurance, MultipartBody.Part image, RequestBody latitude, RequestBody longitude,RequestBody perHourChargers) {

        userRegister = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            CommonUtils.showProgress(activity, "");
            Api api = ApiClient.getApiClient().create(Api.class);

            api.userRegister(username, userEmail, userPhone, password, ServiceType, WorkQualification, ServiceId, SubServices, Device_type, regid, driverLicence, insurance, image, latitude, longitude,perHourChargers).enqueue(new Callback<UserRegisterLoginModel>() {
                @Override
                public void onResponse(Call<UserRegisterLoginModel> call, Response<UserRegisterLoginModel> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.body().getSuccess().equalsIgnoreCase("1")) {
                            userRegister.setValue(response.body());
                        } else {

                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<UserRegisterLoginModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "network Issue", Toast.LENGTH_SHORT).show();
        }
        return userRegister;
    }

    public LiveData<Map> matchOtp(final Activity activity, String userid, String otp) {
        otpData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            CommonUtils.showProgress(activity, "");
            Api api = ApiClient.getApiClient().create(Api.class);

            api.matchtoken(userid, otp).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        if (response.isSuccessful()) {
                            otpData.setValue(response.body());
                        }
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }

        return otpData;
    }

    public LiveData<ResendOtp> resendOtpLiveData(final Activity activity, String Userid) {
        resendOtpMutableLiveData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {
            CommonUtils.showProgress(activity, "");

            Api api = ApiClient.getApiClient().create(Api.class);

            api.resend(Userid).enqueue(new Callback<ResendOtp>() {
                @Override
                public void onResponse(Call<ResendOtp> call, Response<ResendOtp> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        resendOtpMutableLiveData.setValue(response.body());
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<ResendOtp> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "network Issue", Toast.LENGTH_SHORT).show();
        }

        return resendOtpMutableLiveData;
    }

    public LiveData<ProviderLoginModel> loginModelLiveData(final Activity activity, String email, String password, String regId, String deviceType, String latitude, String longitude) {
        userLogin = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            CommonUtils.showProgress(activity, "");

            Api api = ApiClient.getApiClient().create(Api.class);
            api.login(email, password, regId, deviceType, latitude, longitude).enqueue(new Callback<ProviderLoginModel>() {
                @Override
                public void onResponse(Call<ProviderLoginModel> call, Response<ProviderLoginModel> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {

                        userLogin.setValue(response.body());

                    } else {

                    }
                }

                @Override
                public void onFailure(Call<ProviderLoginModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }

        return userLogin;
    }

    public LiveData<CheckEmailPhoneModel> email(final Activity activity, String email) {
        checkEmail = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            Api api = ApiClient.getApiClient().create(Api.class);

            api.checkEmail(email).enqueue(new Callback<CheckEmailPhoneModel>() {
                @Override
                public void onResponse(Call<CheckEmailPhoneModel> call, Response<CheckEmailPhoneModel> response) {
                    if (response.body() != null) {
                        checkEmail.setValue(response.body());
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<CheckEmailPhoneModel> call, Throwable t) {
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
        return checkEmail;
    }

    public LiveData<CheckEmailPhoneModel> phone(final Activity activity, String phone) {
        checkPhone = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            Api api = ApiClient.getApiClient().create(Api.class);

            api.checkPhone(phone).enqueue(new Callback<CheckEmailPhoneModel>() {
                @Override
                public void onResponse(Call<CheckEmailPhoneModel> call, Response<CheckEmailPhoneModel> response) {
                    if (response.body() != null) {
                        checkPhone.setValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<CheckEmailPhoneModel> call, Throwable t) {
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
        return checkPhone;
    }

    public LiveData<CheckSocialLoginModel> CheckSocial(final Activity activity, String ID) {
        checkSocialID = new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)) {

            Api api = ApiClient.getApiClient().create(Api.class);
            CommonUtils.showProgress(activity, "");
            api.CheckSocialID(ID).enqueue(new Callback<CheckSocialLoginModel>() {
                @Override
                public void onResponse(Call<CheckSocialLoginModel> call, Response<CheckSocialLoginModel> response) {
                    CommonUtils.dismissProgress();
                    if (response.isSuccessful()) {
                        checkSocialID.setValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<CheckSocialLoginModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }

        return checkSocialID;
    }

    public LiveData<SocialLoginModel> SocialLogin(final Activity activity, RequestBody name, RequestBody email, RequestBody image, RequestBody phone, RequestBody serviceType, RequestBody work, RequestBody latitude, RequestBody longitude, RequestBody serviceId, RequestBody subServiceId, RequestBody social_id, RequestBody device_type, RequestBody reg_id, RequestBody login_type, MultipartBody.Part driverLicence, MultipartBody.Part insurance) {
        SocialLogin = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            CommonUtils.showProgress(activity, "");
            Api api = ApiClient.getApiClient().create(Api.class);

            api.SocialLogin(name, email, image, phone, serviceType, work, latitude, longitude, serviceId, subServiceId, social_id, device_type, reg_id, login_type, driverLicence, insurance).enqueue(new Callback<SocialLoginModel>() {
                @Override
                public void onResponse(Call<SocialLoginModel> call, Response<SocialLoginModel> response) {
                    CommonUtils.dismissProgress();
                    if (response.isSuccessful()) {
                        SocialLogin.setValue(response.body());
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<SocialLoginModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {

        }

        return SocialLogin;
    }
}
