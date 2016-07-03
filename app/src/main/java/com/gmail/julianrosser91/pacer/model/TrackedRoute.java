package com.gmail.julianrosser91.pacer.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

public class TrackedRoute implements Parcelable {

    private ArrayList<Location> locationNodes;
    private float totalDistance;
    private long startTimeMillis;
    private long endTimeMillis;


    public TrackedRoute() {
        this.locationNodes = new ArrayList<>();
        startTimeMillis = new Date().getTime();
    }

    public ArrayList<Location> getLocationNodes() {
        return locationNodes;
    }

    public Location getLastLocation() {
        if (locationNodes.size() > 0) {
            return locationNodes.get(locationNodes.size() - 1);
        } else {
            return null;
        }
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    // get TIME per KM
    public String getTotalPace() {
        float millis = getTotalTimeInMillis();
        float seconds = millis / 1000;
        float km = totalDistance / 1000;
        float secondsPerKm = seconds / km;
        float minutesPerKm = secondsPerKm / 60;

        float kmPerSecond = km / seconds;
        float kmPerMinute = kmPerSecond * 60;
        float kmPerHour = kmPerMinute * 60;

        Log.i(getClass().getSimpleName(), "\n\nTotal km " + km);
        Log.i(getClass().getSimpleName(), "Total seconds: " + getTotalTimeInMillis() / 1000);


        Log.i(getClass().getSimpleName(), "sec/km: " + secondsPerKm);
        Log.i(getClass().getSimpleName(), "min/km: " + minutesPerKm);
        Log.i(getClass().getSimpleName(), "km/sec: " + kmPerSecond * 60);
        Log.i(getClass().getSimpleName(), "km/min: " + kmPerMinute);
        Log.i(getClass().getSimpleName(), "km/hour: " + kmPerHour);

        return String.valueOf(secondsPerKm);
    }

    public long getTotalTimeInMillis() {
        return endTimeMillis - startTimeMillis;
    }

    private void addLastNodeToTotals(Location location) {
        endTimeMillis = location.getTime();

        // get distance between last 2, add to total
        float distance = getDistanceBetweenLastTwoNodes();
        totalDistance += distance;
        Log.i(getClass().getSimpleName(), "Total Dist: " + totalDistance);

    }

    public void addLocation(Location location) {
        if (locationNodes.size() == 0) {
            startTimeMillis = location.getTime();
        }
        locationNodes.add(location);
        addLastNodeToTotals(location);
        getTotalPace();

    }

    public int getSize() {
        return locationNodes.size();
    }

    public void resetData() {
        locationNodes = new ArrayList<>();
    }

    public float getDistanceBetweenLastTwoNodes() {
        if (locationNodes.size() > 1) {
            Location locA = locationNodes.get(locationNodes.size() - 2);
            Location locB = locationNodes.get(locationNodes.size() - 1);
            return distanceBetweenNodes(locA, locB)[0];
        }
        return 0f;
    }

    public long getTimeBetweenLastTwoNodes() {
        if (locationNodes.size() > 1) {
            Location locA = locationNodes.get(locationNodes.size() - 2);
            Location locB = locationNodes.get(locationNodes.size() - 1);
            return getTimeBetweenNodes(locA, locB);
        }
        return 0;
    }

    public float[] distanceBetweenNodes(Location locA, Location locB) {
        float[] results = new float[3];
        Location.distanceBetween(locA.getLatitude(), locA.getLongitude(), locB.getLatitude(),
                locB.getLongitude(), results);
        return results;
    }

    public long getTimeBetweenNodes(Location locA, Location locB) {
        return locB.getTime() - locA.getTime();
    }


    protected TrackedRoute(Parcel in) {
        locationNodes = in.createTypedArrayList(Location.CREATOR);
    }

    public static final Creator<TrackedRoute> CREATOR = new Creator<TrackedRoute>() {
        @Override
        public TrackedRoute createFromParcel(Parcel in) {
            return new TrackedRoute(in);
        }

        @Override
        public TrackedRoute[] newArray(int size) {
            return new TrackedRoute[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(locationNodes);
    }
}