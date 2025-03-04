package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IInteractionDAO;
import org.solvd.recommendation.model.Interaction;
import org.solvd.recommendation.model.UserInteraction;
import org.solvd.recommendation.service.IInteractionService;
import org.solvd.recommendation.service.IUserInteractionService;

import java.util.List;

/**
 * Interaction service implementation.
 */
public class InteractionService extends AbstractService<Interaction, Long, IInteractionDAO> implements IInteractionService {
    private final IUserInteractionService userInteractionService;

    public InteractionService(IInteractionDAO dao, IUserInteractionService userInteractionService) {
        super(dao);
        this.userInteractionService = userInteractionService;
    }

    @Override
    public void addUserInteraction(UserInteraction interaction) {
        userInteractionService.create(interaction);
    }

    @Override
    public List<UserInteraction> getUserInteractions(Long userId) {
        return userInteractionService.getByUser(userId);
    }

    @Override
    public List<UserInteraction> getMovieInteractions(Long movieId) {
        return userInteractionService.getByMovie(movieId);
    }
}