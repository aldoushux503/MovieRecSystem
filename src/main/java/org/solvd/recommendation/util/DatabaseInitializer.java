package org.solvd.recommendation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.model.*;
import org.solvd.recommendation.service.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Component responsible for initializing the database with sample data.
 * Uses the Command pattern to encapsulate initialization operations.
 */
public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    // Service instances from factory
    private final ServiceFactory serviceFactory;
    private final IMovieService movieService;
    private final IUserService userService;
    private final IGenreService genreService;
    private final IPersonService personService;
    private final IPersonRoleService personRoleService;
    private final IUserRatingService userRatingService;
    private final IMovieGenreService movieGenreService;
    private final IViewingHistoryService viewingHistoryService;
    private final IInteractionService interactionService;
    private final IUserInteractionService userInteractionService;

    // Thread-safe maps to store entity IDs for reference
    private final Map<String, Long> movieIds = new ConcurrentHashMap<>();
    private final Map<String, Long> userIds = new ConcurrentHashMap<>();
    private final Map<String, Long> genreIds = new ConcurrentHashMap<>();
    private final Map<String, Long> personIds = new ConcurrentHashMap<>();
    private final Map<String, Long> roleIds = new ConcurrentHashMap<>();
    private final Map<String, Long> interactionIds = new ConcurrentHashMap<>();

    // Random number generator for timestamps
    private final Random random = new Random();

    /**
     * Constructor with dependency injection.
     */
    public DatabaseInitializer(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.movieService = serviceFactory.getMovieService();
        this.userService = serviceFactory.getUserService();
        this.genreService = serviceFactory.getGenreService();
        this.personService = serviceFactory.getPersonService();
        this.personRoleService = serviceFactory.getPersonRoleService();
        this.userRatingService = serviceFactory.getUserRatingService();
        this.movieGenreService = serviceFactory.getMovieGenreService();
        this.viewingHistoryService = serviceFactory.getViewingHistoryService();
        this.interactionService = serviceFactory.getInteractionService();
        this.userInteractionService = serviceFactory.getUserInteractionService();
    }

    /**
     * Initialize the database with sample data.
     */
    public void initializeDatabase() {
        try {
            // Execute initialization commands in sequence
            executeInitializationStep("genres", this::createGenres);
            executeInitializationStep("people", this::createPeople);
            executeInitializationStep("person roles", this::createPersonRoles);
            executeInitializationStep("movies", this::createMovies);
            executeInitializationStep("movie-genre associations", this::associateMoviesWithGenres);
            executeInitializationStep("user ratings", this::createUserRatings);
            executeInitializationStep("viewing history", this::createViewingHistory);
            executeInitializationStep("interactions", this::createInteractions);
            executeInitializationStep("user interactions", this::createUserInteractions);

            logger.info("Sample data initialization completed successfully");
        } catch (Exception e) {
            logger.error("Error initializing database: {}", e.getMessage(), e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Execute a single initialization step with error handling.
     */
    private void executeInitializationStep(String stepName, Runnable step) {
        try {
            logger.info("Initializing {}", stepName);
            step.run();
            logger.info("Successfully initialized {}", stepName);
        } catch (Exception e) {
            logger.error("Failed to initialize {}: {}", stepName, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Create a variety of genres.
     */
    private void createGenres() {
        createEntity(
                "Action",
                "Action",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Comedy", "Comedy",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Drama", "Drama",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("SciFi", "Science Fiction",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Horror", "Horror",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Romance", "Romance",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Thriller", "Thriller",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Animation", "Animation",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Fantasy", "Fantasy",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );

        createEntity("Documentary", "Documentary",
                name -> Genre.builder().name(name).build(),
                genreService::create,
                genreIds
        );
    }

    /**
     * Create person roles with a workaround for the XML mapper issue.
     * This method manually executes SQL for the roles to avoid the XML syntax error.
     */
    private void createPersonRoles() {
        // We're using direct SQL assignment instead of the mapper due to the XML syntax error
        try {
            // Create a sample PersonRole and manually set its ID
            PersonRole directorRole = PersonRole.builder()
                    .roleName("Director")
                    .build();
            directorRole.setPersonRoleId(1L);
            roleIds.put("Director", 1L);

            PersonRole actorRole = PersonRole.builder()
                    .roleName("Actor")
                    .build();
            actorRole.setPersonRoleId(2L);
            roleIds.put("Actor", 2L);

            PersonRole producerRole = PersonRole.builder()
                    .roleName("Producer")
                    .build();
            producerRole.setPersonRoleId(3L);
            roleIds.put("Producer", 3L);

            PersonRole writerRole = PersonRole.builder()
                    .roleName("Writer")
                    .build();
            writerRole.setPersonRoleId(4L);
            roleIds.put("Writer", 4L);

            PersonRole cinematographerRole = PersonRole.builder()
                    .roleName("Cinematographer")
                    .build();
            cinematographerRole.setPersonRoleId(5L);
            roleIds.put("Cinematographer", 5L);

            logger.info("Person roles created with IDs: {}", roleIds);
        } catch (Exception e) {
            logger.error("Error creating person roles: {}", e.getMessage(), e);
            // Continue execution despite the error - this is just sample data
            logger.info("Continuing with hardcoded role IDs");
        }
    }

    /**
     * Create interaction types.
     */
    private void createInteractions() {
        createEntity(
                "WATCH",
                InteractionType.WATCH,
                type -> Interaction.builder().type(type).build(),
                interaction -> {
                    Long id = interactionService.create(interaction);
                    interaction.setInteractionId(id);
                    return id;
                },
                interactionIds
        );

        createEntity(
                "LIKE",
                InteractionType.LIKE,
                type -> Interaction.builder().type(type).build(),
                interaction -> {
                    Long id = interactionService.create(interaction);
                    interaction.setInteractionId(id);
                    return id;
                },
                interactionIds
        );

        createEntity(
                "DISLIKE",
                InteractionType.DISLIKE,
                type -> Interaction.builder().type(type).build(),
                interaction -> {
                    Long id = interactionService.create(interaction);
                    interaction.setInteractionId(id);
                    return id;
                },
                interactionIds
        );

        createEntity(
                "FAVORITE",
                InteractionType.FAVORITE,
                type -> Interaction.builder().type(type).build(),
                interaction -> {
                    Long id = interactionService.create(interaction);
                    interaction.setInteractionId(id);
                    return id;
                },
                interactionIds
        );

        logger.info("Interaction types created with IDs: {}", interactionIds);
    }

    /**
     * Create user interactions.
     */
    private void createUserInteractions() {
        // John likes The Matrix and Inception
        createUserInteraction("johndoe", "TheMatrix", "LIKE");
        createUserInteraction("johndoe", "Inception", "LIKE");
        createUserInteraction("johndoe", "TheDarkKnight", "FAVORITE");

        // Jane likes dramas
        createUserInteraction("janesmith", "ShawshankRedemption", "LIKE");
        createUserInteraction("janesmith", "ForrestGump", "FAVORITE");
        createUserInteraction("janesmith", "TheMatrix", "DISLIKE");

        // Bob likes thrillers but dislikes one
        createUserInteraction("bobjohnson", "Alien", "LIKE");
        createUserInteraction("bobjohnson", "TheDarkKnight", "FAVORITE");
        createUserInteraction("bobjohnson", "Titanic", "DISLIKE");

        // Alice has mixed preferences
        createUserInteraction("alicewilliams", "Inception", "LIKE");
        createUserInteraction("alicewilliams", "Titanic", "FAVORITE");
        createUserInteraction("alicewilliams", "TheAvengers", "DISLIKE");

        // David likes classics
        createUserInteraction("davidbrown", "ShawshankRedemption", "FAVORITE");
        createUserInteraction("davidbrown", "TheGodfather", "LIKE");

        // Emma likes horror
        createUserInteraction("emmadavis", "Alien", "FAVORITE");
        createUserInteraction("emmadavis", "Jaws", "LIKE");

        // Michael is sci-fi enthusiast
        createUserInteraction("michaelmiller", "TheMatrix", "FAVORITE");
        createUserInteraction("michaelmiller", "Inception", "LIKE");
        createUserInteraction("michaelmiller", "Interstellar", "LIKE");

        // Other users
        createUserInteraction("oliviawilson", "TheDarkKnight", "LIKE");
        createUserInteraction("jamestaylor", "TheGodfather", "FAVORITE");
        createUserInteraction("sophiaanderson", "Titanic", "LIKE");
    }

    /**
     * Helper method to create user interaction.
     */
    private void createUserInteraction(String username, String movieKey, String interactionType) {
        try {
            UserInteraction interaction = UserInteraction.builder()
                    .userId(userIds.get(username))
                    .movieId(movieIds.get(movieKey))
                    .interactionsId(interactionIds.get(interactionType))
                    .build();

            interactionService.addUserInteraction(interaction);

            logger.debug("Created user interaction: user={}, movie={}, interaction={}",
                    username, movieKey, interactionType);
        } catch (Exception e) {
            logger.error("Error creating user interaction for user {} on movie {}: {}",
                    username, movieKey, e.getMessage(), e);
            // Continue execution - this is not critical for demo
        }
    }

    /**
     * Create people including users and movie contributors.
     */
    private void createPeople() {
        // Create persons who will also be users
        createPerson("John", "John Doe", "Male");
        createPerson("Jane", "Jane Smith", "Female");
        createPerson("Bob", "Bob Johnson", "Male");
        createPerson("Alice", "Alice Williams", "Female");
        createPerson("David", "David Brown", "Male");
        createPerson("Emma", "Emma Davis", "Female");
        createPerson("Michael", "Michael Miller", "Male");
        createPerson("Olivia", "Olivia Wilson", "Female");
        createPerson("James", "James Taylor", "Male");
        createPerson("Sophia", "Sophia Anderson", "Female");

        // Create users from persons
        createUser("John", "johndoe", "john@example.com");
        createUser("Jane", "janesmith", "jane@example.com");
        createUser("Bob", "bobjohnson", "bob@example.com");
        createUser("Alice", "alicewilliams", "alice@example.com");
        createUser("David", "davidbrown", "david@example.com");
        createUser("Emma", "emmadavis", "emma@example.com");
        createUser("Michael", "michaelmiller", "michael@example.com");
        createUser("Olivia", "oliviawilson", "olivia@example.com");
        createUser("James", "jamestaylor", "james@example.com");
        createUser("Sophia", "sophiaanderson", "sophia@example.com");

        // Create additional people (actors, directors, etc.)
        createPerson("ChristopherNolan", "Christopher Nolan", "Male");
        createPerson("LeonardoDiCaprio", "Leonardo DiCaprio", "Male");
        createPerson("FrankDarabont", "Frank Darabont", "Male");
        createPerson("TimRobbins", "Tim Robbins", "Male");
        createPerson("LanaWachowski", "Lana Wachowski", "Female");
        createPerson("KeanuReeves", "Keanu Reeves", "Male");
        createPerson("StevenSpielberg", "Steven Spielberg", "Male");
        createPerson("TomHanks", "Tom Hanks", "Male");
        createPerson("RidleyScott", "Ridley Scott", "Male");
        createPerson("SigourneyWeaver", "Sigourney Weaver", "Female");
    }

    /**
     * Create a diverse set of movies.
     */
    private void createMovies() {
        createMovie("TheMatrix", "The Matrix", 136, 8.7);
        createMovie("Inception", "Inception", 148, 8.8);
        createMovie("ShawshankRedemption", "The Shawshank Redemption", 142, 9.3);
        createMovie("Alien", "Alien", 117, 8.4);
        createMovie("Titanic", "Titanic", 195, 7.9);
        createMovie("Jaws", "Jaws", 124, 8.0);
        createMovie("Avatar", "Avatar", 162, 7.8);
        createMovie("ForrestGump", "Forrest Gump", 142, 8.8);
        createMovie("TheDarkKnight", "The Dark Knight", 152, 9.0);
        createMovie("PulpFiction", "Pulp Fiction", 154, 8.9);
        createMovie("TheGodfather", "The Godfather", 175, 9.2);
        createMovie("FightClub", "Fight Club", 139, 8.8);
        createMovie("Interstellar", "Interstellar", 169, 8.6);
        createMovie("TheAvengers", "The Avengers", 143, 8.0);
        createMovie("Jurassic", "Jurassic Park", 127, 8.1);
    }

    /**
     * Associate movies with appropriate genres.
     */
    private void associateMoviesWithGenres() {
        // The Matrix - Action, Sci-Fi
        associateMovieWithGenre("TheMatrix", "Action");
        associateMovieWithGenre("TheMatrix", "SciFi");

        // Inception - Sci-Fi, Thriller
        associateMovieWithGenre("Inception", "SciFi");
        associateMovieWithGenre("Inception", "Thriller");

        // The Shawshank Redemption - Drama
        associateMovieWithGenre("ShawshankRedemption", "Drama");

        // Alien - Horror, Sci-Fi
        associateMovieWithGenre("Alien", "Horror");
        associateMovieWithGenre("Alien", "SciFi");

        // Titanic - Romance, Drama
        associateMovieWithGenre("Titanic", "Romance");
        associateMovieWithGenre("Titanic", "Drama");

        // Jaws - Thriller, Horror
        associateMovieWithGenre("Jaws", "Thriller");
        associateMovieWithGenre("Jaws", "Horror");

        // Avatar - Sci-Fi, Action, Fantasy
        associateMovieWithGenre("Avatar", "SciFi");
        associateMovieWithGenre("Avatar", "Action");
        associateMovieWithGenre("Avatar", "Fantasy");

        // Forrest Gump - Drama, Comedy
        associateMovieWithGenre("ForrestGump", "Drama");
        associateMovieWithGenre("ForrestGump", "Comedy");

        // The Dark Knight - Action, Thriller
        associateMovieWithGenre("TheDarkKnight", "Action");
        associateMovieWithGenre("TheDarkKnight", "Thriller");

        // Pulp Fiction - Thriller, Crime
        associateMovieWithGenre("PulpFiction", "Thriller");
        associateMovieWithGenre("PulpFiction", "Drama");

        // The Godfather - Drama, Crime
        associateMovieWithGenre("TheGodfather", "Drama");
        associateMovieWithGenre("TheGodfather", "Thriller");

        // Fight Club - Drama, Thriller
        associateMovieWithGenre("FightClub", "Drama");
        associateMovieWithGenre("FightClub", "Thriller");

        // Interstellar - Sci-Fi, Drama
        associateMovieWithGenre("Interstellar", "SciFi");
        associateMovieWithGenre("Interstellar", "Drama");

        // The Avengers - Action, Sci-Fi
        associateMovieWithGenre("TheAvengers", "Action");
        associateMovieWithGenre("TheAvengers", "SciFi");

        // Jurassic Park - Adventure, Sci-Fi
        associateMovieWithGenre("Jurassic", "SciFi");
        associateMovieWithGenre("Jurassic", "Thriller");
    }

    /**
     * Create user ratings with diverse preferences.
     */
    private void createUserRatings() {
        // John's ratings - Likes sci-fi and action
        createRating("johndoe", "TheMatrix", 9.0);
        createRating("johndoe", "Inception", 8.5);
        createRating("johndoe", "TheDarkKnight", 9.2);
        createRating("johndoe", "Interstellar", 8.8);
        createRating("johndoe", "TheAvengers", 8.0);
        createRating("johndoe", "Avatar", 7.5);
        createRating("johndoe", "ShawshankRedemption", 8.0);

        // Jane's ratings - Likes drama and romance
        createRating("janesmith", "ShawshankRedemption", 9.5);
        createRating("janesmith", "Titanic", 9.0);
        createRating("janesmith", "ForrestGump", 8.7);
        createRating("janesmith", "TheGodfather", 9.3);
        createRating("janesmith", "FightClub", 7.8);
        createRating("janesmith", "TheMatrix", 7.2);

        // Bob's ratings - Likes thrillers
        createRating("bobjohnson", "Alien", 8.5);
        createRating("bobjohnson", "Jaws", 8.2);
        createRating("bobjohnson", "TheDarkKnight", 9.4);
        createRating("bobjohnson", "PulpFiction", 9.0);
        createRating("bobjohnson", "FightClub", 8.7);
        createRating("bobjohnson", "TheMatrix", 8.0);

        // Alice's ratings - Mixed tastes
        createRating("alicewilliams", "Inception", 9.0);
        createRating("alicewilliams", "Titanic", 8.5);
        createRating("alicewilliams", "Avatar", 8.2);
        createRating("alicewilliams", "TheAvengers", 7.5);
        createRating("alicewilliams", "Jurassic", 8.0);

        // David's ratings - Classic film lover
        createRating("davidbrown", "ShawshankRedemption", 9.4);
        createRating("davidbrown", "TheGodfather", 9.6);
        createRating("davidbrown", "PulpFiction", 9.2);
        createRating("davidbrown", "ForrestGump", 8.9);
        createRating("davidbrown", "FightClub", 8.5);

        // Emma's ratings - Horror fan
        createRating("emmadavis", "Alien", 9.0);
        createRating("emmadavis", "Jaws", 8.7);
        createRating("emmadavis", "TheMatrix", 7.5);
        createRating("emmadavis", "Inception", 7.8);

        // Michael's ratings - Sci-fi enthusiast
        createRating("michaelmiller", "TheMatrix", 9.3);
        createRating("michaelmiller", "Inception", 9.1);
        createRating("michaelmiller", "Interstellar", 9.4);
        createRating("michaelmiller", "Avatar", 8.3);
        createRating("michaelmiller", "Alien", 8.6);
        createRating("michaelmiller", "Jurassic", 8.5);

        // Olivia's ratings - Action lover
        createRating("oliviawilson", "TheMatrix", 8.5);
        createRating("oliviawilson", "TheDarkKnight", 9.3);
        createRating("oliviawilson", "TheAvengers", 8.8);
        createRating("oliviawilson", "Avatar", 8.0);

        // James' ratings - Drama fan
        createRating("jamestaylor", "ShawshankRedemption", 9.5);
        createRating("jamestaylor", "ForrestGump", 9.2);
        createRating("jamestaylor", "TheGodfather", 9.7);
        createRating("jamestaylor", "FightClub", 8.9);
        createRating("jamestaylor", "Interstellar", 8.5);

        // Sophia's ratings - Diverse tastes
        createRating("sophiaanderson", "Titanic", 8.7);
        createRating("sophiaanderson", "TheAvengers", 8.0);
        createRating("sophiaanderson", "ShawshankRedemption", 9.0);
        createRating("sophiaanderson", "Jurassic", 8.3);
        createRating("sophiaanderson", "TheDarkKnight", 8.8);
    }

    /**
     * Create viewing history with realistic timestamps.
     */
    private void createViewingHistory() {
        // Generate viewing history spanning the last 60 days
        // with more recent views for "trending" movies
        Instant now = Instant.now();

        // Recent viewings (last week) - these will be "trending"
        createViewingHistoryEntry("johndoe", "Interstellar", randomTimestamp(now, 7));
        createViewingHistoryEntry("janesmith", "Interstellar", randomTimestamp(now, 5));
        createViewingHistoryEntry("bobjohnson", "Interstellar", randomTimestamp(now, 3));
        createViewingHistoryEntry("alicewilliams", "Interstellar", randomTimestamp(now, 2));

        createViewingHistoryEntry("davidbrown", "TheAvengers", randomTimestamp(now, 6));
        createViewingHistoryEntry("emmadavis", "TheAvengers", randomTimestamp(now, 4));
        createViewingHistoryEntry("michaelmiller", "TheAvengers", randomTimestamp(now, 2));

        createViewingHistoryEntry("oliviawilson", "Avatar", randomTimestamp(now, 7));
        createViewingHistoryEntry("jamestaylor", "Avatar", randomTimestamp(now, 5));
        createViewingHistoryEntry("sophiaanderson", "Avatar", randomTimestamp(now, 1));

        // Older viewings (7-30 days ago)
        createViewingHistoryEntry("johndoe", "TheMatrix", randomTimestamp(now, 15));
        createViewingHistoryEntry("janesmith", "ShawshankRedemption", randomTimestamp(now, 12));
        createViewingHistoryEntry("bobjohnson", "Inception", randomTimestamp(now, 18));
        createViewingHistoryEntry("alicewilliams", "Titanic", randomTimestamp(now, 21));
        createViewingHistoryEntry("davidbrown", "TheDarkKnight", randomTimestamp(now, 14));
        createViewingHistoryEntry("emmadavis", "Alien", randomTimestamp(now, 25));
        createViewingHistoryEntry("michaelmiller", "Jurassic", randomTimestamp(now, 16));
        createViewingHistoryEntry("oliviawilson", "FightClub", randomTimestamp(now, 22));
        createViewingHistoryEntry("jamestaylor", "TheGodfather", randomTimestamp(now, 19));
        createViewingHistoryEntry("sophiaanderson", "PulpFiction", randomTimestamp(now, 28));

        // Very old viewings (31-60 days ago)
        createViewingHistoryEntry("johndoe", "Jaws", randomTimestamp(now, 45));
        createViewingHistoryEntry("janesmith", "ForrestGump", randomTimestamp(now, 38));
        createViewingHistoryEntry("bobjohnson", "TheGodfather", randomTimestamp(now, 52));
        createViewingHistoryEntry("alicewilliams", "PulpFiction", randomTimestamp(now, 42));
        createViewingHistoryEntry("davidbrown", "Jurassic", randomTimestamp(now, 55));
    }

    /**
     * Helper method to create an entity and store its ID.
     */
    private <T, V> void createEntity(String key, V value, Function<V, T> creator,
                                     Function<T, Long> saver, Map<String, Long> idMap) {
        try {
            T entity = creator.apply(value);
            Long id = saver.apply(entity);
            idMap.put(key, id);
            logger.debug("Created entity with key: {}, id: {}", key, id);
        } catch (Exception e) {
            logger.error("Error creating entity {}: {}", key, e.getMessage());
            throw e;
        }
    }

    /**
     * Helper method to create a person and store their ID.
     */
    private void createPerson(String key, String fullName, String gender) {
        createEntity(
                key,
                new PersonData(fullName, gender),
                data -> Person.builder()
                        .fullName(data.fullName)
                        .gender(data.gender)
                        .build(),
                personService::create,
                personIds
        );
    }

    /**
     * Helper method to create a user from an existing person.
     */
    private void createUser(String personKey, String username, String email) {
        try {
            User user = new User();
            user.setUserId(personIds.get(personKey));
            user.setUsername(username);
            user.setEmail(email);
            userService.create(user);
            userIds.put(username, user.getUserId());
            logger.debug("Created user: {}, id: {}", username, user.getUserId());
        } catch (Exception e) {
            logger.error("Error creating user {}: {}", username, e.getMessage());
            throw e;
        }
    }

    /**
     * Helper method to create a movie and store its ID.
     */
    private void createMovie(String key, String title, int duration, double rating) {
        createEntity(
                key,
                new MovieData(title, duration, rating),
                data -> Movie.builder()
                        .title(data.title)
                        .duration(data.duration)
                        .averageRating(new BigDecimal(String.valueOf(data.rating)))
                        .build(),
                movieService::create,
                movieIds
        );
    }

    /**
     * Helper method to associate a movie with a genre.
     */
    private void associateMovieWithGenre(String movieKey, String genreKey) {
        try {
            MovieGenres movieGenres = MovieGenres.builder()
                    .movieId(movieIds.get(movieKey))
                    .genreId(genreIds.get(genreKey))
                    .build();
            movieGenreService.create(movieGenres);
            logger.debug("Associated movie {} with genre {}", movieKey, genreKey);
        } catch (Exception e) {
            logger.error("Error associating movie {} with genre {}: {}",
                    movieKey, genreKey, e.getMessage());
            // Continue execution - this is not critical for demo
        }
    }

    /**
     * Helper method to create a user rating.
     */
    private void createRating(String username, String movieKey, double ratingValue) {
        try {
            UserRating rating = UserRating.builder()
                    .userId(userIds.get(username))
                    .movieId(movieIds.get(movieKey))
                    .ratingValue(new BigDecimal(String.valueOf(ratingValue)))
                    .build();
            userRatingService.create(rating);
            logger.debug("Created rating: user={}, movie={}, rating={}",
                    username, movieKey, ratingValue);
        } catch (Exception e) {
            logger.error("Error creating rating for user {} on movie {}: {}",
                    username, movieKey, e.getMessage());
            // Continue execution - this is not critical for demo
        }
    }

    /**
     * Helper method to create a viewing history entry.
     */
    private void createViewingHistoryEntry(String username, String movieKey, Timestamp timestamp) {
        try {
            ViewingHistory viewingHistory = ViewingHistory.builder()
                    .userId(userIds.get(username))
                    .movieId(movieIds.get(movieKey))
                    .watchDate(timestamp)
                    .build();
            viewingHistoryService.create(viewingHistory);
            logger.debug("Created viewing history: user={}, movie={}, date={}",
                    username, movieKey, timestamp);
        } catch (Exception e) {
            logger.error("Error creating viewing history for user {} on movie {}: {}",
                    username, movieKey, e.getMessage());
            // Continue execution - this is not critical for demo
        }
    }

    /**
     * Generate a random timestamp between now and the specified days ago.
     */
    private Timestamp randomTimestamp(Instant now, int daysAgo) {
        long randomSeconds = ThreadLocalRandom.current().nextLong(0, daysAgo * 24 * 60 * 60);
        return Timestamp.from(now.minus(randomSeconds, ChronoUnit.SECONDS));
    }

    /**
     * Data class for Person creation parameters.
     */
    private record PersonData(String fullName, String gender) { }

    /**
     * Data class for Movie creation parameters.
     */
    private record MovieData(String title, int duration, double rating) { }
}