package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.similarity.ISimilarityCalculator;
import org.solvd.recommendation.algorithm.similarity.SimilarityCalculatorFactory;
import org.solvd.recommendation.algorithm.similarity.SimilarityMethod;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.service.IViewingHistoryService;
import org.solvd.recommendation.service.ServiceFactory;
import org.solvd.recommendation.service.imlp.ViewingHistoryService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Collaborative filtering recommendation algorithm implementation.
 *
 * This algorithm recommends items based on user similarities, finding patterns in the community
 * behavior rather than content attributes. The core concept is: users who agreed in the past
 * will likely agree in the future.
 *
 * The algorithm works in three main steps:
 * 1. Find similar users (neighbors) based on rating patterns
 * 2. Select top N most similar users as a neighborhood
 * 3. Predict ratings using weighted averages from this neighborhood
 *
 * Example:
 * - User A rated movies: {Movie1: 8, Movie2: 6, Movie3: 9}
 * - User B rated movies: {Movie1: 7, Movie2: 6, Movie4: 8}
 * - User C rated movies: {Movie1: 3, Movie2: 2, Movie3: 4}
 *
 * The algorithm might determine:
 * - Similarity(A,B) = 0.85 (high similarity)
 * - Similarity(A,C) = 0.2 (low similarity)
 *
 * To predict User A's rating for Movie4:
 * - Use User B's rating weighted by similarity: 8 * 0.85 = 6.8
 * - Normalized by sum of similarities: 6.8 / 0.85 = 8
 *
 * Thus the algorithm would predict User A would rate Movie4 as 8.
 */
public class CollaborativeFilteringAlgorithm extends AbstractRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(CollaborativeFilteringAlgorithm.class);
    private static final int DEFAULT_NEIGHBOR_COUNT = 10;
    private static final double SIMILARITY_THRESHOLD = 0.1;

    private final ISimilarityCalculator similarityCalculator;
    private final int neighborCount;

    public CollaborativeFilteringAlgorithm() {
        this(SimilarityMethod.PEARSON, DEFAULT_NEIGHBOR_COUNT);
    }

    public CollaborativeFilteringAlgorithm(SimilarityMethod similarityMethod, int neighborCount) {
        super();
        this.similarityCalculator = SimilarityCalculatorFactory.createCalculator(similarityMethod);
        this.neighborCount = neighborCount;
        logger.info("Initialized collaborative filtering with {} similarity and {} neighbors",
                similarityMethod, neighborCount);
    }

    @Override
    public Map<Long, Double> predictRatings(Long userId, List<Long> movieIds) {
        // Get target user's ratings
        Map<Long, BigDecimal> targetUserRatings = getUserRatingsMap(userId);

        // Find similar users (neighbors)
        Map<Long, Double> userSimilarities = findSimilarUsers(userId, targetUserRatings);

        // If no similar users found, return empty result
        if (userSimilarities.isEmpty()) {
            logger.warn("No similar users found for user {}", userId);
            return Collections.emptyMap();
        }

        // Predict ratings for specified movies
        Map<Long, Double> predictions = new HashMap<>();
        for (Long movieId : movieIds) {
            Double predictedRating = predictRating(movieId, userSimilarities, targetUserRatings);
            if (predictedRating != null) {
                predictions.put(movieId, predictedRating);
            }
        }

        return predictions;
    }

    /**
     * Finds users similar to the target user based on rating patterns.
     */
    private Map<Long, Double> findSimilarUsers(Long targetUserId, Map<Long, BigDecimal> targetUserRatings) {
        // Get all users
        List<Long> allUserIds = userService.getAll().stream()
                .map(User::getUserId)
                .filter(id -> !id.equals(targetUserId))
                .toList();

        Map<Long, Double> similarities = new HashMap<>();

        // Calculate similarity with each user
        for (Long userId : allUserIds) {
            Map<Long, BigDecimal> userRatings = getUserRatingsMap(userId);

            // Skip users with no ratings
            if (userRatings.isEmpty()) {
                continue;
            }

            // Calculate similarity between target user and this user
            double similarity = similarityCalculator.calculateSimilarity(targetUserRatings, userRatings);

            // Only consider users with positive similarity above threshold
            if (similarity > SIMILARITY_THRESHOLD) {
                similarities.put(userId, similarity);
            }
        }

        // Sort by similarity and take top N neighbors
        return similarities.entrySet().stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(neighborCount)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    /**
     * Predicts a rating for a specific movie based on similar users' ratings.
     */
    private Double predictRating(Long movieId, Map<Long, Double> similarUsers,
                                 Map<Long, BigDecimal> targetUserRatings) {
        double weightSum = 0;
        double weightedRatingSum = 0;

        // If target user has already rated this movie, don't predict
        if (targetUserRatings.containsKey(movieId)) {
            return null;
        }

        // Calculate weighted sum of ratings from similar users
        for (Map.Entry<Long, Double> entry : similarUsers.entrySet()) {
            Long userId = entry.getKey();
            Double similarity = entry.getValue();

            // Get similar user's rating for this movie
            UserRating rating = ratingService.getByUserAndMovie(userId, movieId);
            if (rating == null) {
                continue;
            }

            // Add to weighted sum
            double userRating = rating.getRatingValue().doubleValue();
            weightedRatingSum += similarity * userRating;
            weightSum += Math.abs(similarity);
        }

        // If no similar users rated this movie, can't predict
        if (weightSum == 0) {
            return null;
        }

        // Calculate normalized weighted average
        return weightedRatingSum / weightSum;
    }

    /**
     * Converts user ratings to a map of movie IDs to rating values.
     */
    private Map<Long, BigDecimal> getUserRatingsMap(Long userId) {
        return ratingService.getAllUserRatings(userId).stream()
                .collect(Collectors.toMap(
                        UserRating::getMovieId,
                        UserRating::getRatingValue
                ));
    }
}