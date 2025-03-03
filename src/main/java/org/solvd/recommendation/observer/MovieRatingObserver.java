package org.solvd.recommendation.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.service.MovieService;
import org.solvd.recommendation.service.ServiceFactory;
import org.solvd.recommendation.service.UserRatingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Observer implementation that updates a movie's average rating when
 * any rating for that movie changes.
 */
public class MovieRatingObserver implements IRatingChangedObserver {
    private static final Logger logger = LoggerFactory.getLogger(MovieRatingObserver.class);
    private static final int SCALE = 2;

    private final MovieService movieService;
    private final UserRatingService userRatingService;

    public MovieRatingObserver() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.movieService = serviceFactory.getMovieService();
        this.userRatingService = serviceFactory.getUserRatingService();
    }

    @Override
    public void onRatingChanged(Long movieId) {
        try {
            // Get the movie
            Movie movie = movieService.getById(movieId);
            if (movie == null) {
                logger.warn("Cannot update average rating for non-existent movie: {}", movieId);
                return;
            }

            // Get all ratings for the movie
            List<UserRating> ratings = userRatingService.getAllMovieRatings(movieId);

            // Calculate new average
            BigDecimal newAverage;
            if (ratings.isEmpty()) {
                newAverage = BigDecimal.ZERO;
            } else {
                BigDecimal sum = ratings.stream()
                        .map(UserRating::getRatingValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                newAverage = sum.divide(BigDecimal.valueOf(ratings.size()), SCALE, RoundingMode.HALF_UP);
            }

            // Update movie if average has changed
            if (!newAverage.equals(movie.getAverageRating())) {
                logger.info("Updating average rating for movie {} from {} to {}",
                        movie.getTitle(), movie.getAverageRating(), newAverage);
                movie.setAverageRating(newAverage);
                movieService.update(movie);
            }
        } catch (Exception e) {
            logger.error("Error updating average rating for movie {}: {}", movieId, e.getMessage());
        }
    }
}