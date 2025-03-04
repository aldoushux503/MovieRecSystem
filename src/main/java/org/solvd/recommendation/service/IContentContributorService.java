package org.solvd.recommendation.service;

import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.util.CompositeKey3;

import java.util.List;

public interface IContentContributorService extends IService<ContentContributor, CompositeKey3<Long, Long, Long>> {
    List<ContentContributor> getByMovie(Long movieId);

    List<ContentContributor> getByPerson(Long personId);
}
