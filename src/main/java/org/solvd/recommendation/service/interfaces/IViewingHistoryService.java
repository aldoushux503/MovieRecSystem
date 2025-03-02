package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.ViewingHistory;

import java.util.List;

/**
 * Service interface for viewing history operations.
 * Tracks what movies users have watched.
 */
public interface IViewingHistoryService {
    ViewingHistory getViewingHistory(Long userId, Long movieId);

    void addViewingHistory(ViewingHistory viewingHistory);

    void updateViewingHistory(ViewingHistory viewingHistory);

    void deleteViewingHistory(ViewingHistory viewingHistory);

    List<ViewingHistory> getUserViewingHistory(Long userId);

    List<Movie> getUserWatchedMovies(Long userId);

    List<User> getUsersWhoWatched(Long movieId);
}
