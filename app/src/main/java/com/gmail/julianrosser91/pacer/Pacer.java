package com.gmail.julianrosser91.pacer;

import android.app.Application;

import com.gmail.julianrosser91.pacer.data.database.RoutesDatabase;

public class Pacer extends Application {

    private static Pacer mInstance;
    private static RoutesDatabase mDatabase;

    public static Pacer getInstance() {
        return mInstance;
    }

    public static RoutesDatabase getRoutesDatabase() {
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mDatabase = new RoutesDatabase(this);
    }
}
