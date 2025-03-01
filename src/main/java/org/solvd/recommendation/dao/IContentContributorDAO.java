package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.ContentContributor;

import java.sql.SQLException;

public interface IContentContributorDAO extends IDAO<ContentContributor> {

    ContentContributor get(long movieId, long personId, int personRole) throws SQLException;
}