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
    public double calculateSimilarity(Map<Long, BigDecimal> vector1, Map<Long, BigDecimal> vector2) {
        // Check if either vector is empty
        if (vector1.isEmpty() || vector2.isEmpty()) {
            logger.debug("One or both vectors are empty, returning similarity of 0.0");
            return 0.0;
        }

        // Create a set of all keys from both vectors
        Set<Long> allKeys = new HashSet<>(vector1.keySet());
        allKeys.addAll(vector2.keySet());

        // Initialize accumulators with BigDecimal
        BigDecimal dotProduct = BigDecimal.ZERO;  // Dot product of the vectors
        BigDecimal magnitude1 = BigDecimal.ZERO;  // Squared magnitude of vector1
        BigDecimal magnitude2 = BigDecimal.ZERO;  // Squared magnitude of vector2

        // Calculate dot product and squared magnitudes
        for (Long key : allKeys) {
            BigDecimal val1 = vector1.getOrDefault(key, BigDecimal.ZERO);  // Default to 0 if key not present
            BigDecimal val2 = vector2.getOrDefault(key, BigDecimal.ZERO);  // Default to 0 if key not present

            dotProduct = dotProduct.add(val1.multiply(val2));  // Accumulate dot product
            magnitude1 = magnitude1.add(val1.multiply(val1));  // Accumulate squared magnitude for vector1
            magnitude2 = magnitude2.add(val2.multiply(val2));  // Accumulate squared magnitude for vector2
        }

        // Calculate square roots of magnitudes
        BigDecimal magnitude1Sqrt = magnitude1.sqrt(MathContext.DECIMAL64);  // Square root of magnitude1
        BigDecimal magnitude2Sqrt = magnitude2.sqrt(MathContext.DECIMAL64);  // Square root of magnitude2

        // Check if either magnitude is zero or negative
        if (magnitude1Sqrt.compareTo(BigDecimal.ZERO) <= 0 || magnitude2Sqrt.compareTo(BigDecimal.ZERO) <= 0) {
            logger.debug("One or both vector magnitudes are zero, returning similarity of 0.0");
            return 0.0;
        }

        // Calculate cosine similarity
        BigDecimal denominator = magnitude1Sqrt.multiply(magnitude2Sqrt);  // Product of magnitudes
        BigDecimal similarity = dotProduct.divide(denominator, MathContext.DECIMAL64);  // Final similarity value
        return similarity.doubleValue();  // Convert BigDecimal to double for return
    }
}