package org.solvd.recommendation.dao.mybatis;


import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IContentContributorDAO;
import org.solvd.recommendation.exeption.*;
import org.solvd.recommendation.mapper.IContentContributorMapper;
import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.util.CompositeKey3;

public class ContentContributorsMyBatisDAO
        extends AbstractMyBatisDAO<ContentContributor, CompositeKey3<Long, Long, Long>, IContentContributorMapper>
        implements IContentContributorDAO {

    public ContentContributorsMyBatisDAO() {
        super(ContentContributor.class);
    }

    @Override
    protected Class<IContentContributorMapper> getMapperClass() {
        return IContentContributorMapper.class;
    }

    @Override
    public ContentContributor get(CompositeKey3<Long, Long, Long> id) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            IContentContributorMapper mapper = session.getMapper(getMapperClass());
            ContentContributor entity = mapper.get(id.getKey1(), id.getKey2(), id.getKey3());

            if (entity == null) {
                throw new EntityNotFoundException(ContentContributor.class, id.toString());
            }

            return entity;
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving ContentContributor with " + id, e);
        }
    }

    @Override
    public CompositeKey3<Long, Long, Long> save(ContentContributor entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IContentContributorMapper mapper = session.getMapper(getMapperClass());
                mapper.save(entity);
                session.commit();
                return createCompositeKey(entity);
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error saving ContentContributor", e);
        }
    }

    @Override
    public void update(ContentContributor entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IContentContributorMapper mapper = session.getMapper(getMapperClass());
                mapper.update(entity);
                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (Exception e) {
            throw new DataAccessException("Error updating ContentContributor", e);
        }
    }

    @Override
    public void delete(ContentContributor entity) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            try {
                IContentContributorMapper mapper = session.getMapper(getMapperClass());
                CompositeKey3<Long, Long, Long> id = createCompositeKey(entity);
                boolean result = mapper.delete(id.getKey1(), id.getKey2(), id.getKey3());

                if (!result) {
                    throw new EntityNotFoundException(ContentContributor.class, id.toString());
                }

                session.commit();
            } catch (Exception e) {
                session.rollback();
                throw e;
            }
        } catch (EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("Error deleting ContentContributor", e);
        }
    }

    private CompositeKey3<Long, Long, Long> createCompositeKey(ContentContributor entity) {
        if (entity == null || entity.getMovieId() == null || entity.getPersonId() == null || entity.getPersonRoleId() == null) {
            throw new IllegalArgumentException("ContentContributor has incomplete key values");
        }
        return new CompositeKey3<>(entity.getMovieId(), entity.getPersonId(), entity.getPersonRoleId());
    }

    @Override
    protected Long getEntityId(ContentContributor entity) {
        throw new UnsupportedOperationException("ContentContributor uses composite key");
    }
}
