package com.omninos.gwappprovider.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.omninos.gwappprovider.modelClasses.CheckSocialLoginModel;
import com.omninos.gwappprovider.modelClasses.ProviderLoginModel;
import com.omninos.gwappprovider.modelClasses.SocialLoginModel;

public class AppPreferences {
    private static AppPreferences appPreference;
    private SharedPreferences sharedPreferences;

    private AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(ConstantData.Gwapp, Context.MODE_PRIVATE);
    }

    public static AppPreferences init(Context context) {
        if (appPreference == null) {
            appPreference = new AppPreferences(context);
        }
        return appPreference;
    }


    public void SaveString(Activity activity, String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String GetString(Activity activity, String key) {

        return sharedPreferences.getString(key, "");
    }

    public void Logout(Activity activity) {

        sharedPreferences.edit().clear().apply();
    }


    public void saveLoginDetail(ProviderLoginModel userLoginModel) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantData.LOG_IN_DATA, gson.toJson(userLoginModel));
        editor.apply();
    }


    public ProviderLoginModel getLoginDetail() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(ConstantData.LOG_IN_DATA, ""), ProviderLoginModel.class);
    }

    public void saveSocialLoginDetail(CheckSocialLoginModel socialLoginModel) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ConstantData.LOG_IN_DATA, gson.toJson(socialLoginModel));
        editor.apply();
    }

    public CheckSocialLoginModel getSocialLoginData() {
        Gson gson = new Gson();
        return gson.fromJson(sharedPreferences.getString(ConstantData.LOG_IN_DATA, ""), CheckSocialLoginModel.class);
    }
}
