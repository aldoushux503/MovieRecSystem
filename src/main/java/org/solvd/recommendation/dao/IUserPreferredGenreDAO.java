package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.UserPreferredGenre;

import java.sql.SQLException;

public interface IUserPreferredGenreDAO extends IDAO<UserPreferredGenre> {

    UserPreferredGenre get(long genreId, long userId) throws SQLException;
}
