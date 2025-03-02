package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IInteractionDAO;
import org.solvd.recommendation.model.Interaction;

/**
 * Interaction service implementation.
 */
public class InteractionService extends AbstractService<Interaction, Long, IInteractionDAO> {
    InteractionService(IInteractionDAO dao) {
        super(dao);
    }
}