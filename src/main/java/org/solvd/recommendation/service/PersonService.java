package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IPersonDAO;
import org.solvd.recommendation.model.Person;

/**
 * Person service implementation.
 */
public class PersonService extends AbstractService<Person, Long, IPersonDAO> {
    PersonService(IPersonDAO dao) {
        super(dao);
    }
}
