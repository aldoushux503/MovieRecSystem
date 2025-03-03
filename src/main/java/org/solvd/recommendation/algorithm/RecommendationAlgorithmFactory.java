package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.similarity.SimilarityMethod;

/**
 * Factory for creating recommendation algorithm instances.
 * Implements the Factory design pattern and Singleton pattern.
 */
public class RecommendationAlgorithmFactory {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationAlgorithmFactory.class);
    private static final RecommendationAlgorithmFactory instance = new RecommendationAlgorithmFactory();

    /**
     * Algorithm types supported by the factory.
     */
    public enum AlgorithmType {
        COLLABORATIVE_FILTERING,
        CONTENT_BASED_FILTERING,
        HYBRID_RECOMMENDATION
    }

    private RecommendationAlgorithmFactory() {
        // Private constructor to enforce Singleton pattern
    }

    /**
     * Gets the singleton instance of the algorithm factory.
     */
    public static RecommendationAlgorithmFactory getInstance() {
        return instance;
    }

    /**
     * Creates a recommendation algorithm instance based on the specified type.
     *
     * @param type Algorithm type to create
     * @return Recommendation algorithm instance
     */
    public IRecommendationAlgorithm createAlgorithm(AlgorithmType type) {
        logger.info("Creating algorithm of type: {}", type);

        return switch (type) {
            case COLLABORATIVE_FILTERING -> new CollaborativeFilteringAlgorithm();
            case CONTENT_BASED_FILTERING -> new ContentBasedFilteringAlgorithm();
            case HYBRID_RECOMMENDATION -> new HybridRecommendationAlgorithm();
            default -> throw new IllegalArgumentException("Unknown algorithm type: " + type);
        };
    }

    /**
     * Creates a collaborative filtering algorithm with custom settings.
     *
     * @param similarityMethod Similarity calculation method
     * @param neighborCount Number of similar users to consider
     * @return Configured collaborative filtering algorithm
     */
    public IRecommendationAlgorithm createCollaborativeAlgorithm(
            SimilarityMethod similarityMethod, int neighborCount) {
        return new CollaborativeFilteringAlgorithm(similarityMethod, neighborCount);
    }

    /**
     * Creates a content-based filtering algorithm with custom settings.
     *
     * @param similarityMethod Similarity calculation method
     * @return Configured content-based filtering algorithm
     */
    public IRecommendationAlgorithm createContentBasedAlgorithm(SimilarityMethod similarityMethod) {
        return new ContentBasedFilteringAlgorithm(similarityMethod);
    }

    /**
     * Creates a hybrid recommendation algorithm with custom weightings.
     *
     * @param collaborativeWeight Weight for collaborative filtering results (0-1)
     * @param contentBasedWeight Weight for content-based filtering results (0-1)
     * @return Configured hybrid recommendation algorithm
     */
    public IRecommendationAlgorithm createHybridAlgorithm(
            double collaborativeWeight, double contentBasedWeight) {
        return new HybridRecommendationAlgorithm(collaborativeWeight, contentBasedWeight);
    }
}