package org.solvd.recommendation.service.interfaces;

import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.User;

import java.util.List;
import java.util.Set;

/**
 * Service interface for user-related operations.
 * Handles user profile management and retrieval.
 */
public interface IUserService {
    User getUserById(Long userId);

    List<User> getAllUsers();

    Long createUser(User user);

    void updateUser(User user);

    void deleteUser(User user);

    Set<Genre> getUserPreferredGenres(Long userId);

    List<User> getSimilarUsers(Long userId, int limit);
}
