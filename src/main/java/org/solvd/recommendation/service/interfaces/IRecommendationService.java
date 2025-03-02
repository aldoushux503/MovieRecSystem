package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.Movie;

import java.util.List;
import java.util.Map;

/**
 * Core service interface for generating movie recommendations.
 * Implements algorithms to suggest movies based on user preferences and behavior.
 */
public interface IRecommendationService {
    List<Movie> recommendMoviesForUser(Long userId, int limit);

    List<Movie> getCollaborativeFilteringRecommendations(Long userId, int limit);

    List<Movie> getContentBasedRecommendations(Long userId, int limit);

    List<Movie> getSimilarMovies(Long movieId, int limit);

    List<Movie> getTrendingMovies(int limit);

    Map<Long, Double> predictUserRatings(Long userId, List<Long> movieIds);
}
