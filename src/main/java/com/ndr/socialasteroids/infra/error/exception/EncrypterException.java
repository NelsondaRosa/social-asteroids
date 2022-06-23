package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class EncrypterException extends AbstractApiRuntimeException{

    public EncrypterException(String message)
    {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
    
}
