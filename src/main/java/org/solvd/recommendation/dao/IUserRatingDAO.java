package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.util.CompositeKey2;

import java.sql.SQLException;

public interface IUserRatingDAO extends IDAO<UserRating, CompositeKey2<Long, Long>> {
}
