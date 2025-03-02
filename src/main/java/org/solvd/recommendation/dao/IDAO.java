package org.solvd.recommendation.dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Generic DAO interface defining standard operations for data access.
 * @param <T> Entity type
 * @param <ID> Entity identifier type
 */
public interface IDAO<T, ID> {

    T get(ID id);


    ID save(T entity);


    void update(T entity);


    void delete(T entity);


    List<T> getAll();
}