package org.solvd.recommendation;

import org.solvd.recommendation.model.Movie;

import java.util.List;

/**
 * Facade interface for recommendation services.
 * Simplifies interaction with the recommendation system.
 */
public interface IRecommendationServiceFacade {
    List<Movie> getHybridRecommendations(String username, int limit);
    List<Movie> getCollaborativeRecommendations(String username, int limit);
    List<Movie> getContentBasedRecommendations(String username, int limit);

}