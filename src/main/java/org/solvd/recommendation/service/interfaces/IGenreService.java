package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.Genre;

import java.util.List;

/**
 * Service interface for genre-related operations.
 * Handles genre information management.
 */
public interface IGenreService {
    Genre getGenreById(Long genreId);

    List<Genre> getAllGenres();

    Long createGenre(Genre genre);

    void updateGenre(Genre genre);

    void deleteGenre(Genre genre);

    void addGenreToMovie(Long movieId, Long genreId);

    void removeGenreFromMovie(Long movieId, Long genreId);

    void addUserPreferredGenre(Long userId, Long genreId);

    void removeUserPreferredGenre(Long userId, Long genreId);
}
