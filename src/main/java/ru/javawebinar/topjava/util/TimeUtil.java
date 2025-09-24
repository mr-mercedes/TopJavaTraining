package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime start, LocalTime end) {
        return lt.compareTo(start) >= 0 && lt.compareTo(end) < 0;
    }
}
