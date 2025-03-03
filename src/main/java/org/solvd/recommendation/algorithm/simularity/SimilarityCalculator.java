package org.solvd.recommendation.algorithm.simularity;

import java.math.BigDecimal;
import java.util.Map;


/**
 * Defines a strategy for calculating similarity between two vectors represented as maps.
 * This interface enables pluggable similarity calculation methods to support flexible recommendation algorithms.
 */
public interface SimilarityCalculator {
    double calculateSimilarity(Map<Long, BigDecimal> vector1, Map<Long, BigDecimal> vector2);
}