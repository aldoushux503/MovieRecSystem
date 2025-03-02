package org.solvd.recommendation.algorithm;

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
    @Override
    public double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {

        Set<Long> commonKeys = new HashSet<>(vector1.keySet());
        commonKeys.retainAll(vector2.keySet());

        if (commonKeys.isEmpty()) {
            return 0.0;
        }

        int n = commonKeys.size();

        double sum1 = 0.0, sum2 = 0.0;
        for (Long key : commonKeys) {
            sum1 += vector1.get(key);
            sum2 += vector2.get(key);
        }
        double mean1 = sum1 / n;
        double mean2 = sum2 / n;

        double numerator = 0.0;
        double denom1 = 0.0;
        double denom2 = 0.0;

        for (Long key : commonKeys) {
            double val1 = vector1.get(key) - mean1;
            double val2 = vector2.get(key) - mean2;

            numerator += val1 * val2;
            denom1 += val1 * val1;
            denom2 += val2 * val2;
        }

        if (denom1 <= 0.0 || denom2 <= 0.0) {
            return 0.0;
        }

        return numerator / (Math.sqrt(denom1) * Math.sqrt(denom2));
    }
}