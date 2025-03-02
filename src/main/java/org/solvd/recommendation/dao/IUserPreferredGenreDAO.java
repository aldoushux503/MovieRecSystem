package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.UserPreferredGenre;
import org.solvd.recommendation.util.CompositeKey2;

import java.sql.SQLException;

public interface IUserPreferredGenreDAO extends IDAO<UserPreferredGenre, CompositeKey2<Long, Long>> {
}
