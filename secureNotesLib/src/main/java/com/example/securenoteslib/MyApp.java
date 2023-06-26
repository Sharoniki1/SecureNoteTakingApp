package com.example.securenoteslib;

import android.app.Application;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPV.init(this);
    }
}
