package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.util.CompositeKey2;

public interface IMovieGenresDAO extends IDAO<MovieGenres, CompositeKey2<Long, Long>> {
}

