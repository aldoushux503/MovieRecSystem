package org.solvd.recommendation.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.algorithm.similarity.ISimilarityCalculator;
import org.solvd.recommendation.algorithm.similarity.SimilarityCalculatorFactory;
import org.solvd.recommendation.algorithm.similarity.SimilarityMethod;
import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.model.UserRating;
import org.solvd.recommendation.service.IGenreService;
import org.solvd.recommendation.service.IMovieGenreService;
import org.solvd.recommendation.service.imlp.GenreService;
import org.solvd.recommendation.service.imlp.MovieGenreService;
import org.solvd.recommendation.service.ServiceFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Content-based filtering algorithm implementation.
 * This algorithm recommends items based on similarities between item attributes
 * and user preferences, particularly focusing on genre preferences.
 */
public class ContentBasedFilteringAlgorithm extends AbstractRecommendationAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(ContentBasedFilteringAlgorithm.class);
    private static final double RATING_THRESHOLD = 7.0; // Consider movies rated 7+ as liked

    private final IMovieGenreService movieGenreService;
    private final IGenreService genreService;
    private final ISimilarityCalculator similarityCalculator;

    public ContentBasedFilteringAlgorithm() {
        this(SimilarityMethod.COSINE);
    }

    public ContentBasedFilteringAlgorithm(SimilarityMethod similarityMethod) {
        super();
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        this.movieGenreService = serviceFactory.getMovieGenreService();
        this.genreService = serviceFactory.getGenreService();
        this.similarityCalculator = SimilarityCalculatorFactory.createCalculator(similarityMethod);
        logger.info("Initialized content-based filtering with {} similarity", similarityMethod);
    }

    @Override
    public Map<Long, Double> predictRatings(Long userId, List<Long> movieIds) {
        // Build user profile based on rated movies and their genres
        Map<Long, Double> userGenrePreferences = buildUserGenreProfile(userId);

        // If no preferences found, return empty result
        if (userGenrePreferences.isEmpty()) {
            logger.warn("No genre preferences found for user {}", userId);
            return Collections.emptyMap();
        }

        // Calculate predicted ratings for candidate movies
        Map<Long, Double> predictions = new HashMap<>();
        for (Long movieId : movieIds) {
            // Get movie's genre profile
            Map<Long, Double> movieGenreProfile = getMovieGenreProfile(movieId);

            // Skip movies with no genre information
            if (movieGenreProfile.isEmpty()) {
                continue;
            }

            // Convert profiles to BigDecimal for similarity calculation
            Map<Long, BigDecimal> userProfile = userGenrePreferences.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> BigDecimal.valueOf(e.getValue())
                    ));

            Map<Long, BigDecimal> movieProfile = movieGenreProfile.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> BigDecimal.valueOf(e.getValue())
                    ));

            // Calculate similarity between user preferences and movie genres
            double similarity = similarityCalculator.calculateSimilarity(userProfile, movieProfile);

            // Scale similarity to 1-10 rating range
            double predictedRating = 5.0 + (similarity * 5.0);
            // Ensure rating is within bounds
            predictedRating = Math.max(1.0, Math.min(10.0, predictedRating));

            predictions.put(movieId, predictedRating);
        }

        return predictions;
    }

    /**
     * Builds a profile of user's genre preferences based on their rated movies.
     * @return Map of genre IDs to preference weights
     */
    private Map<Long, Double> buildUserGenreProfile(Long userId) {
        // Get user's ratings
        List<UserRating> userRatings = ratingService.getAllUserRatings(userId);

        // If no ratings, return empty profile
        if (userRatings.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Long, Double> genreWeights = new HashMap<>();
        Map<Long, Integer> genreCounts = new HashMap<>();

        // Calculate weights for each genre based on user's movie ratings
        for (UserRating rating : userRatings) {
            double ratingValue = rating.getRatingValue().doubleValue();

            // Consider weight relative to rating threshold
            double weight = (ratingValue - 5.0) / 5.0;  // Normalize to [-1,1] range

            // Get movie's genres
            List<MovieGenres> movieGenres = movieGenreService.getByMovie(rating.getMovieId());

            // Update weights for each genre
            for (MovieGenres mg : movieGenres) {
                Long genreId = mg.getGenreId();
                genreWeights.put(genreId, genreWeights.getOrDefault(genreId, 0.0) + weight);
                genreCounts.put(genreId, genreCounts.getOrDefault(genreId, 0) + 1);
            }
        }

        // Normalize weights by genre occurrence count
        Map<Long, Double> normalizedWeights = new HashMap<>();
        for (Map.Entry<Long, Double> entry : genreWeights.entrySet()) {
            Long genreId = entry.getKey();
            Double weight = entry.getValue();
            int count = genreCounts.get(genreId);
            normalizedWeights.put(genreId, weight / count);
        }

        return normalizedWeights;
    }

    /**
     * Creates a genre profile for a movie.
     * @return Map of genre IDs to profile weights
     */
    private Map<Long, Double> getMovieGenreProfile(Long movieId) {
        // Get movie's genres
        List<MovieGenres> movieGenres = movieGenreService.getByMovie(movieId);

        // If no genres found, return empty profile
        if (movieGenres.isEmpty()) {
            return Collections.emptyMap();
        }

        // Create a uniform distribution of weights across genres
        double weight = 1.0 / movieGenres.size();

        return movieGenres.stream()
                .collect(Collectors.toMap(
                        MovieGenres::getGenreId,
                        mg -> weight
                ));
    }
}