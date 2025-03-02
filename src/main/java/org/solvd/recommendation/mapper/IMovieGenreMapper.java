package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.MovieGenre;

import java.sql.SQLException;

@Mapper
public interface IMovieGenreMapper extends IMapper<MovieGenre> {
    MovieGenre get(@Param("movieId") long movieId,@Param("genreId") long genreId) throws SQLException;

}
