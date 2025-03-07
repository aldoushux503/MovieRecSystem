package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.similarity.ISimilarityCalculator;
import org.solvd.recommendation.algorithm.similarity.SimilarityCalculatorFactory;
import org.solvd.recommendation.algorithm.similarity.SimilarityMethod;
import org.solvd.recommendation.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Enhanced collaborative filtering recommendation algorithm implementation.
 *
 * This algorithm recommends items based on user similarities, finding patterns in the community
 * behavior rather than content attributes. It now incorporates not only rating patterns but also
 * user interaction signals like likes, dislikes, and favorites.
 *
 * The algorithm works in three main steps:
 * 1. Find similar users (neighbors) based on rating patterns and interaction behavior
 * 2. Select top N most similar users as a neighborhood
 * 3. Predict ratings using weighted averages from this neighborhood
 */
public class CollaborativeFilteringAlgorithm extends AbstractRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(CollaborativeFilteringAlgorithm.class);
    private static final int DEFAULT_NEIGHBOR_COUNT = 10;
    private static final double SIMILARITY_THRESHOLD = 0.1;

    // Simplified weighting scheme with reduced interaction influence
    private static final double RATING_SIMILARITY_WEIGHT = 0.8;
    private static final double INTERACTION_SIMILARITY_WEIGHT = 0.2;

    // Simplified implicit rating values (only for significant types)
    private static final double FAVORITE_IMPLICIT_RATING = 8.0;
    private static final double LIKE_IMPLICIT_RATING = 7.0;
    private static final double DISLIKE_IMPLICIT_RATING = 3.0;

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
        Map<Long, Double> targetUserRatings = getUserRatingsMap(userId);

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
     * Finds users similar to the target user based on rating patterns and interaction behavior.
     */
    private Map<Long, Double> findSimilarUsers(Long targetUserId, Map<Long, Double> targetUserRatings) {
        // Get all users except the target user
        List<Long> otherUserIds = userService.getAll().stream()
                .map(User::getUserId)
                .filter(id -> !id.equals(targetUserId))
                .toList();

        // Similarity map [userId -> similarityScore]
        Map<Long, Double> similarities = new HashMap<>();

        // Get target user's interactions for efficient comparison
        Map<Long, Set<Long>> targetUserInteractions = getMovieInteractionsByType(targetUserId);

        // Calculate similarity with each user
        for (Long userId : otherUserIds) {
            Map<Long, Double> userRatings = getUserRatingsMap(userId);

            // Skip users with no ratings
            if (userRatings.isEmpty()) continue;

            // Calculate rating-based similarity
            double ratingSimilarity = similarityCalculator.calculateSimilarity(targetUserRatings, userRatings);

            // Skip interaction similarity calculation if rating similarity is too low
            if (ratingSimilarity < SIMILARITY_THRESHOLD / 2) continue;

            // Calculate interaction-based similarity (simplified)
            double interactionSimilarity = calculateSimpleInteractionSimilarity(
                    targetUserInteractions, getMovieInteractionsByType(userId));

            // Combine both similarities with appropriate weights
            double combinedSimilarity = (ratingSimilarity * RATING_SIMILARITY_WEIGHT) +
                    (interactionSimilarity * INTERACTION_SIMILARITY_WEIGHT);

            // Only consider users with positive combined similarity above threshold
            if (combinedSimilarity > SIMILARITY_THRESHOLD) {
                similarities.put(userId, combinedSimilarity);
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
     * Simplified calculation of interaction similarity.
     * Only considers positive and negative interactions instead of four types.
     */
    private double calculateSimpleInteractionSimilarity(
            Map<Long, Set<Long>> user1Interactions, Map<Long, Set<Long>> user2Interactions) {

        if (user1Interactions.isEmpty() || user2Interactions.isEmpty()) {
            return 0.0;
        }

        // Get interaction type IDs
        Long likeId = getInteractionTypeId(InteractionType.LIKE);
        Long favoriteId = getInteractionTypeId(InteractionType.FAVORITE);
        Long dislikeId = getInteractionTypeId(InteractionType.DISLIKE);

        // Collect positive interactions (likes and favorites)
        Set<Long> user1Positive = new HashSet<>();
        if (likeId != null) user1Positive.addAll(user1Interactions.getOrDefault(likeId, Collections.emptySet()));
        if (favoriteId != null) user1Positive.addAll(user1Interactions.getOrDefault(favoriteId, Collections.emptySet()));

        Set<Long> user2Positive = new HashSet<>();
        if (likeId != null) user2Positive.addAll(user2Interactions.getOrDefault(likeId, Collections.emptySet()));
        if (favoriteId != null) user2Positive.addAll(user2Interactions.getOrDefault(favoriteId, Collections.emptySet()));

        // Collect negative interactions (dislikes)
        Set<Long> user1Negative = dislikeId != null ?
                user1Interactions.getOrDefault(dislikeId, Collections.emptySet()) :
                Collections.emptySet();

        Set<Long> user2Negative = dislikeId != null ?
                user2Interactions.getOrDefault(dislikeId, Collections.emptySet()) :
                Collections.emptySet();

        // Calculate positive interaction similarity (60% weight)
        double positiveSimilarity = calculateJaccardSimilarity(user1Positive, user2Positive) * 0.6;

        // Calculate negative interaction similarity (40% weight)
        double negativeSimilarity = calculateJaccardSimilarity(user1Negative, user2Negative) * 0.4;

        return positiveSimilarity + negativeSimilarity;
    }

    /**
     * Calculate Jaccard similarity between two sets of IDs.
     * Measures the overlap ratio between two sets.
     *
     * @param set1 First set
     * @param set2 Second set
     * @return Similarity value [0.0-1.0]
     */
    private double calculateJaccardSimilarity(Set<Long> set1, Set<Long> set2) {
        if (set1.isEmpty() && set2.isEmpty()) {
            return 0.0;
        }

        Set<Long> union = new HashSet<>(set1);
        union.addAll(set2);

        Set<Long> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        return (double) intersection.size() / union.size();
    }

    /**
     * Predicts a rating for a specific movie based on similar users' ratings and interactions.
     * The formula used is:
     * predictedRating = Σ(similarity_u * rating_u) / Σ|similarity_u|
     * where:
     * - u is a user in the set of similar users who rated the movie
     * - similarity_u is the similarity score between the target user and user u
     * - rating_u is the rating given by user u for the movie
     */
    private Double predictRating(Long movieId, Map<Long, Double> similarUsers,
                                 Map<Long, Double> targetUserRatings) {
        double weightSum = 0.0;
        double weightedRatingSum = 0.0;

        // If target user has already rated this movie, don't predict
        if (targetUserRatings.containsKey(movieId)) {
            return null;
        }

        // Strict approach: primarily use existing ratings
        for (Map.Entry<Long, Double> entry : similarUsers.entrySet()) {
            Long userId = entry.getKey();
            Double similarity = entry.getValue();

            // First check if the user has rated this movie
            UserRating rating = ratingService.getByUserAndMovie(userId, movieId);
            if (rating != null) {
                // Add to weighted sum
                double userRating = rating.getRatingValue().doubleValue();
                weightedRatingSum += similarity * userRating;
                weightSum += Math.abs(similarity);
            }
            // Only check interactions for users with high similarity
            else if (similarity > 0.4) {
                UserInteraction interaction = getSignificantInteraction(userId, movieId);
                if (interaction != null) {
                    Double implicitRating = getImplicitRating(interaction);
                    if (implicitRating != null) {
                        // Apply a lower weight for interaction-based ratings
                        double adjustedSimilarity = similarity * 0.5;
                        weightedRatingSum += adjustedSimilarity * implicitRating;
                        weightSum += Math.abs(adjustedSimilarity);
                    }
                }
            }
        }

        // If couldn't gather enough signal, can't predict
        if (weightSum == 0.0) {
            return null;
        }

        // Calculate normalized weighted average
        return weightedRatingSum / weightSum;
    }

    /**
     * Simplified method to get the most significant interaction with a movie.
     */
    private UserInteraction getSignificantInteraction(Long userId, Long movieId) {
        List<UserInteraction> movieInteractions = userInteractionService.getByUser(userId).stream()
                .filter(i -> i.getMovieId().equals(movieId))
                .toList();

        if (movieInteractions.isEmpty()) return null;

        // Interaction priorities: favorite > like > dislike
        Long favoriteId = getInteractionTypeId(InteractionType.FAVORITE);
        Long likeId = getInteractionTypeId(InteractionType.LIKE);
        Long dislikeId = getInteractionTypeId(InteractionType.DISLIKE);

        // Check for interactions in order of priority
        for (Long interactionTypeId : Arrays.asList(favoriteId, likeId, dislikeId)) {
            if (interactionTypeId == null) continue;

            Optional<UserInteraction> interaction = movieInteractions.stream()
                    .filter(i -> i.getInteractionsId().equals(interactionTypeId))
                    .findFirst();

            if (interaction.isPresent()) return interaction.get();
        }

        return null;
    }

    /**
     * Derives an implicit rating value from interaction.
     */
    private Double getImplicitRating(UserInteraction interaction) {
        if (interaction == null) return null;

        Interaction interactionType = interactionService.getById(interaction.getInteractionsId());
        if (interactionType == null) return null;

        try {
            InteractionType type = InteractionType.valueOf(String.valueOf(interactionType.getType()));

            return switch (type) {
                case FAVORITE -> FAVORITE_IMPLICIT_RATING;
                case LIKE -> LIKE_IMPLICIT_RATING;
                case DISLIKE -> DISLIKE_IMPLICIT_RATING;
                default -> null;
            };
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Gets the interaction type ID for the specified interaction type.
     */
    private Long getInteractionTypeId(InteractionType type) {
        for (Interaction interaction : interactionService.getAll()) {
            try {
                String interactionType = String.valueOf(interaction.getType());
                if (interactionType.equals(type.name())) {
                    return interaction.getInteractionId();
                }
            } catch (Exception e) {
                logger.warn("Error processing interaction type: {}", e.getMessage());
            }
        }
        return null;
    }

    /**
     * Gets user's interactions with movies, grouped by interaction type.
     */
    private Map<Long, Set<Long>> getMovieInteractionsByType(Long userId) {
        List<UserInteraction> interactions = userInteractionService.getByUser(userId);
        Map<Long, Set<Long>> moviesByInteractionType = new HashMap<>();

        for (UserInteraction interaction : interactions) {
            moviesByInteractionType.computeIfAbsent(
                            interaction.getInteractionsId(), k -> new HashSet<>())
                    .add(interaction.getMovieId());
        }

        return moviesByInteractionType;
    }

    /**
     * Converts user ratings to a map of movie IDs to rating values.
     */
    private Map<Long, Double> getUserRatingsMap(Long userId) {
        return ratingService.getAllUserRatings(userId).stream()
                .collect(Collectors.toMap(
                        UserRating::getMovieId,
                        rating -> rating.getRatingValue().doubleValue()
                ));
    }
}