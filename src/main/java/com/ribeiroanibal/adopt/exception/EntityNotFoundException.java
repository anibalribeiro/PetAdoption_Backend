package com.ribeiroanibal.adopt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when entity with given ID or by given query is not found in database.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends EntityException {

    public EntityNotFoundException(final String entityName, final Object id) {
        super(ApplicationException.ERROR_ENTITY_NOT_FOUND_BY_ID, entityName.toLowerCase(), id);
    }
}
