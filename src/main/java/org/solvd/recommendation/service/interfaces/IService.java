package org.solvd.recommendation.service.interfaces;

import java.util.List;


/**
 * Generic service interface defining standard CRUD operations.
 *
 * @param <T> Entity type
 * @param <ID> Entity identifier type
 */
public interface IService<T, ID> {
    T getById(ID id);

    ID create(T entity);

    void update(T entity);

    void delete(T entity);

    List<T> getAll();
}
