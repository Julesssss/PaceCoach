package com.gmail.julianrosser91.pacer.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

public class RoutesDatabase {

    // Database fields
    private SQLiteDatabase mDatabase;
    private RoutesDbHelper mDbHelper;

    public RoutesDatabase(Context context) {
        mDbHelper = RoutesDbHelper.getInstance(context);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void printDatabaseData() {
        Cursor cursor = getDatabaseCursor();
        try {
            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(RoutesContract.RoutesEntry._ID));
                String itemLat = cursor.getString(cursor.getColumnIndexOrThrow(RoutesContract.RoutesEntry.COLUMN_NAME_LAT));
                String itemLon = cursor.getString(cursor.getColumnIndexOrThrow(RoutesContract.RoutesEntry.COLUMN_NAME_LON));
                String itemTime = cursor.getString(cursor.getColumnIndexOrThrow(RoutesContract.RoutesEntry.COLUMN_NAME_TIME));
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
        values.put(RoutesContract.RoutesEntry.COLUMN_NAME_LAT, location.getLatitude());
        values.put(RoutesContract.RoutesEntry.COLUMN_NAME_LON, location.getLongitude());
        values.put(RoutesContract.RoutesEntry.COLUMN_NAME_TIME, location.getTime());

        // Insert the new row, returning primary key value of new row
        return mDatabase.insert(RoutesContract.RoutesEntry.TABLE_NAME, null, values);
    }

    public void deleteTable() {
        mDatabase.execSQL(RoutesDbHelper.SQL_DELETE_ENTRIES);
        mDbHelper.onCreate(mDatabase);
    }

    public void deleteRow() {
        // Define WHERE
        String selection = RoutesContract.RoutesEntry._ID + " LIKE ?";
        // Specify arguments in placeholder order
        String[] selectionArgs = { "query" };
        // issue SQL statement
        mDatabase.delete(RoutesContract.RoutesEntry.TABLE_NAME, null, null);
    }

    public Cursor getDatabaseCursor() {
        // Define projection that specifies which columns from the mDatabase you will actually use after query
        String[] projection = {
                RoutesContract.RoutesEntry._ID,
                RoutesContract.RoutesEntry.COLUMN_NAME_LAT,
                RoutesContract.RoutesEntry.COLUMN_NAME_LON,
                RoutesContract.RoutesEntry.COLUMN_NAME_TIME
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = RoutesContract.RoutesEntry._ID + " = ?";
        String[] selectionArgs = { "5" };

        // How we want the results sorted in the resulting Cursor
        String sortOrder = RoutesContract.RoutesEntry._ID + " ASC";

        return mDatabase.query(
                RoutesContract.RoutesEntry.TABLE_NAME, // the table to query
                projection,           // the columns to return
                null,            // the columns for the WHERE clause
                null,         // the values for the WHERE clause
                null,                 // don't group the rows
                null,                 // don't filter by row groups
                sortOrder);
    }
}
