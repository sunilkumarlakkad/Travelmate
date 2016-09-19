package com.example.sunillakkad.travelmate.utils;

/**
 * Created by mm98568 on 9/19/16.
 */
public class StringUtils {
    public static String getPrettyTime(String strSecond) {
        int second = Integer.parseInt(strSecond);

        int minute = second / 60;
        int hour = 0;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        String result;
        if (hour == 0) {
            result = String.valueOf(minute) + " min";
        } else {
            result = String.valueOf(hour) + "H " + String.valueOf(minute) + " min";
        }
        return result;
    }

    public static String getPrettyDistance(String strDistance) {
        double dist = Double.parseDouble(strDistance);
        dist = dist * 0.000621371;
        int temp = (int) dist;
        return "(" + String.valueOf(temp) + " mi)";
    }
}
