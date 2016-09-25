package com.gmail.julianrosser91.pacer.data.database;

import android.provider.BaseColumns;

public final class RoutesContract {

    /**
     * Prevent accidental instantiation
     */
    private RoutesContract() {
    }

    /**
     * Inner class that defines the table contents
     * */
    public static class RoutesEntry implements BaseColumns {

        public static final String TABLE_NAME = "routes_table";
        public static final String COLUMN_NAME_LAT = "latitude";
        public static final String COLUMN_NAME_LON = "longitude";
        public static final String COLUMN_NAME_TIME = "timestamp";

    }

}
