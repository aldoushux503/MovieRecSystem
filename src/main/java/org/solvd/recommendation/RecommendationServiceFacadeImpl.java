package org.solvd.recommendation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.*;
import org.solvd.recommendation.service.*;

import java.util.*;

/**
 * Implementation of the RecommendationServiceFacade.
 * Provides a simplified interface to the various recommendation services.
 */
public class RecommendationServiceFacadeImpl implements IRecommendationServiceFacade {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationServiceFacadeImpl.class);

    private final IRecommendationService recommendationService;
    private final IUserService userService;
    private final IMovieService movieService;

    /**
     * Constructor with service factory injection.
     */
    public RecommendationServiceFacadeImpl(ServiceFactory serviceFactory) {
        this.recommendationService = serviceFactory.getRecommendationService();
        this.userService = serviceFactory.getUserService();
        this.movieService = serviceFactory.getMovieService();
    }

    @Override
    public List<Movie> getHybridRecommendations(String username, int limit) {
        Long userId = getUserId(username);
        if (userId == null) {
            logger.warn("User not found: {}", username);
            return Collections.emptyList();
        }

        logger.info("Getting hybrid recommendations for user {}", username);
        List<Movie> recommendations = recommendationService.recommendMoviesForUser(userId, limit);
        logger.info("Found {} hybrid recommendations for user {}", recommendations.size(), username);
        return recommendations;
    }

    @Override
    public List<Movie> getCollaborativeRecommendations(String username, int limit) {
        Long userId = getUserId(username);
        if (userId == null) {
            logger.warn("User not found: {}", username);
            return Collections.emptyList();
        }

        logger.info("Getting collaborative recommendations for user {}", username);
        List<Movie> recommendations = recommendationService.getCollaborativeFilteringRecommendations(userId, limit);
        logger.info("Found {} collaborative recommendations for user {}", recommendations.size(), username);
        return recommendations;
    }

    @Override
    public List<Movie> getContentBasedRecommendations(String username, int limit) {
        Long userId = getUserId(username);
        if (userId == null) {
            logger.warn("User not found: {}", username);
            return Collections.emptyList();
        }

        logger.info("Getting content-based recommendations for user {}", username);
        List<Movie> recommendations = recommendationService.getContentBasedRecommendations(userId, limit);
        logger.info("Found {} content-based recommendations for user {}", recommendations.size(), username);
        return recommendations;
    }

    /**
     * Get user ID from username.
     */
    private Long getUserId(String username) {
        // Direct search in the database
        List<User> users = userService.getAll().stream()
                .filter(u -> username.equals(u.getUsername()))
                .toList();

        if (!users.isEmpty()) {
            Long userId = users.get(0).getUserId();
            logger.debug("Found user ID {} for username: {}", userId, username);
            return userId;
        }

        logger.warn("User not found: {}", username);
        return null;
    }
}