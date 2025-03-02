package org.solvd.recommendation.exeption;

public class InvalidEntityException extends DataAccessException {

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntityException(Class<?> entityClass, String issue) {
        super(String.format("Invalid %s: %s", entityClass.getSimpleName(), issue));
    }
}
