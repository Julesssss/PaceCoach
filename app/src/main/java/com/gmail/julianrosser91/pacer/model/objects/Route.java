package com.gmail.julianrosser91.pacer.model.objects;

import com.gmail.julianrosser91.pacer.utils.PaceUtils;

import java.util.ArrayList;

public class Route {

    public interface RouteUpdateListener {

        void onRouteUpdated(RouteUpdate routeUpdate);
    }
    private ArrayList<Split> splits;

    private long startTimeInMillis;
    private RouteUpdateListener mListener;
    // Pre computed totals
    private long distanceInMeters; // check this isn't null!!!!!

    private float pace; // time per km
    private float speed;
    private RouteUpdate lastRouteUpdate;

    public Route(RouteUpdateListener listener) {
        this.splits = new ArrayList<>();
        initialiseTotals();
        this.mListener = listener;
    }

    private void initialiseTotals() {
        distanceInMeters = 0;
        pace = 0;
        lastRouteUpdate = RouteUpdate.getEmptyRouteUpdate();
    }

    public void addSplit(Split split) {
        if (splits.size() == 0) {
            startTimeInMillis = System.currentTimeMillis(); // todo - check in future - is this real time!
        }
        splits.add(split);
        recomputeTotals(split);
        updateListeners();
    }

    private void updateListeners() {
        RouteUpdate routeUpdate = new RouteUpdate(getSpeed(), getDistance(), getDuration(), getPace());
        mListener.onRouteUpdated(routeUpdate);
    }

    private void recomputeTotals(Split split) {
        distanceInMeters += split.getMeters();
        speed = split.getKmPerHour();
        pace = PaceUtils.getPace(getDistance(), getDuration());
        lastRouteUpdate.updateInfo(getSpeed(), getDistance(), getDuration(), getPace());
    }

    public void reset() {

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
        return System.currentTimeMillis() - startTimeInMillis;
    }

    private long getDistance() {
        return distanceInMeters;
    }

    private float getPace() {
        return pace;
    }

}
