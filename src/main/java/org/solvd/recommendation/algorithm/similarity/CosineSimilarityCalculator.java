package org.solvd.recommendation.algorithm.similarity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements the Cosine Similarity calculation between two vectors.
 * Cosine Similarity measures the cosine of the angle between two vectors, focusing on their
 * orientation rather than magnitude. Ideal for comparing user preferences or item features.
 * Example:
 * - Vector1: {1: 4, 2: 3}
 * - Vector2: {1: 5, 2: 1}
 * - Dot Product = (4 * 5) + (3 * 1) = 23
 * - Magnitude1 = sqrt(4² + 3²) = 5
 * - Magnitude2 = sqrt(5² + 1²) ≈ 5.1
 * - Similarity = 23 / (5 * 5.1) ≈ 0.9
 */
public class CosineSimilarityCalculator implements ISimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(CosineSimilarityCalculator.class);

    @Override
    public double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        // Check if either vector is empty
        if (vector1.isEmpty() || vector2.isEmpty()) {
            logger.debug("One or both vectors are empty, returning similarity of 0.0");
            return 0.0;
        }

        // Create a set of all keys from both vectors
        Set<Long> allKeys = new HashSet<>(vector1.keySet());
        allKeys.addAll(vector2.keySet());

        // Initialize accumulators
        double dotProduct = 0.0;  // Dot product of the vectors
        double magnitude1 = 0.0;  // Squared magnitude of vector1
        double magnitude2 = 0.0;  // Squared magnitude of vector2

        // Calculate dot product and squared magnitudes
        for (Long key : allKeys) {
            double val1 = vector1.getOrDefault(key, 0.0);  // Default to 0 if key not present
            double val2 = vector2.getOrDefault(key, 0.0);  // Default to 0 if key not present

            dotProduct += val1 * val2;  // Accumulate dot product
            magnitude1 += val1 * val1;  // Accumulate squared magnitude for vector1
            magnitude2 += val2 * val2;  // Accumulate squared magnitude for vector2
        }

        // Calculate square roots of magnitudes
        double magnitude1Sqrt = Math.sqrt(magnitude1);  // Square root of magnitude1
        double magnitude2Sqrt = Math.sqrt(magnitude2);  // Square root of magnitude2

        // Check if either magnitude is zero
        if (magnitude1Sqrt <= 0.0 || magnitude2Sqrt <= 0.0) {
            logger.debug("One or both vector magnitudes are zero, returning similarity of 0.0");
            return 0.0;
        }

        // Calculate cosine similarity
        double denominator = magnitude1Sqrt * magnitude2Sqrt;  // Product of magnitudes
        return dotProduct / denominator;  // Final similarity value
    }
}