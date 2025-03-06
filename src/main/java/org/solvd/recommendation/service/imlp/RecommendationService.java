package org.solvd.recommendation.service.imlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.IRecommendationAlgorithm;
import org.solvd.recommendation.algorithm.RecommendationAlgorithmFactory;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.model.ViewingHistory;
import org.solvd.recommendation.service.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the recommendation service interface.
 * Provides methods for generating movie recommendations using different algorithms.
 */

public class RecommendationService implements IRecommendationService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    private static final int DEFAULT_TRENDING_DAYS = 30;

    private final RecommendationAlgorithmFactory algorithmFactory;

    public RecommendationService() {
        this.algorithmFactory = RecommendationAlgorithmFactory.getInstance();
    }

    @Override
    public List<Movie> recommendMoviesForUser(Long userId, int limit) {
        logger.info("Generating {} recommendations for user {}", limit, userId);

        // Use hybrid recommendation by default for best results
        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.HYBRID_RECOMMENDATION);

        return algorithm.recommendMovies(userId, limit);
    }

    @Override
    public List<Movie> getCollaborativeFilteringRecommendations(Long userId, int limit) {
        logger.info("Generating {} collaborative filtering recommendations for user {}", limit, userId);

        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.COLLABORATIVE_FILTERING);

        return algorithm.recommendMovies(userId, limit);
    }

    @Override
    public List<Movie> getContentBasedRecommendations(Long userId, int limit) {
        logger.info("Generating {} content-based recommendations for user {}", limit, userId);

        IRecommendationAlgorithm algorithm = algorithmFactory.createAlgorithm(
                RecommendationAlgorithmFactory.AlgorithmType.CONTENT_BASED_FILTERING);

        return algorithm.recommendMovies(userId, limit);
    }

}