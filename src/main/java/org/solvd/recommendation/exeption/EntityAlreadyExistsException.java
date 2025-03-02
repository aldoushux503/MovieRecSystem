package org.solvd.recommendation.exeption;

public class EntityAlreadyExistsException extends DataAccessException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public EntityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityAlreadyExistsException(Class<?> entityClass, Object id) {
        super(String.format("%s with ID %s already exists", entityClass.getSimpleName(), id));
    }
}
