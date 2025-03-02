package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.UserInteraction;

import java.util.List;
import java.util.Map;

/**
 * Service interface for user interactions with movies.
 * Manages likes, shares, saves, and other interactions.
 */
public interface IInteractionService {
    void addInteraction(UserInteraction interaction);

    void removeInteraction(UserInteraction interaction);

    List<UserInteraction> getUserInteractions(Long userId);

    List<UserInteraction> getMovieInteractions(Long movieId);

    boolean hasInteraction(Long userId, Long movieId, Long interactionTypeId);

    Map<Long, Integer> getInteractionCountsByMovie(Long movieId);
}
