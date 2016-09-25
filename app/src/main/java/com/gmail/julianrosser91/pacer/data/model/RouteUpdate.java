package com.gmail.julianrosser91.pacer.data.model;

import com.gmail.julianrosser91.pacer.utils.TimeUtils;

public class RouteUpdate {

    private float speed;
    private long distance;
    private long duration;
    private float pace;

    public RouteUpdate(float speed, long distance, long duration, float pace) {
        this.speed = speed;
        this.distance = distance;
        this.duration = duration;
        this.pace = pace;
    }

    public static RouteUpdate getEmptyRouteUpdate() {
        return new RouteUpdate(0f, 0, 0, 0f);
    }

    public String getSpeed() {
        return "" + Math.round(speed * 1000f) / 1000f;
    }

    public String getDistance() {
        return "" + distance;
    }

    public String getDuration() {
        return "" + TimeUtils.formatTimeInSeconds(duration / 1000);
    }

    public String getPace() {
        return "" + TimeUtils.formatTimeInSeconds(pace);
    }

    public void updateInfo(float speed, long distanceInMeters, long duration, float pace) {
        this.speed = speed;
        this.distance = distanceInMeters;
        this.duration = duration;
        this.pace = pace;
    }
}
