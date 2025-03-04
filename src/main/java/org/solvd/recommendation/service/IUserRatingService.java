package org.solvd.recommendation.service;

import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;

public interface IUserRatingService extends IService<UserRating, CompositeKey2<Long, Long>> {
    UserRating getRating(Long userId, Long movieId);

    void saveRating(UserRating rating);

    void updateRating(UserRating rating);

    void deleteRating(UserRating rating);

    List<UserRating> getUserRatings(Long userId);

    List<UserRating> getMovieRatings(Long movieId);

    double getAverageRating(Long movieId);
    List<UserRating> getAllMovieRatings(Long movieId);

    UserRating getByUserAndMovie(Long userId, Long movieId);
    List<UserRating> getAllUserRatings(Long userId);
}
