package com.gmail.julianrosser91.pacer;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.gmail.julianrosser91.pacer.data.database.RoutesDatabase;
import com.gmail.julianrosser91.pacer.utils.PermissionHelper;

public class Pacer extends Application {

    private static Pacer mInstance;
    private static RoutesDatabase mDatabase;

    public static Pacer getInstance() {
        return mInstance;
    }

    public static RoutesDatabase getRoutesDatabase(Context context) {
        if (mDatabase == null) {
            return new RoutesDatabase(context);
        } else {
            return mDatabase;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mDatabase = new RoutesDatabase(this);
        PermissionHelper.initialiseDefaultPreferenceValues(this);
    }
}
