package org.solvd.recommendation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.*;
import org.solvd.recommendation.service.*;
import org.solvd.recommendation.service.facade.IRecommendationServiceFacade;
import org.solvd.recommendation.service.facade.RecommendationServiceFacadeImpl;

import java.util.*;
import java.util.function.Consumer;

/**
 * Main class for the Movie Recommendation System.
 *
 * This system analyzes user behavior and preferences to develop personalized
 * movie recommendations that balance suggesting familiar content with
 * discovering new, engaging titles.
 *
 * It combines collaborative filtering (based on user similarities) and
 * content-based filtering (based on movie attributes) to predict what
 * a user is most likely to enjoy watching next.
 */
public class MovieRecommendation {
    private static final Logger logger = LoggerFactory.getLogger(MovieRecommendation.class);

    // Service facade for recommendation operations
    private final IRecommendationServiceFacade recommendationFacade;

    // Database initialization component
    private final DatabaseInitializer databaseInitializer;

    /**
     * Constructor with dependency injection.
     *
     * @param recommendationFacade The facade for recommendation services
     * @param databaseInitializer The initializer for sample data
     */
    public MovieRecommendation(
            IRecommendationServiceFacade recommendationFacade,
            DatabaseInitializer databaseInitializer) {
        this.recommendationFacade = recommendationFacade;
        this.databaseInitializer = databaseInitializer;
    }

    /**
     * Main entry point for the application.
     */
    public static void main(String[] args) {
        try {
            // Create service factories and facades using the Builder pattern
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            IRecommendationServiceFacade recommendationFacade = new RecommendationServiceFacadeImpl(serviceFactory);
            DatabaseInitializer databaseInitializer = new DatabaseInitializer(serviceFactory);

            // Create main application with dependencies injected
            MovieRecommendation recommendation = new MovieRecommendation(
                    recommendationFacade, databaseInitializer);

            // Execute the demo
            recommendation.execute();
        } catch (Exception e) {
            logger.error("Error during execution", e);
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Execute the complete recommendation system demonstration.
     */
    public void execute() {
        printHeader("MOVIE RECOMMENDATION SYSTEM");

        // Initialize database with sample data
        logger.info("Initializing database with sample data");
        System.out.println("Initializing database with sample data...");
        databaseInitializer.initializeDatabase();

        // Demonstrate recommendation algorithms
        printHeader("PERSONALIZED RECOMMENDATION ALGORITHMS");
        demonstrateRecommendationAlgorithms();

        logger.info("Recommendation demonstration completed successfully");
        System.out.println("\nRecommendation system operation completed successfully!");
    }

    /**
     * Demonstrate the core recommendation algorithms.
     */
    private void demonstrateRecommendationAlgorithms() {
        demonstrateHybridRecommendations();
        demonstrateCollaborativeFiltering();
        demonstrateContentBasedFiltering();
    }

    /**
     * Demonstrate hybrid recommendations which balance different approaches.
     */
    private void demonstrateHybridRecommendations() {
        printSectionHeader("HYBRID RECOMMENDATIONS",
                "Combines collaborative and content-based approaches for balanced recommendations.");

        executeForMultipleUsers(
                Arrays.asList("johndoe", "janesmith", "michaelmiller"),
                username -> {
                    String description = getUserDescription(username);
                    System.out.println("\nPersonalized Recommendations for " + username + " " + description + ":");
                    recommendationFacade.getHybridRecommendations(username, 5)
                            .forEach(this::printRecommendation);
                }
        );
    }

    /**
     * Demonstrate collaborative filtering which finds similar users.
     */
    private void demonstrateCollaborativeFiltering() {
        printSectionHeader("COLLABORATIVE FILTERING",
                "Finds users with similar tastes and recommends what they enjoyed.");

        executeForMultipleUsers(
                Arrays.asList("johndoe", "janesmith"),
                username -> {
                    System.out.println("\nSocial Recommendations for " + username + ":");
                    recommendationFacade.getCollaborativeRecommendations(username, 5)
                            .forEach(this::printRecommendation);
                }
        );
    }

    /**
     * Demonstrate content-based filtering which analyzes movie attributes.
     */
    private void demonstrateContentBasedFiltering() {
        printSectionHeader("CONTENT-BASED FILTERING",
                "Analyzes attributes of movies you've enjoyed to find similar content.");

        executeForMultipleUsers(
                Arrays.asList("johndoe", "emmadavis"),
                username -> {
                    String description = username.equals("emmadavis") ? " (Horror Fan)" : "";
                    System.out.println("\nGenre-Based Recommendations for " + username + description + ":");
                    recommendationFacade.getContentBasedRecommendations(username, 5)
                            .forEach(this::printRecommendation);
                }
        );
    }

    /**
     * Execute an operation for multiple users.
     */
    private void executeForMultipleUsers(List<String> usernames, Consumer<String> operation) {
        usernames.forEach(operation);
    }

    /**
     * Get a description for a user based on their username.
     */
    private String getUserDescription(String username) {
        switch (username) {
            case "johndoe": return "(Sci-Fi & Action Fan)";
            case "janesmith": return "(Drama & Romance Fan)";
            case "michaelmiller": return "(Sci-Fi Enthusiast)";
            case "emmadavis": return "(Sci-Fi Fan)";
            default: return "";
        }
    }

    /**
     * Print a movie recommendation in a formatted way.
     */
    private void printRecommendation(Movie movie) {
        System.out.printf("   • %s (Rating: %.1f/10) - %d min\n",
                movie.getTitle(),
                movie.getAverageRating().doubleValue(),
                movie.getDuration());
    }

    /**
     * Print a major section header.
     */
    private void printHeader(String title) {
        System.out.println("\n===================================================");
        System.out.println("       " + title);
        System.out.println("===================================================\n");
    }

    /**
     * Print a subsection header with description.
     */
    private void printSectionHeader(String title, String description) {
        System.out.println("\n--- " + title + " ---");
        System.out.println(description);
    }

}