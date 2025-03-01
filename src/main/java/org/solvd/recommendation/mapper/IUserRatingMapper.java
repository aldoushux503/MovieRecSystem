package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.UserRating;

import java.sql.SQLException;

@Mapper
public interface IUserRatingMapper extends IMapper<UserRating> {
    UserRating get(@Param("movieId") long movieId,@Param("userId") long userId) throws SQLException;
}
