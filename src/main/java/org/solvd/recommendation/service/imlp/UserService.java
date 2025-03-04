package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IUserDAO;
import org.solvd.recommendation.model.User;

/**
 * User service implementation.
 */
public class UserService extends AbstractService<User, Long, IUserDAO> {
    public UserService(IUserDAO dao) {
        super(dao);
    }
}