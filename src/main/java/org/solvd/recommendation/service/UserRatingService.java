package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IUserRatingDAO;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.observer.MovieRatingObserver;
import org.solvd.recommendation.observer.IRatingChangedObserver;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserRating service implementation.
 */
public class UserRatingService extends AbstractService<UserRating, CompositeKey2<Long, Long>, IUserRatingDAO> {


    private final List<IRatingChangedObserver> observers = new ArrayList<>();


    UserRatingService(IUserRatingDAO dao) {
        super(dao);

        // Register the default observer to update movie average ratings
        registerObserver(new MovieRatingObserver());
    }

    @Override
    public void update(UserRating entity) {
        super.update(entity);
        notifyRatingChanged(entity.getMovieId());
    }

    /**
     * Register a new observer to be notified of rating changes.
     */
    public void registerObserver(IRatingChangedObserver observer) {
        observers.add(observer);
    }

    /**
     * Unregister an observer so it no longer receives notifications.
     */
    public void unregisterObserver(IRatingChangedObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notify all observers that a rating has changed for a movie.
     */
    private void notifyRatingChanged(Long movieId) {
        for (IRatingChangedObserver observer : observers) {
            observer.onRatingChanged(movieId);
        }
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
