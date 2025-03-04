package org.solvd.recommendation.observer;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.service.IMovieService;
import org.solvd.recommendation.service.IUserRatingService;
import org.solvd.recommendation.service.ServiceFactory;

import java.math.BigDecimal;

// Implementation of observer that updates movie average ratings
public class MovieRatingObserver implements IRatingChangedObserver {
    private final IMovieService movieService;

    public MovieRatingObserver(IMovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void onRatingChanged(UserRating rating) {
        if (rating == null) return;

        // Get the movie
        Long movieId = rating.getMovieId();
        Movie movie = movieService.getById(movieId);
        if (movie == null) return;

        // Get new average rating
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        IUserRatingService ratingService = serviceFactory.getUserRatingService();
        double avgRating = ratingService.getAverageRating(movieId);

        // Update movie's average rating
        movie.setAverageRating(BigDecimal.valueOf(avgRating));
        movieService.update(movie);
    }
}
