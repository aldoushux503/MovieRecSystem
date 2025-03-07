package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.similarity.ISimilarityCalculator;
import org.solvd.recommendation.algorithm.similarity.SimilarityCalculatorFactory;
import org.solvd.recommendation.algorithm.similarity.SimilarityMethod;
import org.solvd.recommendation.model.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Content-based filtering algorithm implementation enhanced with user interaction support.
 */
public class ContentBasedFilteringAlgorithm extends AbstractRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(ContentBasedFilteringAlgorithm.class);
    private static final double RATING_THRESHOLD = 5.0;

    private final ISimilarityCalculator similarityCalculator;

    public ContentBasedFilteringAlgorithm() {
        this(SimilarityMethod.COSINE);
    }

    public ContentBasedFilteringAlgorithm(SimilarityMethod similarityMethod) {
        super();
        this.similarityCalculator = SimilarityCalculatorFactory.createCalculator(similarityMethod);
        logger.info("Initialized content-based filtering with {} similarity", similarityMethod);
    }

    @Override
    public Map<Long, Double> predictRatings(Long userId, List<Long> movieIds) {
        Map<Long, Double> userGenrePreferences = buildUserGenreProfile(userId);

        if (userGenrePreferences.isEmpty()) {
            logger.warn("No genre preferences found for user {}", userId);
            return Collections.emptyMap();
        }

        Map<Long, Double> predictions = new HashMap<>();
        for (Long movieId : movieIds) {
            Map<Long, Double> movieGenreProfile = getMovieGenreProfile(movieId);

            if (movieGenreProfile.isEmpty()) continue;

            double similarity = similarityCalculator.calculateSimilarity(
                    userGenrePreferences, movieGenreProfile);

            // Scale similarity to 1-10 rating range and ensure it's within bounds
            double predictedRating = Math.max(1.0, Math.min(10.0, 5.0 + (similarity * 5.0)));
            predictions.put(movieId, predictedRating);
        }

        return predictions;
    }

    private Map<Long, Double> buildUserGenreProfile(Long userId) {
        Map<Long, Double> genreWeights = new HashMap<>();
        Map<Long, Integer> genreCounts = new HashMap<>();

        // Process ratings
        processUserPreferences(
                userId,
                ratingService.getAllUserRatings(userId),
                UserRating::getMovieId,
                rating -> (rating.getRatingValue().doubleValue() - RATING_THRESHOLD) / 5.0,
                genreWeights,
                genreCounts
        );

        // Process interactions
        processUserPreferences(
                userId,
                userInteractionService.getByUser(userId),
                UserInteraction::getMovieId,
                interaction -> getInteractionWeight(interaction.getInteractionsId()),
                genreWeights,
                genreCounts
        );

        // Normalize weights
        return genreWeights.entrySet().stream()
                .filter(e -> genreCounts.containsKey(e.getKey()) && genreCounts.get(e.getKey()) > 0)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() / genreCounts.get(e.getKey())
                ));
    }

    /**
     * Generic method to process user preferences (either ratings or interactions)
     */
    private <T> void processUserPreferences(
            Long userId,
            List<T> items,
            Function<T, Long> movieIdExtractor,
            Function<T, Double> weightCalculator,
            Map<Long, Double> genreWeights,
            Map<Long, Integer> genreCounts) {

        if (items.isEmpty()) return;

        for (T item : items) {
            double weight = weightCalculator.apply(item);
            if (weight == 0.0) continue;

            Long movieId = movieIdExtractor.apply(item);
            for (MovieGenres mg : movieGenreService.getByMovie(movieId)) {
                Long genreId = mg.getGenreId();
                genreWeights.merge(genreId, weight, Double::sum);
                genreCounts.merge(genreId, 1, Integer::sum);
            }
        }
    }

    /**
     * Gets the preference weight for a given interaction ID.
     */
    private double getInteractionWeight(Long interactionId) {
        if (interactionId == null) return 0.0;

        // Get interaction from service
        Interaction interaction = interactionService.getById(interactionId);
        if (interaction == null) return 0.0;

        // Return weight based on interaction type
        String typeStr = String.valueOf(interaction.getType());
        try {
            InteractionType type = InteractionType.valueOf(typeStr);

            return switch (type) {
                case LIKE -> LIKE_WEIGHT;
                case FAVORITE -> FAVORITE_WEIGHT;
                case DISLIKE -> DISLIKE_WEIGHT;
                case WATCH -> WATCH_WEIGHT;
                default -> {
                    logger.warn("Unhandled interaction type: {}", type);
                    yield 0.0;
                }
            };
        } catch (IllegalArgumentException e) {
            logger.warn("Unknown interaction type: {}", typeStr);
            return 0.0;
        }
    }


    private Map<Long, Double> getMovieGenreProfile(Long movieId) {
        List<MovieGenres> movieGenres = movieGenreService.getByMovie(movieId);

        if (movieGenres.isEmpty()) return Collections.emptyMap();

        double weight = 1.0 / movieGenres.size();
        return movieGenres.stream()
                .collect(Collectors.toMap(
                        MovieGenres::getGenreId,
                        mg -> weight,
                        (v1, v2) -> v1
                ));
    }
}