package com.gmail.julianrosser91.pacer.utils;

public class PaceUtils {

    public static float getKmPerHour(float millis, float meters) {
        float seconds = millis / 1000;
        float km = meters / 1000;
        float kmPerSecond = km / seconds;
        float kmPerMinute = kmPerSecond * 60;
        return kmPerMinute * 60;
    }

    public static float getPace(long distance, long duration) {
        double millis = (double) duration;
        double secs = millis / 1000;
        double km = (double) distance / 1000;

        double kmh = (km * 3600) / secs;
        double kmm = kmh / 60;
        double kms = kmm / 60;
        double mps = kms * 1000; // improve

        double secPerKm = (1000 / mps);

        return (float) secPerKm;
    }
}
