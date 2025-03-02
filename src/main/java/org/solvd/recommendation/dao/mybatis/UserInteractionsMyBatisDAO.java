package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSession;

import org.solvd.recommendation.dao.IUserInteractionDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IUserInteractionMapper;
import org.solvd.recommendation.model.UserInteraction;
import org.solvd.recommendation.util.CompositeKey3;

public class UserInteractionsMyBatisDAO
        extends AbstractMyBatisDAO<UserInteraction, CompositeKey3<Long, Long, Long>, IUserInteractionMapper>
        implements IUserInteractionDAO {

    public UserInteractionsMyBatisDAO() {
        super(UserInteraction.class);
    }

    @Override
    protected Class<IUserInteractionMapper> getMapperClass() {
        return IUserInteractionMapper.class;
    }

    @Override
    public UserInteraction get(CompositeKey3<Long, Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IUserInteractionMapper mapper = session.getMapper(getMapperClass());
            UserInteraction entity = mapper.get(id.getKey1(), id.getKey2(), id.getKey3());

            if (entity == null) {
                throw new EntityNotFoundException(UserInteraction.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving UserInteraction with " + id, e);
        }
    }

    @Override
    public CompositeKey3<Long, Long, Long> save(UserInteraction entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserInteractionMapper mapper = session.getMapper(getMapperClass());
                mapper.save(entity);
                session.commit();
                return createCompositeKey(entity);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving UserInteraction", e);
        }
    }

    @Override
    public void update(UserInteraction entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserInteractionMapper mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating UserInteraction", e);
        }
    }

    @Override
    public void delete(UserInteraction entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserInteractionMapper mapper = session.getMapper(getMapperClass());
                CompositeKey3<Long, Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2(), id.getKey3());

                if (!result) {
                    throw new EntityNotFoundException(UserInteraction.class, id.toString());
                }

                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error deleting UserInteraction", e);
        }
    }

    private CompositeKey3<Long, Long, Long> createCompositeKey(UserInteraction entity) {
        if (entity == null || entity.getUserId() == null || entity.getMovieId() == null || entity.getInteractionId() == null) {
            throw new IllegalArgumentException("UserInteraction has incomplete key values");
        }
        return new CompositeKey3<>(entity.getUserId(), entity.getMovieId(), entity.getInteractionId());
    }

    @Override
    protected Long getEntityId(UserInteraction entity) {
        throw new UnsupportedOperationException("UserInteraction uses composite key");
    }
}