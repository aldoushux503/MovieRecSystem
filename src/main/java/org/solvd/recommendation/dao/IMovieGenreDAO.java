package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.MovieGenre;
import org.solvd.recommendation.util.CompositeKey2;

import java.sql.SQLException;

public interface IMovieGenreDAO extends IDAO<MovieGenre, CompositeKey2<Long, Long>> {
}

