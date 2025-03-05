package org.solvd.recommendation.algorithm.similarity;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Defines a strategy for calculating similarity between two vectors represented as maps.
 * This interface enables pluggable similarity calculation methods to support flexible recommendation algorithms.
 */
public interface ISimilarityCalculator {
    double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2);
}