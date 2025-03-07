package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.similarity.SimilarityMethod;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Hybrid recommendation algorithm implementation that combines
 * collaborative filtering and content-based filtering approaches.
 *
 * This algorithm addresses limitations of individual recommendation approaches by
 * blending their predictions with configurable weights. It now incorporates user
 * interaction data to better adapt to each user's individual behavior patterns.
 *
 * The algorithm dynamically adjusts its blending strategy based on:
 * - New user detection (cold start): favors content-based approach
 * - Interaction richness: adjusts weights based on user interaction patterns
 * - Power user detection: favors collaborative filtering for users with many ratings
 */
public class HybridRecommendationAlgorithm extends AbstractRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(HybridRecommendationAlgorithm.class);

    // Thresholds for user behavior patterns
    private static final int NEW_USER_THRESHOLD = 5;     // Users with fewer ratings are considered new
    private static final int RICH_INTERACTION_THRESHOLD = 10;   // Users with more interactions have rich interaction data
    private static final int POWER_USER_THRESHOLD = 20;         // Users with many ratings are power users

    // Weight adjustment factors
    private static final double COLD_START_ADJUSTMENT = 0.25;   // Increase content-based weight for new users
    private static final double INTERACTION_ADJUSTMENT = 0.15;  // Adjust based on interaction richness
    private static final double POWER_USER_ADJUSTMENT = 0.15;   // Increase collaborative weight for power users

    private final IRecommendationAlgorithm collaborativeAlgorithm;
    private final IRecommendationAlgorithm contentBasedAlgorithm;
    private double collaborativeWeight;
    private double contentBasedWeight;

    /**
     * Creates a hybrid algorithm with equal weighting.
     */
    public HybridRecommendationAlgorithm() {
        this(0.5, 0.5);
    }

    /**
     * Creates a hybrid algorithm with specified weights.
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
        // Adapt weights based on user profile
        double[] adjustedWeights = calculateAdaptiveWeights(userId);
        double adjustedCollaborativeWeight = adjustedWeights[0];
        double adjustedContentBasedWeight = adjustedWeights[1];

        logger.info("User {}: adjusted weights to collaborative={}, content-based={}",
                userId, adjustedCollaborativeWeight, adjustedContentBasedWeight);

        // Get predictions from both algorithms
        Map<Long, Double> collaborativePredictions = collaborativeAlgorithm.predictRatings(userId, movieIds);
        Map<Long, Double> contentBasedPredictions = contentBasedAlgorithm.predictRatings(userId, movieIds);

        // Log the number of predictions from each algorithm
        logger.debug("User {}: collaborative predictions={}, content-based predictions={}",
                userId, collaborativePredictions.size(), contentBasedPredictions.size());

        // Combine predictions
        Map<Long, Double> hybridPredictions = new HashMap<>();

        // Process collaborative predictions
        if (!collaborativePredictions.isEmpty()) {
            for (Map.Entry<Long, Double> entry : collaborativePredictions.entrySet()) {
                Long movieId = entry.getKey();
                Double rating = entry.getValue() * adjustedCollaborativeWeight;
                hybridPredictions.put(movieId, rating);
            }
        }

        // Process content-based predictions and combine with collaborative
        if (!contentBasedPredictions.isEmpty()) {
            for (Map.Entry<Long, Double> entry : contentBasedPredictions.entrySet()) {
                Long movieId = entry.getKey();
                Double contentRating = entry.getValue() * adjustedContentBasedWeight;

                // Add to existing prediction or set as new prediction
                if (hybridPredictions.containsKey(movieId)) {
                    hybridPredictions.put(movieId,
                            hybridPredictions.get(movieId) + contentRating);
                } else {
                    hybridPredictions.put(movieId, contentRating);
                }
            }
        }

        return hybridPredictions;
    }

    /**
     * Determines if a user is new based on rating count.
     */
    private boolean isNewUser(Long userId) {
        int ratingCount = ratingService.getAllUserRatings(userId).size();
        return ratingCount < NEW_USER_THRESHOLD;
    }

    /**
     * Calculates adaptive weights based on user profile analysis
     * @return double array with [collaborativeWeight, contentBasedWeight]
     */
    private double[] calculateAdaptiveWeights(Long userId) {
        // Get user profile data
        int ratingCount = ratingService.getAllUserRatings(userId).size();
        int interactionCount = userInteractionService.getByUser(userId).size();

        // Start with base weights
        double adjustedCollaborativeWeight = this.collaborativeWeight;
        double adjustedContentBasedWeight = this.contentBasedWeight;

        // Factor 1: Cold Start - New users get higher content-based weight
        if (isNewUser(userId)) {
            adjustedContentBasedWeight += COLD_START_ADJUSTMENT;
            adjustedCollaborativeWeight -= COLD_START_ADJUSTMENT;
            logger.debug("Cold start adjustment for user {}: +{} to content-based",
                    userId, COLD_START_ADJUSTMENT);
        }

        // Factor 2: Interaction Richness - More interactions enhance collaborative filtering
        if (interactionCount > RICH_INTERACTION_THRESHOLD) {
            adjustedCollaborativeWeight += INTERACTION_ADJUSTMENT;
            adjustedContentBasedWeight -= INTERACTION_ADJUSTMENT;
            logger.debug("Interaction richness adjustment for user {}: +{} to collaborative",
                    userId, INTERACTION_ADJUSTMENT);
        }

        // Factor 3: Power User - Users with many ratings benefit from collaborative filtering
        if (ratingCount > POWER_USER_THRESHOLD) {
            adjustedCollaborativeWeight += POWER_USER_ADJUSTMENT;
            adjustedContentBasedWeight -= POWER_USER_ADJUSTMENT;
            logger.debug("Power user adjustment for user {}: +{} to collaborative",
                    userId, POWER_USER_ADJUSTMENT);
        }

        // Normalize to ensure weights are in valid range and sum to 1.0
        adjustedCollaborativeWeight = Math.max(0.1, Math.min(0.9, adjustedCollaborativeWeight));
        adjustedContentBasedWeight = 1.0 - adjustedCollaborativeWeight;

        return new double[] {adjustedCollaborativeWeight, adjustedContentBasedWeight};
    }
}