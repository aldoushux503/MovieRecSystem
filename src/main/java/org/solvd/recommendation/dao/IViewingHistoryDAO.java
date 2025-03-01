package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.ViewingHistory;

import java.sql.SQLException;

public interface IViewingHistoryDAO extends IDAO<ViewingHistory> {

    ViewingHistory get(long movieId, long userId) throws SQLException;
}
