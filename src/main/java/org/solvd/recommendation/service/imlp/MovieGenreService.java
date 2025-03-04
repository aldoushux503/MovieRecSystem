package org.solvd.recommendation.service.imlp;


import org.solvd.recommendation.dao.IMovieGenresDAO;
import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.service.IMovieGenreService;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MovieGenre service implementation.
 */

public class MovieGenreService
        extends AbstractService<MovieGenres, CompositeKey2<Long, Long>, IMovieGenresDAO>
        implements IMovieGenreService {

    public MovieGenreService(IMovieGenresDAO dao) {
        super(dao);
    }

    @Override
    public MovieGenres getByMovieAndGenre(Long movieId, Long genreId) {
        try {
            return dao.get(new CompositeKey2<>(movieId, genreId));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<MovieGenres> getByMovie(Long movieId) {
        return dao.getAll().stream()
                .filter(mg -> mg.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieGenres> getByGenre(Long genreId) {
        return dao.getAll().stream()
                .filter(mg -> mg.getGenreId().equals(genreId))
                .collect(Collectors.toList());
    }
}
