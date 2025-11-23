package ru.javawebinar.topjava.web.converter;

import org.springframework.format.Formatter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomDateFormatter implements Formatter<LocalDate> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    @NonNull
    public LocalDate parse(@NonNull String text, @NonNull Locale locale) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    @Override
    @NonNull
    public String print(LocalDate localDate, @NonNull Locale locale) {
        return localDate.toString();
    }
}
