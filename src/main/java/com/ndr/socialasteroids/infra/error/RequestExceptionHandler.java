package com.ndr.socialasteroids.infra.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ndr.socialasteroids.infra.error.exception.DataInconsistencyException;
import com.ndr.socialasteroids.infra.error.exception.DataNotFoundException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RequestExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(DataNotFoundException.class)
    protected ResponseEntity<ErrorDetails> handleEntityNotFound(DataNotFoundException exception)
    {
        ErrorDetails error = new ErrorDetails(HttpStatus.NOT_FOUND, exception.getMessage());
        return buildResponse(error);
    }

    @ExceptionHandler(DataInconsistencyException.class)
    protected ResponseEntity<ErrorDetails> handleDataInconsistency(DataInconsistencyException exception)
    {
        return null;
    }

    private ResponseEntity<ErrorDetails> buildResponse(ErrorDetails error)
    {
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}
