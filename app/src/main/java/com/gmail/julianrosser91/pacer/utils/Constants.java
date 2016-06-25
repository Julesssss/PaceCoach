package com.gmail.julianrosser91.pacer.utils;

import java.text.SimpleDateFormat;

public class Constants {

    /**
     * Time and Date
     */
    private static final int MILLISECONDS_PER_SECOND = 1000;
    final static public SimpleDateFormat DATE_FORMAT_LAST_UPDATED = new SimpleDateFormat("hh:mm.ss");

    /**
     * FusedLocationProvider preferences
     */
    public static final int UPDATE_INTERVAL_IN_SECONDS = 1;
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;
    public static final int FASTEST_INTERVAL_IN_MILLISECONDS = 100;

    /**
     * Request codes
     */
    public final static int LOCATION_PERMISSION_REQUEST = 100;



}
