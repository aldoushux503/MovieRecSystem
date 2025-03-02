package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IViewingHistoryDAO;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewingHistory service implementation.
 */
public class ViewingHistoryService extends AbstractService<ViewingHistory, CompositeKey2<Long, Long>, IViewingHistoryDAO> {
    ViewingHistoryService(IViewingHistoryDAO dao) {
        super(dao);
    }

    public ViewingHistory getByUserAndMovie(Long userId, Long movieId) {
        return dao.get(new CompositeKey2<>(userId, movieId));
    }

    public List<ViewingHistory> getUserHistory(Long userId) {
        return dao.getAll().stream()
                .filter(history -> history.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}