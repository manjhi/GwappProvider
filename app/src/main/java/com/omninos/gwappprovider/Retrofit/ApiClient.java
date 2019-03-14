package com.omninos.gwappprovider.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static Retrofit retrofit,retrofitRoute=null;

    private static final String BASE_URL_DIRECTION = "https://maps.googleapis.com/";
    private static final String BASE_URL = "http://infosif.com/gwappApplication/index.php/api/user/";

    public static Retrofit getApiClient(){

        if (retrofit==null){


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }



    public static Retrofit getClientRoute(){

        if(retrofitRoute ==null){


            retrofitRoute = new Retrofit.Builder()
                    .baseUrl(BASE_URL_DIRECTION)
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        }
        return retrofitRoute;

    }
}
