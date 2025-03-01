package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.solvd.recommendation.model.UserInteraction;

import java.sql.SQLException;

@Mapper
public interface IUserInteractionMapper extends IMapper<UserInteraction> {
    UserInteraction get(@Param("movieId") long movieId,@Param("userId") long userId, @Param("interactionId") long interactionId) throws SQLException;

}
