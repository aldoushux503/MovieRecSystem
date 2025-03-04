package org.solvd.recommendation.service;

import org.solvd.recommendation.model.UserInteraction;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;

public interface IUserInteractionService extends IService<UserInteraction, CompositeKey3<Long, Long, Long>> {
    List<UserInteraction> getByUser(Long userId);

    List<UserInteraction> getByMovie(Long movieId);
}
