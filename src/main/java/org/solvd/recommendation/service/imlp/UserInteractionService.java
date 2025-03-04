package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IUserInteractionDAO;
import org.solvd.recommendation.model.UserInteraction;
import org.solvd.recommendation.service.IUserInteractionService;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserInteraction service implementation.
 */
public class UserInteractionService
        extends AbstractService<UserInteraction, CompositeKey3<Long, Long, Long>, IUserInteractionDAO>
        implements IUserInteractionService {

    public UserInteractionService(IUserInteractionDAO dao) {
        super(dao);
    }

    public List<UserInteraction> getByUser(Long userId) {
        return dao.getAll().stream()
                .filter(ui -> ui.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<UserInteraction> getByMovie(Long movieId) {
        return dao.getAll().stream()
                .filter(ui -> ui.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
}