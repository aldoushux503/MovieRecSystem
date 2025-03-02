package org.solvd.recommendation.dao;

import org.solvd.recommendation.model.ContentContributor;
import org.solvd.recommendation.util.CompositeKey3;

import java.sql.SQLException;


public interface IContentContributorDAO extends IDAO<ContentContributor, CompositeKey3<Long, Long, Integer>> {
}