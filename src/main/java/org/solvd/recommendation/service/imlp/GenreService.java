package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IGenreDAO;
import org.solvd.recommendation.model.Genre;

/**
 * Genre service implementation.
 */
public class GenreService extends AbstractService<Genre, Long, IGenreDAO> {
    public GenreService(IGenreDAO dao) {
        super(dao);
    }
}
