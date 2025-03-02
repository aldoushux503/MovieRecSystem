package org.solvd.recommendation.dao.mybatis;


import org.solvd.recommendation.dao.IPersonDAO;
import org.solvd.recommendation.mapper.IPersonMapper;
import org.solvd.recommendation.model.Person;

public class PersonMyBatisDAO extends AbstractMyBatisDAO<Person, Long, IPersonMapper> implements IPersonDAO {

    public PersonMyBatisDAO() {
        super(Person.class);
    }

    @Override
    protected Class<IPersonMapper> getMapperClass() {
        return IPersonMapper.class;
    }

    @Override
    protected Long getEntityId(Person entity) {
        return entity.getPersonId();
    }
}