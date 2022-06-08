package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends AbstractApiRuntimeException
{
    public DataNotFoundException(String message)
    {
        super(HttpStatus.NOT_FOUND, message);
    }
    
}
