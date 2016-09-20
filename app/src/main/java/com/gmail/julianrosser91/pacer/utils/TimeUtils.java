package com.gmail.julianrosser91.pacer.utils;

public class TimeUtils {

    public static String formatTimeInSeconds(float seconds) {

        int mins = (int) (seconds / 60);
        int secs = (int) (seconds % 60);

        String sSecs = String.valueOf(secs);
        if (secs < 10) {
            sSecs = "0" + sSecs;
        }
        return String.valueOf(mins) + ":" + sSecs;
    }

}
