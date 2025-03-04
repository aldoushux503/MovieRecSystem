package org.solvd.recommendation.service;

import org.solvd.recommendation.model.Interaction;
import org.solvd.recommendation.model.UserInteraction;

import java.util.List;

public interface IInteractionService extends IService<Interaction, Long> {
    void addUserInteraction(UserInteraction interaction);

    List<UserInteraction> getUserInteractions(Long userId);

    List<UserInteraction> getMovieInteractions(Long movieId);
}
