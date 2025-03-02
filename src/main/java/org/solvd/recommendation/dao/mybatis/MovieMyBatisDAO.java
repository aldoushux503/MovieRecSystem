package org.solvd.recommendation.dao.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.solvd.recommendation.dao.IMovieDAO;
import org.solvd.recommendation.mapper.IMovieMapper;
import org.solvd.recommendation.model.Movie;

import java.sql.SQLException;
import java.util.List;



public class MovieMyBatisDAO extends AbstractMyBatisDAO<Movie, Long, IMovieMapper> implements IMovieDAO {

    public MovieMyBatisDAO() {
        super(Movie.class);
    }

    @Override
    protected Class<IMovieMapper> getMapperClass() {
        return IMovieMapper.class;
    }

    @Override
    protected Long getEntityId(Movie entity) {
        return entity.getMovieId();
    }
}