package org.solvd.recommendation.algorithm;

import java.util.Collections;
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
public class CosineSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
        if (vector1.isEmpty() || vector2.isEmpty()) {
            return 0.0;
        }

        Set<Long> allKeys = new HashSet<>(vector1.keySet());
        allKeys.addAll(vector2.keySet());

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (Long key : allKeys) {
            double val1 = vector1.getOrDefault(key, 0.0);
            double val2 = vector2.getOrDefault(key, 0.0);

            dotProduct += val1 * val2;
            magnitude1 += val1 * val1;
            magnitude2 += val2 * val2;
        }

        magnitude1 = Math.sqrt(magnitude1);
        magnitude2 = Math.sqrt(magnitude2);

        if (magnitude1 <= 0.0 || magnitude2 <= 0.0) {
            return 0.0;
        }

        return dotProduct / (magnitude1 * magnitude2);
    }
}