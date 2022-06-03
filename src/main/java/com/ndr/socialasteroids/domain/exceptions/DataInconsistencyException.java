package com.ndr.socialasteroids.domain.exceptions;

public class DataInconsistencyException extends Exception {
    public DataInconsistencyException(){
        super("Inconsistency identified, operation halted.");
    }
}
