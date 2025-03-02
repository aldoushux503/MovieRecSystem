package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IUserInteractionDAO;
import org.solvd.recommendation.model.UserInteraction;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserInteraction service implementation.
 */
public class UserInteractionService extends AbstractService<UserInteraction, CompositeKey3<Long, Long, Long>, IUserInteractionDAO> {
    UserInteractionService(IUserInteractionDAO dao) {
        super(dao);
    }

    public UserInteraction getByUserMovieAndInteraction(Long userId, Long movieId, Long interactionId) {
        return dao.get(new CompositeKey3<>(userId, movieId, interactionId));
    }

    public List<UserInteraction> getByUser(Long userId) {
        return dao.getAll().stream()
                .filter(ui -> ui.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}