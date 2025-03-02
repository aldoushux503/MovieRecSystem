package org.solvd.recommendation.service;

import org.solvd.recommendation.dao.IDAO;
import org.solvd.recommendation.service.interfaces.IService;

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
    protected final D dao;

    protected AbstractService(D dao) {
        this.dao = dao;
    }

    @Override
    public T getById(ID id) {
        return dao.get(id);
    }

    @Override
    public ID create(T entity) {
        return dao.save(entity);
    }

    @Override
    public void update(T entity) {
        dao.update(entity);
    }

    @Override
    public void delete(T entity) {
        dao.delete(entity);
    }

    @Override
    public List<T> getAll() {
        return dao.getAll();
    }
}