package com.omninos.gwappprovider.ViewModels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.modelClasses.WorkListModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkListViewModel extends ViewModel {

    private MutableLiveData<WorkListModel> workListModelMutableLiveData;

    public LiveData<WorkListModel> workListModelLiveData(final Activity activity, String providerId) {
        workListModelMutableLiveData = new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)) {

            Api api = ApiClient.getApiClient().create(Api.class);

            CommonUtils.showProgress(activity, "");

            api.workList(providerId).enqueue(new Callback<WorkListModel>() {
                @Override
                public void onResponse(Call<WorkListModel> call, Response<WorkListModel> response) {
                    CommonUtils.dismissProgress();
                    if (response.isSuccessful()) {
                        workListModelMutableLiveData.setValue(response.body());
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<WorkListModel> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }

        return workListModelMutableLiveData;
    }
}
