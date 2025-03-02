package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.ViewingHistory;

import java.sql.SQLException;

@Mapper
public interface IViewingHistoryMapper extends IMapper<ViewingHistory> {
    ViewingHistory get(@Param("movieId") long movieId,@Param("userId") long userId) throws SQLException;
    boolean delete(@Param("movieId") long movieId,@Param("userId") long userId) throws SQLException;
}
