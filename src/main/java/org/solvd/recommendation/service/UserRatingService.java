package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IUserRatingDAO;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserRating service implementation.
 */
public class UserRatingService extends AbstractService<UserRating, CompositeKey2<Long, Long>, IUserRatingDAO> {
    UserRatingService(IUserRatingDAO dao) {
        super(dao);
    }

    public UserRating getByUserAndMovie(Long userId, Long movieId) {
        return dao.get(new CompositeKey2<>(userId, movieId));
    }

    public List<UserRating> getAllUserRatings(Long userId) {
        return dao.getAll().stream()
                .filter(rating -> rating.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<UserRating> getAllMovieRatings(Long movieId) {
        return dao.getAll().stream()
                .filter(rating -> rating.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
}
