package com.ndr.socialasteroids.infra.error.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class AbstractApiRuntimeException extends RuntimeException{
    private HttpStatus status;

    public AbstractApiRuntimeException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }
}