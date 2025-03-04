package org.solvd.recommendation.service.imlp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.solvd.recommendation.dao.IDAO;
import org.solvd.recommendation.service.IService;

import java.util.List;

/**
 * Abstract implementation of the service interface that delegates operations to the DAO layer.
 * Follows Template Method pattern by defining the structure of the algorithm.
 *
 * @param <T> Entity type
 * @param <ID> Entity identifier type
 * @param <D> DAO type
 */
public abstract class AbstractService<T, ID, D extends IDAO<T, ID>> implements IService<T, ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractService.class);

    protected final D dao;

    protected AbstractService(D dao) {
        this.dao = dao;
        LOGGER.debug("AbstractService created for {}", dao.getClass().getSimpleName());
    }

    @Override
    public T getById(ID id) {
        LOGGER.debug("Getting entity by ID: {}", id);
        return dao.get(id);
    }

    @Override
    public ID create(T entity) {
        LOGGER.debug("Creating entity: {}", entity);
        return dao.save(entity);
    }

    @Override
    public void update(T entity) {
        LOGGER.debug("Updating entity: {}", entity);
        dao.update(entity);
    }

    @Override
    public void delete(T entity) {
        LOGGER.debug("Deleting entity: {}", entity);
        dao.delete(entity);
    }

    @Override
    public List<T> getAll() {
        LOGGER.debug("Getting all entities");
        return dao.getAll();
    }
}