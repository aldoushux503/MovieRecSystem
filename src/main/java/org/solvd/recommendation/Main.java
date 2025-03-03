package org.solvd.recommendation;


import org.solvd.recommendation.model.*;
import org.solvd.recommendation.service.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        // Get service instances through factory (demonstrates Factory pattern)
        ServiceFactory serviceFactory = ServiceFactory.getInstance();

        // Entity services
        MovieService movieService = serviceFactory.getMovieService();
        UserService userService = serviceFactory.getUserService();
        GenreService genreService = serviceFactory.getGenreService();
        PersonService personService = serviceFactory.getPersonService();
        PersonRoleService personRoleService = serviceFactory.getPersonRoleService();

        // Relationship services
        UserRatingService userRatingService = serviceFactory.getUserRatingService();
        MovieGenreService movieGenreService = serviceFactory.getMovieGenreService();
        ViewingHistoryService viewingHistoryService = serviceFactory.getViewingHistoryService();



        System.out.println("Movie Recommendation System");
        System.out.println("==========================");

        try {
            // Create Person (CRUD - Create operation)
            Person person1 = new Person();
            person1.setFullName("John Doe");
            Long person1Id = personService.create(person1);
            System.out.println("Created person with ID: " + person1Id);

            // Create User from Person
            User user1 = new User();
            user1.setUsername("johndoe");
            user1.setEmail("john@example.com");
            userService.create(user1);
            System.out.println("Created user: " + user1.getUsername());

            // Create second user
            Person person2 = new Person();
            person2.setFullName("Jane Smith");
            Long person2Id = personService.create(person2);

            User user2 = new User();
            user2.setUserId(person2Id);
            user2.setUsername("janesmith");
            user2.setEmail("jane@example.com");
            userService.create(user2);
            System.out.println("Created user: " + user2.getUsername());

            // Create genres
            Genre actionGenre = new Genre();
            actionGenre.setName("Action");
            Long actionGenreId = genreService.create(actionGenre);

            Genre sciFiGenre = new Genre();
            sciFiGenre.setName("Science Fiction");
            Long sciFiGenreId = genreService.create(sciFiGenre);

            Genre dramaGenre = new Genre();
            dramaGenre.setName("Drama");
            Long dramaGenreId = genreService.create(dramaGenre);

            System.out.println("Created genres: Action, Science Fiction, Drama");

            // Create movies
            Movie movie1 = Movie.builder()
                    .title("The Matrix")
                    .duration(136)
                    .averageRating(new BigDecimal("8.7"))
                    .build();
            Long movie1Id = movieService.create(movie1);

            Movie movie2 = Movie.builder()
                    .title("Inception")
                    .duration(148)
                    .averageRating(new BigDecimal("8.8"))
                    .build();
            Long movie2Id = movieService.create(movie2);

            Movie movie3 = Movie.builder()
                    .title("The Shawshank Redemption")
                    .duration(142)
                    .averageRating(new BigDecimal("9.3"))
                    .build();
            Long movie3Id = movieService.create(movie3);

            System.out.println("Created movies: The Matrix, Inception, The Shawshank Redemption");

            // Associate genres with movies
            MovieGenres mg1 = new MovieGenres();
            mg1.setMovieId(movie1Id);
            mg1.setGenreId(actionGenreId);
            movieGenreService.create(mg1);

            MovieGenres mg2 = new MovieGenres();
            mg2.setMovieId(movie1Id);
            mg2.setGenreId(sciFiGenreId);
            movieGenreService.create(mg2);

            MovieGenres mg3 = new MovieGenres();
            mg3.setMovieId(movie2Id);
            mg3.setGenreId(sciFiGenreId);
            movieGenreService.create(mg3);

            MovieGenres mg4 = new MovieGenres();
            mg4.setMovieId(movie3Id);
            mg4.setGenreId(dramaGenreId);
            movieGenreService.create(mg4);

            System.out.println("Associated movies with genres");

            // Add user ratings
            UserRating ur1 = new UserRating();
            ur1.setUserId(user1.getUserId());
            ur1.setMovieId(movie1Id);
            ur1.setRatingValue(new BigDecimal("9.0"));
            userRatingService.create(ur1);

            UserRating ur2 = new UserRating();
            ur2.setUserId(user1.getUserId());
            ur2.setMovieId(movie2Id);
            ur2.setRatingValue(new BigDecimal("8.5"));
            userRatingService.create(ur2);

            UserRating ur3 = new UserRating();
            ur3.setUserId(user2.getUserId());
            ur3.setMovieId(movie1Id);
            ur3.setRatingValue(new BigDecimal("8.0"));
            userRatingService.create(ur3);

            UserRating ur4 = new UserRating();
            ur4.setUserId(user2.getUserId());
            ur4.setMovieId(movie3Id);
            ur4.setRatingValue(new BigDecimal("9.5"));
            userRatingService.create(ur4);

            System.out.println("Added user ratings");

            // Add viewing history
            ViewingHistory vh1 = new ViewingHistory();
            vh1.setUserId(user1.getUserId());
            vh1.setMovieId(movie1Id);
            vh1.setWatchDate(Timestamp.from(Instant.now().minusSeconds(86400)));
            viewingHistoryService.create(vh1);

            ViewingHistory vh2 = new ViewingHistory();
            vh2.setUserId(user2.getUserId());
            vh2.setMovieId(movie3Id);
            vh2.setWatchDate(Timestamp.from(Instant.now().minusSeconds(43200)));
            viewingHistoryService.create(vh2);

            System.out.println("Added viewing history");

            System.out.println("\n--- CRUD Operations Demo ---");

            // CRUD - Read operation
            Movie retrievedMovie = movieService.getById(movie1Id);
            System.out.println("Retrieved movie: " + retrievedMovie.getTitle() +
                    " (Duration: " + retrievedMovie.getDuration() + " min)");

            // CRUD - Update operation
            retrievedMovie.setDuration(135);
            movieService.update(retrievedMovie);
            System.out.println("Updated movie duration");

            // Verify update
            Movie updatedMovie = movieService.getById(movie1Id);
            System.out.println("Verified updated duration: " + updatedMovie.getDuration() + " min");

            // Get all movies
            System.out.println("\nAll movies in database:");
            movieService.getAll().forEach(movie ->
                    System.out.println("- " + movie.getTitle() + " (ID: " + movie.getMovieId() + ")"));

            // Get user ratings
            System.out.println("\nRatings for 'The Matrix':");
            userRatingService.getAllMovieRatings(movie1Id).forEach(rating -> {
                User ratingUser = userService.getById(rating.getUserId());
                System.out.println("- " + ratingUser.getUsername() + ": " + rating.getRatingValue());
            });


            // CRUD - Delete operation demonstration
            Genre testGenre = new Genre();
            testGenre.setName("Test Genre");
            Long testGenreId = genreService.create(testGenre);
            System.out.println("\nCreated test genre with ID: " + testGenreId);

            Genre retrievedGenre = genreService.getById(testGenreId);
            genreService.delete(retrievedGenre);
            System.out.println("Deleted test genre");

            System.out.println("\nDemo completed successfully!");

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }


}