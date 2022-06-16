package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends AbstractApiRuntimeException {

    public RefreshTokenException(String message)
    {
        super(HttpStatus.UNAUTHORIZED, message);
    }
    
}
