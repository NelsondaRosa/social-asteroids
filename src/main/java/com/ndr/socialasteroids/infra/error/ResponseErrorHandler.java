package com.ndr.socialasteroids.infra.error;

import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.InexistentResourceException;

@Order(Ordered.HIGHEST_PRECEDENCE) @ControllerAdvice
public class ResponseErrorHandler extends ResponseEntityExceptionHandler
{

    Logger logger = LoggerFactory.getLogger(ResponseErrorHandler.class);

    @ExceptionHandler(InexistentResourceException.class)
    protected ResponseEntity<ErrorDetails> handleInexistentResource(InexistentResourceException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, exception.getMessage());
        return buildResponse(error);
    }

    @ExceptionHandler(DataInconsistencyException.class)
    protected ResponseEntity<ErrorDetails> handleDataInconsistency(DataInconsistencyException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.CONFLICT, exception.getMessage());
        return buildResponse(error);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorDetails> handleNotSuchElement(NoSuchElementException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "No such data in the DB");
        return buildResponse(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleEntityNotFound(EntityNotFoundException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "Data can't be found anymore");
        return buildResponse(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.BAD_REQUEST, "Sent data can't be handled");
        return buildResponse(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.UNAUTHORIZED,
                "Requesting user is not allowed to do this");

        return buildResponse(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDetails> handleException(Exception exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");

        logger.error(exception.getMessage());
        exception.printStackTrace();

        return buildResponse(error);
    }

    private ResponseEntity<ErrorDetails> buildResponse(ErrorDetails error)
    {
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
