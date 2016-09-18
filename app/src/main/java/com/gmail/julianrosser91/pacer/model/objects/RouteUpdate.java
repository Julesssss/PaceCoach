package com.gmail.julianrosser91.pacer.model.objects;

public class RouteUpdate {

    private String speed;
    private long distance;
    private long duration;
    private float pace;

    public RouteUpdate(String speed, long distance, long duration, float pace) {
        this.speed = speed;
        this.distance = distance;
        this.duration = duration;
        this.pace = pace;
    }

    public String getSpeed() {
        return speed;
    }

    public String getDistance() {
        return "" + distance;
    }

    public String getDuration() {
        return "" + duration;
    }

    public String getPace() {
        return "" + pace;
    }

    public void updateInfo(String speed, long distanceInMeters, long duration, float pace) {
        this.speed = speed;
        this.distance = distanceInMeters;
        this.duration = duration;
        this.pace = pace;
    }
}
