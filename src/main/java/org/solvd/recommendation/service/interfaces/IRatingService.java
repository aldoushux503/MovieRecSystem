package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.UserRating;

import java.util.List;
import java.util.Map;

/**
 * Service interface for user ratings.
 * Manages the ratings given by users to movies.
 */
public interface IRatingService {
    UserRating getRating(Long userId, Long movieId);

    void saveRating(UserRating rating);

    void updateRating(UserRating rating);

    void deleteRating(UserRating rating);

    List<UserRating> getUserRatings(Long userId);

    double getAverageRating(Long movieId);

    Map<Long, Double> getUserRatingsMap(Long userId);
}
