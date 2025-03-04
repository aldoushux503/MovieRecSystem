package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Hybrid recommendation algorithm implementation that combines
 * collaborative filtering and content-based filtering approaches.
 *
 * This approach leverages the strengths of both methods:
 * - Collaborative filtering for capturing community preferences and discovering new content
 * - Content-based filtering for handling cold start problem and providing explainable recommendations
 */
public class HybridRecommendationAlgorithm extends AbstractRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(HybridRecommendationAlgorithm.class);

    private final IRecommendationAlgorithm collaborativeAlgorithm;
    private final IRecommendationAlgorithm contentBasedAlgorithm;
    private final double collaborativeWeight;
    private final double contentBasedWeight;

    /**
     * Creates a hybrid algorithm with equal weighting.
     */
    public HybridRecommendationAlgorithm() {
        this(0.5, 0.5);
    }

    /**
     * Creates a hybrid algorithm with specified weights.
     *
     * @param collaborativeWeight Weight for collaborative filtering results (0-1)
     * @param contentBasedWeight Weight for content-based filtering results (0-1)
     */
    public HybridRecommendationAlgorithm(double collaborativeWeight, double contentBasedWeight) {
        super();
        this.collaborativeAlgorithm = new CollaborativeFilteringAlgorithm();
        this.contentBasedAlgorithm = new ContentBasedFilteringAlgorithm();

        // Ensure weights sum to 1.0
        double total = collaborativeWeight + contentBasedWeight;
        this.collaborativeWeight = collaborativeWeight / total;
        this.contentBasedWeight = contentBasedWeight / total;

        logger.info("Initialized hybrid algorithm with collaborative weight={}, content-based weight={}",
                this.collaborativeWeight, this.contentBasedWeight);
    }

    @Override
    public Map<Long, Double> predictRatings(Long userId, List<Long> movieIds) {
        // Get predictions from both algorithms
        Map<Long, Double> collaborativePredictions = collaborativeAlgorithm.predictRatings(userId, movieIds);
        Map<Long, Double> contentBasedPredictions = contentBasedAlgorithm.predictRatings(userId, movieIds);

        // Combine predictions
        Map<Long, Double> hybridPredictions = new HashMap<>();

        // Process collaborative predictions
        if (!collaborativePredictions.isEmpty()) {
            for (Map.Entry<Long, Double> entry : collaborativePredictions.entrySet()) {
                Long movieId = entry.getKey();
                Double rating = entry.getValue() * collaborativeWeight;
                hybridPredictions.put(movieId, rating);
            }
        }

        // Process content-based predictions and combine with collaborative
        if (!contentBasedPredictions.isEmpty()) {
            for (Map.Entry<Long, Double> entry : contentBasedPredictions.entrySet()) {
                Long movieId = entry.getKey();
                Double contentRating = entry.getValue() * contentBasedWeight;

                // Add to existing prediction or set as new prediction
                if (hybridPredictions.containsKey(movieId)) {
                    hybridPredictions.put(movieId, hybridPredictions.get(movieId) + contentRating);
                } else {
                    hybridPredictions.put(movieId, contentRating);
                }
            }
        }

        // Handle cold start (new users with few ratings)
        if (isNewUser(userId) && !contentBasedPredictions.isEmpty()) {
            logger.info("Adapting for new user {}: increasing content-based weight", userId);

            // For new users, overwrite with content-based predictions for better cold start handling
            for (Map.Entry<Long, Double> entry : contentBasedPredictions.entrySet()) {
                Long movieId = entry.getKey();
                if (!hybridPredictions.containsKey(movieId)) {
                    hybridPredictions.put(movieId, entry.getValue());
                }
            }
        }

        return hybridPredictions;
    }

    /**
     * Determines if a user is new to the system based on rating count.
     */
    private boolean isNewUser(Long userId) {
        int ratingCount = ratingService.getAllUserRatings(userId).size();
        return ratingCount < 5; // Users with fewer than 5 ratings are considered new
    }
}