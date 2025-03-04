package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IUserRatingDAO;
import org.solvd.recommendation.dao.IViewingHistoryDAO;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.service.IUserService;
import org.solvd.recommendation.service.IViewingHistoryService;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewingHistory service implementation.
 */

public class ViewingHistoryService
        extends AbstractService<ViewingHistory, CompositeKey2<Long, Long>, IViewingHistoryDAO>
        implements IViewingHistoryService {
    private final IUserService userService;

    public ViewingHistoryService(IViewingHistoryDAO dao, IUserService userService) {
        super(dao);
        this.userService = userService;
    }

    @Override
    public ViewingHistory getViewingHistory(Long userId, Long movieId) {
        try {
            return dao.get(new CompositeKey2<>(userId, movieId));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void addViewingHistory(ViewingHistory viewingHistory) {
        dao.save(viewingHistory);
    }

    @Override
    public void updateViewingHistory(ViewingHistory viewingHistory) {
        dao.update(viewingHistory);
    }

    @Override
    public void deleteViewingHistory(ViewingHistory viewingHistory) {
        dao.delete(viewingHistory);
    }

    @Override
    public List<ViewingHistory> getUserViewingHistory(Long userId) {
        if (userId == null) {
            return dao.getAll();
        }
        return dao.getAll().stream()
                .filter(history -> history.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersWhoWatched(Long movieId) {
        List<ViewingHistory> history = dao.getAll().stream()
                .filter(vh -> vh.getMovieId().equals(movieId))
                .toList();

        return history.stream()
                .map(vh -> userService.getById(vh.getUserId()))
                .collect(Collectors.toList());
    }

}