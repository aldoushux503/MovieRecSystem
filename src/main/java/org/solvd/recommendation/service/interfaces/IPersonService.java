package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.Person;
import org.solvd.recommendation.model.PersonRole;

import java.util.List;

/**
 * Service interface for person-related operations.
 * Handles information about directors, actors, and other contributors.
 */
public interface IPersonService {
    Person getPersonById(Long personId);

    List<Person> getAllPeople();

    Long createPerson(Person person);

    void updatePerson(Person person);

    void deletePerson(Person person);

    List<Movie> getMoviesByPerson(Long personId, Integer roleId);

    List<PersonRole> getAllPersonRoles();
}
