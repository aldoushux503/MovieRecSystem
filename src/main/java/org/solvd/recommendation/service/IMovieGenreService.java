package org.solvd.recommendation.service;

import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;

public interface IMovieGenreService extends IService<MovieGenres, CompositeKey2<Long, Long>> {
    MovieGenres getByMovieAndGenre(Long movieId, Long genreId);

    List<MovieGenres> getByMovie(Long movieId);

    List<MovieGenres> getByGenre(Long genreId);
}
