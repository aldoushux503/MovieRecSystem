package org.solvd.recommendation.exeption;

public class EntityNotFoundException extends DataAccessException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(Class<?> entityClass, Object id) {
        super(String.format("%s with ID %s not found", entityClass.getSimpleName(), id));
    }

    public EntityNotFoundException(Class<?> entityClass, String criteria) {
        super(String.format("%s with %s not found", entityClass.getSimpleName(), criteria));
    }
}
