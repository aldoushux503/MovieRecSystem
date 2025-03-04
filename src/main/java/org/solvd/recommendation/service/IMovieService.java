package org.solvd.recommendation.service;

import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.Person;

import java.util.List;

public interface IMovieService extends IService<Movie, Long> {
    List<Genre> getMovieGenres(Long movieId);

    List<Person> getMovieContributors(Long movieId, Integer roleId);

    List<Movie> getMoviesByGenre(Long genreId);
}
