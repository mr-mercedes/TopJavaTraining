package ru.javawebinar.topjava.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {

    private final Integer id;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;


    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public static Meal of(LocalDateTime dateTime, String description, int calories) {
        return new Meal(null, dateTime, description, calories);
    }

    public static Meal of(Integer id, LocalDateTime dateTime, String description, int calories) {
        return new Meal(id, dateTime, description, calories);
    }

    public Meal withId(Integer id) {
        return new Meal(id, dateTime, description, calories);
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
