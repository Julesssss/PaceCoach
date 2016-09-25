package com.gmail.julianrosser91.pacer.data.model;

import android.location.Location;

import com.gmail.julianrosser91.pacer.utils.PaceUtils;

import java.util.ArrayList;

public class Route {

    private ArrayList<Location> mLocations;
    private ArrayList<Split> mSplits;
    private long mStartTimeInMillis;
    private RouteUpdateListener mListener;

    // Pre computed totals
    private long mDistanceInMeters;
    private float mCurrentPace; // time per km
    private float mCurrentSpeed;
    private RouteUpdate mLastRouteUpdate;

    public Route() {
        initialiseTotals();
    }

    public Route(RouteUpdateListener listener) {
        initialiseTotals();
        this.mListener = listener;
    }

    private void initialiseTotals() {
        this.mLocations = new ArrayList<>();
        this.mSplits = new ArrayList<>();
        mDistanceInMeters = 0;
        mCurrentPace = 0;
        mCurrentSpeed = 0;
        mLastRouteUpdate = RouteUpdate.getEmptyRouteUpdate();
    }

    public void addLocation(Location location) {
        if (mLocations.size() == 0) {
            mStartTimeInMillis = location.getTime();
        }
        mLocations.add(location);
        createSplitFromLastLocations();
    }

    private void addSplit(Split split) {
        mSplits.add(split);
        recomputeTotals(split);
        updateListeners();
    }

    private void updateListeners() {
        RouteUpdate routeUpdate = new RouteUpdate(getCurrentSpeed(), getDistance(), getDuration(), getCurrentPace());
        if (mListener != null) {
            mListener.onRouteUpdated(routeUpdate);
        }
    }

    private void recomputeTotals(Split split) {
        mDistanceInMeters += split.getMeters();
        mCurrentSpeed = split.getKmPerHour();
        mCurrentPace = PaceUtils.getPace(getDistance(), getDuration());
        mLastRouteUpdate.updateInfo(getCurrentSpeed(), getDistance(), getDuration(), getCurrentPace());
    }

    public void reset() {
        mSplits.clear();
        initialiseTotals();
        updateListeners();
    }

    /**
     * Getters
     */

    public RouteUpdate getLastRouteUpdate() {
        return mLastRouteUpdate;
    }

    private float getCurrentSpeed() {
        return mCurrentSpeed;
    }

    private long getDuration() {
        if (mSplits.size() != 0) {
            return System.currentTimeMillis() - mStartTimeInMillis;
        } else return 0;
    }

    public ArrayList<Location> getLocations() {
        return mLocations;
    }

    private long getDistance() {
        return mDistanceInMeters;
    }

    private float getCurrentPace() {
        return mCurrentPace;
    }

    private void createSplitFromLastLocations() {
        int locationCount = mLocations.size();
        if (locationCount > 1) {
            Location locationA = mLocations.get(locationCount - 2);
            Location locationB = mLocations.get(locationCount - 1);
            long meters = (long) locationA.distanceTo(locationB);
            long seconds = locationB.getTime() - locationA.getTime();
            addSplit(new Split(meters, seconds, locationB.getSpeed()));
        }
    }

    public interface RouteUpdateListener {

        void onRouteUpdated(RouteUpdate routeUpdate);
    }

}
