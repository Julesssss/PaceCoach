package com.gmail.julianrosser91.pacer.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.gmail.julianrosser91.pacer.R;

public class PermissionHelper {

    public static boolean isLocationPermissionEnabled(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public static void askForLocationPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.LOCATION_PERMISSION_REQUEST);
    }

    public static void initialiseDefaultPreferenceValues(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.pref_general, true);
        PreferenceManager.setDefaultValues(context, R.xml.pref_notification, true);
        PreferenceManager.setDefaultValues(context, R.xml.pref_developer, true);
    }

    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void showGpsPrompt(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

}
