package ru.javawebinar.topjava.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    boolean exists(ID id);

    List<T> findAll();

    T create(T entity);

    Optional<T> findById(ID id);

    T update(T entity);

    void delete(ID id);
}
