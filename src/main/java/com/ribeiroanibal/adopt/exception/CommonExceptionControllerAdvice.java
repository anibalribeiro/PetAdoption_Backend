package com.ribeiroanibal.adopt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class provides custom exception handling for exceptions.
 */
@ControllerAdvice
public class CommonExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionControllerAdvice.class);

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "IllegalArgumentException was thrown during " +
            "processing request. Either request is invalid or there is a bug in the code.";

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> illegalArgumentExceptionHandler(final IllegalArgumentException ex) {
        logger.error(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, ex);
        final ApiError error = ApiError.of(ApplicationException.ERROR_PARAMETERS_INVALID);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> entityNotFoundHandler(final EntityNotFoundException ex) {
        logger.info("Entity not found exception {}.", ex.getMessage(), ex);
        final ApiError error = ApiError.of(ApplicationException.ERROR_ENTITY_NOT_FOUND_BY_ID, ex.getMessageParams());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityExistException.class})
    public ResponseEntity<?> entityAlreadyExistsHandler(final EntityExistException ex) {
        logger.info("Entity already exists exception {}.", ex.getMessage(), ex);
        final ApiError error = ApiError.of(ApplicationException.ERROR_ENTITY_EXISTS, ex.getMessageParams());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityException.class})
    public ResponseEntity<?> entityExceptionHandler(final EntityException ex) {
        logger.info("Entity exception {}", ex.getMessage(), ex);
        final ApiError error = new ApiError(ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<?> applicationExceptionHandler(final ApplicationException ex) {
        logger.error("Application exception.", ex);
        final ApiError error = ApiError.of(ApplicationException.ERROR_APPLICATION);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedException(final AccessDeniedException ex) {
        logger.info("Access denied exception {}", ex.getMessage());
        final ApiError error = ApiError.of(ApplicationException.ERROR_ACCESS_DENIED);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> authenticationException(final AuthenticationException ex) {
        logger.info("Permission denied.", ex);
        final ApiError error = ApiError.of(ApplicationException.ERROR_ACCESS_BAD_CREDENTIALS, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(final Exception ex) {
        logger.error("Unexpected exception occurred.", ex);
        final ApiError error = ApiError.of(ApplicationException.ERROR_APPLICATION);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
