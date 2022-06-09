package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class InexistentResourceException extends AbstractApiRuntimeException
{
    public InexistentResourceException(String message)
    {
        super(HttpStatus.NOT_FOUND, message);
    }
}
