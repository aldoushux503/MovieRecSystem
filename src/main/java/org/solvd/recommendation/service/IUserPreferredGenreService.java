package org.solvd.recommendation.service;

import org.solvd.recommendation.model.UserPreferredGenre;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;

public interface IUserPreferredGenreService extends IService<UserPreferredGenre, CompositeKey2<Long, Long>> {
    UserPreferredGenre getByUserAndGenre(Long userId, Long genreId);

    List<UserPreferredGenre> getByUser(Long userId);
}
