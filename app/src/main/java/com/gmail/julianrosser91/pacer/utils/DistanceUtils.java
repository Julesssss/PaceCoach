package com.gmail.julianrosser91.pacer.utils;

public class DistanceUtils {

    public static String getKmFromMeters(long meters) {
        double kmm = (meters / 10f);

        String kmString = String.valueOf(kmm /100f);
        if (kmString.length() < 3) {
            kmString += "0";
        } else if (kmString.length() > 3) {
            kmString = kmString.substring(0, 4);
        }
        return kmString;
    }
}
