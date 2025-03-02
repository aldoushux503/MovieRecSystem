package org.solvd.recommendation.dao.mybatis;

import org.solvd.recommendation.dao.IUserDAO;
import org.solvd.recommendation.mapper.IUserMapper;
import org.solvd.recommendation.model.User;

public class UserMyBatisDAO extends AbstractMyBatisDAO<User, Long, IUserMapper> implements IUserDAO {

    public UserMyBatisDAO() {
        super(User.class);
    }

    @Override
    protected Class<IUserMapper> getMapperClass() {
        return IUserMapper.class;
    }

    @Override
    protected Long getEntityId(User entity) {
        return entity.getUserId();
    }
}