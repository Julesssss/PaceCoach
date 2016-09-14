package com.gmail.julianrosser91.pacer;

import android.app.Application;

public class Pacer extends Application {

    private static Pacer mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Pacer getInstance() {
        return mInstance;
    }
}
