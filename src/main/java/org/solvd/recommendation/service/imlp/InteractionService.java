package org.solvd.recommendation.service.imlp;

import org.solvd.recommendation.dao.IInteractionDAO;
import org.solvd.recommendation.model.Interaction;

/**
 * Interaction service implementation.
 */
public class InteractionService extends AbstractService<Interaction, Long, IInteractionDAO> {
    public InteractionService(IInteractionDAO dao) {
        super(dao);
    }
}