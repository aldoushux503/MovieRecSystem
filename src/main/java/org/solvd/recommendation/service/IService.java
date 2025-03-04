package org.solvd.recommendation.service;

import java.util.List;

public interface IService<T, ID> {
    T getById(ID id);

    ID create(T entity);

    void update(T entity);

    void delete(T entity);

    List<T> getAll();
}
