package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.DAOFactory;
import org.solvd.recommendation.service.interfaces.IRecommendationService;

/**
 * Factory for creating service instances.
 * Implements Singleton pattern to ensure only one factory instance exists.
 * Follows Abstract Factory pattern by providing methods to create related service objects.
 */
public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();

    // Lazily initialized service instances
    private IRecommendationService recommendationService;

    private ServiceFactory() {
        // Private constructor to enforce Singleton pattern
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public MovieService getMovieService() {
        return new MovieService(DAOFactory.getMovieDAO());
    }

    public UserService getUserService() {
        return new UserService(DAOFactory.getUserDAO());
    }

    public GenreService getGenreService() {
        return new GenreService(DAOFactory.getGenreDAO());
    }

    public PersonService getPersonService() {
        return new PersonService(DAOFactory.getPersonDAO());
    }

    public PersonRoleService getPersonRoleService() {
        return new PersonRoleService(DAOFactory.getPersonRoleDAO());
    }

    public InteractionService getInteractionService() {
        return new InteractionService(DAOFactory.getInteractionDAO());
    }

    public UserRatingService getUserRatingService() {
        return new UserRatingService(DAOFactory.getUserRatingDAO());
    }

    public ViewingHistoryService getViewingHistoryService() {
        return new ViewingHistoryService(DAOFactory.getViewingHistoryDAO());
    }

    public MovieGenreService getMovieGenreService() {
        return new MovieGenreService(DAOFactory.getMovieGenreDAO());
    }

    public UserPreferredGenreService getUserPreferredGenreService() {
        return new UserPreferredGenreService(DAOFactory.getUserPreferredGenreDAO());
    }

    public ContentContributorService getContentContributorService() {
        return new ContentContributorService(DAOFactory.getContentContributorDAO());
    }

    public UserInteractionService getUserInteractionService() {
        return new UserInteractionService(DAOFactory.getUserInteractionDAO());
    }

    /**
     * Gets the recommendation service instance.
     * Uses lazy initialization for performance optimization.
     */
    public IRecommendationService getRecommendationService() {
        if (recommendationService == null) {
            recommendationService = new RecommendationService();
        }
        return recommendationService;
    }
}