package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IContentContributorDAO;
import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.service.IContentContributorService;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ContentContributor service implementation.
 */
public class ContentContributorService
        extends AbstractService<ContentContributor, CompositeKey3<Long, Long, Long>, IContentContributorDAO>
        implements IContentContributorService {

    public ContentContributorService(IContentContributorDAO dao) {
        super(dao);
    }

    @Override
    public List<ContentContributor> getByMovie(Long movieId) {
        return dao.getAll().stream()
                .filter(cc -> cc.getMovieId().equals(movieId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContentContributor> getByPerson(Long personId) {
        return dao.getAll().stream()
                .filter(cc -> cc.getPersonId().equals(personId))
                .collect(Collectors.toList());
    }
}