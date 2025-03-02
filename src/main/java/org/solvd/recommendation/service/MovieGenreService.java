package org.solvd.recommendation.service;


import org.solvd.recommendation.dao.IMovieGenreDAO;
import org.solvd.recommendation.model.MovieGenre;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieGenre service implementation.
 */
public class MovieGenreService extends AbstractService<MovieGenre, CompositeKey2<Long, Long>, IMovieGenreDAO> {
    MovieGenreService(IMovieGenreDAO dao) {
        super(dao);
    }

    public MovieGenre getByMovieAndGenre(Long movieId, Long genreId) {
        return dao.get(new CompositeKey2<>(movieId, genreId));
    }

    public List<MovieGenre> getByMovie(Long movieId) {
        return dao.getAll().stream()
                .filter(mg -> mg.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
}
