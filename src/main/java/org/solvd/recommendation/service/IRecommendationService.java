package org.solvd.recommendation.service;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.service.imlp.RecommendationService;

import java.util.List;
import java.util.Map;

public interface IRecommendationService {
    List<Movie> recommendMoviesForUser(Long userId, int limit);

    List<Movie> getCollaborativeFilteringRecommendations(Long userId, int limit);

    List<Movie> getContentBasedRecommendations(Long userId, int limit);

    List<Movie> getSimilarMovies(Long movieId, int limit);

    List<Movie> getTrendingMovies(int limit);

    Map<Long, Double> predictUserRatings(Long userId, List<Long> movieIds);
}
