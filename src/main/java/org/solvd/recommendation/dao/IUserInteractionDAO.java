package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.UserInteraction;

import java.sql.SQLException;

public interface IUserInteractionDAO extends IDAO<UserInteraction> {

    UserInteraction get(long movieId, long userId, long interactionId) throws SQLException;
}
