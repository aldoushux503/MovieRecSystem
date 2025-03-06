package org.solvd.recommendation.service;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.service.imlp.RecommendationService;

import java.util.List;
import java.util.Map;

public interface IRecommendationService {
    List<Movie> recommendMoviesForUser(Long userId, int limit);

    List<Movie> getCollaborativeFilteringRecommendations(Long userId, int limit);

    List<Movie> getContentBasedRecommendations(Long userId, int limit);

}
