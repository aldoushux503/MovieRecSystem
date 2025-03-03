package org.solvd.recommendation.dao;

import org.solvd.recommendation.dao.mybatis.*;

public class DAOFactory {

    public enum DAOType {
        MYBATIS
    }

    private static final DAOType DEFAULT_DAO_TYPE = DAOType.MYBATIS;

    public static IGenreDAO getGenreDAO() {
        return getGenreDAO(DEFAULT_DAO_TYPE);
    }

    public static IGenreDAO getGenreDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new GenreMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IMovieDAO getMovieDAO() {
        return getMovieDAO(DEFAULT_DAO_TYPE);
    }

    public static IMovieDAO getMovieDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new MovieMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IUserDAO getUserDAO() {
        return getUserDAO(DEFAULT_DAO_TYPE);
    }

    public static IUserDAO getUserDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new UserMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IPersonDAO getPersonDAO() {
        return getPersonDAO(DEFAULT_DAO_TYPE);
    }

    public static IPersonDAO getPersonDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new PersonMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IUserRatingDAO getUserRatingDAO() {
        return getUserRatingDAO(DEFAULT_DAO_TYPE);
    }

    public static IUserRatingDAO getUserRatingDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new UserRatingMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IInteractionDAO getInteractionDAO() {
        return getInteractionDAO(DEFAULT_DAO_TYPE);
    }

    public static IInteractionDAO getInteractionDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new InteractionMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IViewingHistoryDAO getViewingHistoryDAO() {
        return getViewingHistoryDAO(DEFAULT_DAO_TYPE);
    }

    public static IViewingHistoryDAO getViewingHistoryDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new ViewingHistoryMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IMovieGenresDAO getMovieGenreDAO() {
        return getMovieGenreDAO(DEFAULT_DAO_TYPE);
    }

    public static IMovieGenresDAO getMovieGenreDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new MovieGenresMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IUserPreferredGenreDAO getUserPreferredGenreDAO() {
        return getUserPreferredGenreDAO(DEFAULT_DAO_TYPE);
    }

    public static IUserPreferredGenreDAO getUserPreferredGenreDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new UserPreferredGenresMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IContentContributorDAO getContentContributorDAO() {
        return getContentContributorDAO(DEFAULT_DAO_TYPE);
    }

    public static IContentContributorDAO getContentContributorDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new ContentContributorsMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IUserInteractionDAO getUserInteractionDAO() {
        return getUserInteractionDAO(DEFAULT_DAO_TYPE);
    }

    public static IUserInteractionDAO getUserInteractionDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new UserInteractionsMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }

    public static IPersonRoleDAO getPersonRoleDAO() {
        return getPersonRoleDAO(DEFAULT_DAO_TYPE);
    }

    public static IPersonRoleDAO getPersonRoleDAO(DAOType type) {
        return switch (type) {
            case MYBATIS -> new PersonRoleMyBatisDAO();
            default -> throw new IllegalArgumentException("Unsupported DAO type: " + type);
        };
    }
}