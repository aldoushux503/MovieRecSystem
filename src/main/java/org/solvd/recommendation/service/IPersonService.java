package org.solvd.recommendation.service;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.Person;

import java.util.List;

public interface IPersonService extends IService<Person, Long> {
    List<Movie> getMoviesByPerson(Long personId, Integer roleId);
}
