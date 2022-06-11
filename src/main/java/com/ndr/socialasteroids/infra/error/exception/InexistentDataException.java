package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class InexistentDataException extends AbstractApiRuntimeException
{
    public InexistentDataException(String message)
    {
        super(HttpStatus.NOT_FOUND, message);
    }
}
