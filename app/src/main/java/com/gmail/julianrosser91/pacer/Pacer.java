package com.gmail.julianrosser91.pacer;

import android.app.Application;
import android.content.Context;

import com.gmail.julianrosser91.pacer.data.database.RoutesDbHelper;

public class Pacer extends Application {

    private static Pacer mInstance;
    private static RoutesDbHelper mDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mDbHelper = RoutesDbHelper.getInstance(this);
    }

    public static Pacer getInstance() {
        return mInstance;
    }

    public static RoutesDbHelper getDbHelper(Context context) {
        if (mDbHelper != null) {
            return mDbHelper;
        } else {
            mDbHelper = RoutesDbHelper.getInstance(context);
            return mDbHelper;
        }
    }
}
