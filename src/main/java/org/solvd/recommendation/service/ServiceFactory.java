package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.DAOFactory;
import org.solvd.recommendation.service.imlp.*;

/**
 * Factory for creating service instances.
 * Implements Singleton pattern to ensure only one factory instance exists.
 * Follows Abstract Factory pattern by providing methods to create related service objects.
 */
public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();

    // Lazily initialized service instances
    private IGenreService genreService;
    private IMovieService movieService;
    private IPersonService personService;
    private IUserRatingService userRatingService;
    private IUserService userService;
    private IInteractionService interactionService;
    private IViewingHistoryService viewingHistoryService;
    private IRecommendationService recommendationService;
    private IMovieGenreService movieGenreService;
    private IUserPreferredGenreService userPreferredGenreService;
    private IContentContributorService contentContributorService;
    private IUserInteractionService userInteractionService;
    private IPersonRoleService personRoleService;

    private ServiceFactory() {
        // Private constructor to enforce Singleton pattern
    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public IMovieService getMovieService() {
        if (movieService == null) {
            movieService = new MovieService(DAOFactory.getMovieDAO(),
                    getMovieGenreService(),
                    getContentContributorService());
        }
        return movieService;
    }

    public IUserService getUserService() {
        if (userService == null) {
            userService = new UserService(DAOFactory.getUserDAO(),
                    getUserPreferredGenreService());
        }
        return userService;
    }

    public IGenreService getGenreService() {
        if (genreService == null) {
            genreService = new GenreService(DAOFactory.getGenreDAO(),
                    getMovieGenreService());
        }
        return genreService;
    }

    public IPersonService getPersonService() {
        if (personService == null) {
            personService = new PersonService(DAOFactory.getPersonDAO(),
                    getContentContributorService());
        }
        return personService;
    }

    public IPersonRoleService getPersonRoleService() {
        if (personRoleService == null) {
            personRoleService = new PersonRoleService(DAOFactory.getPersonRoleDAO());
        }
        return personRoleService;
    }

    public IInteractionService getInteractionService() {
        if (interactionService == null) {
            interactionService = new InteractionService(DAOFactory.getInteractionDAO(),
                    getUserInteractionService());
        }
        return interactionService;
    }

    public IUserRatingService getUserRatingService() {
        if (userRatingService == null) {
            userRatingService = new UserRatingService(DAOFactory.getUserRatingDAO(),
                   getMovieService());
        }
        return userRatingService;
    }

    public IViewingHistoryService getViewingHistoryService() {
        if (viewingHistoryService == null) {
            viewingHistoryService = new ViewingHistoryService(DAOFactory.getViewingHistoryDAO(),
                    (UserService) getUserService());
        }
        return viewingHistoryService;
    }

    public IMovieGenreService getMovieGenreService() {
        if (movieGenreService == null) {
            movieGenreService = new MovieGenreService(DAOFactory.getMovieGenreDAO());
        }
        return movieGenreService;
    }

    public IUserPreferredGenreService getUserPreferredGenreService() {
        if (userPreferredGenreService == null) {
            userPreferredGenreService = new UserPreferredGenreService(DAOFactory.getUserPreferredGenreDAO());
        }
        return userPreferredGenreService;
    }

    public IContentContributorService getContentContributorService() {
        if (contentContributorService == null) {
            contentContributorService = new ContentContributorService(DAOFactory.getContentContributorDAO());
        }
        return contentContributorService;
    }

    public IUserInteractionService getUserInteractionService() {
        if (userInteractionService == null) {
            userInteractionService = new UserInteractionService(DAOFactory.getUserInteractionDAO());
        }
        return userInteractionService;
    }

    public IRecommendationService getRecommendationService() {
        if (recommendationService == null) {
            recommendationService = new RecommendationService();
        }
        return recommendationService;
    }
}