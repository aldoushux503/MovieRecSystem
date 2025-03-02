package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IMovieGenreDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IMovieGenreMapper;
import org.solvd.recommendation.model.MovieGenre;
import org.solvd.recommendation.util.CompositeKey2;


public class MovieGenresMyBatisDAO extends AbstractMyBatisDAO<MovieGenre, CompositeKey2<Long, Long>, IMovieGenreMapper> implements IMovieGenreDAO {

    public MovieGenresMyBatisDAO() {
        super(MovieGenre.class);
    }

    @Override
    protected Class<IMovieGenreMapper> getMapperClass() {
        return IMovieGenreMapper.class;
    }

    @Override
    public MovieGenre get(CompositeKey2<Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IMovieGenreMapper mapper = session.getMapper(getMapperClass());
            MovieGenre entity = mapper.get(id.getKey1(), id.getKey2());

            if (entity == null) {
                throw new EntityNotFoundException(MovieGenre.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving MovieGenre with " + id, e);
        }
    }

    @Override
    public CompositeKey2<Long, Long> save(MovieGenre entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IMovieGenreMapper mapper = session.getMapper(getMapperClass());
                mapper.save(entity);
                session.commit();
                return createCompositeKey(entity);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving MovieGenre", e);
        }
    }

    @Override
    public void update(MovieGenre entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IMovieGenreMapper mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating MovieGenre", e);
        }
    }

    @Override
    public void delete(MovieGenre entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IMovieGenreMapper mapper = session.getMapper(getMapperClass());
                CompositeKey2<Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2());

                if (!result) {
                    throw new EntityNotFoundException(MovieGenre.class, id.toString());
                }

                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error deleting MovieGenre", e);
        }
    }

    private CompositeKey2<Long, Long> createCompositeKey(MovieGenre entity) {
        if (entity == null || entity.getMovieId() == null || entity.getGenreId() == null) {
            throw new IllegalArgumentException("MovieGenre has incomplete key values");
        }
        return new CompositeKey2<>(entity.getMovieId(), entity.getGenreId());
    }

    @Override
    protected Long getEntityId(MovieGenre entity) {
        throw new UnsupportedOperationException("MovieGenre uses composite key");
    }
}