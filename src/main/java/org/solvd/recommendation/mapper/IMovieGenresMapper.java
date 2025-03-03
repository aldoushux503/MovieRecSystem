package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.MovieGenres;

import java.sql.SQLException;

@Mapper
public interface IMovieGenresMapper extends IMapper<MovieGenres> {
    MovieGenres get(@Param("movieId") long movieId, @Param("genreId") long genreId) throws SQLException;
    boolean delete(@Param("movieId") long movieId,@Param("genreId") long genreId) throws SQLException;

}
