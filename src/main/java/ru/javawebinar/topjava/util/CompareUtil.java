package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.AbstractNamedEntity;

import java.util.Comparator;

public class CompareUtil {

    public static <T extends AbstractNamedEntity> Comparator<T> compareByName() {
        return Comparator.comparing(AbstractNamedEntity::getName, String.CASE_INSENSITIVE_ORDER);
    }
}
