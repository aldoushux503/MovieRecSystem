package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.service.IMovieService;
import org.solvd.recommendation.service.IUserRatingService;
import org.solvd.recommendation.service.IUserService;
import org.solvd.recommendation.service.imlp.MovieService;
import org.solvd.recommendation.service.imlp.UserRatingService;
import org.solvd.recommendation.service.imlp.UserService;
import org.solvd.recommendation.service.ServiceFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for recommendation algorithms.
 * Provides common functionality and template methods.
 */
public abstract class AbstractRecommendationAlgorithm implements IRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRecommendationAlgorithm.class);

    protected final IUserService userService;
    protected final IMovieService movieService;
    protected final IUserRatingService ratingService;

    protected AbstractRecommendationAlgorithm() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.userService = serviceFactory.getUserService();
        this.movieService = serviceFactory.getMovieService();
        this.ratingService = serviceFactory.getUserRatingService();
    }

    @Override
    public List<Movie> recommendMovies(Long userId, int limit) {
        // Validate input
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive");
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Get all movies
        List<Movie> allMovies = movieService.getAll();

        // Get movies already rated by the user
        List<Long> ratedMovieIds = ratingService.getAllUserRatings(userId)
                .stream()
                .map(UserRating::getMovieId)
                .toList();

        // Filter out movies already rated
        List<Movie> candidateMovies = allMovies.stream()
                .filter(movie -> !ratedMovieIds.contains(movie.getMovieId()))
                .toList();

        // Predict ratings for candidate movies
        Map<Long, Double> predictedRatings = predictRatings(userId,
                candidateMovies.stream()
                        .map(Movie::getMovieId)
                        .collect(Collectors.toList()));

        // Sort candidates by predicted rating
        return candidateMovies.stream()
                .filter(movie -> predictedRatings.containsKey(movie.getMovieId()))
                .sorted((m1, m2) -> Double.compare(
                        predictedRatings.getOrDefault(m2.getMovieId(), 0.0),
                        predictedRatings.getOrDefault(m1.getMovieId(), 0.0)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Template method to be implemented by concrete algorithms.
     */
    @Override
    public abstract Map<Long, Double> predictRatings(Long userId, List<Long> movieIds);
}