package ru.javawebinar.topjava.web.converter;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomTimeFormatter implements Formatter<LocalTime> {

    private static final String TIME_FORMAT = "HH:mm:ss";

    @Override
    @NonNull
    public LocalTime parse(@NonNull String text, @NonNull Locale locale) {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    @Override
    @NonNull
    public String print(LocalTime localTime, @NonNull Locale locale) {
        return localTime.toString();
    }
}
