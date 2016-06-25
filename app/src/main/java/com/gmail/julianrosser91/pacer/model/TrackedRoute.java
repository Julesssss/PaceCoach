package com.gmail.julianrosser91.pacer.model;

import android.location.Location;

import java.util.ArrayList;

public class TrackedRoute {

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

    // todo - getDistanceBetweenNodes()

    // todo - getTotalDistance()

    // todo - getTimeBetweenNodes

    // todo - getTotalTime
}