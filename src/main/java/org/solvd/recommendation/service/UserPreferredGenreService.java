package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IUserPreferredGenreDAO;
import org.solvd.recommendation.model.UserPreferredGenre;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserPreferredGenre service implementation.
 */
public class UserPreferredGenreService extends AbstractService<UserPreferredGenre, CompositeKey2<Long, Long>, IUserPreferredGenreDAO> {
    UserPreferredGenreService(IUserPreferredGenreDAO dao) {
        super(dao);
    }

    public UserPreferredGenre getByUserAndGenre(Long userId, Long genreId) {
        return dao.get(new CompositeKey2<>(userId, genreId));
    }

    public List<UserPreferredGenre> getByUser(Long userId) {
        return dao.getAll().stream()
                .filter(upg -> upg.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}