package org.solvd.recommendation.algorithm.similarity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements the Pearson Correlation Coefficient calculation between two vectors.
 * Measures the linear relationship between two variables by comparing deviations from their
 * means. Useful for capturing how user ratings correlate relative to their averages.
 *
 * Example:
 * - Vector1: {1: 4, 2: 2}
 * - Vector2: {1: 5, 2: 1}
 * - Mean1 = (4 + 2) / 2 = 3
 * - Mean2 = (5 + 1) / 2 = 3
 * - Deviations: Vector1: [1, -1], Vector2: [2, -2]
 * - Numerator = (1 * 2) + (-1 * -2) = 4
 * - Denominator = sqrt(1² + (-1)²) * sqrt(2² + (-2)²) ≈ 4
 * - Correlation = 4 / 4 = 1
 */
public class PearsonCorrelationCalculator implements ISimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(PearsonCorrelationCalculator.class);

    @Override
    public double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        // Find common keys (items rated by both users)
        Set<Long> commonKeys = new HashSet<>(vector1.keySet());
        commonKeys.retainAll(vector2.keySet());

        if (commonKeys.isEmpty()) {
            logger.debug("No common items found between vectors, returning similarity of 0.0");
            return 0.0;
        }

        int n = commonKeys.size();

        // Calculate sums for means
        double sum1 = 0.0;
        double sum2 = 0.0;
        for (Long key : commonKeys) {
            sum1 += vector1.get(key);
            sum2 += vector2.get(key);
        }

        // Calculate means
        double mean1 = sum1 / n;
        double mean2 = sum2 / n;

        // Initialize accumulators for correlation components
        double numerator = 0.0;
        double denom1 = 0.0;
        double denom2 = 0.0;

        // Calculate numerator and denominators for correlation
        for (Long key : commonKeys) {
            double val1 = vector1.get(key) - mean1;
            double val2 = vector2.get(key) - mean2;

            numerator += val1 * val2;
            denom1 += val1 * val1;
            denom2 += val2 * val2;
        }

        // Check for zero variance
        if (denom1 <= 0.0 || denom2 <= 0.0) {
            logger.debug("Zero variance in one or both vectors, returning similarity of 0.0");
            return 0.0;
        }

        // Calculate square roots of denominators
        double sqrtDenom1 = Math.sqrt(denom1);
        double sqrtDenom2 = Math.sqrt(denom2);

        // Calculate Pearson correlation
        double denominator = sqrtDenom1 * sqrtDenom2;
        return numerator / denominator;
    }
}

