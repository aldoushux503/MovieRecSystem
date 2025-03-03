package org.solvd.recommendation;

import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.service.ServiceFactory;
import org.solvd.recommendation.service.interfaces.IRecommendationService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Demonstration class for the movie recommendation system.
 * Shows how to use different recommendation algorithms.
 */
public class RecommendationDemo {

    public static void main(String[] args) {
        System.out.println("Movie Recommendation System Demo");
        System.out.println("================================");

        // First run the Main class to initialize the database with sample data
        System.out.println("Initializing database with sample data...");
        Main.main(args);

        // Get the recommendation service
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        IRecommendationService recommendationService = serviceFactory.getRecommendationService();

        // User IDs from sample data in Main class
        Long user1Id = 1L; // John Doe
        Long user2Id = 2L; // Jane Smith

        // Demonstrate different recommendation approaches
        System.out.println("\n--- Recommendation Demo ---");

        // Get recommendations using default hybrid approach
        System.out.println("\nHybrid Recommendations for John Doe:");
        demonstrateRecommendations(recommendationService.recommendMoviesForUser(user1Id, 5));

        System.out.println("\nHybrid Recommendations for Jane Smith:");
        demonstrateRecommendations(recommendationService.recommendMoviesForUser(user2Id, 5));

        // Get collaborative filtering recommendations
        System.out.println("\nCollaborative Filtering Recommendations for John Doe:");
        demonstrateRecommendations(recommendationService.getCollaborativeFilteringRecommendations(user1Id, 5));

        // Get content-based recommendations
        System.out.println("\nContent-Based Recommendations for John Doe:");
        demonstrateRecommendations(recommendationService.getContentBasedRecommendations(user1Id, 5));

        // Get similar movies
        Long matrixMovieId = 1L; // The Matrix movie ID from sample data
        System.out.println("\nMovies Similar to 'The Matrix':");
        demonstrateRecommendations(recommendationService.getSimilarMovies(matrixMovieId, 5));

        // Get trending movies
        System.out.println("\nTrending Movies:");
        demonstrateRecommendations(recommendationService.getTrendingMovies(5));

        // Predict user ratings
        List<Long> movieIds = List.of(1L, 2L, 3L); // Sample movie IDs
        System.out.println("\nPredicted Ratings for John Doe:");
        Map<Long, Double> predictedRatings = recommendationService.predictUserRatings(user1Id, movieIds);
        for (Map.Entry<Long, Double> entry : predictedRatings.entrySet()) {
            Movie movie = serviceFactory.getMovieService().getById(entry.getKey());
            System.out.printf("- %s: %.1f/10.0\n", movie.getTitle(), entry.getValue());
        }

        System.out.println("\nRecommendation Demo completed successfully!");
    }

    /**
     * Helper method to print movie recommendations.
     */
    private static void demonstrateRecommendations(List<Movie> recommendations) {
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available.");
            return;
        }

        for (Movie movie : recommendations) {
            System.out.printf("- %s (Rating: %.1f/10.0)\n",
                    movie.getTitle(),
                    movie.getAverageRating().doubleValue());
        }
    }
}