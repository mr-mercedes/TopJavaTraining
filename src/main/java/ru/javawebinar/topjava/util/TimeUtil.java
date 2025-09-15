package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime start, LocalTime end) {
        if (!start.isAfter(end)) {
            return !lt.isBefore(start) && lt.isBefore(end);
        }
        return !lt.isBefore(start) || lt.isBefore(end);
    }
}
