package org.solvd.recommendation.dao.mybatis;

import org.solvd.recommendation.dao.IPersonRoleDAO;
import org.solvd.recommendation.mapper.IPersonRoleMapper;
import org.solvd.recommendation.model.PersonRole;

public class PersonRoleMyBatisDAO extends AbstractMyBatisDAO<PersonRole, Long, IPersonRoleMapper> implements IPersonRoleDAO {

    public PersonRoleMyBatisDAO() {
        super(PersonRole.class);
    }

    @Override
    protected Class<IPersonRoleMapper> getMapperClass() {
        return IPersonRoleMapper.class;
    }

    @Override
    protected Long getEntityId(PersonRole entity) {
        return entity.getPersonRoleId();
    }
}