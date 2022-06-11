package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class DuplicateValueException extends AbstractApiRuntimeException
{

    public DuplicateValueException(String message)
    {
        super(HttpStatus.CONFLICT, message);
        //TODO Auto-generated constructor stub
    }
}
