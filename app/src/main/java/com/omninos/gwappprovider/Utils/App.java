package com.omninos.gwappprovider.Utils;

import android.app.Application;
import android.content.Context;

public class App extends Application {
//    private static App instance;
//
//    private static AppPreferences appPreferences;
//
//    public static App getContext() {
//        return instance;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        appPreferences=new AppPreferences();
//    }
//
//    public static AppPreferences getAppPreferences() {
//        return appPreferences;
//    }


    private static App instance;

    private Context context;
    private static AppPreferences appPreference;
    private static SingltonPojo sinltonPojo;

    public static App getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appPreference = AppPreferences.init(context);
        sinltonPojo = new SingltonPojo();
    }

    public static SingltonPojo getSinltonPojo() {
        return sinltonPojo;
    }

    public static AppPreferences getAppPreference() {
        return appPreference;
    }
}
