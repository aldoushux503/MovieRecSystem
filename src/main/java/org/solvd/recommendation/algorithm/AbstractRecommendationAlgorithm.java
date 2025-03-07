package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.*;
import org.solvd.recommendation.service.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract base class for recommendation algorithms.
 */
public abstract class AbstractRecommendationAlgorithm implements IRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRecommendationAlgorithm.class);

    // Common interaction weight constants for all algorithms
    protected static final double LIKE_WEIGHT = 0.5;
    protected static final double FAVORITE_WEIGHT = 0.8;
    protected static final double DISLIKE_WEIGHT = -0.7;
    protected static final double WATCH_WEIGHT = 0.2;

    protected final IUserService userService;
    protected final IMovieService movieService;
    protected final IUserRatingService ratingService;
    protected final IViewingHistoryService viewingHistoryService;
    protected final IUserInteractionService userInteractionService;
    protected final IInteractionService interactionService;
    protected final IMovieGenreService movieGenreService;

    protected AbstractRecommendationAlgorithm() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.userService = serviceFactory.getUserService();
        this.movieService = serviceFactory.getMovieService();
        this.ratingService = serviceFactory.getUserRatingService();
        this.viewingHistoryService = serviceFactory.getViewingHistoryService();
        this.userInteractionService = serviceFactory.getUserInteractionService();
        this.interactionService = serviceFactory.getInteractionService();
        this.movieGenreService = serviceFactory.getMovieGenreService();
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