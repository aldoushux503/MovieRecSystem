package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IMovieDAO;
import org.solvd.recommendation.model.Movie;

/**
 * Movie service implementation.
 */
public class MovieService extends AbstractService<Movie, Long, IMovieDAO> {
    MovieService(IMovieDAO dao) {
        super(dao);
    }
}
