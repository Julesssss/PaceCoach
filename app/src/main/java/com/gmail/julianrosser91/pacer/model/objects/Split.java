package com.gmail.julianrosser91.pacer.model.objects;

public class Split {

    long seconds;
    long meters;
    float speed;

    public Split(long meters, long seconds) {
        this.seconds = 0;
        this.meters = 0;
    }

    public Split(long meters, long seconds, float speed) {
        this.seconds = seconds;
        this.meters = meters;
        this.speed = speed;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getMeters() {
        return meters;
    }

    public float getKmPerHour() {
        return speed;
    }

}