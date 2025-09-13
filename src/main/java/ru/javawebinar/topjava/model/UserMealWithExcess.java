package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final AtomicBoolean excessRef;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, AtomicBoolean excessRef) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excessRef = excessRef;
    }

    public UserMealWithExcess(UserMeal userMeal, AtomicBoolean excessRef) {
        this(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excessRef);
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excessRef.get() +
                '}';
    }
}
