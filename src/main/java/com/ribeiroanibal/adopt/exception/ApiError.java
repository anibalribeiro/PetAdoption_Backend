package com.ribeiroanibal.adopt.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Pojo to wrap the exceptions and return it to FE
 */
@Data
@AllArgsConstructor
public class ApiError {

    @NotBlank
    private final String errorId;
    private final Object[] messageParams;

    public static ApiError of(final String errorId,
                              final Object... messageParams) {
        return new ApiError(errorId, messageParams);
    }

    public ApiError(final ApplicationException exception) {
        notNull(exception, "exception");
        this.errorId = exception.getMessageKey();
        this.messageParams = exception.getMessageParams();
    }
}
