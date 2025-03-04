package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IMovieDAO;
import org.solvd.recommendation.model.Genre;
import org.solvd.recommendation.model.Movie;
import org.solvd.recommendation.model.MovieGenres;
import org.solvd.recommendation.model.Person;
import org.solvd.recommendation.service.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Movie service implementation.
 */

public class MovieService extends AbstractService<Movie, Long, IMovieDAO> implements IMovieService {
    private final IMovieGenreService movieGenreService;
    private final IContentContributorService contentContributorService;

    public MovieService(IMovieDAO dao, IMovieGenreService movieGenreService, IContentContributorService contentContributorService) {
        super(dao);
        this.movieGenreService = movieGenreService;
        this.contentContributorService = contentContributorService;
    }

    @Override
    public List<Genre> getMovieGenres(Long movieId) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        IGenreService genreService =  serviceFactory.getGenreService();

        return movieGenreService.getByMovie(movieId).stream()
                .map(mg -> genreService.getById(mg.getGenreId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> getMovieContributors(Long movieId, Integer roleId) {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        IPersonService personService = serviceFactory.getPersonService();

        return contentContributorService.getByMovie(movieId).stream()
                .filter(cc -> roleId == null || cc.getPersonRoleId().equals(roleId))
                .map(cc -> personService.getById(cc.getPersonId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> getMoviesByGenre(Long genreId) {
        List<MovieGenres> genreMovies = movieGenreService.getByGenre(genreId);

        return genreMovies.stream()
                .map(mg -> getById(mg.getMovieId()))
                .collect(Collectors.toList());
    }
}