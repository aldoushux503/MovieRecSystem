package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Param;

import java.sql.SQLException;
import java.util.List;

public interface IMapper<T> {
    T get(@Param("id") long id) throws SQLException;
    long save(T entity) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(@Param("id") long id) throws SQLException;
    List<T> getAll() throws SQLException;
}
