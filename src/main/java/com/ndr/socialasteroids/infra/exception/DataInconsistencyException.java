package com.ndr.socialasteroids.infra.exception;

public class DataInconsistencyException extends RuntimeException {
    public DataInconsistencyException(String msg){
        super(msg);
    }
}
