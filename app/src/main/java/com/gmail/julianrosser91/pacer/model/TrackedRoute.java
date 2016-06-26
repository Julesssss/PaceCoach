package com.gmail.julianrosser91.pacer.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TrackedRoute implements Parcelable {

    private ArrayList<Location> locationNodes;

    public TrackedRoute() {
        this.locationNodes = new ArrayList<>();
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

    public ArrayList<Location> getLocationNodes() {
        return locationNodes;
    }

    public void addLocation(Location location) {
        locationNodes.add(location);
    }

    public int getSize() {
        return locationNodes.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(locationNodes);
    }

    // todo - getDistanceBetweenNodes()

    public float getDistanceBetweenLastTwoNodes() {
        if (locationNodes.size() > 1) {
            Location locA = locationNodes.get(locationNodes.size() - 2);
            Location locB = locationNodes.get(locationNodes.size() - 1);
            return distanceBetweenNodes(locA, locB)[0];
        }
        return 111f;
    }

    public float[] distanceBetweenNodes(Location locA, Location locB) {
        float[] results = new float[3];
        Location.distanceBetween(locA.getLatitude(), locA.getLongitude(), locB.getLatitude(),
                locB.getLongitude(), results);
        return results;
    }

    // todo - getTotalDistance()

    // todo - getTimeBetweenNodes

    // todo - getTotalTime
}