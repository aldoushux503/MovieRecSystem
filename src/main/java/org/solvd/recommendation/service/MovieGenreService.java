package org.solvd.recommendation.service;


import org.solvd.recommendation.dao.IMovieGenresDAO;
import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieGenre service implementation.
 */
public class MovieGenreService extends AbstractService<MovieGenres, CompositeKey2<Long, Long>, IMovieGenresDAO> {
    MovieGenreService(IMovieGenresDAO dao) {
        super(dao);
    }

    public MovieGenres getByMovieAndGenre(Long movieId, Long genreId) {
        return dao.get(new CompositeKey2<>(movieId, genreId));
    }

    public List<MovieGenres> getByMovie(Long movieId) {
        return dao.getAll().stream()
                .filter(mg -> mg.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
}
