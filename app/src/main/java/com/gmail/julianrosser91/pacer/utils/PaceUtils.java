package com.gmail.julianrosser91.pacer.utils;

public class PaceUtils {

    public static float getKmPerHour(float millis, float meters) {
        float seconds = millis / 1000;
        float km = meters / 1000;
        float kmPerSecond = km / seconds;
        float kmPerMinute = kmPerSecond * 60;
        return kmPerMinute * 60;
    }
}
