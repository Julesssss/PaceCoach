package com.gmail.julianrosser91.pacer;

public class GPSCoordinates {
    private float longitude = -1;
    private float latitude = -1;

    public GPSCoordinates(float theLatitude, float theLongitude) {
        longitude = theLongitude;
        latitude = theLatitude;
    }

    public GPSCoordinates(double theLatitude, double theLongitude) {
        longitude = (float) theLongitude;
        latitude = (float) theLatitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }
}