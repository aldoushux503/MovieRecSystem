package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IGenreDAO;
import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.service.IGenreService;
import org.solvd.recommendation.service.IMovieGenreService;

/**
 * Genre service implementation.
 */

public class GenreService extends AbstractService<Genre, Long, IGenreDAO> implements IGenreService {
    private final IMovieGenreService movieGenreService;

    public GenreService(IGenreDAO dao, IMovieGenreService movieGenreService) {
        super(dao);
        this.movieGenreService = movieGenreService;
    }

    @Override
    public void addGenreToMovie(Long movieId, Long genreId) {
        MovieGenres movieGenres = new MovieGenres();
        movieGenres.setMovieId(movieId);
        movieGenres.setGenreId(genreId);
        movieGenreService.create(movieGenres);
    }

    @Override
    public void removeGenreFromMovie(Long movieId, Long genreId) {
        MovieGenres movieGenres = movieGenreService.getByMovieAndGenre(movieId, genreId);
        if (movieGenres != null) {
            movieGenreService.delete(movieGenres);
        }
    }
}