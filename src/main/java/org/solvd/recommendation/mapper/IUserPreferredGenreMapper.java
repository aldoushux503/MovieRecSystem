package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.UserPreferredGenre;

import java.sql.SQLException;

@Mapper
public interface IUserPreferredGenreMapper extends IMapper<UserPreferredGenre> {
    UserPreferredGenre get(@Param("genreId") long genreId,@Param("userId") long userId) throws SQLException;
    boolean delete(@Param("genreId") long genreId,@Param("userId") long userId) throws SQLException;
}
