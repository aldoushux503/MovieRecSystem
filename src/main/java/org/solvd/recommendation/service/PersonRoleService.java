package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IPersonRoleDAO;
import org.solvd.recommendation.model.PersonRole;

/**
 * PersonRole service implementation.
 */
public class PersonRoleService extends AbstractService<PersonRole, Long, IPersonRoleDAO> {
    PersonRoleService(IPersonRoleDAO dao) {
        super(dao);
    }
}
