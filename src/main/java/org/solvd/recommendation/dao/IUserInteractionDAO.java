package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.UserInteraction;
import org.solvd.recommendation.util.CompositeKey3;

import java.sql.SQLException;


public interface IUserInteractionDAO extends IDAO<UserInteraction, CompositeKey3<Long, Long, Long>> {
}