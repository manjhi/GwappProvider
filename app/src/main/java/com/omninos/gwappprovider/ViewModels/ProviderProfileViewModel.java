package com.omninos.gwappprovider.ViewModels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.modelClasses.GetProviderProfile;
import com.omninos.gwappprovider.modelClasses.UpdateProviderProfile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderProfileViewModel extends ViewModel {

    private MutableLiveData<GetProviderProfile> getProviderProfileMutableLiveData;
    private MutableLiveData<UpdateProviderProfile> updateProviderProfileMutableLiveData;


    public LiveData<GetProviderProfile> getProviderProfile(final Activity activity, String providerId) {
        getProviderProfileMutableLiveData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            CommonUtils.showProgress(activity, "");
            Api api = ApiClient.getApiClient().create(Api.class);
            api.getProfile(providerId).enqueue(new Callback<GetProviderProfile>() {
                @Override
                public void onResponse(Call<GetProviderProfile> call, Response<GetProviderProfile> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        getProviderProfileMutableLiveData.setValue(response.body());
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<GetProviderProfile> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
        return getProviderProfileMutableLiveData;
    }

    public LiveData<UpdateProviderProfile> updateProfile(final Activity activity, RequestBody providerId, RequestBody provideName, RequestBody IDNumber, RequestBody ExpiryDate,RequestBody number, MultipartBody.Part image) {

        updateProviderProfileMutableLiveData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            Api api = ApiClient.getApiClient().create(Api.class);
            CommonUtils.showProgress(activity, "");
            api.updateProfile(providerId, provideName, IDNumber, ExpiryDate,number,image).enqueue(new Callback<UpdateProviderProfile>() {
                @Override
                public void onResponse(Call<UpdateProviderProfile> call, Response<UpdateProviderProfile> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        updateProviderProfileMutableLiveData.setValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<UpdateProviderProfile> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }

        return updateProviderProfileMutableLiveData;
    }
}
