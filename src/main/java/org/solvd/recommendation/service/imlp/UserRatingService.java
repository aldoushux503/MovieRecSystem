package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IContentContributorDAO;
import org.solvd.recommendation.dao.IUserRatingDAO;
import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.observer.MovieRatingObserver;
import org.solvd.recommendation.observer.IRatingChangedObserver;
import org.solvd.recommendation.service.IContentContributorService;
import org.solvd.recommendation.service.IMovieService;
import org.solvd.recommendation.service.IUserRatingService;
import org.solvd.recommendation.util.CompositeKey2;
import org.solvd.recommendation.util.CompositeKey3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserRating service implementation.
 */

public class UserRatingService
        extends AbstractService<UserRating, CompositeKey2<Long, Long>, IUserRatingDAO>
        implements IUserRatingService {
    private final List<IRatingChangedObserver> observers = new ArrayList<>();
    private final IMovieService movieService;

    public UserRatingService(IUserRatingDAO dao, IMovieService movieService) {
        super(dao);
        this.movieService = movieService;
        // Add the movie rating observer to update average ratings
        addObserver(new MovieRatingObserver(movieService));
    }

    @Override
    public UserRating getRating(Long userId, Long movieId) {
        return getByUserAndMovie(userId, movieId);
    }

    @Override
    public void saveRating(UserRating rating) {
        UserRating existingRating = getByUserAndMovie(rating.getUserId(), rating.getMovieId());
        if (existingRating == null) {
            dao.save(rating);
            notifyRatingChanged(rating);
        } else {
            existingRating.setRatingValue(rating.getRatingValue());
            dao.update(existingRating);
            notifyRatingChanged(existingRating);
        }
    }

    @Override
    public void updateRating(UserRating rating) {
        dao.update(rating);
        notifyRatingChanged(rating);
    }

    @Override
    public void deleteRating(UserRating rating) {
        dao.delete(rating);
        notifyRatingChanged(null);
    }

    @Override
    public List<UserRating> getUserRatings(Long userId) {
        return getAllUserRatings(userId);
    }

    @Override
    public List<UserRating> getMovieRatings(Long movieId) {
        return getAllMovieRatings(movieId);
    }

    @Override
    public double getAverageRating(Long movieId) {
        List<UserRating> ratings = getAllMovieRatings(movieId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream()
                .mapToDouble(r -> r.getRatingValue().doubleValue())
                .sum();

        return BigDecimal.valueOf(sum / ratings.size())
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    @Override
    public UserRating getByUserAndMovie(Long userId, Long movieId) {
        try {
            return dao.get(new CompositeKey2<>(userId, movieId));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<UserRating> getAllUserRatings(Long userId) {
        return dao.getAll().stream()
                .filter(rating -> rating.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserRating> getAllMovieRatings(Long movieId) {
        return dao.getAll().stream()
                .filter(rating -> rating.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }

    // Observer pattern methods
    public void addObserver(IRatingChangedObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IRatingChangedObserver observer) {
        observers.remove(observer);
    }

    private void notifyRatingChanged(UserRating rating) {
        for (IRatingChangedObserver observer : observers) {
            observer.onRatingChanged(rating);
        }
    }




}

