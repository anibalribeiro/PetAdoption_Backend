package com.ribeiroanibal.adopt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when entity is already inserted in database.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EntityExistException extends EntityException {

    public EntityExistException(final String entityName, final String username) {
        super(ApplicationException.ERROR_ENTITY_EXISTS, entityName.toLowerCase(), username);
    }
}
