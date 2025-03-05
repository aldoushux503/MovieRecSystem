package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.service.*;
import org.solvd.recommendation.service.imlp.MovieService;
import org.solvd.recommendation.service.imlp.UserRatingService;
import org.solvd.recommendation.service.imlp.UserService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for recommendation algorithms.
 *
 * This class implements the template method pattern, providing common functionality
 * that all recommendation algorithms share while allowing specific implementations
 * to define their own rating prediction strategy.
 *
 * Key responsibilities:
 * 1. Manages common services used by all recommendation algorithms
 * 2. Implements the standard recommendation workflow
 * 3. Defines the template method (predictRatings) for specific algorithms to implement
 *
 * The recommendation process follows these steps:
 * 1. Validate input parameters
 * 2. Retrieve all available movies
 * 3. Filter out movies already rated by the user
 * 4. Predict ratings for candidate movies using algorithm-specific logic
 * 5. Sort by predicted rating and return the top N recommendations
 *
 * Example usage:
 * IRecommendationAlgorithm algorithm = new CollaborativeFilteringAlgorithm();
 * List<Movie> recommendations = algorithm.recommendMovies(userId, 10);
 */
public abstract class AbstractRecommendationAlgorithm implements IRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRecommendationAlgorithm.class);

    protected final IUserService userService;
    protected final IMovieService movieService;
    protected final IUserRatingService ratingService;
    protected final IViewingHistoryService viewingHistoryService;

    protected AbstractRecommendationAlgorithm() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.userService = serviceFactory.getUserService();
        this.movieService = serviceFactory.getMovieService();
        this.ratingService = serviceFactory.getUserRatingService();
        this.viewingHistoryService = serviceFactory.getViewingHistoryService();
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

        // Get movies already watched by the user (from viewing history)
        List<Long> watchedMovieIds = viewingHistoryService.getUserViewingHistory(userId)
                .stream()
                .map(ViewingHistory::getMovieId)
                .toList();

        // Combine both lists to get all movies to exclude
        Set<Long> excludedMovieIds = new HashSet<>(ratedMovieIds);
        excludedMovieIds.addAll(watchedMovieIds);

        // Filter out both rated and watched movies
        List<Movie> candidateMovies = allMovies.stream()
                .filter(movie -> !excludedMovieIds.contains(movie.getMovieId()))
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