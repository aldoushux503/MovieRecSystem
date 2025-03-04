package org.solvd.recommendation.service;


import org.solvd.recommendation.model.Genre;

public interface IGenreService extends IService<Genre, Long> {
    void addGenreToMovie(Long movieId, Long genreId);

    void removeGenreFromMovie(Long movieId, Long genreId);
}
