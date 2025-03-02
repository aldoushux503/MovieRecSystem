package org.solvd.recommendation.dao.mybatis;


import org.solvd.recommendation.dao.IGenreDAO;
import org.solvd.recommendation.mapper.IGenreMapper;
import org.solvd.recommendation.model.Genre;

public class GenreMyBatisDAO extends AbstractMyBatisDAO<Genre, Long, IGenreMapper> implements IGenreDAO {

    public GenreMyBatisDAO() {
        super(Genre.class);
    }

    @Override
    protected Class<IGenreMapper> getMapperClass() {
        return IGenreMapper.class;
    }

    @Override
    protected Long getEntityId(Genre entity) {
        return entity.getGenreId();
    }
}