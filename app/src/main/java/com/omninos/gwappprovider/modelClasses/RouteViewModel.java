package com.omninos.gwappprovider.modelClasses;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.CommonUtils;
import com.omninos.gwappprovider.directionApi.DirectionPojo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteViewModel extends ViewModel {

    private MutableLiveData<DirectionPojo> directionPojoMutableLiveData;

    public LiveData<DirectionPojo> getRoute(final Activity activity, Map<String, String> data){
        directionPojoMutableLiveData=new MutableLiveData<>();
        if (CommonUtils.isNetworkConnected(activity)){

            CommonUtils.showProgress(activity,"");
            Api api=ApiClient.getClientRoute().create(Api.class);

            api.getPolyLine(data).enqueue(new Callback<DirectionPojo>() {
                @Override
                public void onResponse(Call<DirectionPojo> call, Response<DirectionPojo> response) {
                    if (response.isSuccessful()){
                        directionPojoMutableLiveData.setValue(response.body());
                    }else {

                    }
                }

                @Override
                public void onFailure(Call<DirectionPojo> call, Throwable t) {
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }
        return directionPojoMutableLiveData;
    }
}
