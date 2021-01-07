package com.ribeiroanibal.adopt.exception;

/**
 * Abstract exception class to be extended by possible Entity errors
 */
public abstract class EntityException extends ApplicationException {

    public EntityException(final String messageKey, final Object... params) {
        super(messageKey, params);
    }
}
