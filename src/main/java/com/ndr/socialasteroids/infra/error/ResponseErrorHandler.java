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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.DuplicateValueException;
import com.ndr.socialasteroids.infra.error.exception.InexistentDataException;
import com.ndr.socialasteroids.infra.error.exception.JwtException;
import com.ndr.socialasteroids.infra.error.exception.RefreshTokenException;

@Order(Ordered.HIGHEST_PRECEDENCE) @ControllerAdvice
public class ResponseErrorHandler extends ResponseEntityExceptionHandler
{

    Logger logger = LoggerFactory.getLogger(ResponseErrorHandler.class);

    // ------------------------- CUSTOM EXCEPTIONS ----------------------------
    @ExceptionHandler(InexistentDataException.class)
    protected ResponseEntity<ErrorDetails> handleInexistentResource(InexistentDataException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(DataInconsistencyException.class)
    protected ResponseEntity<ErrorDetails> handleDataInconsistency(DataInconsistencyException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(DuplicateValueException.class)
    protected ResponseEntity<ErrorDetails> handleDuplicateValue(DuplicateValueException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorDetails> handleUserAuthentication(JwtException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(RefreshTokenException.class)
    protected ResponseEntity<ErrorDetails> handleRefrehToken(RefreshTokenException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());
        exception.printStackTrace();
        return buildResponse(error);
    }

    // ------------------------- IMPORTED EXCEPTIONS ----------------------------
    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorDetails> handleNotSuchElement(NoSuchElementException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "No such data in the DB");
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleEntityNotFound(EntityNotFoundException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "Data can't be found anymore");
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException exception, WebRequest request)
    {
        logger.error(exception.getMessage());
        ErrorDetails error = new ErrorDetails(HttpStatus.BAD_REQUEST, "Sent data is incorrect");
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.UNAUTHORIZED, "Access denied for this operation");
        exception.printStackTrace();
        return buildResponse(error);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleUsernameNotFound(UsernameNotFoundException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "Wrong user data");
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorDetails> handleAuthentication(AuthenticationException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.UNAUTHORIZED, "Unauthorized");
        exception.printStackTrace();
        return buildResponse(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDetails> handleServerException(Exception exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");

        logger.error("Unexpected error : " + exception.getMessage());
        exception.printStackTrace();

        return buildResponse(error);
    }

    private ResponseEntity<ErrorDetails> buildResponse(ErrorDetails error)
    {
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
