package org.solvd.recommendation.service;

import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.util.CompositeKey2;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;

public interface IViewingHistoryService extends IService<ViewingHistory, CompositeKey2<Long, Long>>{
    ViewingHistory getViewingHistory(Long userId, Long movieId);

    void addViewingHistory(ViewingHistory viewingHistory);

    void updateViewingHistory(ViewingHistory viewingHistory);

    void deleteViewingHistory(ViewingHistory viewingHistory);

    List<ViewingHistory> getUserViewingHistory(Long userId);

    List<User> getUsersWhoWatched(Long movieId);
}
