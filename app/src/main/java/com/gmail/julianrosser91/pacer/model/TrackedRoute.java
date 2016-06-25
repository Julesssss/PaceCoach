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

    // todo - getTotalDistance()

    // todo - getTimeBetweenNodes

    // todo - getTotalTime
}