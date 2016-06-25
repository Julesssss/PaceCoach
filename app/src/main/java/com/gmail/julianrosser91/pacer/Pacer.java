package com.gmail.julianrosser91.pacer;

import android.app.Application;

/**
 * Created by julianrosser on 16/06/2016.
 */
public class Pacer extends Application {

    private static Pacer mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static Pacer getmInstance() {
        return mInstance;
    }
}
