package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.MovieGenre;

import java.sql.SQLException;

public interface IMovieGenreDAO extends IDAO<MovieGenre> {

    MovieGenre get(long movieId, long genreId) throws SQLException;
}
