package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;

public final class DateTimeFormatter {

    public static String formatLocalDateTime(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) return null;
        if (pattern == null || pattern.isEmpty()) pattern = "yyyy-MM-dd HH:mm";
        return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern(pattern));
    }
}
