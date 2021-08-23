package com.harini.primary.utill;

import java.util.Locale;

public class TimeFormatter {
    public static String convert24hToAmPm(String time) {// expected format hh:mm
        int hourInt = Integer.parseInt(time.split(":")[0]);
        int minuteInt = Integer.parseInt(time.split(":")[1]);
        String timePeriod = "am";

        if (hourInt == 0) {
            hourInt = 12;
        } else if (hourInt >= 12) {
            timePeriod = "pm";
            hourInt = hourInt > 12 ? (hourInt - 12) : hourInt;
        }
        return String.format(Locale.getDefault(), "%02d:%02d %s", hourInt, minuteInt, timePeriod);
    }
}
