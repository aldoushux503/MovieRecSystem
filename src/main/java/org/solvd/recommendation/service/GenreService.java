package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IGenreDAO;
import org.solvd.recommendation.model.Genre;

/**
 * Genre service implementation.
 */
public class GenreService extends AbstractService<Genre, Long, IGenreDAO> {
    GenreService(IGenreDAO dao) {
        super(dao);
    }
}
