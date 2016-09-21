package com.gmail.julianrosser91.pacer.model.objects;

import android.location.Location;

import com.gmail.julianrosser91.pacer.utils.PaceUtils;

import java.util.ArrayList;
import java.util.Random;

public class Route {

    private ArrayList<Location> locations;
    private ArrayList<Split> splits;
    private long startTimeInMillis;
    private RouteUpdateListener mListener;

    // Pre computed totals
    private long distanceInMeters;
    private float pace; // time per km
    private float speed;
    private RouteUpdate lastRouteUpdate;

    public Route() {
        initialiseTotals();
    }

    public Route(RouteUpdateListener listener) {
        initialiseTotals();
        this.mListener = listener;
    }

    private void initialiseTotals() {
        this.locations = new ArrayList<>();
        this.splits = new ArrayList<>();
        distanceInMeters = 0;
        pace = 0;
        speed = 0;
        lastRouteUpdate = RouteUpdate.getEmptyRouteUpdate();
    }

    public void addLocation(Location location) {
        if (locations.size() == 0) {
            startTimeInMillis = System.currentTimeMillis(); // todo - check in future - is this real time!
        }
        locations.add(location);
        createSplitFromLastLocations();
    }

    public void addSplit(Split split) {
        splits.add(split);
        recomputeTotals(split);
        updateListeners();
    }

    private void updateListeners() {
        RouteUpdate routeUpdate = new RouteUpdate(getSpeed(), getDistance(), getDuration(), getPace());
        if (mListener != null) {
            mListener.onRouteUpdated(routeUpdate);
        }
    }

    private void recomputeTotals(Split split) {
        distanceInMeters += split.getMeters();
        speed = split.getKmPerHour();
        pace = PaceUtils.getPace(getDistance(), getDuration());
        lastRouteUpdate.updateInfo(getSpeed(), getDistance(), getDuration(), getPace());
    }

    public void reset() {
        splits.clear();
        initialiseTotals();
        updateListeners();
    }

    /**
     * Getters
     */

    public RouteUpdate getLastRouteUpdate() {
        return lastRouteUpdate;
    }

    private float getSpeed() {
        return speed;
    }

    private long getDuration() {
        if (splits.size() != 0) {
            return System.currentTimeMillis() - startTimeInMillis;
        } else return 0;
    }

    private long getDistance() {
        return distanceInMeters;
    }

    private float getPace() {
        return pace;
    }

    private void createSplitFromLastLocations() {
        int locationCount = locations.size();
        if (locationCount > 1) {
            Location locationA = locations.get(locationCount - 2);
            Location locationB = locations.get(locationCount - 1);
            long meters = (long) locationA.distanceTo(locationB);
            long seconds = locationB.getTime() - locationA.getTime();
            float speed = new Random().nextFloat() * 15;
            addSplit(new Split(meters, seconds, speed));
        }
    }

    public interface RouteUpdateListener {

        void onRouteUpdated(RouteUpdate routeUpdate);
    }

}
