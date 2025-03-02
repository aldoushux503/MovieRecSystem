package org.solvd.recommendation.dao.mybatis;


import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IDAO;
import org.solvd.recommendation.exeption.DataAccessException;
import org.solvd.recommendation.exeption.DatabaseConnectionException;
import org.solvd.recommendation.exeption.EntityNotFoundException;
import org.solvd.recommendation.mapper.IMapper;

import java.sql.SQLException;
import java.util.List;
import org.apache.ibatis.session.SqlSessionFactory;
import org.solvd.recommendation.util.MyBatisUtil;

/**
 * Abstract implementation of the DAO interface using MyBatis.
 * Provides common CRUD operations for all entities.
 *
 * @param <T> Entity type
 * @param <ID> Entity identifier type
 * @param <M> Mapper interface type
 */
public abstract class AbstractMyBatisDAO<T, ID, M extends IMapper<T>> implements IDAO<T, ID> {
    protected final SqlSessionFactory sqlSessionFactory;
    protected final Class<T> entityClass;

    protected AbstractMyBatisDAO(Class<T> entityClass) {
        this.sqlSessionFactory = MyBatisUtil.getSqlSessionFactory();
        this.entityClass = entityClass;
    }

    protected abstract Class<M> getMapperClass();

    @Override
    public T get(ID id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            M mapper = session.getMapper(getMapperClass());
            T entity = mapper.get((Long) id);

            if (entity == null) {
                throw new EntityNotFoundException(entityClass, id);
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving " + entityClass.getSimpleName() + " with ID: " + id, e);
        }
    }

    @Override
    public ID save(T entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                M mapper = session.getMapper(getMapperClass());
                long id = mapper.save(entity);
                session.commit();
                return (ID) Long.valueOf(id);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving " + entityClass.getSimpleName(), e);
        }
    }

    @Override
    public void update(T entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                M mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating " + entityClass.getSimpleName(), e);
        }
    }

    @Override
    public void delete(T entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                M mapper = session.getMapper(getMapperClass());
                Long id = getEntityId(entity);
                mapper.delete(id);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error deleting " + entityClass.getSimpleName(), e);
        }
    }

    @Override
    public List<T> getAll() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            M mapper = session.getMapper(getMapperClass());
            return mapper.getAll();
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving all " + entityClass.getSimpleName() + " entities", e);
        }
    }

    protected abstract Long getEntityId(T entity);

    protected SqlSession openSession() {
        try {
            return sqlSessionFactory.openSession();
        } catch (Exception e) {
            throw new DatabaseConnectionException("Error opening database session", e);
        }
    }
}