package com.omninos.gwappprovider.ViewModels;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.widget.Toast;

import com.omninos.gwappprovider.Retrofit.Api;
import com.omninos.gwappprovider.Retrofit.ApiClient;
import com.omninos.gwappprovider.Utils.CommonUtils;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {

    private MutableLiveData<Map> sendAcceptReject;

    public LiveData<Map> sendData(final Activity activity, String type, String reason, String bookingId){

        sendAcceptReject=new MutableLiveData<>();

        if (CommonUtils.isNetworkConnected(activity)){

            CommonUtils.showProgress(activity,"");

            Api api=ApiClient.getApiClient().create(Api.class);

            api.SendAcceptRejectOrBidServices(type,reason,bookingId).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    CommonUtils.dismissProgress();
                    if (response.body()!=null){
                        sendAcceptReject.setValue(response.body());
                    }else {

                    }
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }


        return sendAcceptReject;
    }
}
