package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class TimeUtil {
    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime start, LocalTime end) {
        return !lt.isBefore(start) && lt.isBefore(end);
    }
}
