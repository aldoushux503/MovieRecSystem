package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IUserPreferredGenreDAO;
import org.solvd.recommendation.model.UserPreferredGenre;
import org.solvd.recommendation.service.IUserPreferredGenreService;
import org.solvd.recommendation.util.CompositeKey2;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserPreferredGenre service implementation.
 */

public class UserPreferredGenreService
        extends AbstractService<UserPreferredGenre, CompositeKey2<Long, Long>, IUserPreferredGenreDAO>
        implements IUserPreferredGenreService {

    public UserPreferredGenreService(IUserPreferredGenreDAO dao) {
        super(dao);
    }

    @Override
    public UserPreferredGenre getByUserAndGenre(Long userId, Long genreId) {
        try {
            return dao.get(new CompositeKey2<>(userId, genreId));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<UserPreferredGenre> getByUser(Long userId) {
        return dao.getAll().stream()
                .filter(upg -> upg.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}