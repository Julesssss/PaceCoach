package com.gmail.julianrosser91.pacer.model.objects;

import com.gmail.julianrosser91.pacer.utils.PaceUtils;

public class Split {

    long seconds;
    long meters;


    public Split() {
        this.seconds = 0;
        this.meters = 0;
    }

    public Split(long meters, long seconds) {
        this.seconds = seconds;
        this.meters = meters;
    }

    public long getSeconds() {
        return seconds;
    }

    public long getMeters() {
        return meters;
    }

    public String getKmPerHour() {
        return "" + PaceUtils.getKmPerHour(seconds * 1000, meters);
    }

}