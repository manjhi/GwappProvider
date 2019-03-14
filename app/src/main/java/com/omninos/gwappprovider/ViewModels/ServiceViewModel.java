package com.omninos.gwappprovider.ViewModels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.modelClasses.ServiceModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceViewModel extends ViewModel {

    private MutableLiveData<ServiceModel> serviceViewModelMutableLiveData;

    public LiveData<ServiceModel> getServivceList(final Activity activity) {

        serviceViewModelMutableLiveData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            CommonUtils.showProgress(activity, "");
            Api api = ApiClient.getApiClient().create(Api.class);

            api.serviceList().enqueue(new Callback<ServiceModel>() {
                @Override
                public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                    CommonUtils.dismissProgress();
                    if (response.body() != null) {
                        serviceViewModelMutableLiveData.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<ServiceModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
        return serviceViewModelMutableLiveData;
    }
}
