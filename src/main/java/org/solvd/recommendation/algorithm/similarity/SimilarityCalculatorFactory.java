package org.solvd.recommendation.algorithm.similarity;


/**
 * Factory class for creating instances of SimilarityCalculator based on the specified method.
 * Provides a centralized way to instantiate similarity calculators, promoting flexibility and
 * maintainability in the recommendation system.
 */
public class SimilarityCalculatorFactory {


    public static ISimilarityCalculator createCalculator(SimilarityMethod method) {
        return switch (method) {
            case COSINE -> new CosineSimilarityCalculator();
            case PEARSON -> new PearsonCorrelationCalculator();
            default -> throw new IllegalArgumentException("Unknown similarity method: " + method);
        };
    }
}