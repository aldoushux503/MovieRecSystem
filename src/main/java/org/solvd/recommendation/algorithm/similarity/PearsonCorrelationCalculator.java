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
public class PearsonCorrelationCalculator implements SimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(PearsonCorrelationCalculator.class);

    @Override
    public double calculateSimilarity(Map<Long, BigDecimal> vector1, Map<Long, BigDecimal> vector2) {
        // Find common keys (items rated by both users)
        Set<Long> commonKeys = new HashSet<>(vector1.keySet());
        commonKeys.retainAll(vector2.keySet());

        if (commonKeys.isEmpty()) {
            logger.debug("No common items found between vectors, returning similarity of 0.0");
            return 0.0;
        }

        int n = commonKeys.size();

        // Calculate sums for means
        BigDecimal sum1 = BigDecimal.ZERO;
        BigDecimal sum2 = BigDecimal.ZERO;
        for (Long key : commonKeys) {
            sum1 = sum1.add(vector1.get(key));
            sum2 = sum2.add(vector2.get(key));
        }

        // Calculate means
        BigDecimal mean1 = sum1.divide(BigDecimal.valueOf(n), MathContext.DECIMAL64);
        BigDecimal mean2 = sum2.divide(BigDecimal.valueOf(n), MathContext.DECIMAL64);

        // Initialize accumulators for correlation components
        BigDecimal numerator = BigDecimal.ZERO;
        BigDecimal denom1 = BigDecimal.ZERO;
        BigDecimal denom2 = BigDecimal.ZERO;

        // Calculate numerator and denominators for correlation
        for (Long key : commonKeys) {
            BigDecimal val1 = vector1.get(key).subtract(mean1);
            BigDecimal val2 = vector2.get(key).subtract(mean2);

            numerator = numerator.add(val1.multiply(val2));
            denom1 = denom1.add(val1.multiply(val1));
            denom2 = denom2.add(val2.multiply(val2));
        }

        // Check for zero variance
        if (denom1.compareTo(BigDecimal.ZERO) <= 0 || denom2.compareTo(BigDecimal.ZERO) <= 0) {
            logger.debug("Zero variance in one or both vectors, returning similarity of 0.0");
            return 0.0;
        }

        // Calculate square roots of denominators
        BigDecimal sqrtDenom1 = denom1.sqrt(MathContext.DECIMAL64);
        BigDecimal sqrtDenom2 = denom2.sqrt(MathContext.DECIMAL64);

        // Calculate Pearson correlation
        BigDecimal denominator = sqrtDenom1.multiply(sqrtDenom2);
        BigDecimal correlation = numerator.divide(denominator, MathContext.DECIMAL64);
        return correlation.doubleValue();
    }
}

