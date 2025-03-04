package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IUserDAO;
import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.User;
import org.solvd.recommendation.service.IUserPreferredGenreService;
import org.solvd.recommendation.service.IUserService;
import org.solvd.recommendation.service.ServiceFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * User service implementation.
 */

public class UserService extends AbstractService<User, Long, IUserDAO> implements IUserService {
    private final IUserPreferredGenreService userPreferredGenreService;

    public UserService(IUserDAO dao, IUserPreferredGenreService userPreferredGenreService) {
        super(dao);
        this.userPreferredGenreService = userPreferredGenreService;
    }

    @Override
    public Set<Genre> getUserPreferredGenres(Long userId) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        GenreService genreService = (GenreService) serviceFactory.getGenreService();

        return userPreferredGenreService.getByUser(userId).stream()
                .map(upg -> genreService.getById(upg.getGenreId()))
                .collect(Collectors.toSet());
    }
}