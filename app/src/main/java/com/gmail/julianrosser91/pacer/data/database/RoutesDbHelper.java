package com.gmail.julianrosser91.pacer.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmail.julianrosser91.pacer.data.database.RoutesContract.RoutesEntry;

public class RoutesDbHelper extends SQLiteOpenHelper {

    // Statements for creation and deletion
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + RoutesEntry.TABLE_NAME + " (" +
            RoutesEntry._ID + " INTEGER PRIMARY KEY," +
            RoutesEntry.COLUMN_NAME_LAT + TEXT_TYPE + COMMA_SEP +
            RoutesEntry.COLUMN_NAME_LON + TEXT_TYPE + COMMA_SEP +
            RoutesEntry.COLUMN_NAME_TIME + TEXT_TYPE + " )";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + RoutesEntry.TABLE_NAME;

    // If database schema changes, we must increment the mDatabase version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Routes.db";

    private static RoutesDbHelper mInstance = null;

    private RoutesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static RoutesDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RoutesDbHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is a cache for online data, so upgrading should discard data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
