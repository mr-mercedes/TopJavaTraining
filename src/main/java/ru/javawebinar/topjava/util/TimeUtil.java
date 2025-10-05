package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        if  (startTime == null && endTime == null) return true;
        final LocalTime start = (startTime != null) ? startTime : LocalTime.MIN;
        final LocalTime end = (endTime != null) ? endTime : LocalTime.MAX;
        return !lt.isBefore(start) && lt.isBefore(end);
    }
}
