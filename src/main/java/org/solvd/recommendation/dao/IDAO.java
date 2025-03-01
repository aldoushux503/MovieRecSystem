package org.solvd.recommendation.dao;

import java.sql.SQLException;
import java.util.List;

public interface IDAO<T>{
    T get(long id) throws SQLException;
    long save(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    List<T> getAll() throws SQLException;
}