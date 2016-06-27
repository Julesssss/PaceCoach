package com.gmail.julianrosser91.pacer.utils;

import java.text.SimpleDateFormat;

public class Constants {

    /**
     * Time and Date
     */
    private static final int MILLISECONDS_PER_SECOND = 3000;
    final static public SimpleDateFormat DATE_FORMAT_LAST_UPDATED = new SimpleDateFormat("hh:mm.ss");
    final static public SimpleDateFormat DATE_FORMAT_SPLIT_TIME = new SimpleDateFormat("m.ss");

    /**
     * FusedLocationProvider preferences
     */
    public static final int UPDATE_INTERVAL_IN_SECONDS = 1;
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;
    public static final int FASTEST_INTERVAL_IN_MILLISECONDS = 1000;

    /**
     * Request codes
     */
    public final static int LOCATION_PERMISSION_REQUEST = 100;



}
