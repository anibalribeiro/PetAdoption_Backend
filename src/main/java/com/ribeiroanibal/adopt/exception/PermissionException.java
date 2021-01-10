package com.ribeiroanibal.adopt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when user doesnt have permission to update/remove entity
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionException extends ApplicationException {

    public PermissionException(final String entityName, final Long entityId) {
        super(ApplicationException.ERROR_ACCESS_PERMISSION, entityName.toLowerCase(), entityId);
    }
}
