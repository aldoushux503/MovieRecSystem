package org.solvd.recommendation.service.imlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.IRecommendationAlgorithm;
import org.solvd.recommendation.algorithm.RecommendationAlgorithmFactory;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.service.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the recommendation service interface.
 * Provides methods for generating movie recommendations using different algorithms.
 */

public class RecommendationService implements IRecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    private static final int DEFAULT_TRENDING_DAYS = 30;

    private final RecommendationAlgorithmFactory algorithmFactory;
    private final IMovieService movieService;
    private final IUserRatingService ratingService;
    private final IViewingHistoryService viewingHistoryService;

    public RecommendationService() {
        this.algorithmFactory = RecommendationAlgorithmFactory.getInstance();
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.movieService = serviceFactory.getMovieService();
        this.ratingService = serviceFactory.getUserRatingService();
        this.viewingHistoryService = serviceFactory.getViewingHistoryService();
    }

    @Override
    public List<Movie> recommendMoviesForUser(Long userId, int limit) {
        logger.info("Generating {} recommendations for user {}", limit, userId);

        // Use hybrid recommendation by default for best results
        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.HYBRID_RECOMMENDATION);

        return algorithm.recommendMovies(userId, limit);
    }

    @Override
    public List<Movie> getCollaborativeFilteringRecommendations(Long userId, int limit) {
        logger.info("Generating {} collaborative filtering recommendations for user {}", limit, userId);

        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.COLLABORATIVE_FILTERING);

        return algorithm.recommendMovies(userId, limit);
    }

    @Override
    public List<Movie> getContentBasedRecommendations(Long userId, int limit) {
        logger.info("Generating {} content-based recommendations for user {}", limit, userId);

        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.CONTENT_BASED_FILTERING);

        return algorithm.recommendMovies(userId, limit);
    }

    @Override
    public List<Movie> getSimilarMovies(Long movieId, int limit) {
        logger.info("Finding {} similar movies to movie {}", limit, movieId);

        // Get the target movie
        Movie targetMovie = movieService.getById(movieId);
        if (targetMovie == null) {
            throw new IllegalArgumentException("Movie not found: " + movieId);
        }

        // Get all other movies
        List<Movie> allMovies = movieService.getAll().stream()
                .filter(movie -> !movie.getMovieId().equals(movieId))
                .toList();

        // Get users who rated this movie highly
        List<UserRating> movieRatings = ratingService.getMovieRatings(movieId);
        List<Long> userIds = movieRatings.stream()
                .filter(rating -> rating.getRatingValue().doubleValue() >= 7.0) // Users who liked this movie
                .map(UserRating::getUserId)
                .toList();

        // If no users found, return empty result
        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // For each movie, calculate similarity score based on common users who liked both movies
        Map<Long, Double> similarityScores = new HashMap<>();

        for (Movie movie : allMovies) {
            Long otherMovieId = movie.getMovieId();
            List<UserRating> otherMovieRatings = ratingService.getMovieRatings(otherMovieId);

            // Count users who rated both movies highly
            long commonUsers = otherMovieRatings.stream()
                    .filter(rating -> userIds.contains(rating.getUserId()) &&
                            rating.getRatingValue().doubleValue() >= 7.0)
                    .count();

            if (commonUsers > 0) {
                // Calculate Jaccard similarity: intersection / union
                double similarity = (double) commonUsers / (userIds.size() + otherMovieRatings.size() - commonUsers);
                similarityScores.put(otherMovieId, similarity);
            }
        }

        // Sort by similarity and select top results
        return allMovies.stream()
                .filter(movie -> similarityScores.containsKey(movie.getMovieId()))
                .sorted(Comparator.comparing(movie -> -similarityScores.get(movie.getMovieId())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getTrendingMovies(int limit) {
        logger.info("Getting {} trending movies", limit);

        // Calculate trending period (e.g., last 30 days)
        Timestamp cutoffDate = Timestamp.from(Instant.now().minus(DEFAULT_TRENDING_DAYS, ChronoUnit.DAYS));

        // Count views for each movie in the trending period
        Map<Long, Long> viewCounts = new HashMap<>();

        List<ViewingHistory> recentViews = viewingHistoryService.getUserViewingHistory(null).stream()
                .filter(history -> history.getWatchDate().after(cutoffDate))
                .toList();

        // Count views per movie
        for (ViewingHistory history : recentViews) {
            Long movieId = history.getMovieId();
            viewCounts.put(movieId, viewCounts.getOrDefault(movieId, 0L) + 1);
        }

        // Get all movies
        List<Movie> allMovies = movieService.getAll();

        // Sort by view count (trending score)
        return allMovies.stream()
                .filter(movie -> viewCounts.containsKey(movie.getMovieId()))
                .sorted(Comparator.comparing(movie -> -viewCounts.get(movie.getMovieId())))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, Double> predictUserRatings(Long userId, List<Long> movieIds) {
        logger.info("Predicting ratings for user {} for {} movies", userId, movieIds.size());

        // Use hybrid algorithm for best prediction results
        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.HYBRID_RECOMMENDATION);

        return algorithm.predictRatings(userId, movieIds);
    }
}