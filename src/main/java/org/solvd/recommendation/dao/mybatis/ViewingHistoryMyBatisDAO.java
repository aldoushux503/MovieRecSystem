package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IViewingHistoryDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IViewingHistoryMapper;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.util.CompositeKey2;

public class ViewingHistoryMyBatisDAO
        extends AbstractMyBatisDAO<ViewingHistory, CompositeKey2<Long, Long>, IViewingHistoryMapper>
        implements IViewingHistoryDAO {

    public ViewingHistoryMyBatisDAO() {
        super(ViewingHistory.class);
    }

    @Override
    protected Class<IViewingHistoryMapper> getMapperClass() {
        return IViewingHistoryMapper.class;
    }

    @Override
    public ViewingHistory get(CompositeKey2<Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IViewingHistoryMapper mapper = session.getMapper(getMapperClass());
            ViewingHistory entity = mapper.get(id.getKey1(), id.getKey2());

            if (entity == null) {
                throw new EntityNotFoundException(ViewingHistory.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving ViewingHistory with " + id, e);
        }
    }

    @Override
    public CompositeKey2<Long, Long> save(ViewingHistory entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IViewingHistoryMapper mapper = session.getMapper(getMapperClass());
                mapper.save(entity);
                session.commit();
                return createCompositeKey(entity);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving ViewingHistory", e);
        }
    }

    @Override
    public void update(ViewingHistory entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IViewingHistoryMapper mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating ViewingHistory", e);
        }
    }

    @Override
    public void delete(ViewingHistory entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IViewingHistoryMapper mapper = session.getMapper(getMapperClass());
                CompositeKey2<Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2());

                if (!result) {
                    throw new EntityNotFoundException(ViewingHistory.class, id.toString());
                }

                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error deleting ViewingHistory", e);
        }
    }

    private CompositeKey2<Long, Long> createCompositeKey(ViewingHistory entity) {
        if (entity == null || entity.getUserId() == null || entity.getMovieId() == null) {
            throw new IllegalArgumentException("ViewingHistory has incomplete key values");
        }
        return new CompositeKey2<>(entity.getUserId(), entity.getMovieId());
    }

    @Override
    protected Long getEntityId(ViewingHistory entity) {
        throw new UnsupportedOperationException("ViewingHistory uses composite key");
    }
}