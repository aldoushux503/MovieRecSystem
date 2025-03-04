package org.solvd.recommendation.service;

import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.User;

import java.util.Set;

public interface IUserService extends IService<User, Long> {
    Set<Genre> getUserPreferredGenres(Long userId);
}
