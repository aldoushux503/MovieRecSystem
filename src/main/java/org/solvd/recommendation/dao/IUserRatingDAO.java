package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.UserRating;

import java.sql.SQLException;

public interface IUserRatingDAO extends IDAO<UserRating> {

    UserRating get(long movieId, long userId) throws SQLException;
}
