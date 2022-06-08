package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class DataInconsistencyException extends AbstractApiRuntimeException
{
    public DataInconsistencyException(String message)
    {
        super(HttpStatus.CONFLICT, message);
    }
}
