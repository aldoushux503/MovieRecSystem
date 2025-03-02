package org.solvd.recommendation.dao.mybatis;


import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IUserPreferredGenreDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IUserPreferredGenreMapper;
import org.solvd.recommendation.model.UserPreferredGenre;
import org.solvd.recommendation.util.CompositeKey2;

public class UserPreferredGenresMyBatisDAO
        extends AbstractMyBatisDAO<UserPreferredGenre, CompositeKey2<Long, Long>, IUserPreferredGenreMapper>
        implements IUserPreferredGenreDAO {

    public UserPreferredGenresMyBatisDAO() {
        super(UserPreferredGenre.class);
    }

    @Override
    protected Class<IUserPreferredGenreMapper> getMapperClass() {
        return IUserPreferredGenreMapper.class;
    }

    @Override
    public UserPreferredGenre get(CompositeKey2<Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IUserPreferredGenreMapper mapper = session.getMapper(getMapperClass());
            UserPreferredGenre entity = mapper.get(id.getKey1(), id.getKey2());

            if (entity == null) {
                throw new EntityNotFoundException(UserPreferredGenre.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving UserPreferredGenre with " + id, e);
        }
    }

    @Override
    public CompositeKey2<Long, Long> save(UserPreferredGenre entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserPreferredGenreMapper mapper = session.getMapper(getMapperClass());
                mapper.save(entity);
                session.commit();
                return createCompositeKey(entity);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving UserPreferredGenre", e);
        }
    }

    @Override
    public void update(UserPreferredGenre entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserPreferredGenreMapper mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating UserPreferredGenre", e);
        }
    }

    @Override
    public void delete(UserPreferredGenre entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IUserPreferredGenreMapper mapper = session.getMapper(getMapperClass());
                CompositeKey2<Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2());

                if (!result) {
                    throw new EntityNotFoundException(UserPreferredGenre.class, id.toString());
                }

                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error deleting UserPreferredGenre", e);
        }
    }

    private CompositeKey2<Long, Long> createCompositeKey(UserPreferredGenre entity) {
        if (entity == null || entity.getUserId() == null || entity.getGenreId() == null) {
            throw new IllegalArgumentException("UserPreferredGenre has incomplete key values");
        }
        return new CompositeKey2<>(entity.getUserId(), entity.getGenreId());
    }

    @Override
    protected Long getEntityId(UserPreferredGenre entity) {
        throw new UnsupportedOperationException("UserPreferredGenre uses composite key");
    }
}
