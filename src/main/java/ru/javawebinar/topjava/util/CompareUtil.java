package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.AbstractNamedEntity;

import java.util.Comparator;
import java.util.List;

public class CompareUtil {

    public static <T extends AbstractNamedEntity> List<T> sortByName(List<T> list) {
        list.sort(Comparator.comparing(AbstractNamedEntity::getName));
        return list;
    }
}
