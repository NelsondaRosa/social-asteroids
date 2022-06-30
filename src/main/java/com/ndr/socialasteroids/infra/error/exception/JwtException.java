package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

public class JwtException extends AbstractApiRuntimeException{

    public JwtException(String message)
    {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public JwtException()
    {
        super(HttpStatus.UNAUTHORIZED, null);
    }
    
}
