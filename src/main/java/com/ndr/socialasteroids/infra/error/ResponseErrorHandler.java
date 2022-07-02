package com.ndr.socialasteroids.infra.error;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.DuplicateValueException;
import com.ndr.socialasteroids.infra.error.exception.EncrypterException;
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

        return buildResponse(error,exception);
    }

    @ExceptionHandler(DataInconsistencyException.class)
    protected ResponseEntity<ErrorDetails> handleDataInconsistency(DataInconsistencyException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());

        return buildResponse(error,exception);
    }

    @ExceptionHandler(DuplicateValueException.class)
    protected ResponseEntity<ErrorDetails> handleDuplicateValue(DuplicateValueException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());

        return buildResponse(error,exception);
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<ErrorDetails> handleUserAuthentication(JwtException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());

        return buildResponse(error,exception);
    }

    @ExceptionHandler(RefreshTokenException.class)
    protected ResponseEntity<ErrorDetails> handleRefrehToken(RefreshTokenException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());

        return buildResponse(error,exception);
    }

    @ExceptionHandler(EncrypterException.class)
    protected ResponseEntity<ErrorDetails> handleEncoderException(EncrypterException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(exception.getStatus(), exception.getMessage());

        return buildResponse(error,exception);
    }

    // ------------------------- IMPORTED EXCEPTIONS ----------------------------

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorDetails> handleNotSuchElement(NoSuchElementException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "No such data");

        return buildResponse(error,exception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleEntityNotFound(EntityNotFoundException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "Data can't be found");

        return buildResponse(error,exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorDetails> handleIllegalArgument(IllegalArgumentException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.BAD_REQUEST, "Sent data is incorrect");

        return buildResponse(error,exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorDetails> handleAccessDenied(AccessDeniedException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.UNAUTHORIZED, "Access denied for this operation");

        return buildResponse(error,exception);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleUsernameNotFound(UsernameNotFoundException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, "Wrong login credentials");

        return buildResponse(error,exception);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorDetails> handleAuthentication(AuthenticationException exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.UNAUTHORIZED, "Authentication failed");

        return buildResponse(error,exception);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDetails> handleServerException(Exception exception, WebRequest request)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");

        exception.printStackTrace();
        logger.info(exception.getMessage());

        return buildResponse(error, exception);
    }

    private ResponseEntity<ErrorDetails> buildResponse(ErrorDetails error, Throwable throwable)
    {
        throwable.printStackTrace();
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
