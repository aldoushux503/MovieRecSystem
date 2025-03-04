package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IPersonRoleDAO;
import org.solvd.recommendation.model.PersonRole;
import org.solvd.recommendation.service.IPersonRoleService;

/**
 * PersonRole service implementation.
 */
public class PersonRoleService extends AbstractService<PersonRole, Long, IPersonRoleDAO> implements IPersonRoleService {

    public PersonRoleService(IPersonRoleDAO dao) {
        super(dao);
    }
}