package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IContentContributorDAO;
import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ContentContributor service implementation.
 */
public class ContentContributorService extends AbstractService<ContentContributor, CompositeKey3<Long, Long, Integer>, IContentContributorDAO> {
    ContentContributorService(IContentContributorDAO dao) {
        super(dao);
    }

    public ContentContributor getByMoviePersonAndRole(Long movieId, Long personId, Integer roleId) {
        return dao.get(new CompositeKey3<>(movieId, personId, roleId));
    }

    public List<ContentContributor> getByMovie(Long movieId) {
        return dao.getAll().stream()
                .filter(cc -> cc.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }
}