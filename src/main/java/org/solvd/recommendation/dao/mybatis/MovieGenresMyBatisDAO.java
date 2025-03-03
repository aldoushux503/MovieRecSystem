package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IMovieGenresDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IMovieGenresMapper;
import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.util.CompositeKey2;


public class MovieGenresMyBatisDAO extends AbstractMyBatisDAO<MovieGenres, CompositeKey2<Long, Long>, IMovieGenresMapper> implements IMovieGenresDAO {

    public MovieGenresMyBatisDAO() {
        super(MovieGenres.class);
    }

    @Override
    protected Class<IMovieGenresMapper> getMapperClass() {
        return IMovieGenresMapper.class;
    }

    @Override
    public MovieGenres get(CompositeKey2<Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IMovieGenresMapper mapper = session.getMapper(getMapperClass());
            MovieGenres entity = mapper.get(id.getKey1(), id.getKey2());

            if (entity == null) {
                throw new EntityNotFoundException(MovieGenres.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving MovieGenre with " + id, e);
        }
    }

    @Override
    public CompositeKey2<Long, Long> save(MovieGenres entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IMovieGenresMapper mapper = session.getMapper(getMapperClass());
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
    public void update(MovieGenres entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IMovieGenresMapper mapper = session.getMapper(getMapperClass());
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
    public void delete(MovieGenres entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IMovieGenresMapper mapper = session.getMapper(getMapperClass());
                CompositeKey2<Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2());

                if (!result) {
                    throw new EntityNotFoundException(MovieGenres.class, id.toString());
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

    private CompositeKey2<Long, Long> createCompositeKey(MovieGenres entity) {
        if (entity == null || entity.getMovieId() == null || entity.getGenreId() == null) {
            throw new IllegalArgumentException("MovieGenre has incomplete key values");
        }
        return new CompositeKey2<>(entity.getMovieId(), entity.getGenreId());
    }

    @Override
    protected Long getEntityId(MovieGenres entity) {
        throw new UnsupportedOperationException("MovieGenre uses composite key");
    }
}