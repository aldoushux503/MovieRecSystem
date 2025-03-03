package org.solvd.recommendation.algorithm;

import org.solvd.recommendation.model.Movie;

import java.util.List;
import java.util.Map;

/**
 * Base interface for recommendation algorithms.
 * Defines the contract for generating movie recommendations based on user preferences.
 */
public interface IRecommendationAlgorithm {
    /**
     * Generates movie recommendations for a specific user.
     */
    List<Movie> recommendMovies(Long userId, int limit);

    /**
     * Predicts user's potential rating for specified movies.
     */
    Map<Long, Double> predictRatings(Long userId, List<Long> movieIds);
}