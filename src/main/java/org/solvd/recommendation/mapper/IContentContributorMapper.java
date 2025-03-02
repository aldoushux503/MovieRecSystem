package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.ContentContributor;

import java.sql.SQLException;

@Mapper
public interface IContentContributorMapper extends IMapper<ContentContributor> {
    ContentContributor get(@Param("movieId") long movieId, @Param("personId") long personId, @Param("personRole") long personRole) throws SQLException;
    boolean delete(@Param("movieId") long movieId, @Param("personId") long personId, @Param("personRole") long personRole) throws SQLException;

}
