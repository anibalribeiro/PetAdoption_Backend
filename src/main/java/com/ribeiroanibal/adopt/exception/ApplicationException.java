package com.ribeiroanibal.adopt.exception;

import static org.apache.commons.lang3.Validate.notNull;

public abstract class ApplicationException extends RuntimeException {
    static final String ERROR_APPLICATION = "ERROR.APPLICATION";
    static final String ERROR_ACCESS_DENIED = "ERROR.ACCESS.DENIED";
    static final String ERROR_ACCESS_PERMISSION = "ERROR.ACCESS.PERMISSION";
    static final String ERROR_ACCESS_BAD_CREDENTIALS = "ERROR.ACCESS.BAD_CREDENTIALS";
    static final String ERROR_ENTITY_NOT_FOUND_BY_ID = "ERROR.ENTITY.NOT_FOUND.BY_ID";
    static final String ERROR_ENTITY_EXISTS = "ERROR.ENTITY.EXISTS";
    static final String ERROR_PARAMETERS_INVALID = "ERROR.PARAMETERS.INVALID";

    private final String messageKey;
    private final Object[] messageParams;

    public ApplicationException(final String messageKey,
                                final Object... params) {
        this.messageKey = notNull(messageKey, "messageKey");
        this.messageParams = params;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getMessageParams() {
        return messageParams;
    }
}
