package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.Person;

import java.util.List;
import java.util.Map;

/**
 * Service interface for movie-related operations.
 * Handles movie information management and retrieval.
 */
public interface IMovieService {
    Movie getMovieById(Long movieId);

    List<Movie> getAllMovies();

    Long createMovie(Movie movie);

    void updateMovie(Movie movie);

    void deleteMovie(Movie movie);

    List<Genre> getMovieGenres(Long movieId);

    List<Person> getMovieContributors(Long movieId, Integer roleId);

    List<Movie> getMoviesByGenre(Long genreId);

    Map<Long, Double> getMoviePopularityScores(List<Long> movieIds);
}
