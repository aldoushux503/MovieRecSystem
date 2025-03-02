package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IUserPreferredGenreDAO;
import org.solvd.recommendation.dao.IUserRatingDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IUserPreferredGenreMapper;
import org.solvd.recommendation.mapper.IUserRatingMapper;
import org.solvd.recommendation.model.UserPreferredGenre;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.util.CompositeKey2;

public class UserRatingMyBatisDAO
        extends AbstractMyBatisDAO<UserRating, CompositeKey2<Long, Long>, IUserRatingMapper>
        implements IUserRatingDAO {

    public UserRatingMyBatisDAO() {
        super(UserRating.class);
    }

    @Override
    protected Class<IUserRatingMapper> getMapperClass() {
        return IUserRatingMapper.class;
    }

    @Override
    public UserRating get(CompositeKey2<Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IUserRatingMapper mapper = session.getMapper(getMapperClass());
            UserRating entity = mapper.get(id.getKey1(), id.getKey2());

            if (entity == null) {
                throw new EntityNotFoundException(UserRating.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving UserRating with " + id, e);
        }
    }

    @Override
    public CompositeKey2<Long, Long> save(UserRating entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserRatingMapper mapper = session.getMapper(getMapperClass());
                mapper.save(entity);
                session.commit();
                return createCompositeKey(entity);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving UserRating", e);
        }
    }

    @Override
    public void update(UserRating entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserRatingMapper mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating UserRating", e);
        }
    }

    @Override
    public void delete(UserRating entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserRatingMapper mapper = session.getMapper(getMapperClass());
                CompositeKey2<Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2());

                if (!result) {
                    throw new EntityNotFoundException(UserRating.class, id.toString());
                }

                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error deleting UserRating", e);
        }
    }

    private CompositeKey2<Long, Long> createCompositeKey(UserRating entity) {
        if (entity == null || entity.getUserId() == null || entity.getMovieId() == null) {
            throw new IllegalArgumentException("UserRating has incomplete key values");
        }
        return new CompositeKey2<>(entity.getUserId(), entity.getMovieId());
    }

    @Override
    protected Long getEntityId(UserRating entity) {
        throw new UnsupportedOperationException("UserRating uses composite key");
    }
}