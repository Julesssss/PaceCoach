package com.gmail.julianrosser91.pacer.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.gmail.julianrosser91.pacer.data.database.RoutesContract.RoutesEntry;

public class RoutesDbHelper extends SQLiteOpenHelper {

    // Database instance
    private SQLiteDatabase mDatabase;

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

    public RoutesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDatabase = getWritableDatabase();
    }

    public static RoutesDbHelper getInstance(Context context) {
        return new RoutesDbHelper(context);
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

    /**
     * CRUD functions
     */
    public void printDatabaseData() {
        Cursor cursor = getDatabaseCursor();
        try {
            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(RoutesEntry._ID));
                String itemLat = cursor.getString(cursor.getColumnIndexOrThrow(RoutesEntry.COLUMN_NAME_LAT));
                String itemLon = cursor.getString(cursor.getColumnIndexOrThrow(RoutesEntry.COLUMN_NAME_LON));
                String itemTime = cursor.getString(cursor.getColumnIndexOrThrow(RoutesEntry.COLUMN_NAME_TIME));
                Log.i(getClass().getSimpleName(), "ROW " + itemId + ": "+ itemLat + " || "
                        + itemLon + " --- " + itemTime);
            }
        } finally {
            cursor.close();
        }
    }

    public long addLocationToDatabase(Location location) {
        // Create a new map of values, where column names are keys
        ContentValues values = new ContentValues();
        values.put(RoutesEntry.COLUMN_NAME_LAT, location.getLatitude());
        values.put(RoutesEntry.COLUMN_NAME_LON, location.getLongitude());
        values.put(RoutesEntry.COLUMN_NAME_TIME, location.getTime());

        // Insert the new row, returning primary key value of new row
        return mDatabase.insert(RoutesContract.RoutesEntry.TABLE_NAME, null, values);
    }

    public void deleteTable() {
        mDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(mDatabase);
    }

    public void deleteRow() {
        // Define WHERE
        String selection = RoutesEntry._ID + " LIKE ?";
        // Specify arguments in placeholder order
        String[] selectionArgs = { "query" };
        // issue SQL statement
        mDatabase.delete(RoutesEntry.TABLE_NAME, null, null);
    }

    public Cursor getDatabaseCursor() {
        // Define projection that specifies which columns from the mDatabase you will actually use after query
        String[] projection = {
                RoutesEntry._ID,
                RoutesEntry.COLUMN_NAME_LAT,
                RoutesEntry.COLUMN_NAME_LON,
                RoutesEntry.COLUMN_NAME_TIME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = RoutesEntry._ID + " = ?";
        String[] selectionArgs = { "5" };

        // How we want the results sorted in the resulting Cursor
        String sortOrder = RoutesEntry._ID + " ASC";

        return mDatabase.query(
                RoutesEntry.TABLE_NAME, // the table to query
                projection,           // the columns to return
                null,            // the columns for the WHERE clause
                null,         // the values for the WHERE clause
                null,                 // don't group the rows
                null,                 // don't filter by row groups
                sortOrder);
    }
}
