package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IPersonRoleDAO;
import org.solvd.recommendation.model.PersonRole;

/**
 * PersonRole service implementation.
 */
public class PersonRoleService extends AbstractService<PersonRole, Long, IPersonRoleDAO> {
    public PersonRoleService(IPersonRoleDAO dao) {
        super(dao);
    }
}
