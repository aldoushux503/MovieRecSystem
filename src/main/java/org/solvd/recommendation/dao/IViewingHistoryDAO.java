package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.util.CompositeKey2;

import java.sql.SQLException;

public interface IViewingHistoryDAO extends IDAO<ViewingHistory, CompositeKey2<Long, Long>> {
}