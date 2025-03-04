package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IPersonDAO;
import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.Person;
import org.solvd.recommendation.service.IContentContributorService;
import org.solvd.recommendation.service.IPersonService;
import org.solvd.recommendation.service.ServiceFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Person service implementation.
 */

public class PersonService extends AbstractService<Person, Long, IPersonDAO> implements IPersonService {
    private final IContentContributorService contentContributorService;

    public PersonService(IPersonDAO dao, IContentContributorService contentContributorService) {
        super(dao);
        this.contentContributorService = contentContributorService;
    }

    @Override
    public List<Movie> getMoviesByPerson(Long personId, Integer roleId) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        MovieService movieService = (MovieService) serviceFactory.getMovieService();

        List<ContentContributor> contributions = contentContributorService.getByPerson(personId).stream()
                .filter(cc -> roleId == null || cc.getPersonRoleId().equals(roleId))
                .toList();

        return contributions.stream()
                .map(cc -> movieService.getById(cc.getMovieId()))
                .collect(Collectors.toList());
    }
}